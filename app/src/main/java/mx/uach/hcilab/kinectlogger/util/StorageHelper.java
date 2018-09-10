package mx.uach.hcilab.kinectlogger.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import mx.uach.hcilab.kinectlogger.Therapist;

public class StorageHelper {

    private static final String TAG = "Storage Helper";

    private static final String STORAGE_URL = "gs://xboxloggerapp.appspot.com";

    public static void uploadImage (Context context, Uri filePath, String key, final OnUploadImageListener listener) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(STORAGE_URL);
        StorageReference childRef = storageRef.child(key + ".jpg");

        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), filePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap cropImg;
            int width = bmp.getWidth();
            int height = bmp.getHeight();
            if(width > height){
                int crop = (width - height) / 2;
                cropImg = Bitmap.createBitmap(bmp, crop, 0, height, height);
            } else {
                int crop = (height - width) / 2;
                cropImg = Bitmap.createBitmap(bmp, 0, crop, width, width);
            }
            cropImg.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            //uploading the image
            UploadTask uploadTask = childRef.putBytes(data);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if(listener != null) listener.onSuccess();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if(listener != null) listener.onFailure(e);
                }
            });
        } catch (IOException e) {
            if(listener != null) listener.onFailure(e);
        }
    }
    public static void downloadImage(String key, final OnDownloadImageListener listener){

        try {
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            StorageReference myRef = mStorageRef.child(key + ".jpg");
            final File localFile = File.createTempFile("images", "jpg");
            myRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Log.d("MyLog", "Downloaded image");
                            if(listener != null) listener.onSuccess(localFile);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    if(listener != null) listener.onFailure(exception);
                }
            });

        } catch (IOException e) {
            if(listener != null) listener.onFailure(e);
            e.printStackTrace();
        }
    }

    public interface OnUploadImageListener {
        public void onSuccess();
        public void onFailure(Exception e);
    }
    public interface OnDownloadImageListener {
        public void onSuccess(File file);
        public void onFailure(Exception e);
    }
}

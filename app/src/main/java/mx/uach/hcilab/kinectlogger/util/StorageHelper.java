package mx.uach.hcilab.kinectlogger.util;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import mx.uach.hcilab.kinectlogger.Therapist;

public class StorageHelper {

    private static final String TAG = "Storage Helper";

    private static final String STORAGE_URL = "gs://xboxloggerapp.appspot.com";

    public static void uploadImage (Uri filePath, String key, final OnUploadImageListener listener) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(STORAGE_URL);
        StorageReference childRef = storageRef.child(key + ".jpg");

        //uploading the image
        UploadTask uploadTask = childRef.putFile(filePath);

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

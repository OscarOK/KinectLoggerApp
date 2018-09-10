package mx.uach.hcilab.kinectlogger.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import mx.uach.hcilab.kinectlogger.Patient;
import mx.uach.hcilab.kinectlogger.Therapist;

public class FirestoreHelper {
    private static final String TAG = "Firestore Helper";

    public static final String THERAPIST_COLLECTION = "therapists";
    public static final String PATIENT_COLLECTION = "patients";


    public interface OnUploadListener {
        public void onSuccess();
    }


    public static void uploadTherapist(Context context, Therapist therapist){
        uploadTherapist(context, therapist, null);
    }

    public static void uploadTherapist(Context context, final Therapist therapist, final OnUploadListener listener){

        StorageHelper.uploadImage(context, therapist.getPicturePath(), therapist.getKey(),
                new StorageHelper.OnUploadImageListener() {
            @Override
            public void onSuccess() {FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
                CollectionReference collection = firestoreDB.collection(THERAPIST_COLLECTION);
                collection.document(therapist.getKey()).set(therapist.toMap())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Terapeuta añadido correctamente");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, e.getMessage());
                            }
                        });
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, e.getMessage());
            }
        });
    }

    public static void uploadPatient(Context context, final Patient patient){
        StorageHelper.uploadImage(context, patient.getPicturePath(), patient.getKey(),
                new StorageHelper.OnUploadImageListener() {
                    @Override
                    public void onSuccess() {
                        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
                        CollectionReference collection = firestoreDB.collection(PATIENT_COLLECTION);
                        collection.document(patient.getKey()).set(patient.toMap())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Paciente añadido correctamente");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, e.getMessage());
                                    }
                                });
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
    }

    public static String generateUniqueKey(User user){
        String paternal = user.getPaternal();
        String maternal = user.getMaternal();
        String name = user.getName();

        return (paternal.substring(0, Math.min(paternal.length(), 4)) +
                maternal.substring(0, Math.min(maternal.length(), 4)) +
                name.substring(0, Math.min(name.length(), 4))).trim();
    }
}

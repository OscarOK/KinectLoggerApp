package mx.uach.hcilab.kinectlogger.util;

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


    public static void uploadTherapist(Therapist therapist){
        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
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

        StorageHelper.uploadImage(therapist.getPicturePath(), therapist.getKey(), null);
    }

    public static void uploadPatient(Patient patient){
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

        StorageHelper.uploadImage(patient.getPicturePath(), patient.getKey(), null);
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

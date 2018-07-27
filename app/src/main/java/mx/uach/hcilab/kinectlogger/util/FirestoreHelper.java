package mx.uach.hcilab.kinectlogger.util;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import mx.uach.hcilab.kinectlogger.Therapist;

public class FirestoreHelper {
    private static final String TAG = "Firestore Helper";

    private static final String THERAPIST_COLLECTION = "therapists";

    public static void addTherapist(Therapist therapist){
        String uniqueKey = generateUniqueKey(therapist);

        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        CollectionReference collection = firestoreDB.collection(THERAPIST_COLLECTION);
        collection.document(uniqueKey).set(therapist.toMap())
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

        StorageHelper.uploadImage(therapist.getPicturePath(), uniqueKey, null);
    }

    public static String generateUniqueKey(Therapist therapist){
        String paternal = therapist.getPaternal();
        String maternal = therapist.getMaternal();
        String name = therapist.getName();

        return (paternal.substring(0, Math.min(paternal.length(), 4)) +
                maternal.substring(0, Math.min(maternal.length(), 4)) +
                name.substring(0, Math.min(name.length(), 4))).trim();
    }
}

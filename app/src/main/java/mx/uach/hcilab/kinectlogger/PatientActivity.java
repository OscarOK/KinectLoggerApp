package mx.uach.hcilab.kinectlogger;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.HashMap;

import javax.annotation.Nullable;

import mx.uach.hcilab.kinectlogger.adapter.FireStoreAdapter;
import mx.uach.hcilab.kinectlogger.games.RiverRushActivity;
import mx.uach.hcilab.kinectlogger.util.FirestoreHelper;
import mx.uach.hcilab.kinectlogger.util.GameLogger;
import mx.uach.hcilab.kinectlogger.util.RecyclerTouchListener;
import mx.uach.hcilab.kinectlogger.util.SelectorItemTransformer;
import mx.uach.hcilab.kinectlogger.util.StorageHelper;
import mx.uach.hcilab.kinectlogger.util.User;

public class PatientActivity extends AppCompatActivity {

    private static final String TAG = "Patient Activity";

    DiscreteScrollView mRecyclerView;
    FloatingActionButton mConfirmFab;
    TextView mWarningText;

    String therapistKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        therapistKey = getIntent().getStringExtra(Therapist.THERAPIST_KEY);

        if(getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (DiscreteScrollView) findViewById(R.id.patientRecyclerView);
        mConfirmFab = (FloatingActionButton) findViewById(R.id.patientConfirmFab);
        mWarningText = (TextView) findViewById(R.id.patientWarningText);

        mConfirmFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmSelection();
            }
        });

        fireStoreAdapter.startListening();
        mRecyclerView.setAdapter(fireStoreAdapter);

        mRecyclerView.setItemTransformer(new SelectorItemTransformer.Builder()
                .setMaxScale(1.1f)
                .setMinScale(0.6f)
                .build());
        mRecyclerView.setSlideOnFling(true);
        mRecyclerView.setSlideOnFlingThreshold(1050);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                mRecyclerView.smoothScrollToPosition(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }



    @Override
    protected void onResume() {
        FirebaseFirestore.getInstance().collection(FirestoreHelper.PATIENT_COLLECTION).limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().isEmpty()){
                            mWarningText.setVisibility(View.VISIBLE);
                        } else {
                            mWarningText.setVisibility(View.GONE);
                        }
                    }
                });
        fireStoreAdapter.startListening();
        super.onResume();
    }

    @Override
    protected void onStop() {
        fireStoreAdapter.stopListening();
        super.onStop();
    }

    FireStoreAdapter<PatientActivity.PatientViewHolder> fireStoreAdapter =
            new FireStoreAdapter<PatientViewHolder>(
            FirebaseFirestore.getInstance().collection(FirestoreHelper.PATIENT_COLLECTION).orderBy(Patient.NAME)
    ) {

        HashMap<String, String> images = new HashMap<>();

        @NonNull
        @Override
        public PatientActivity.PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            mWarningText.setVisibility(View.GONE);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
            return new PatientViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final @NonNull PatientViewHolder holder, int position) {
            DocumentSnapshot snapshot = this.getSnapshot(position);
            final String name = snapshot.getString(Patient.NAME);
            final String paternal = snapshot.getString(Patient.PATERNAL);
            final String maternal = snapshot.getString(Patient.MATERNAL);
            final String key = snapshot.getString(Patient.KEY);
            holder.text.setText(snapshot.getString(Patient.NAME));

            if(images.containsKey(key)){
                String filePath = images.get(key);
                Bitmap photo = BitmapFactory.decodeFile(filePath);
                holder.image.setImageBitmap(photo);
            } else {
                holder.image.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_pp));
                StorageHelper.downloadImage(key, new StorageHelper.OnDownloadImageListener() {
                    @Override
                    public void onSuccess(File file) {
                        String filePath = file.getPath();
                        Bitmap photo = BitmapFactory.decodeFile(filePath);
                        images.put(key, filePath);
                        holder.image.setImageBitmap(photo);
                    }

                    @Override
                    public void onFailure(Exception e) {
                    }
                });
            }
        }
    };

    public class PatientViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;
        public PatientViewHolder(View itemView) {
            super(itemView);
            this.image = (ImageView) itemView.findViewById(R.id.userItemImageView);
            this.text = (TextView) itemView.findViewById(R.id.userItemTextView);
        }
    }

    public void confirmSelection(){
        int position = mRecyclerView.getCurrentItem();
        if(position == -1) { //No patients exist
            return;
        }
        Log.d(TAG, "pos " + position + ", size " + fireStoreAdapter.getItemCount());
        DocumentSnapshot snapshot = fireStoreAdapter.getSnapshot(position);
        String key = snapshot.getString(User.KEY);

        Intent intent = new Intent(PatientActivity.this, GameSelectorActivity.class);
        intent.putExtra(Patient.PATIENT_KEY, key);
        intent.putExtra(Therapist.THERAPIST_KEY, therapistKey);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.patient_selector_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if(id == R.id.action_add_patient){
            Intent intent = new Intent(PatientActivity.this, PatientManagerActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}

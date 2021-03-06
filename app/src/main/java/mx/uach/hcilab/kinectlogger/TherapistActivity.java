package mx.uach.hcilab.kinectlogger;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.w3c.dom.Text;

import java.io.File;
import java.util.HashMap;

import butterknife.BindView;
import mx.uach.hcilab.kinectlogger.adapter.FireStoreAdapter;
import mx.uach.hcilab.kinectlogger.util.BitmapHelper;
import mx.uach.hcilab.kinectlogger.util.FirestoreHelper;
import mx.uach.hcilab.kinectlogger.util.RecyclerTouchListener;
import mx.uach.hcilab.kinectlogger.util.SelectorItemTransformer;
import mx.uach.hcilab.kinectlogger.util.StorageHelper;
import mx.uach.hcilab.kinectlogger.util.User;

public class TherapistActivity extends AppCompatActivity {

    private static final String TAG = "Therapist Activity";

    DiscreteScrollView mRecyclerView;
    FloatingActionButton mConfirmFab;
    TextView mWarningText;

    HashMap<String, String> images = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.therapist_title);
        setContentView(R.layout.activity_therapists);

        mRecyclerView = (DiscreteScrollView) findViewById(R.id.therapistRecyclerView);
        mConfirmFab = (FloatingActionButton) findViewById(R.id.therapistConfirmFab);
        mWarningText = (TextView) findViewById(R.id.therapistWarningText);

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
                //mRecyclerView.smoothScrollToPosition(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    protected void onResume() {
        fireStoreAdapter.startListening();
        FirebaseFirestore.getInstance().collection(FirestoreHelper.THERAPIST_COLLECTION).limit(1).get()
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
        super.onResume();
    }

    @Override
    protected void onStop() {
        fireStoreAdapter.stopListening();
        super.onStop();
    }

    FireStoreAdapter<TherapistViewHolder> fireStoreAdapter = new FireStoreAdapter<TherapistViewHolder>(
            FirebaseFirestore.getInstance().collection(FirestoreHelper.THERAPIST_COLLECTION).orderBy(Therapist.NAME)
    ) {

        @NonNull
        @Override
        public TherapistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            mWarningText.setVisibility(View.GONE);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
            return new TherapistViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final @NonNull TherapistViewHolder holder, int position) {
            DocumentSnapshot snapshot = this.getSnapshot(position);
            final String name = snapshot.getString(Therapist.NAME);
            final String paternal = snapshot.getString(Therapist.PATERNAL);
            final String maternal = snapshot.getString(Therapist.MATERNAL);
            final String key = snapshot.getString(Therapist.KEY);
            holder.text.setText(snapshot.getString(Therapist.NAME));

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
                        Log.e(TAG, e.getMessage());
                    }
                });
            }
        }
    };

    public class TherapistViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;
        public TherapistViewHolder(View itemView) {
            super(itemView);
            this.image = (ImageView) itemView.findViewById(R.id.userItemImageView);
            this.text = (TextView) itemView.findViewById(R.id.userItemTextView);
        }
    }

    public void confirmSelection(){
        int position = mRecyclerView.getCurrentItem();
        if(position == -1) { //No therapists exist
            return;
        }
        DocumentSnapshot snapshot = fireStoreAdapter.getSnapshot(position);
        String key = snapshot.getString(User.KEY);

        Intent intent = new Intent(TherapistActivity.this, PatientActivity.class);
        intent.putExtra(Therapist.THERAPIST_KEY, key);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.therapist_selector_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_add_therapist){
            Intent intent = new Intent(TherapistActivity.this, TherapistManagerActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}

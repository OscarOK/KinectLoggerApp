package mx.uach.hcilab.kinectlogger;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import mx.uach.hcilab.kinectlogger.adapter.GamesAdapter;
import mx.uach.hcilab.kinectlogger.games.LeaksActivity;
import mx.uach.hcilab.kinectlogger.games.RallyBall;
import mx.uach.hcilab.kinectlogger.games.ReflexRidgeActivity;
import mx.uach.hcilab.kinectlogger.games.RiverRushActivity;
import mx.uach.hcilab.kinectlogger.util.FirestoreHelper;
import mx.uach.hcilab.kinectlogger.util.GameCard;

public class GameSelectorActivity extends AppCompatActivity {

    private ArrayList<GameCard> gameCards;
    private RecyclerView recyclerViewGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selector);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            String therapistKey = getIntent().getStringExtra(Therapist.THERAPIST_KEY);
            String patientKey = getIntent().getStringExtra(Patient.PATIENT_KEY);
            final String names[] = new String[2];
            names[0] = "";
            names[1] = "";
            FirebaseFirestore.getInstance().collection(FirestoreHelper.THERAPIST_COLLECTION)
                    .document(therapistKey).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String name = documentSnapshot.getString(Therapist.NAME);
                    names[0] = name;
                    getSupportActionBar().setTitle("Juegos       /       Terapeuta: " + names[0]
                            + "       /       Paciente: " + names[1]);
                }
            });

            FirebaseFirestore.getInstance().collection(FirestoreHelper.PATIENT_COLLECTION)
                    .document(patientKey).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String name = documentSnapshot.getString(Patient.NAME);
                    names[1] = name;
                    getSupportActionBar().setTitle("Juegos       /       Terapeuta: " + names[0]
                            + "       /       Paciente: " + names[1]);
                }
            });
        }

        recyclerViewGames = findViewById(R.id.game_selector_recycle_view);
        recyclerViewGames.setLayoutManager(
                new LinearLayoutManager(this,
                        LinearLayoutManager.HORIZONTAL,
                        false)
        );

        gameCards = new ArrayList<>();
        gameCards.add(new GameCard(R.drawable.river_rush_landscape, R.string.river_rush_title, new Intent(this, RiverRushActivity.class).putExtras(getIntent())));
        gameCards.add(new GameCard(R.drawable.rally_ball_landscape, R.string.rally_ball_title, new Intent(this, RallyBall.class).putExtras(getIntent())));
        gameCards.add(new GameCard(R.drawable.reflex_ridge_landscape, R.string.reflex_ridge_title, new Intent(this, ReflexRidgeActivity.class).putExtras(getIntent())));
        gameCards.add(new GameCard(R.drawable.leaks_landscape, R.string.leaks_title, new Intent(this, LeaksActivity.class).putExtras(getIntent())));

        GamesAdapter gamesAdapter = new GamesAdapter(gameCards);
        recyclerViewGames.setAdapter(gamesAdapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerViewGames); //Snaps cards to the center of screen
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.game_selector_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(GameSelectorActivity.this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}

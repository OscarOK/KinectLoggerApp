package mx.uach.hcilab.kinectlogger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.MenuItem;

import java.util.ArrayList;

import mx.uach.hcilab.kinectlogger.adapter.GamesAdapter;
import mx.uach.hcilab.kinectlogger.games.LeaksActivity;
import mx.uach.hcilab.kinectlogger.games.RallyBall;
import mx.uach.hcilab.kinectlogger.games.ReflexRidgeActivity;
import mx.uach.hcilab.kinectlogger.games.RiverRushActivity;
import mx.uach.hcilab.kinectlogger.util.GameCard;

public class GameSelectorActivity extends AppCompatActivity {

    private ArrayList<GameCard> gameCards;
    private RecyclerView recyclerViewGames;

    private String therapistKey;
    private String patientKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selector);

        if(getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        therapistKey = getIntent().getStringExtra(Therapist.THERAPIST_KEY);
        patientKey = getIntent().getStringExtra(Patient.PATIENT_KEY);

        recyclerViewGames = findViewById(R.id.game_selector_recycle_view);
        recyclerViewGames.setLayoutManager(
                new LinearLayoutManager(this,
                        LinearLayoutManager.HORIZONTAL,
                        false)
        );

        String therapistKey = getIntent().getStringExtra(Therapist.THERAPIST_KEY);
        String patientKey = getIntent().getStringExtra(Patient.PATIENT_KEY);

        Intent intent = new Intent();
        intent.putExtra(Patient.PATIENT_KEY, patientKey);
        intent.putExtra(Therapist.THERAPIST_KEY, therapistKey);

        gameCards = new ArrayList<>();
        gameCards.add(new GameCard(R.drawable.river_rush_landscape, R.string.river_rush_title, intent.setClass(this, RiverRushActivity.class)));
        gameCards.add(new GameCard(R.drawable.rally_ball_landscape, R.string.rally_ball_title, intent.setClass(this, RallyBall.class)));
        gameCards.add(new GameCard(R.drawable.reflex_ridge_landscape, R.string.reflex_ridge_title, intent.setClass(this, ReflexRidgeActivity.class)));
        gameCards.add(new GameCard(R.drawable.leaks_landscape, R.string.leaks_title, intent.setClass(this, LeaksActivity.class)));

        GamesAdapter gamesAdapter = new GamesAdapter(gameCards);
        recyclerViewGames.setAdapter(gamesAdapter);



        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerViewGames); //Snaps cards to the center of screen
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

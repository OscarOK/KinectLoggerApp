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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selector);

        if(getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

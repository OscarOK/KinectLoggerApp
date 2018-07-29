package mx.uach.hcilab.kinectlogger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import mx.uach.hcilab.kinectlogger.adapter.GamesAdapter;

public class GameSelectorActivity extends AppCompatActivity {

    private ArrayList<GameCard> gameCards;
    private RecyclerView recyclerViewGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selector);

        recyclerViewGames = findViewById(R.id.game_selector_recycle_view);
        recyclerViewGames.setLayoutManager(
                new LinearLayoutManager(this,
                        LinearLayoutManager.HORIZONTAL,
                        false)
        );

        gameCards = new ArrayList<>();
        gameCards.add(new GameCard(R.drawable.river_rush_landscape, R.string.river_rush_title, new Intent(this, RiverRushActivity.class)));
        gameCards.add(new GameCard(R.drawable.rally_ball_landscape, R.string.rally_ball_title, new Intent(this, RallyBall.class)));
        gameCards.add(new GameCard(R.drawable.reflex_ridge_landscape, R.string.reflex_ridge_title, new Intent(this, ReflexRidgeActivity.class)));
        gameCards.add(new GameCard(R.drawable.leaks_landscape, R.string.leaks_title, new Intent(this, LeaksActivity.class)));

        GamesAdapter gamesAdapter = new GamesAdapter(gameCards);
        recyclerViewGames.setAdapter(gamesAdapter);
    }
}

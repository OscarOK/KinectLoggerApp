package mx.uach.hcilab.kinectlogger;

import android.graphics.PorterDuff;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class RiverRushActivity extends AppCompatActivity {

    private ImageButton badJump;
    private ImageButton inhibitionJump;
    private ImageButton goodJump;
    private ImageButton badMiddle;
    private ImageButton inhibitionMiddle;
    private ImageButton goodMiddle;
    private ImageButton badLeft;
    private ImageButton cloud;
    private ImageButton badRight;

    private boolean isHappy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_river_rush);

        // Menu
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cloud = findViewById(R.id.river_rush_cloud);

        cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isHappy) {
                    cloudEvent(R.drawable.ic_happy_cloud, R.color.colorAccent);
                    // TODO: START CHRONOMETER
                } else {
                    cloudEvent(R.drawable.ic_sad_cloud, android.R.color.white);
                    // TODO: STOP CHRONOMETER
                    // TODO: ADD TIME GOT TO LOG
                }

                isHappy = !isHappy;
            }
        });

        inhibitionMiddle = findViewById(R.id.river_rush_middle_inhibition);

        inhibitionMiddle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RiverRushActivity.this, "TAG: " + inhibitionMiddle.getTag().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.games_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.action_finish_reflex_ridge) {
            // TODO: OPEN POINTS FRAGMENT
        }

        return super.onOptionsItemSelected(item);
    }

    private void cloudEvent(int imageId, @ColorRes int color) {
        cloud.setImageResource(imageId);
        cloud.getBackground().setColorFilter(
                ContextCompat.getColor(
                        RiverRushActivity.this, color),
                PorterDuff.Mode.MULTIPLY);
    }

}

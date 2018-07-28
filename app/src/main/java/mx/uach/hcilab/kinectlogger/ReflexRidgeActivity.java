package mx.uach.hcilab.kinectlogger;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ReflexRidgeActivity extends AppCompatActivity {

    private boolean inhibitionFlag = false;
    private boolean badFlag        = false;

    private Button buttonBad;
    private Button buttonInhibition;

    private ImageButton imageButtons[] = new ImageButton[5];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflex_ridge);

        // Menu
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // TODO: OPEN level_selector_fragment
        // TODO: OPEN general_time_fragment
        // TODO: OPEN confirmation_fragment

        // Init actions buttons
        imageButtons[0] = findViewById(R.id.button_jump);
        imageButtons[1] = findViewById(R.id.button_right);
        imageButtons[2] = findViewById(R.id.button_squad);
        imageButtons[3] = findViewById(R.id.button_left);
        imageButtons[4] = findViewById(R.id.button_boost);

        // Init state buttons
        buttonBad = findViewById(R.id.button_bad);
        buttonInhibition = findViewById(R.id.button_inhibition);

        buttonBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inhibitionFlag) {
                    inhibitionFlag = false;
                }

                badFlag = true;
                coloringButtons(R.color.colorBadRed);
            }
        });

        buttonInhibition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (badFlag) {
                    badFlag = false;
                }

                inhibitionFlag = true;
                coloringButtons(R.color.colorInhibitionYellow);
            }
        });

        for (int i = 0; i < imageButtons.length; i++) {
            final int finalI = i;
            imageButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("ImageButton Action", String.valueOf(finalI));

                    if (badFlag) {
                        Log.i("ImageButton Status: ", "BAD");
                    } else if (inhibitionFlag) {
                        Log.i("ImageButton Status: ", "INHIBITION");
                    } else {
                        Log.i("ImageButton Status: ", "GOOD");
                    }

                    if (!badFlag || !inhibitionFlag) {
                        badFlag = false;
                        inhibitionFlag = false;
                        coloringButtons(R.color.colorGoodGreen);
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.reflex_ridge_menu, menu);
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

    private void coloringButtons(@ColorRes int id) {
        // THANKS TO SOJIN (https://stackoverflow.com/users/388889/sojin)
        // https://stackoverflow.com/questions/13842447/android-set-button-background-programmatically
        for (ImageButton imageButton : imageButtons) {
            imageButton.getBackground().setColorFilter(
                    ContextCompat.getColor(
                            ReflexRidgeActivity.this, id),
                    PorterDuff.Mode.MULTIPLY);
        }
    }
}

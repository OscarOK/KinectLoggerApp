package mx.uach.hcilab.kinectlogger;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import mx.uach.hcilab.kinectlogger.fragments.LevelSelector;

public class ReflexRidgeActivity extends AppCompatActivity implements LevelSelector.OnInputListener {

    private static final long RESPONSE_DELAY = 1000;

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
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        DialogFragment levelSelector = new LevelSelector();
        fragmentTransaction.add(levelSelector, "level_selector");
        fragmentTransaction.commit();




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

        @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                flagsDown();
                Log.e("DELAY ", "APPLIED");
            }
        };

        buttonBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacksAndMessages(null);

                if (inhibitionFlag) {
                    inhibitionFlag = false;
                }

                handler.sendEmptyMessageDelayed(0, RESPONSE_DELAY);

                badFlag = true;
                coloringButtons(R.color.colorBadRed);
            }
        });

        buttonInhibition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacksAndMessages(null);

                if (badFlag) {
                    badFlag = false;
                }

                handler.sendEmptyMessageDelayed(1, RESPONSE_DELAY);

                inhibitionFlag = true;
                coloringButtons(R.color.colorInhibitionYellow);
            }
        });

        for (int i = 0; i < imageButtons.length; i++) {
            final int finalI = i;
            imageButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handler.removeCallbacksAndMessages(null);
                    Log.i("ImageButton Action", String.valueOf(finalI));

                    if (badFlag) {
                        Log.i("ImageButton Status", "BAD");
                    } else if (inhibitionFlag) {
                        Log.i("ImageButton Status", "INHIBITION");
                    } else {
                        Log.i("ImageButton Status", "GOOD");
                    }

                    if (!badFlag || !inhibitionFlag) {
                        flagsDown();
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

    private void flagsDown() {
        badFlag = false;
        inhibitionFlag = false;
        coloringButtons(R.color.colorGoodGreen);
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

    @Override
    public void sendSelectedNumber(int number) {
        Toast.makeText(this, String.valueOf(number), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChoose() {
        finish();
    }
}

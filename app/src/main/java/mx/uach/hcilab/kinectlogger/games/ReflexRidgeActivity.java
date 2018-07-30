package mx.uach.hcilab.kinectlogger.games;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
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

import mx.uach.hcilab.kinectlogger.R;
import mx.uach.hcilab.kinectlogger.fragments.GeneralTimeSelector;
import mx.uach.hcilab.kinectlogger.fragments.LevelSelector;
import mx.uach.hcilab.kinectlogger.fragments.PointsSelector;

public class ReflexRidgeActivity extends AppCompatActivity implements
        LevelSelector.OnInputListener, GeneralTimeSelector.OnInputListener,
        PointsSelector.OnInputListener {

    private static final long RESPONSE_DELAY = 1000;

    private boolean inhibitionFlag = false;
    private boolean badFlag = false;

    private Button buttonBad;
    private Button buttonInhibition;

    private ImageButton imageButtons[] = new ImageButton[5];

    private FragmentManager fragmentManager;
    private DialogFragment[] fragments = new DialogFragment[3];
    private int fragmentIndex = 0;

    private static final int MAX_LEVEL = 9;
    private int selected_level = 1;
    private int general_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflex_ridge);

        // Menu
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Fragments stuff
        fragments[0] = LevelSelector.newInstance(MAX_LEVEL, selected_level);
        fragments[1] = GeneralTimeSelector.newInstance("0");
        fragments[2] = new PointsSelector();

        fragmentManager = getSupportFragmentManager();
        fragments[fragmentIndex].show(fragmentManager, "fragment_" + fragmentIndex);
        fragmentManager.beginTransaction().addToBackStack("add_fragment_" + fragmentIndex).commit();


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
        menuInflater.inflate(R.menu.games_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.action_finish_reflex_ridge) {
            fragments[fragmentIndex].show(fragmentManager, "fragment_" + fragmentIndex);
            fragmentManager.beginTransaction().addToBackStack("add_fragment_" + fragmentIndex).commit();
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
        selected_level = number;
        fragmentIndex++;
        if (fragmentIndex == 0) {
            fragments[fragmentIndex] = LevelSelector.newInstance(MAX_LEVEL, selected_level);
        } else if (fragmentIndex == 1) {
            fragments[fragmentIndex] = GeneralTimeSelector.newInstance(String.valueOf(general_time));
        }
        fragments[fragmentIndex].show(fragmentManager, "fragment_" + fragmentIndex);
        fragmentManager.beginTransaction().addToBackStack("add_fragment_" + fragmentIndex).commit();
    }

    @Override
    public void sendSelectedTime(int time) {
        general_time = time;
        fragmentIndex++;

        String message = getResources().getString(R.string.confirmation_message, selected_level, general_time);

        AlertDialog.Builder builder = new AlertDialog.Builder(ReflexRidgeActivity.this);
        builder.setTitle(R.string.confirmation_title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.fragment_start_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for(int j = 0; j < fragmentManager.getBackStackEntryCount(); j++) {
                    fragmentManager.popBackStack();
                }
                // TODO: START CHRONOMETER
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(R.string.fragment_back_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                goBack();
            }
        });
        builder.setNeutralButton(R.string.fragment_cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onChoose();
            }
        });
        Dialog dialogFragment = builder.create();
        dialogFragment.setCanceledOnTouchOutside(false);
        dialogFragment.show();
    }

    @Override
    public void sendSelectedPoints(int points) {
        Toast.makeText(this, String.valueOf(points), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void goBack() {
        fragmentIndex--;
        fragmentManager.popBackStack("fragment_" + fragmentIndex, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if (fragmentIndex == 0) {
            fragments[fragmentIndex] = LevelSelector.newInstance(MAX_LEVEL, selected_level);
        } else if (fragmentIndex == 1) {
            fragments[fragmentIndex] = GeneralTimeSelector.newInstance(String.valueOf(general_time));
        }
        fragments[fragmentIndex].show(fragmentManager, "fragment_" + fragmentIndex);
        fragmentManager.beginTransaction().addToBackStack("add_fragment_" + fragmentIndex).commit();
    }

    @Override
    public void onChoose() {
        finish();
    }
}

package mx.uach.hcilab.kinectlogger.games;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import mx.uach.hcilab.kinectlogger.Patient;
import mx.uach.hcilab.kinectlogger.R;
import mx.uach.hcilab.kinectlogger.Therapist;
import mx.uach.hcilab.kinectlogger.fragments.ConfirmFragment;
import mx.uach.hcilab.kinectlogger.fragments.GeneralTimeSelector;
import mx.uach.hcilab.kinectlogger.fragments.LevelSelector;
import mx.uach.hcilab.kinectlogger.fragments.PointsSelector;
import mx.uach.hcilab.kinectlogger.util.GameLogger;
import mx.uach.hcilab.kinectlogger.util.GameLogger.ReflexRidge.State;
import mx.uach.hcilab.kinectlogger.util.SoundPlayerHelper;

public class ReflexRidgeActivity extends AppCompatActivity implements
        LevelSelector.OnInputListener, GeneralTimeSelector.OnInputListener,
        PointsSelector.OnInputListener, ConfirmFragment.OnInputListener {

    private static final long RESPONSE_DELAY = 1000;

    private static final String TAG = "ReflexRidgeActivity";

    private boolean inhibitionFlag = false;
    private boolean badFlag = false;

    private FragmentManager fragmentManager;
    private DialogFragment[] fragments = new DialogFragment[3];
    private int fragmentIndex = 0;

    private static final int MAX_LEVEL = 9;
    private int levelSelected = 1;
    private int generalTime;

    private static Handler generalTimeHandler;
    private static Handler stateHandler;
    private GameLogger.ReflexRidge logger;
    private long gameTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflex_ridge);

        // Menu
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Fragments stuff
        fragments[0] = LevelSelector.newInstance(MAX_LEVEL, levelSelected);
        fragments[1] = GeneralTimeSelector.newInstance("0");
        fragments[2] = new PointsSelector();

        fragmentManager = getSupportFragmentManager();
        fragments[fragmentIndex].show(fragmentManager, "fragment_" + fragmentIndex);
        fragmentManager.beginTransaction().addToBackStack("add_fragment_" + fragmentIndex).commit();

        generalTimeHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                endSession();
            }
        };

        stateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                flagsDown();
            }
        };

        logger = new GameLogger.ReflexRidge(
                getIntent().getStringExtra(Therapist.THERAPIST_KEY),
                getIntent().getStringExtra(Patient.PATIENT_KEY)
        );
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
            // FINISH AND DO A ROLLBACK
            finish();
            // OR EQUIVALENT TO CLICK FINISH SESSION
            //endSession();

        } else if (id == R.id.action_finish_reflex_ridge) {
            endSession();
        }

        return super.onOptionsItemSelected(item);
    }

    private void flagsDown() {
        badFlag = false;
        inhibitionFlag = false;
        clearColorFilter();
    }

    public void logEvent(View v) {
        int id = v.getId();

        SoundPlayerHelper.playButtonSound(ReflexRidgeActivity.this);

        stateHandler.removeCallbacksAndMessages(null);

        State state = State.GOOD;

        if (badFlag) {
            state = State.BAD;
        } else if (inhibitionFlag) {
            state = State.INHIBITION;
        }

        switch (id) {
            case R.id.button_jump:
                logger.LogJump(state);
                break;
            case R.id.button_left:
                logger.LogLeft(state);
                break;
            case R.id.button_boost:
                logger.LogBoost(state);
                break;
            case R.id.button_right:
                logger.LogRight(state);
                break;
            case R.id.button_squad:
                logger.LogSquat(state);
                break;
            default:
                Log.i(TAG, "logEvent: NOT SUPPORTED ACTION");
        }

        if (badFlag || inhibitionFlag) {
            flagsDown();
        }
    }

    public void statusEvent(View v) {
        int id = v.getId();

        SoundPlayerHelper.playButtonSound(ReflexRidgeActivity.this);

        switch (id) {
            case R.id.button_bad:
                badFlag = true;
                applyStatus(inhibitionFlag, R.color.colorBadRed);
                break;
            case R.id.button_inhibition:
                inhibitionFlag = true;
                applyStatus(badFlag, R.color.colorInhibitionYellow);
                break;
            default:
                Log.i(TAG, "statusEvent: NOT SUPPORTED ACTION");
        }
    }

    private void endSession() {
        generalTimeHandler.removeCallbacksAndMessages(null);
        fragments[fragmentIndex].show(fragmentManager, "fragment_" + fragmentIndex);
        fragmentManager.beginTransaction().addToBackStack("add_fragment_" + fragmentIndex).commit();
        gameTime = System.nanoTime() - gameTime;
        gameTime /= 1000000000;
        logger.LogExtraTime((int) (generalTime - gameTime));
    }

    private void applyColorFilter(@ColorRes int color) {
        int[] imageButtons = {
                R.id.button_jump,
                R.id.button_left, R.id.button_boost, R.id.button_right,
                R.id.button_squad
        };

        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(
                getResources().getColor(color),
                PorterDuff.Mode.SRC_ATOP
        );

        for (int id : imageButtons) {
            ImageButton imageButton = findViewById(id);
            imageButton.getBackground().setColorFilter(colorFilter);
        }
    }

    private void clearColorFilter() {
        int[] imageButtons = {
                R.id.button_jump,
                R.id.button_left, R.id.button_boost, R.id.button_right,
                R.id.button_squad
        };

        for (int id : imageButtons) {
            ImageButton imageButton = findViewById(id);
            imageButton.getBackground().clearColorFilter();
        }
    }

    private void applyStatus(boolean otherFlag, @ColorRes int color) {
        stateHandler.removeCallbacksAndMessages(null);

        if (otherFlag) {
            otherFlag = false;
        }

        stateHandler.sendEmptyMessageDelayed(0, RESPONSE_DELAY);

        applyColorFilter(color);
    }

    @Override
    public void sendSelectedLevel(int level) {
        levelSelected = level;
        fragmentIndex++;
        if (fragmentIndex == 0) {
            fragments[fragmentIndex] = LevelSelector.newInstance(MAX_LEVEL, levelSelected);
        } else if (fragmentIndex == 1) {
            fragments[fragmentIndex] = GeneralTimeSelector.newInstance(String.valueOf(generalTime));
        }
        fragments[fragmentIndex].show(fragmentManager, "fragment_" + fragmentIndex);
        fragmentManager.beginTransaction().addToBackStack("add_fragment_" + fragmentIndex).commit();
    }

    @Override
    public void sendSelectedTime(int generalTime) {
        this.generalTime = generalTime;
        fragmentIndex++;

        DialogFragment confirmDialog = ConfirmFragment
                .newInstance(
                        getResources().getString(
                                R.string.confirmation_message, levelSelected, this.generalTime
                        ));

        confirmDialog.show(fragmentManager, "confirmation");
    }

    @Override
    public void sendSelectedPoints(int points) {
        logger.LogPoints(points);
        finish();
    }

    @Override
    public void confirmPressed() {
        for (int j = 0; j < fragmentManager.getBackStackEntryCount(); j++) {
            fragmentManager.popBackStack();
        }

        generalTimeHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                fragments[fragmentIndex].show(fragmentManager, "fragment_" + fragmentIndex);
                fragmentManager.beginTransaction().addToBackStack("add_fragment_" + fragmentIndex).commit();
            }
        };
        gameTime = System.nanoTime();
        generalTimeHandler.sendEmptyMessageDelayed(1, generalTime * 1000);
        logger.LogLevel(levelSelected);
        logger.LogGeneralTime(generalTime);
    }

    @Override
    public void goBack() {
        fragmentIndex--;
        fragmentManager.popBackStack("fragment_" + fragmentIndex, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if (fragmentIndex == 0) {
            fragments[fragmentIndex] = LevelSelector.newInstance(MAX_LEVEL, levelSelected);
        } else if (fragmentIndex == 1) {
            fragments[fragmentIndex] = GeneralTimeSelector.newInstance(String.valueOf(generalTime));
        }
        fragments[fragmentIndex].show(fragmentManager, "fragment_" + fragmentIndex);
        fragmentManager.beginTransaction().addToBackStack("add_fragment_" + fragmentIndex).commit();
    }

    @Override
    public void onChoose() {
        finish();
    }
}

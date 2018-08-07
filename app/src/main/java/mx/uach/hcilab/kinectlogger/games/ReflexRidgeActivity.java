package mx.uach.hcilab.kinectlogger.games;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorRes;
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
import mx.uach.hcilab.kinectlogger.util.AdderFragmentHelper;
import mx.uach.hcilab.kinectlogger.util.GameLogger;
import mx.uach.hcilab.kinectlogger.util.GameLogger.ReflexRidge.State;
import mx.uach.hcilab.kinectlogger.util.SoundPlayerHelper;

public class ReflexRidgeActivity extends AppCompatActivity implements
        LevelSelector.OnInputListener, GeneralTimeSelector.OnInputListener,
        PointsSelector.OnInputListener, ConfirmFragment.OnInputListener {

    private static final String TAG_STATUS_DELAY = "STATUS_DELAY_TIME";
    private static final String SHARED_PREFERENCES_NAME = "GAMES_PREFERENCES";
    private static long responseDelay;

    private static final String TAG = "ReflexRidgeActivity";

    private boolean inhibitionFlag = false;
    private boolean badFlag = false;

    private FragmentManager fragmentManager;
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

        // Set delay time
        responseDelay = (long) (1000 * getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE).getFloat(TAG_STATUS_DELAY, 1));

        // Fragments stuff
        fragmentManager = getSupportFragmentManager();
        AdderFragmentHelper.addLevelSelector(fragmentManager, MAX_LEVEL, levelSelected);

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
        gameTime = System.nanoTime() - gameTime;
        gameTime /= 1000000000;
        logger.LogExtraTime((int) (generalTime - gameTime));
        AdderFragmentHelper.addPointsSelector(fragmentManager);
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

        stateHandler.sendEmptyMessageDelayed(0, responseDelay);

        applyColorFilter(color);
    }

    @Override
    public void sendSelectedLevel(int level) {
        levelSelected = level;
        fragmentIndex++;
        AdderFragmentHelper.addGeneralTimeSelector(fragmentManager, String.valueOf(generalTime));
    }

    @Override
    public void sendSelectedTime(int generalTime) {
        this.generalTime = generalTime;
        fragmentIndex++;
        AdderFragmentHelper.addConfirmationFragment(
                fragmentManager,
                getResources().getString(R.string.confirmation_message, levelSelected, generalTime)
        );
    }

    @Override
    public void sendSelectedPoints(int points) {
        logger.LogPoints(points);
        finish();
    }

    @Override
    public void confirmPressed() {
        generalTimeHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                endSession();
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

        switch (fragmentIndex) {
            case 0:
                AdderFragmentHelper.addLevelSelector(fragmentManager, MAX_LEVEL, levelSelected);
                break;
            case 1:
                AdderFragmentHelper.addGeneralTimeSelector(fragmentManager, String.valueOf(generalTime));
            default:
                Log.e(TAG, "goBack: Fragment not supported");
        }
    }

    @Override
    public void onChoose() {
        finish();
    }
}

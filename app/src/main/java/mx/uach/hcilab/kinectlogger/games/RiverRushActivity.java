package mx.uach.hcilab.kinectlogger.games;

import android.graphics.PorterDuff;
import android.support.annotation.ColorRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import mx.uach.hcilab.kinectlogger.fragments.LevelSelector;
import mx.uach.hcilab.kinectlogger.fragments.PointsSelector;
import mx.uach.hcilab.kinectlogger.util.GameLogger;
import mx.uach.hcilab.kinectlogger.util.SoundPlayerHelper;

public class RiverRushActivity extends AppCompatActivity implements
        LevelSelector.OnInputListener, PointsSelector.OnInputListener,
        ConfirmFragment.OnInputListener {

    private static final String TAG = "RiverRushActivity";

    private static final int MAX_LEVEL = 6;
    private int selected_level = 1;

    private boolean isHappy = false;

    private FragmentManager fragmentManager;
    private DialogFragment[] fragments = new DialogFragment[2];
    private int fragmentIndex = 0;

    private GameLogger.RiverRush logger;

    private long cloudTime;
    private long gameTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_river_rush);

        // Menu
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Fragments stuff
        fragments[0] = LevelSelector.newInstance(MAX_LEVEL, selected_level);
        fragments[1] = new PointsSelector();

        fragmentManager = getSupportFragmentManager();
        fragments[fragmentIndex].show(fragmentManager, "fragment_" + fragmentIndex);
        fragmentManager.beginTransaction().addToBackStack("add_fragment_" + fragmentIndex).commit();

        // GameLogger Instance
        logger = new GameLogger.RiverRush(
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
            endSession();
        } else if (id == R.id.action_finish_reflex_ridge) {
            endSession();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * OnClick event for actions
     * @param v ImageButtons
     */
    public void logEvent(View v) {
        int id = v.getId();

        SoundPlayerHelper.playButtonSound(RiverRushActivity.this);

        switch (id) {
            // JUMP EVENTS
            case R.id.river_rush_jump_bad:
                logger.LogJump(GameLogger.RiverRush.State.BAD);
                break;
            case R.id.river_rush_jump_inhibition:
                logger.LogJump(GameLogger.RiverRush.State.INHIBITION);
                break;
            case R.id.river_rush_jump_good:
                logger.LogJump(GameLogger.RiverRush.State.GOOD);
                break;

            // MIDDLE EVENTS
            case R.id.river_rush_middle_bad:
                logger.LogMiddleBar(GameLogger.RiverRush.State.BAD);
                break;
            case R.id.river_rush_middle_inhibition:
                logger.LogMiddleBar(GameLogger.RiverRush.State.INHIBITION);
                break;
            case R.id.river_rush_middle_good:
                logger.LogMiddleBar(GameLogger.RiverRush.State.GOOD);
                break;

            // LEFT AND RIGHT BAR HIT
            case R.id.river_rush_left_bar:
                logger.LogLeftBar();
                break;
            case R.id.river_rush_right_bar:
                logger.LogRightBar();
                break;

            default:
                Log.i(TAG, "logEvent: NOT SUPPORTED ACTION");
        }
    }

    public void cloudClick(View v) {
        if (!isHappy) {
            // Change cloud button appearance
            cloudEvent(R.drawable.ic_happy_cloud, R.color.colorAccent);
            // Disable buttons
            changeButtonsAvailability();
            // Start cloud time
            cloudTime = System.nanoTime();
        } else {
            // Change cloud button appearance
            cloudEvent(R.drawable.ic_sad_cloud, android.R.color.white);
            // Able buttons
            changeButtonsAvailability();
            // Stop cloud time
            cloudTime = System.nanoTime() - cloudTime;
            // Log cloud time
            logger.LogCloudTime((int) (cloudTime / 1000000000));
        }

        isHappy = !isHappy;
    }

    private void cloudEvent(int imageId, @ColorRes int color) {
        ImageButton button = findViewById(R.id.river_rush_cloud);

        button.setImageResource(imageId);
        button.getBackground().setColorFilter(
                ContextCompat.getColor(
                        RiverRushActivity.this, color),
                PorterDuff.Mode.MULTIPLY);
    }

    private void changeButtonsAvailability() {
        // Array for disabled/able buttons
        int[] disableButtonsId = {
                R.id.river_rush_middle_bad, R.id.river_rush_middle_inhibition,
                R.id.river_rush_middle_good,
                R.id.river_rush_left_bar, R.id.river_rush_right_bar
        };

        for (int id : disableButtonsId) {
            ImageButton button = findViewById(id);
            button.setEnabled(isHappy);
        }
    }

    // FRAGMENTS INTERACTION METHODS
    @Override
    public void sendSelectedLevel(int level) {
        selected_level = level;
        fragmentIndex++;
        DialogFragment confirmDialog = ConfirmFragment
                .newInstance(
                        getResources().getString(
                                R.string.confirmation_message_no_time, selected_level
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
        logger.LogLevel(selected_level);
        gameTime = System.nanoTime();
    }

    private void endSession() {
        fragments[fragmentIndex].show(fragmentManager, "fragment_" + fragmentIndex);
        fragmentManager.beginTransaction().addToBackStack("add_fragment_" + fragmentIndex).commit();
        gameTime = System.nanoTime() - gameTime;
        if (isHappy) {
            cloudClick(new ImageButton(this).findViewById(R.id.river_rush_cloud));
        }
    }

    @Override
    public void goBack() {
        fragmentIndex--;
        fragmentManager.popBackStack("fragment_" + fragmentIndex, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if (fragmentIndex == 0) {
            fragments[fragmentIndex] = LevelSelector.newInstance(MAX_LEVEL, selected_level);
        }
        fragments[fragmentIndex].show(fragmentManager, "fragment_" + fragmentIndex);
        fragmentManager.beginTransaction().addToBackStack("add_fragment_" + fragmentIndex).commit();
    }

    @Override
    public void onChoose() {
        finish();
    }
}

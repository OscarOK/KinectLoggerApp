package mx.uach.hcilab.kinectlogger;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.support.annotation.ColorRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import mx.uach.hcilab.kinectlogger.fragments.GeneralTimeSelector;
import mx.uach.hcilab.kinectlogger.fragments.LevelSelector;
import mx.uach.hcilab.kinectlogger.fragments.PointsSelector;

public class RiverRushActivity extends AppCompatActivity implements LevelSelector.OnInputListener, PointsSelector.OnInputListener {

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

    private FragmentManager fragmentManager;
    private DialogFragment[] fragments = new DialogFragment[2];
    private int fragmentIndex = 0;

    private int selected_level = 1;
    private static final int MAX_LEVEL = 6;

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
            fragments[fragmentIndex].show(fragmentManager, "fragment_" + fragmentIndex);
            fragmentManager.beginTransaction().addToBackStack("add_fragment_" + fragmentIndex).commit();
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

    @Override
    public void sendSelectedNumber(int number) {
        selected_level = number;

        fragmentIndex++;

        String message = getResources().getString(R.string.confirmation_message_no_time, selected_level);

        AlertDialog.Builder builder = new AlertDialog.Builder(RiverRushActivity.this);
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
        finish();
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

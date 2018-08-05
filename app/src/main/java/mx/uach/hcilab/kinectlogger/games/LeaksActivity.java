package mx.uach.hcilab.kinectlogger.games;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import mx.uach.hcilab.kinectlogger.Patient;
import mx.uach.hcilab.kinectlogger.R;
import mx.uach.hcilab.kinectlogger.Therapist;
import mx.uach.hcilab.kinectlogger.fragments.ConfirmFragment;
import mx.uach.hcilab.kinectlogger.fragments.LevelSelector;
import mx.uach.hcilab.kinectlogger.util.GameLogger;

public class LeaksActivity extends AppCompatActivity implements LevelSelector.OnInputListener {

    ImageButton cabeza, torzo, bderecho, bizquierdo, pderecha, pizquierda;
    Button boton;
    private long gameTime,gameTime2,gameTime3;
    private GameLogger.Leaks leaks;
    int contador = 0, selected_level=1, time,timeHelper, generalTime=0;
    private static final int MAX_LEVEL = 9;
    ImageView vista;
    FrameLayout levelFragment;

    FragmentManager fragmentManager;
    LevelSelector fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaks);
        gameTime = System.nanoTime();

        fragment = LevelSelector.newInstance(MAX_LEVEL, selected_level);

        leaks = new GameLogger.Leaks(
                getIntent().getStringExtra(Therapist.THERAPIST_KEY),
                getIntent().getStringExtra(Patient.PATIENT_KEY)
        );

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        cabeza = (ImageButton) findViewById(R.id.cabeza);
        torzo = (ImageButton) findViewById(R.id.torzo);
        bderecho = (ImageButton) findViewById(R.id.bderecho);
        bizquierdo = (ImageButton) findViewById(R.id.bizquierdo);
        pderecha = (ImageButton) findViewById(R.id.pderecha);
        pizquierda = (ImageButton) findViewById(R.id.pizquierda);
        boton = (Button) findViewById(R.id.button);
        vista = (ImageView) findViewById(R.id.imageView);
        levelFragment = (FrameLayout) findViewById(R.id.fragmentContainer);


        fragment = new LevelSelector();
        fragmentManager = getSupportFragmentManager();
        fragment.show(fragmentManager, "fragment_");
        fragmentManager.beginTransaction().addToBackStack("add_fragment_").commit();


        cabeza.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction()== MotionEvent.ACTION_DOWN) {
                    leaks.LogBodyPart(GameLogger.Leaks.Parts.HEAD);
                    cabeza.setImageDrawable(getResources().getDrawable(R.drawable.ic_cabezav));
                }
                else{
                    cabeza.setImageDrawable(getResources().getDrawable(R.drawable.ic_cabeza));
                }
                return false;
            }
        });
        torzo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction()== MotionEvent.ACTION_DOWN) {
                    leaks.LogBodyPart(GameLogger.Leaks.Parts.CHEST);
                    torzo.setImageDrawable(getResources().getDrawable(R.drawable.ic_torzov));
                }
                else{
                    torzo.setImageDrawable(getResources().getDrawable(R.drawable.ic_torzo));
                }
                return false;
            }
        });
        bderecho.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction()== MotionEvent.ACTION_DOWN) {
                    leaks.LogBodyPart(GameLogger.Leaks.Parts.RIGHT_ARM);
                    bderecho.setImageDrawable(getResources().getDrawable(R.drawable.ic_bderechov));
                }
                else{
                    bderecho.setImageDrawable(getResources().getDrawable(R.drawable.ic_derecho));
                }
                return false;
            }
        });
        bizquierdo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction()== MotionEvent.ACTION_DOWN) {
                    leaks.LogBodyPart(GameLogger.Leaks.Parts.LEFT_ARM);
                    bizquierdo.setImageDrawable(getResources().getDrawable(R.drawable.ic_bizquierdov));
                }
                else{
                    bizquierdo.setImageDrawable(getResources().getDrawable(R.drawable.ic_izquierdo));
                }
                return false;
            }
        });
        pderecha.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction()== MotionEvent.ACTION_DOWN) {
                    leaks.LogBodyPart(GameLogger.Leaks.Parts.RIGHT_LEG);
                    pderecha.setImageDrawable(getResources().getDrawable(R.drawable.ic_pderechav));
                }
                else{
                    pderecha.setImageDrawable(getResources().getDrawable(R.drawable.ic_pderecha));
                }
                return false;
            }
        });
        pizquierda.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction()== MotionEvent.ACTION_DOWN) {
                    leaks.LogBodyPart(GameLogger.Leaks.Parts.LEFT_LEG);
                    pizquierda.setImageDrawable(getResources().getDrawable(R.drawable.ic_pizquierdav));
                }
                else{
                    pizquierda.setImageDrawable(getResources().getDrawable(R.drawable.ic_pizquierda));
                }
                return false;
            }
        });
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contador++;
                switch (contador){
                    case 1:
                        gameTime = System.nanoTime() - gameTime;
                        timeHelper = (int)(gameTime/1000000000);
                        time = 45 - timeHelper;
                        generalTime = timeHelper + generalTime;
                        if(time > 0) {
                            leaks.LogWaveTime(1, time);

                        }else{
                            leaks.LogWaveTime(1,0);
                        }
                        gameTime2 = System.nanoTime();
                        time=0;
                        timeHelper=0;
                        vista.setImageDrawable(getResources().getDrawable(R.drawable.ic_oleada2));
                        break;

                    case 2:
                        gameTime2 = System.nanoTime() - gameTime2;
                        timeHelper = (int) (gameTime2/1000000000);
                        time = 45 - timeHelper;
                        generalTime = timeHelper + generalTime;
                        if(time > 0) {
                            leaks.LogWaveTime(2, time);
                        }else{
                            leaks.LogWaveTime(2,0);
                        }
                        gameTime3 = System.nanoTime();
                        time = 0;
                        timeHelper = 0;
                        vista.setImageDrawable(getResources().getDrawable(R.drawable.ic_oleada3));

                        break;

                    case 3 :
                        gameTime3 = System.nanoTime() - gameTime3;
                        timeHelper = (int) (gameTime3/1000000000);
                        generalTime = timeHelper + generalTime;
                        time = 45 - timeHelper;
                        if(time > 0) {
                            leaks.LogWaveTime(3, time);
                        }else{
                            leaks.LogWaveTime(3,0);
                        }
                        leaks.LogGeneralTime(generalTime);
                        finish();
                }
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
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.action_finish_reflex_ridge) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChoose() {
        finish();
    }

    @Override
    public void goBack() {
        fragmentManager.popBackStack("fragment_", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragment.show(fragmentManager, "fragment_");
        fragmentManager.beginTransaction().addToBackStack("add_fragment_").commit();
    }


    @Override
    public void sendSelectedLevel(int level) {
        selected_level = level;
        String message = getResources().getString(R.string.confirmation_message_no_time, selected_level);

        AlertDialog.Builder builder = new AlertDialog.Builder(LeaksActivity.this);
        builder.setTitle(R.string.confirmation_title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.fragment_start_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int j = 0; j < fragmentManager.getBackStackEntryCount(); j++) {
                    fragmentManager.popBackStack();
                }

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
        leaks.LogLevel(selected_level);
    }


}

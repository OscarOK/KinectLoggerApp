package mx.uach.hcilab.kinectlogger.games;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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

import mx.uach.hcilab.kinectlogger.R;
import mx.uach.hcilab.kinectlogger.fragments.LevelSelector;
import mx.uach.hcilab.kinectlogger.Patient;
import mx.uach.hcilab.kinectlogger.Therapist;
import mx.uach.hcilab.kinectlogger.fragments.ConfirmFragment;
import mx.uach.hcilab.kinectlogger.util.GameLogger;

public class RallyBall extends AppCompatActivity implements LevelSelector.OnInputListener{

    ImageButton cabeza,torzo, bderecho, bizquierdo, pderecha, pizquierda;
    Button boton,boton2;
    private long gameTime , gameTime2, gameTime3;
    int contador=0,selected_level, time, timeHelper, generalTime;
    private GameLogger.RallyBall rally;
    ImageView vista;
    FrameLayout levelFragment;
    FragmentManager fragmentManager;
    LevelSelector fragment;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rally_ball);

        gameTime = System.nanoTime();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        rally = new mx.uach.hcilab.kinectlogger.util.GameLogger.RallyBall(
                getIntent().getStringExtra(Therapist.THERAPIST_KEY),
                getIntent().getStringExtra(Patient.PATIENT_KEY)
        );

        cabeza = (ImageButton) findViewById(R.id.cabeza);
        torzo = (ImageButton) findViewById(R.id.torzo);
        bderecho = (ImageButton) findViewById(R.id.bderecho);
        bizquierdo = (ImageButton) findViewById(R.id.bizquierdo);
        pderecha = (ImageButton) findViewById(R.id.pderecha);
        pizquierda = (ImageButton) findViewById(R.id.pizquierda);
        boton = (Button) findViewById(R.id.button);
        boton2 = (Button) findViewById(R.id.button2);
        vista = (ImageView) findViewById(R.id.imageView);
        levelFragment = (FrameLayout) findViewById(R.id.fragmentContainer);


        fragment= new LevelSelector();
        fragmentManager = getSupportFragmentManager();
        fragment.show(fragmentManager, "fragment_");
        fragmentManager.beginTransaction().addToBackStack("add_fragment_").commit();


        cabeza.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()== MotionEvent.ACTION_DOWN) {
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
                            rally.LogWaveTime(1, time);

                        }else{
                            rally.LogWaveTime(1,0);
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
                            rally.LogWaveTime(2, time);
                        }else{
                            rally.LogWaveTime(2,0);
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
                            rally.LogWaveTime(3, time);
                        }else{
                            rally.LogWaveTime(3,0);
                        }
                        rally.LogGeneralTime(generalTime);
                        finish();
                }
            }
        });

        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        AlertDialog.Builder builder = new AlertDialog.Builder(RallyBall.this);
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


}


package mx.uach.hcilab.kinectlogger;

import android.annotation.SuppressLint;
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

public class RallyBall extends AppCompatActivity implements LevelSelectorFragment.OnFragmentInteractionListener{

    ImageButton cabeza,torzo, bderecho, bizquierdo, pderecha, pizquierda;
    Button boton,boton2;
    int contador=0;
    ImageView vista;
    FrameLayout levelFragment;
    LevelSelectorFragment fragment;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rally_ball);

        getSupportActionBar().setTitle("Rally Ball"); // for set actionbar title

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        fragment= new LevelSelectorFragment();
        transaction.add(R.id.fragmentContainer, fragment);
        transaction.commit();


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
                        vista.setImageDrawable(getResources().getDrawable(R.drawable.ic_oleada2));
                        break;

                    case 2:
                        vista.setImageDrawable(getResources().getDrawable(R.drawable.ic_oleada3));
                        break;

                    case 3 : finish();
                }
            }
        });

        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.games_menu, menu);
        return true;
    }

    @Override
    public void onClickSelection() {
        levelFragment.setClickable(false);
        levelFragment.setFocusable(false);
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }
}


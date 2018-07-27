package mx.uach.hcilab.kinectlogger;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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

        imageButtons[0] = findViewById(R.id.button_jump);
        imageButtons[1] = findViewById(R.id.button_right);
        imageButtons[2] = findViewById(R.id.button_squad);
        imageButtons[3] = findViewById(R.id.button_left);
        imageButtons[4] = findViewById(R.id.button_boost);

        buttonBad        = findViewById(R.id.button_bad);
        buttonInhibition = findViewById(R.id.button_inhibition);

        buttonBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coloringButtons(R.color.colorBadRed);
            }
        });

        buttonInhibition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coloringButtons(R.color.colorInhibitionYellow);
            }
        });
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

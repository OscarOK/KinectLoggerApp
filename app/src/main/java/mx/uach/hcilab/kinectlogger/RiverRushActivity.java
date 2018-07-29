package mx.uach.hcilab.kinectlogger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

public class RiverRushActivity extends AppCompatActivity {

    private ImageButton badJump;
    private ImageButton inhibitionJump;
    private ImageButton goodJump;
    private ImageButton badMiddle;
    private ImageButton inhibitionMiddle;
    private ImageButton goodMiddle;
    private ImageButton badLeft;
    private ImageButton cloud;
    private ImageButton badRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_river_rush);

        cloud = findViewById(R.id.river_rush_cloud);
    }


}

package mx.uach.hcilab.kinectlogger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.BindView;

public class PatientManagerActivity extends AppCompatActivity {

    @BindView(R.)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients_manager);
        setTitle(getString(R.string.patient_manager_activity_title));
    }
}

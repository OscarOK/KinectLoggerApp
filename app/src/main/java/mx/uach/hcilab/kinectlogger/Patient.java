package mx.uach.hcilab.kinectlogger;

import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

import mx.uach.hcilab.kinectlogger.util.User;

public class Patient extends User{

    public static final String PATIENT_KEY = "patient_key";

    Patient(String name, String paternal, String maternal, Uri picturePath) {
        super(name, paternal, maternal, picturePath);
    }

    Patient(String name, String paternal, String maternal) {
        super(name, paternal, maternal);
    }
}

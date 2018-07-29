package mx.uach.hcilab.kinectlogger;

import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

import mx.uach.hcilab.kinectlogger.util.User;

public class Therapist extends User{

    public static final String THERAPIST_KEY = "therapist_key";

    public Therapist(String name, String paternal, String maternal, Uri picturePath) {
        super(name, paternal, maternal, picturePath);
    }

    public Therapist(String name, String paternal, String maternal) {
        super(name, paternal, maternal);
    }
}

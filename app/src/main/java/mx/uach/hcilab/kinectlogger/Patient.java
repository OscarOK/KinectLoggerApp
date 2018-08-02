package mx.uach.hcilab.kinectlogger;

import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

import mx.uach.hcilab.kinectlogger.util.User;

public class Patient extends User{

    public static final String PATIENT_KEY = "patient_key";
    public static final String BIRTHDAY = "birthday";
    private String birthday;

    Patient(String name, String paternal, String maternal, String birthday, Uri picturePath) {
        super(name, paternal, maternal, picturePath);
        this.birthday = birthday;
    }

    Patient(String name, String paternal, String maternal, String birthday) {
        super(name, paternal, maternal);
        this.birthday = birthday;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> map = (HashMap)super.toMap();
        map.put(BIRTHDAY, this.birthday);
        return  map;


    }
}

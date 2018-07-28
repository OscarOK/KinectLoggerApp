package mx.uach.hcilab.kinectlogger.util;

import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

public class User {

    private static final String NAME = "name";
    private static final String PATERNAL = "paternal";
    private static final String MATERNAL = "maternal";

    private String name, paternal, maternal;
    private Uri picturePath;

    public User(String name, String paternal, String maternal, Uri picturePath){
        this.name = name;
        this.paternal = paternal;
        this.maternal = maternal;
        this.picturePath = picturePath;
    }

    public User(String name, String paternal, String maternal){
        this.name = name;
        this.paternal = paternal;
        this.maternal = maternal;
    }

    public void setName(String name){ this.name = name; }
    public String getName() { return this.name; }

    public void setPaternal(String paternal){ this.paternal = paternal; }
    public String getPaternal() { return this.paternal; }

    public void setMaternal(String maternal){ this.maternal = maternal; }
    public String getMaternal() { return this.maternal; }

    public void setPicturePath(Uri picturePath) { this.picturePath = picturePath; }
    public Uri getPicturePath() { return this.picturePath; }

    public Map<String, Object> toMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put(NAME, this.name);
        map.put(PATERNAL, this.paternal);
        map.put(MATERNAL, this.maternal);
        return map;
    }

}

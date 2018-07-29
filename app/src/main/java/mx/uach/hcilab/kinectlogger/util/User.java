package mx.uach.hcilab.kinectlogger.util;

import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

public class User {

    public static final String NAME = "name";
    public static final String PATERNAL = "paternal";
    public static final String MATERNAL = "maternal";
    public static final String KEY = "key";

    private String name, paternal, maternal, key;
    private Uri picturePath;

    public User(String name, String paternal, String maternal, Uri picturePath){
        this.name = name;
        this.paternal = paternal;
        this.maternal = maternal;
        this.picturePath = picturePath;
        this.key = FirestoreHelper.generateUniqueKey(this);
    }

    public User(String name, String paternal, String maternal){
        this.name = name;
        this.paternal = paternal;
        this.maternal = maternal;
        this.key = FirestoreHelper.generateUniqueKey(this);
    }

    public void setName(String name){ this.name = name; }
    public String getName() { return this.name; }

    public void setPaternal(String paternal){ this.paternal = paternal; }
    public String getPaternal() { return this.paternal; }

    public void setMaternal(String maternal){ this.maternal = maternal; }
    public String getMaternal() { return this.maternal; }

    public void setPicturePath(Uri picturePath) { this.picturePath = picturePath; }
    public Uri getPicturePath() { return this.picturePath; }

    public void setKey(String key) { this.key = key; }
    public String getKey() { return this.key; }

    public Map<String, Object> toMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put(KEY, this.key);
        map.put(NAME, this.name);
        map.put(PATERNAL, this.paternal);
        map.put(MATERNAL, this.maternal);
        return map;
    }

}

package mx.uach.hcilab.kinectlogger.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import mx.uach.hcilab.kinectlogger.R;

public class SoundPlayerHelper {
    public static void playButtonSound(Context context){
        MediaPlayer.create(context, R.raw.bell).start();
    }
}

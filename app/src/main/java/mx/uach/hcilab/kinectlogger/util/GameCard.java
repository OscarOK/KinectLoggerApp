package mx.uach.hcilab.kinectlogger.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class GameCard {
    private int idThumbnail;
    private int title;
    private Intent intent;

    public GameCard(int idThumbnail, int title, Intent intent) {
        this.idThumbnail = idThumbnail;
        this.title = title;
        this.intent = intent;
    }

    public int getIdThumbnail() {
        return idThumbnail;
    }

    public void setIdThumbnail(int idThumbnail) {
        this.idThumbnail = idThumbnail;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }
}

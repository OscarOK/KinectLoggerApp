package mx.uach.hcilab.kinectlogger.util;


import android.support.v4.app.FragmentManager;

import mx.uach.hcilab.kinectlogger.fragments.ConfirmFragment;
import mx.uach.hcilab.kinectlogger.fragments.GeneralTimeSelector;
import mx.uach.hcilab.kinectlogger.fragments.LevelSelector;
import mx.uach.hcilab.kinectlogger.fragments.PointsSelector;

public class AdderFragmentHelper {
    public static void addLevelSelector(FragmentManager fragmentManager, int MAX_LEVEL, int selectedLevel) {
        LevelSelector levelSelector = LevelSelector.newInstance(MAX_LEVEL, selectedLevel);
        fragmentManager.beginTransaction()
                .add(levelSelector, "levelSelector")
                .commit();
    }

    public static void addGeneralTimeSelector(FragmentManager fragmentManager, String display) {
        GeneralTimeSelector generalTimeSelector = GeneralTimeSelector.newInstance(display);
        fragmentManager.beginTransaction()
                .add(generalTimeSelector, "generalTimeSelector")
                .commit();
    }

    public static void addConfirmationFragment(FragmentManager fragmentManager, String message) {
        ConfirmFragment confirmFragment = ConfirmFragment.newInstance(message);
        fragmentManager.beginTransaction()
                .add(confirmFragment, "confirmationFragment")
                .commit();
    }

    public static void addPointsSelector(FragmentManager fragmentManager) {
        PointsSelector pointsSelector = PointsSelector.newInstance();
        fragmentManager.beginTransaction()
                .add(pointsSelector, "pointsSelector")
                .commit();
    }
}

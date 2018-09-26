package mx.uach.hcilab.kinectlogger.util;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameLogger {

    public static final String TAG = "GameLogger";

    public static class RiverRush {

        private static final String GAME_TAG = "RiverRushTest";

        public enum State {
            GOOD, BAD, INHIBITION
        }

        private DocumentReference sessionReference = null;

        private String therapistKey, patientKey;
        private long date;

        private int goodJump = 0,      badJump = 0,      inhibitionJump = 0;
        private int goodMiddleBar = 0, badMiddleBar = 0, inhibitionMiddleBar = 0;
        private int leftBar = 0,       rightBar = 0;
        private int level = 0,         points = 0;
        private List<Integer> cloudTimes = new ArrayList<>();

        public RiverRush (final String therapistKey, final String patientKey) {
            this.therapistKey = therapistKey;
            this.patientKey = patientKey;
            this.date = System.currentTimeMillis();

            final CollectionReference riverRushCollection = FirebaseFirestore.getInstance().collection(GAME_TAG);
            sessionReference = riverRushCollection.document();
            sessionReference.set(this.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    riverRushCollection.whereEqualTo("open", true).whereEqualTo("paired", false)
                            .whereEqualTo("patientKey", patientKey).limit(2).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        List<DocumentSnapshot> list = task.getResult().getDocuments();
                                        for(DocumentSnapshot doc : list) {
                                            if(doc.getReference().getId().equals(sessionReference.getId())) continue;

                                            if (doc.exists()) {
                                                DocumentReference ref = doc.getReference();
                                                sessionReference.update("paired", true);
                                                sessionReference.update("pairKey", ref.getId());
                                                ref.update("paired", true);
                                                ref.update("pairKey", sessionReference.getId());
                                                Log.d(TAG, "paired correctly, keys: " + sessionReference.getId() + " - " + ref.getId());
                                                break;
                                            } else {
                                                Log.d(TAG, "document doesn't exist");
                                            }
                                        }
                                    } else {
                                        Log.d(TAG, "task unsuccessful");
                                    }
                                }
                            });
                }
            });


        }

        public void LogJump (State state) {
            switch (state){
                case GOOD: goodJump++; sessionReference.update("goodJumps", goodJump); break;
                case BAD: badJump++; sessionReference.update("badJumps", badJump); break;
                case INHIBITION: inhibitionJump++; sessionReference.update("inhibitionJumps", inhibitionJump); break;
            }
        }

        public void LogMiddleBar (State state) {
            switch (state){
                case GOOD: goodMiddleBar++; sessionReference.update("goodMiddleBars", goodMiddleBar); break;
                case BAD: badMiddleBar++; sessionReference.update("badMiddleBars", badMiddleBar); break;
                case INHIBITION: inhibitionMiddleBar++; sessionReference.update("inhibitionMiddleBars", inhibitionMiddleBar); break;
            }
        }

        public void LogLeftBar () {
            leftBar++;
            sessionReference.update("leftBars", leftBar);
        }

        public void LogRightBar () {
            rightBar++;
            sessionReference.update("rightBars", rightBar);
        }

        public void LogCloudTime (int time) {
            cloudTimes.add(time);
            sessionReference.update("cloudTimes", cloudTimes);
        }

        public void LogPoints (int points) {
            this.points = points;
            sessionReference.update("points", points,
                    "open", false);
        }

        public void LogLevel (int level) {
            this.level = level;
            sessionReference.update("level", level);
        }

        public void CloseLog(){
            sessionReference.update("open", false);
        }

        private Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("therapistKey", therapistKey);
            map.put("patientKey", patientKey);
            map.put("date", date);

            map.put("goodJumps", goodJump);
            map.put("badJumps", badJump);
            map.put("inhibitionJumps", inhibitionJump);
            map.put("goodMiddleBars", goodMiddleBar);
            map.put("badMiddleBars", badMiddleBar);
            map.put("inhibitionMiddleBars", inhibitionMiddleBar);
            map.put("leftBars", leftBar);
            map.put("rightBars", rightBar);

            map.put("level", level);
            map.put("points", points);
            map.put("cloudTimes", cloudTimes);

            map.put("open", true);
            map.put("paired", false);
            map.put("pairKey", null);

            return map;
        }

    }

    public static class ReflexRidge {

        private static final String GAME_TAG = "ReflexRidgeTest";

        public enum State {
            GOOD, BAD, INHIBITION
        }

        DocumentReference sessionReference;

        private String therapistKey, patientKey;
        private long date;

//        private boolean open = true;

        private int goodJump = 0,  badJump = 0,  inhibitionJump = 0;
        private int goodLeft = 0,  badLeft = 0,  inhibitionLeft = 0;
        private int goodBoost = 0, badBoost = 0, inhibitionBoost = 0;
        private int goodRight = 0, badRight = 0, inhibitionRight = 0;
        private int goodSquat = 0, badSquat = 0, inhibitionSquat = 0;
        private int level = 0;
        private int generalTime = 0;
        private int extraTime = 0;
        private int points = 0;

        public ReflexRidge (final String therapistKey, final String patientKey) {
            this.therapistKey = therapistKey;
            this.patientKey = patientKey;
            date = System.currentTimeMillis();

            final CollectionReference reflexRidgeCollection = FirebaseFirestore.getInstance().collection(GAME_TAG);
            sessionReference = reflexRidgeCollection.document();
            sessionReference.set(this.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d(TAG, "Debug 1");
                    reflexRidgeCollection.whereEqualTo("open", true).whereEqualTo("paired", false)
                            .whereEqualTo("patientKey", patientKey).limit(2).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        List<DocumentSnapshot> list = task.getResult().getDocuments();
                                        for(DocumentSnapshot doc : list) {
                                            if(doc.getReference().getId().equals(sessionReference.getId())) continue;

                                            if (doc.exists()) {
                                                DocumentReference ref = doc.getReference();
                                                sessionReference.update("paired", true);
                                                sessionReference.update("pairKey", ref.getId());
                                                ref.update("paired", true);
                                                ref.update("pairKey", sessionReference.getId());
                                                Log.d(TAG, "paired correctly, keys: " + sessionReference.getId() + " - " + ref.getId());
                                                break;
                                            } else {
                                                Log.d(TAG, "document doesn't exist");
                                            }
                                        }
                                    } else {
                                        Log.d(TAG, "task unsuccessful");
                                    }
                                }
                            });
                }
            });

        }

        public void LogJump (State state) {
            switch (state){
                case GOOD: goodJump++; sessionReference.update("goodJumps", goodJump); break;
                case BAD: badJump++; sessionReference.update("badJumps", badJump); break;
                case INHIBITION: inhibitionJump++; sessionReference.update("inhibitionJumps", inhibitionJump); break;
            }
        }

        public void LogLeft (State state) {
            switch (state){
                case GOOD: goodLeft++; sessionReference.update("goodLefts", goodLeft); break;
                case BAD: badLeft++; sessionReference.update("badLefts", badLeft); break;
                case INHIBITION: inhibitionLeft++; sessionReference.update("inhibitionLefts", inhibitionLeft); break;
            }
        }

        public void LogBoost (State state) {
            switch (state){
                case GOOD: goodBoost++; sessionReference.update("goodBoosts", goodBoost); break;
                case BAD: badBoost++; sessionReference.update("badBoosts", badBoost); break;
                case INHIBITION: inhibitionBoost++; sessionReference.update("inhibitionBoosts", inhibitionBoost); break;
            }
        }

        public void LogRight (State state) {
            switch (state){
                case GOOD: goodRight++; sessionReference.update("goodRights", goodRight); break;
                case BAD: badRight++; sessionReference.update("badRights", badRight); break;
                case INHIBITION: inhibitionRight++; sessionReference.update("inhibitionRights", inhibitionRight); break;
            }
        }

        public void LogSquat (State state) {
            switch (state){
                case GOOD: goodSquat++; sessionReference.update("goodSquats", goodSquat); break;
                case BAD: badSquat++; sessionReference.update("badSquats", badSquat); break;
                case INHIBITION: inhibitionSquat++; sessionReference.update("inhibitionSquats", inhibitionSquat); break;
            }
        }

        public void LogLevel (int level) {
            this.level = level;
            sessionReference.update("level", level);
        }

        public void LogGeneralTime (int time) {
            this.generalTime = time;
            sessionReference.update("generalTime", generalTime);
        }

        public void LogExtraTime (int time) {
            this.extraTime = time;
            sessionReference.update("extraTime", extraTime);
        }

        public void LogPoints (int points) {
            this.points = points;
            sessionReference.update(
                    "points", points,
                    "open", false);
        }

        public void CloseLog () {
            sessionReference.update("open", false);
        }

        private Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("therapistKey", therapistKey);
            map.put("patientKey", patientKey);
            map.put("date", date);
            map.put("goodJumps", goodJump);
            map.put("badJumps", badJump);
            map.put("inhibitionJumps", inhibitionJump);
            map.put("goodLefts", goodLeft);
            map.put("badLefts", badLeft);
            map.put("inhibitionLefts", inhibitionLeft);
            map.put("goodBoosts", goodBoost);
            map.put("badBoosts", badBoost);
            map.put("inhibitionBoosts", inhibitionBoost);
            map.put("goodRights", goodRight);
            map.put("badRights", badRight);
            map.put("inhibitionRights", inhibitionRight);
            map.put("goodSquats", goodSquat);
            map.put("badSquats", badSquat);
            map.put("inhibitionSquats", inhibitionSquat);
            map.put("level", level);
            map.put("generalTime", generalTime);
            map.put("extraTime", extraTime);
            map.put("points", points);
            map.put("open", true);
            map.put("paired", false);
            map.put("pairKey", null);

            return map;
        }
    }

    public static class RallyBall {

        private static final String GAME_TAG = "RallyBallTest";

        public enum Parts {
            HEAD, LEFT_ARM, RIGHT_ARM, CHEST, LEFT_LEG, RIGHT_LEG
        }

        DocumentReference sessionReference;

        private String therapistKey, patientKey;
        private long date;

        private int head = 0, leftArm = 0, rightArm = 0, chest = 0, leftLeg = 0, rightLeg = 0;
        private int inhibition = 0;

        private int firstWaveTime = 0, secondWaveTime = 0, thirdWaveTime = 0;
        private int generalTime = 0;
        private int level = 0;
        private int points = 0;

        public RallyBall (final String therapistKey, final String patientKey) {
            this.therapistKey = therapistKey;
            this.patientKey = patientKey;
            this.date = System.currentTimeMillis();

            final CollectionReference rallyBallCollection = FirebaseFirestore.getInstance().collection(GAME_TAG);
            sessionReference = rallyBallCollection.document();
            sessionReference.set(this.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                rallyBallCollection.whereEqualTo("open", true).whereEqualTo("paired", false)
                        .whereEqualTo("patientKey", patientKey).limit(2).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    List<DocumentSnapshot> list = task.getResult().getDocuments();
                                    for(DocumentSnapshot doc : list) {
                                        if(doc.getReference().getId().equals(sessionReference.getId())) continue;

                                        if (doc.exists()) {
                                            DocumentReference ref = doc.getReference();
                                            sessionReference.update("paired", true);
                                            sessionReference.update("pairKey", ref.getId());
                                            ref.update("paired", true);
                                            ref.update("pairKey", sessionReference.getId());
                                            Log.d(TAG, "paired correctly, keys: " + sessionReference.getId() + " - " + ref.getId());
                                            break;
                                        } else {
                                            Log.d(TAG, "document doesn't exist");
                                        }
                                    }
                                }
                            }
                        });
                    }
                });
        }

        public void LogInhibition(){
            inhibition++;
            sessionReference.update("inhibitions", inhibition);
        }

        public void LogBodyPart (Parts part) {
            switch (part){
                case HEAD:      head++;     sessionReference.update("heads", head); break;
                case LEFT_ARM:  leftArm++;  sessionReference.update("leftArms", leftArm); break;
                case RIGHT_ARM: rightArm++; sessionReference.update("rightArms", rightArm); break;
                case CHEST:     chest++;    sessionReference.update("chests", chest); break;
                case LEFT_LEG:  leftLeg++;  sessionReference.update("leftLegs", leftLeg); break;
                case RIGHT_LEG: rightLeg++; sessionReference.update("rightLegs", rightLeg); break;
            }
        }

        public void LogWaveTime (int wave, int time) {
            switch (wave) {
                case 1: firstWaveTime = time;  sessionReference.update("firstWaveTime", firstWaveTime); break;
                case 2: secondWaveTime = time; sessionReference.update("secondWaveTime", secondWaveTime); break;
                case 3: thirdWaveTime = time;  sessionReference.update("thirdWaveTime", thirdWaveTime); break;
            }
        }

        public void LogGeneralTime (int time) {
            this.generalTime = time;
            sessionReference.update("generalTime", generalTime);
        }

        public void LogLevel (int level) {
            this.level = level;
            sessionReference.update("level", level);
        }

        public void LogPoints(int points){
            this.points = points;
            sessionReference.update("points", points,
                    "open", false);
        }

        public void CloseLog () {
            sessionReference.update("open", false);
        }

        private Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("therapistKey", therapistKey);
            map.put("patientKey", patientKey);
            map.put("date", date);

            map.put("heads", head);
            map.put("leftArms", leftArm);
            map.put("rightArms", rightArm);
            map.put("chests", chest);
            map.put("leftLegs", leftLeg);
            map.put("rightLegs", rightLeg);

            map.put("inhibitions", inhibition);

            map.put("firstWaveTime", firstWaveTime);
            map.put("secondWaveTime", secondWaveTime);
            map.put("thirdWaveTime", thirdWaveTime);

            map.put("generalTime", generalTime);
            map.put("level", level);
            map.put("points", points);

            map.put("open", true);
            map.put("paired", false);
            map.put("pairKey", null);

            return map;
        }

    }

    public static class Leaks {

        private static final String GAME_TAG = "LeaksTest";

        public enum Parts {
            HEAD, LEFT_ARM, RIGHT_ARM, CHEST, LEFT_LEG, RIGHT_LEG
        }

        DocumentReference sessionReference;

        private String therapistKey, patientKey;
        private long date;

        private int head = 0, leftArm = 0, rightArm = 0, chest = 0, leftLeg = 0, rightLeg = 0;
        private int firstWaveTime = 0, secondWaveTime = 0, thirdWaveTime = 0;
        private int generalTime = 0;
        private int level = 0;
        private int points = 0;

        public Leaks (final String therapistKey, final String patientKey) {
            this.therapistKey = therapistKey;
            this.patientKey = patientKey;
            this.date = System.currentTimeMillis();

            final CollectionReference leaksCollection = FirebaseFirestore.getInstance().collection(GAME_TAG);
            sessionReference = leaksCollection.document();
            sessionReference.set(this.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    leaksCollection.whereEqualTo("open", true).whereEqualTo("paired", false)
                            .whereEqualTo("patientKey", patientKey).limit(2).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        List<DocumentSnapshot> list = task.getResult().getDocuments();
                                        for(DocumentSnapshot doc : list) {
                                            if(doc.getReference().getId().equals(sessionReference.getId())) continue;

                                            if (doc.exists()) {
                                                DocumentReference ref = doc.getReference();
                                                sessionReference.update("paired", true);
                                                sessionReference.update("pairKey", ref.getId());
                                                ref.update("paired", true);
                                                ref.update("pairKey", sessionReference.getId());
                                                Log.d(TAG, "paired correctly, keys: " + sessionReference.getId() + " - " + ref.getId());
                                                break;
                                            } else {
                                                Log.d(TAG, "document doesn't exist");
                                            }
                                        }
                                    }
                                }
                            });
                }
            });
        }

        public void LogBodyPart (Parts part) {
            switch (part){
                case HEAD: head++;          sessionReference.update("heads", head); break;
                case LEFT_ARM: leftArm++;   sessionReference.update("leftArms", leftArm); break;
                case RIGHT_ARM: rightArm++; sessionReference.update("rightArms", rightArm); break;
                case CHEST: chest++;        sessionReference.update("chests", chest); break;
                case LEFT_LEG: leftLeg++;   sessionReference.update("leftLegs", leftLeg); break;
                case RIGHT_LEG: rightLeg++; sessionReference.update("rightLegs", rightLeg); break;
            }
        }

        public void LogWaveTime (int wave, int time) {
            switch (wave) {
                case 1: firstWaveTime = time;  sessionReference.update("firstWaveTime", firstWaveTime); break;
                case 2: secondWaveTime = time; sessionReference.update("secondWaveTime", secondWaveTime); break;
                case 3: thirdWaveTime = time;  sessionReference.update("thirdWaveTime", thirdWaveTime); break;
            }
        }

        public void LogGeneralTime (int time) {
            this.generalTime = time;
            sessionReference.update("generalTime", generalTime);
        }

        public void LogLevel (int level) {
            this.level = level;
            sessionReference.update("level", level);
        }

        public void LogPoints (int points) {
            this.points = points;
            sessionReference.update("points", points,
                    "open", false);
        }

        public void CloseLog () {
            sessionReference.update("open", false);
        }

        private Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("therapistKey", therapistKey);
            map.put("patientKey", patientKey);
            map.put("date", date);

            map.put("heads", head);
            map.put("leftArms", leftArm);
            map.put("rightArms", rightArm);
            map.put("chests", chest);
            map.put("leftLegs", leftLeg);
            map.put("rightLegs", rightLeg);

            map.put("firstWaveTime", firstWaveTime);
            map.put("secondWaveTime", secondWaveTime);
            map.put("thirdWaveTime", thirdWaveTime);

            map.put("generalTime", generalTime);
            map.put("level", level);
            map.put("points", points);

            map.put("open", true);
            map.put("paired", false);
            map.put("pairKey", null);

            return map;
        }

    }

    /*  Examples

        GameLogger.RallyBall rallyBall = new GameLogger.RallyBall(therapistKey, patientKey);
        rallyBall.LogBodyPart(GameLogger.RallyBall.Parts.LEFT_ARM);
        rallyBall.LogBodyPart(GameLogger.RallyBall.Parts.CHEST);
        rallyBall.LogBodyPart(GameLogger.RallyBall.Parts.RIGHT_LEG);
        rallyBall.LogInhibition();
        rallyBall.LogLevel(2);
        rallyBall.LogGeneralTime(56);
        rallyBall.LogWaveTime(1, 35);
        rallyBall.LogWaveTime(2, 13);
        rallyBall.LogWaveTime(3, 6);
        rallyBall.CloseLog();

        GameLogger.RiverRush riverRush = new GameLogger.RiverRush(therapistKey, patientKey);
        riverRush.LogRightBar();
        riverRush.LogMiddleBar(GameLogger.RiverRush.State.GOOD);
        riverRush.LogCloudTime(5);
        riverRush.LogCloudTime(10);
        riverRush.LogCloudTime(30);
        riverRush.LogCloudTime(50);
        riverRush.LogLevel(5);
        riverRush.CloseLog();

        GameLogger.Leaks leaks = new GameLogger.Leaks(therapistKey, patientKey);
        leaks.LogBodyPart(GameLogger.Leaks.Parts.CHEST);
        leaks.LogWaveTime(2, 17);
        leaks.CloseLog();

        GameLogger.ReflexRidge reflexRidge = new GameLogger.ReflexRidge(therapistKey, patientKey);
        reflexRidge.LogBoost(GameLogger.ReflexRidge.State.GOOD);
        reflexRidge.LogGeneralTime(50);
        reflexRidge.LogSquat(GameLogger.ReflexRidge.State.INHIBITION);
        reflexRidge.CloseLog();
    */
}

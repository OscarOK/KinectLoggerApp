package mx.uach.hcilab.kinectlogger.util;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameLogger {

    public static class RiverRush {

        private static final String GAME_TAG = "RiverRush";

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

        public RiverRush (String therapistKey, String patientKey) {
            this.therapistKey = therapistKey;
            this.patientKey = patientKey;
            this.date = System.currentTimeMillis();

            sessionReference = FirebaseFirestore.getInstance().collection(GAME_TAG).document();
        }

        public void LogJump (State state) {
            switch (state){
                case GOOD: goodJump++; break;
                case BAD: badJump++; break;
                case INHIBITION: inhibitionJump++; break;
            }
            uploadLog();
        }

        public void LogMiddleBar (State state) {
            switch (state){
                case GOOD: goodMiddleBar++; break;
                case BAD: badMiddleBar++; break;
                case INHIBITION: inhibitionMiddleBar++; break;
            }
            uploadLog();
        }

        public void LogLeftBar () {
            leftBar++;
            uploadLog();
        }

        public void LogRightBar () {
            rightBar++;
            uploadLog();
        }

        public void LogCloudTime (int time) {
            cloudTimes.add(time);
            uploadLog();
        }

        public void LogPoints (int points) {
            this.points = points;
            uploadLog();
        }

        public void LogLevel (int level) {
            this.level = level;
            uploadLog();
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

            return map;
        }

        private void uploadLog() {
            sessionReference.set(this.toMap());
        }
    }

    public static class ReflexRidge {

        private static final String GAME_TAG = "ReflexRidge";

        public enum State {
            GOOD, BAD, INHIBITION
        }

        DocumentReference sessionReference;

        private String therapistKey, patientKey;
        private long date;

        private int goodJump = 0,  badJump = 0,  inhibitionJump = 0;
        private int goodLeft = 0,  badLeft = 0,  inhibitionLeft = 0;
        private int goodBoost = 0, badBoost = 0, inhibitionBoost = 0;
        private int goodRight = 0, badRight = 0, inhibitionRight = 0;
        private int goodSquat = 0, badSquat = 0, inhibitionSquat = 0;
        private int level = 0;
        private int generalTime = 0;
        private int extraTime = 0;
        private int points = 0;

        public ReflexRidge (String therapistKey, String patientKey) {
            this.therapistKey = therapistKey;
            this.patientKey = patientKey;
            date = System.currentTimeMillis();

            sessionReference = FirebaseFirestore.getInstance().collection(GAME_TAG).document();
        }

        public void LogJump (State state) {
            switch (state){
                case GOOD: goodJump++; break;
                case BAD: badJump++; break;
                case INHIBITION: inhibitionJump++; break;
            }
            uploadLog();
        }

        public void LogLeft (State state) {
            switch (state){
                case GOOD: goodLeft++; break;
                case BAD: badLeft++; break;
                case INHIBITION: inhibitionLeft++; break;
            }
            uploadLog();
        }

        public void LogBoost (State state) {
            switch (state){
                case GOOD: goodBoost++; break;
                case BAD: badBoost++; break;
                case INHIBITION: inhibitionBoost++; break;
            }
            uploadLog();
        }

        public void LogRight (State state) {
            switch (state){
                case GOOD: goodRight++; break;
                case BAD: badRight++; break;
                case INHIBITION: inhibitionRight++; break;
            }
            uploadLog();
        }

        public void LogSquat (State state) {
            switch (state){
                case GOOD: goodSquat++; break;
                case BAD: badSquat++; break;
                case INHIBITION: inhibitionSquat++; break;
            }
            uploadLog();
        }

        public void LogLevel (int level) {
            this.level = level;
            uploadLog();
        }

        public void LogGeneralTime (int time) {
            this.generalTime = time;
            uploadLog();
        }

        public void LogExtraTime (int time) {
            this.extraTime = time;
            uploadLog();
        }

        public void LogPoints (int points) {
            this.points = points;
            uploadLog();
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

            return map;
        }

        private void uploadLog() {
            sessionReference.set(this.toMap());
        }
    }

    public static class RallyBall {

        private static final String GAME_TAG = "RallyBall";

        public enum Parts {
            HEAD, LEFT_ARM, RIGHT_ARM, CHEST, LEFT_LEG, RIGHT_LEG
        }

        public enum State {
            GOOD, INHIBITION
        }

        DocumentReference sessionReference;

        private String therapistKey, patientKey;
        private long date;

        private int goodHead = 0,      inhibitionHead = 0;
        private int goodLeftArm = 0,   inhibitionLeftArm = 0;
        private int goodRightArm = 0,  inhibitionRightArm = 0;
        private int goodChest = 0,     inhibitionChest = 0;
        private int goodLeftLeg = 0,   inhibitionLeftLeg = 0;
        private int goodRightLeg = 0, inhibitionRightLeg = 0;

        private int firstWaveTime = 0, secondWaveTime = 0, thirdWaveTime = 0;
        private int generalTime = 0;
        private int level = 0;

        public RallyBall (String therapistKey, String patientKey) {
            this.therapistKey = therapistKey;
            this.patientKey = patientKey;
            this.date = System.currentTimeMillis();

            sessionReference = FirebaseFirestore.getInstance().collection(GAME_TAG).document();
        }

        public void LogBodyPart (Parts part, State state) {
            switch (part){
                case HEAD:
                    if(state == State.GOOD)
                        goodHead++;
                    else
                        inhibitionHead++;
                    break;
                case LEFT_ARM:
                    if(state == State.GOOD)
                        goodLeftArm++;
                    else
                        inhibitionLeftArm++;
                    break;
                case RIGHT_ARM:
                    if(state == State.GOOD)
                        goodRightArm++;
                    else
                        inhibitionRightArm++;
                    break;
                case CHEST:
                    if(state == State.GOOD)
                        goodChest++;
                    else
                        inhibitionChest++;
                    break;
                case LEFT_LEG:
                    if(state == State.GOOD)
                        goodLeftLeg++;
                    else
                        inhibitionLeftLeg++;
                    break;
                case RIGHT_LEG:
                    if(state == State.GOOD)
                        goodRightLeg++;
                    else
                        inhibitionRightLeg++;
                    break;
            }
            uploadLog();
        }

        public void LogWaveTime (int wave, int time) {
            switch (wave) {
                case 1: firstWaveTime = time;  break;
                case 2: secondWaveTime = time; break;
                case 3: thirdWaveTime = time;  break;
            }
            uploadLog();
        }

        public void LogGeneralTime (int time) {
         this.generalTime = time;
         uploadLog();
        }

        public void LogLevel (int level) {
            this.level = level;
            uploadLog();
        }

        private Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("therapistKey", therapistKey);
            map.put("patientKey", patientKey);
            map.put("date", date);

            map.put("goodHeads", goodHead);
            map.put("inhibitionHeads", inhibitionHead);
            map.put("goodLeftArms", goodLeftArm);
            map.put("inhibitionLeftArms", inhibitionLeftArm);
            map.put("goodRightArms", goodRightArm);
            map.put("inhibitionRightArms", inhibitionRightArm);
            map.put("goodChests", goodChest);
            map.put("inhibitionChests", inhibitionChest);
            map.put("goodLeftLegs", goodLeftLeg);
            map.put("inhibitionLeftLegs", inhibitionLeftLeg);
            map.put("goodRightLegs", goodLeftLeg);
            map.put("inhibitionRightLegs", inhibitionLeftLeg);

            map.put("firstWaveTime", firstWaveTime);
            map.put("secondWaveTime", secondWaveTime);
            map.put("thirdWaveTime", thirdWaveTime);

            map.put("generalTime", generalTime);
            map.put("level", level);

            return map;
        }

        private void uploadLog(){
            sessionReference.set(this.toMap());
        }

    }

    public static class Leaks {

        private static final String GAME_TAG = "Leaks";

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

        public Leaks (String therapistKey, String patientKey) {
            this.therapistKey = therapistKey;
            this.patientKey = patientKey;
            this.date = System.currentTimeMillis();

            sessionReference = FirebaseFirestore.getInstance().collection(GAME_TAG).document();
        }

        public void LogBodyPart (Parts part) {
            switch (part){
                case HEAD: head++; break;
                case LEFT_ARM: leftArm++; break;
                case RIGHT_ARM: rightArm++; break;
                case CHEST: chest++; break;
                case LEFT_LEG: leftLeg++; break;
                case RIGHT_LEG: rightLeg++; break;
            }
            uploadLog();
        }

        public void LogWaveTime (int wave, int time) {
            switch (wave) {
                case 1: firstWaveTime = time;  break;
                case 2: secondWaveTime = time; break;
                case 3: thirdWaveTime = time;  break;
            }
            uploadLog();
        }

        public void LogGeneralTime (int time) {
            this.generalTime = time;
            uploadLog();
        }

        public void LogLevel (int level) {
            this.level = level;
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

            return map;
        }

        private void uploadLog(){
            sessionReference.set(this.toMap());
        }

    }

    /*  Examples

        GameLogger.RallyBall rallyBall = new GameLogger.RallyBall(therapistKey, patientKey);
        rallyBall.LogBodyPart(GameLogger.RallyBall.Parts.LEFT_ARM, GameLogger.RallyBall.State.INHIBITION);
        rallyBall.LogBodyPart(GameLogger.RallyBall.Parts.CHEST, GameLogger.RallyBall.State.GOOD);
        rallyBall.LogBodyPart(GameLogger.RallyBall.Parts.RIGHT_LEG, GameLogger.RallyBall.State.GOOD);
        rallyBall.LogLevel(2);
        rallyBall.LogGeneralTime(56);
        rallyBall.LogWaveTime(1, 35);
        rallyBall.LogWaveTime(2, 13);
        rallyBall.LogWaveTime(3, 6);

        GameLogger.RiverRush riverRush = new GameLogger.RiverRush(therapistKey, patientKey);
        riverRush.LogRightBar();
        riverRush.LogMiddleBar(GameLogger.RiverRush.State.GOOD);
        riverRush.LogCloudTime(5);
        riverRush.LogCloudTime(10);
        riverRush.LogCloudTime(30);
        riverRush.LogCloudTime(50);
        riverRush.LogLevel(5);

        GameLogger.Leaks leaks = new GameLogger.Leaks(therapistKey, patientKey);
        leaks.LogBodyPart(GameLogger.Leaks.Parts.CHEST);
        leaks.LogWaveTime(2, 17);

        GameLogger.ReflexRidge reflexRidge = new GameLogger.ReflexRidge(therapistKey, patientKey);
        reflexRidge.LogBoost(GameLogger.ReflexRidge.State.GOOD);
        reflexRidge.LogGeneralTime(50);
        reflexRidge.LogSquat(GameLogger.ReflexRidge.State.INHIBITION);
    */
}

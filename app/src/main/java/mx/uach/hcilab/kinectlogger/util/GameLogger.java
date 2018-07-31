package mx.uach.hcilab.kinectlogger.util;

public class GameLogger {

    public static class RiverRush {

        public enum State {
            GOOD, BAD, INHIBITION;
        }

        private String therapistKey, patientKey;

        public RiverRush (String therapistKey, String patientKey) {
            this.therapistKey = therapistKey;
            this.patientKey = patientKey;
        }

        public void LogJump (State state) {
            
        }

        public void LogMiddleBar (State state) {

        }

        public void LogLeftBar () {

        }

        public void LogRightBar () {

        }

        public void LogCloudTime (int time) {

        }

        public void LogPoints (int points) {

        }

        public void LogLevel (int level) {

        }
    }

    public static class ReflexRidge {

        public enum State {
            GOOD, BAD, INHIBITION;
        }

        private String therapistKey, patientKey;

        public ReflexRidge (String therapistKey, String patientKey) {
            this.therapistKey = therapistKey;
            this.patientKey = patientKey;
        }

        public void LogJump (State state) {

        }

        public void LogLeft (State state) {

        }

        public void LogBoost (State state) {

        }

        public void LogRight (State state) {

        }

        public void LogSquat (State state) {

        }

        public void LogLevel (int level) {

        }

        public void LogGeneralTime (int time) {

        }

        public void LogExtraTime (int time) {

        }

        public void LogPoints (int points) {

        }
    }

    public static class RallyBall {

        public enum Parts {
            HEAD, LEFT_ARM, RIGHT_ARM, CHEST, LEFT_LEG, RIGHT_LEG
        }

        public enum State {
            GOOD, INHIBITION
        }

        private String therapistKey, patientKey;

        public RallyBall (String therapistKey, String patientKey) {
            this.therapistKey = therapistKey;
            this.patientKey = patientKey;
        }

        public void LogBodyPart (Parts part, State state) {

        }

        public void LogWaveTime (int wave, int time) {

        }

        public void LogGeneralTime(int time) {

        }

    }

    public static class Leaks {

        public enum Parts {
            HEAD, LEFT_ARM, RIGHT_ARM, CHEST, LEFT_LEG, RIGHT_LEG
        }

        private String therapistKey, patientKey;

        public Leaks (String therapistKey, String patientKey) {
            this.therapistKey = therapistKey;
            this.patientKey = patientKey;
        }

        public void LogBodyPart (Parts part) {

        }

        public void LogWaveTime (int wave, int time) {

        }

        public void LogGeneralTime(int time) {

        }

    }
}

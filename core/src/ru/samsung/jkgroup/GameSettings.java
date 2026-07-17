package ru.samsung.jkgroup;

public class GameSettings {

    // Device settings

    public static final int SCREEN_WIDTH = 720;
    public static final int SCREEN_HEIGHT = 1280;

    // Physics settings

    public static final float STEP_TIME = 1f / 60f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 6;
    public static final float SCALE = 0.05f;

    public static float SUB_FORCE_RATIO = 10;
    public static float SHIPS_VELOCITY = 10;
    public static long STARTING_SHIPS_APPEARANCE_COOL_DOWN = 2000; // in [ms] - milliseconds
    public static int TORPEDO_VELOCITY = 50; // in [m/s] - meter per second
    public static int SHOOTING_COOL_DOWN = 2200; // in [ms] - milliseconds

    public static final short SHIPS_BIT = 2;
    public static final short SUB_BIT = 4;
    public static final short TORPEDO_BIT = 8;

    // Object sizes

    public static final int SUB_WIDTH = 150;
    public static final int SUB_HEIGHT = 150;
    public static final int SHIPS_WIDTH = 140;
    public static final int SHIPS_HEIGHT = 100;
    public static final int TORPEDO_WIDTH = 15;
    public static final int TORPEDO_HEIGHT = 45;



}

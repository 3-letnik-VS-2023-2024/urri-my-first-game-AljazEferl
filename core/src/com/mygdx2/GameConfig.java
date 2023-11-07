package com.mygdx2;

public class GameConfig {
    public static final float WIDTH = 800f; // pixels; should be used in the desktop launcher
    public static final float HEIGHT = 680f;    // pixels; should be used in the desktop launcher

    public static final float HUD_WIDTH = 800f; // world units
    public static final float HUD_HEIGHT = 680f;    // world units

    public static final float WORLD_WIDTH = 40f;    // world units
    public static final float WORLD_HEIGHT = 68f;   // world units

    public static final float CAR_WIDTH = 5f;   // world units
    public static final float CAR_START_X = WORLD_WIDTH / 2f - CAR_WIDTH / 2f;

    private GameConfig() {
    }
}

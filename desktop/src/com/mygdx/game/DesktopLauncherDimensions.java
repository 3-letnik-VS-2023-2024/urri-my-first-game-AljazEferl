package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.game.dimensions.GameDimensions;

public class DesktopLauncherDimensions {

    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("My GDX Game");
        config.setWindowedMode(1200,600);
        new Lwjgl3Application(new GameDimensions(), config);

    }
}

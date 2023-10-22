package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx2.MyGdxGame2;

public class DesktopLauncher2 {
    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("My GDX Game");
        //config.setWindowedMode(800,600)
        new Lwjgl3Application(new MyGdxGame2(), config);

    }
}

package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.jumping.ball.JumpingBall;

public class RollingBall {

    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("My GDX Game");
        //config.setWindowedMode(800,600)
        new Lwjgl3Application(new com.rollingball.RollingBall(), config);

    }
}

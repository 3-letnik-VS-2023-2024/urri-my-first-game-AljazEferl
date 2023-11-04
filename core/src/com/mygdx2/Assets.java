package com.mygdx2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Assets{

    public static Texture piratesShipImg;
    public static Texture rockImg;
    public static Texture treasureImg;
    public static Texture ammoImg;
    public static Texture background;
    public static Sound damageShip;
    public static Sound laughPirate;
    public static BitmapFont font;
    public static Texture powerUpImg;

    public static Texture loadTexture (String file) {
        return new Texture(Gdx.files.internal(file));
    }

    public static void load () {
        background = loadTexture("images/background2.png");
        piratesShipImg = loadTexture("images/piratesShip.png");
        rockImg = loadTexture("images/rock.png");
        treasureImg = loadTexture("images/treasure.png");
        ammoImg = loadTexture("images/ammo.png");
        powerUpImg = loadTexture("images/shield.png");
        font = new BitmapFont(Gdx.files.internal("fonts/arial-32.fnt"));
        laughPirate = Gdx.audio.newSound(Gdx.files.internal("sounds/laugh.mp3"));
        damageShip = Gdx.audio.newSound(Gdx.files.internal("sounds/damage.mp3"));
    }

    public static void playSound (Sound sound) {
        sound.play(1);
    }

    public static void dispose() {

        background.dispose();
        piratesShipImg.dispose();
        rockImg.dispose();
        treasureImg.dispose();
        ammoImg.dispose();
        powerUpImg.dispose();
        font.dispose();
        laughPirate.dispose();
        damageShip.dispose();
    }
}

package com.mygdx2;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Health extends GameObject {
    private int health;
    public Health(float x, float y, float width, float height,int health) {
        super(x, y, width, height);
        this.health = health;
    }

    @Override
    public void render(SpriteBatch batch){
        Assets.font.setColor(Color.BLACK);
        Assets.font.draw(batch,
                "HEALTH: " + "PirateShip.getHealth()",
                20f, Gdx.graphics.getHeight() - 20f
        );
    }

}

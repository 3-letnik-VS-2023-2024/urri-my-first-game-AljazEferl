package com.mygdx2;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOver extends GameObject {

    public GameOver(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public void render(SpriteBatch batch){
        Assets.font.setColor(Color.RED);
        Assets.font.draw(batch,
                "GAME OVER",
                20f, Gdx.graphics.getHeight() - 20f
        );
    }

}

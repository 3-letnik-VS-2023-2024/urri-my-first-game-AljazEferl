package com.mygdx2;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Score extends GameObject {
    private int score;
    public Score(float x, float y, float width, float height, int score) {
        super(x, y, width, height);
        this.score = score;
    }

    @Override
    public void render(SpriteBatch batch){
        Assets.font.setColor(Color.YELLOW);
        Assets.font.draw(batch,
                "SCORE: " + PirateShip.getTreasureCollected() ,
                20f, Gdx.graphics.getHeight() - 60f
        );
    }

}

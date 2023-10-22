package com.mygdx2;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RockHits extends GameObject {
    private int hits;
    public RockHits(float x, float y, float width, float height, int hits) {
        super(x, y, width, height);
        this.hits = hits;
    }

    @Override
    public void render(SpriteBatch batch){
        Assets.font.setColor(Color.BLACK);
        Assets.font.draw(batch,
                "HITS: " + PirateShip.getRockHits(),
                20f, Gdx.graphics.getHeight() - 100f
        );
    }

}

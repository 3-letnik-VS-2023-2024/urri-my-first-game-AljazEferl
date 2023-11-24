package com.mygdx2;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOver extends GameObject {
    private BitmapFont font;

    public GameOver(float x, float y, float width, float height,BitmapFont font) {
        super(x, y, width, height);
        this.font = font;
    }

    @Override
    public void render(SpriteBatch batch){
       /* Assets.font.setColor(Color.RED);
        Assets.font.draw(batch,
                "GAME OVER",
                20f, Gdx.graphics.getHeight() - 20f
        );*/
       font.setColor(Color.RED);
       font.draw(batch,
                "GAME OVER",
                20f, Gdx.graphics.getHeight() - 20f
        );
    }

}

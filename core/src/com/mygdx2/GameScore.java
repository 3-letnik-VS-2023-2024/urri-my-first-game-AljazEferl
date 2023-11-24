package com.mygdx2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScore extends GameObject {
    private int score;
    private int shipHealth;
    private int rockHits;
    private BitmapFont font;
    public GameScore(float x, float y, float width, float height, int score, int shipHealth, int rockHits, BitmapFont font) {
        super(x, y, width, height);
        this.score = score;
        this.shipHealth = shipHealth;
        this.rockHits = rockHits;
        this.font = font;
    }

    int getScore(){
        return score;
    }
    int getShipHealth(){
        return shipHealth;
    }
    void setScore(int score){
        this.score = score;
    }
    void setShipHealth(int shipHealth){
        this.shipHealth = shipHealth;
    }
    int getRockHits(){
        return  rockHits;
    }
    void setRockHits(int rockHits){
        this.rockHits = rockHits;
    }

    @Override
    public void render(SpriteBatch batch) {
        /*Assets.font.setColor(Color.BLACK);
        Assets.font.draw(batch, "SCORE: " + getScore() , 20f, Gdx.graphics.getHeight() - 60f);
        Assets.font.draw(batch, "HITS: " + getRockHits(), 20f, Gdx.graphics.getHeight() - 100f);
        Assets.font.draw(batch, "HEALTH: " + getShipHealth(), 20f, Gdx.graphics.getHeight() - 20f);*/
        font.setColor(Color.BLACK);
        font.draw(batch, "SCORE: " + getScore() , 20f, Gdx.graphics.getHeight() - 60f);
        font.draw(batch, "HITS: " + getRockHits(), 20f, Gdx.graphics.getHeight() - 100f);
        font.draw(batch, "HEALTH: " + getShipHealth(), 20f, Gdx.graphics.getHeight() - 20f);
    }
    public boolean isEnd(){
        if(shipHealth <= 0){
            return  true;
        }
        return false;
    }
    public void reset(){
        setScore(0);
        setShipHealth(100);
        setRockHits(0);
    }

}

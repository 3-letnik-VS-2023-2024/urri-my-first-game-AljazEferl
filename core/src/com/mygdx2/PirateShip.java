package com.mygdx2;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class PirateShip extends DynamicGameObject {
    private static  int health = 100;
    private static  int treasureCollected = 0;
    private static  int rockHits = 0;

    public PirateShip(float x, float y, float width, float height, Vector2 velocity, long createTime) {
        super(x, y, width, height, velocity, createTime);
    }

    public void moveLeft(float delta){
        position.x -= velocity.x * delta;
        if (position.x < 0)
            position.x = 0f;
    }
    public void moveRight(float delta) {
        position.x += velocity.x * delta;
        if (position.x > Gdx.graphics.getWidth() - bounds.getWidth())
            position.x = (float) (Gdx.graphics.getWidth() - bounds.getWidth());
    }
    public static int getRockHits(){
        return rockHits;
    }
    public void setRockHits(int rockHits){
        PirateShip.rockHits = rockHits;
    }
    public static int getTreasureCollected(){
        return treasureCollected;
    }
    public void setTreasureCollected(int treasureCollected){
        PirateShip.treasureCollected = treasureCollected;
    }
    public static int getHealth(){
        return  health;
    }
    public void setHealth(int health){
        PirateShip.health = health;
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        batch.draw(Assets.piratesShipImg, position.x,position.y, bounds.width+20, bounds.height+20);
    }
    public Rectangle rectangleBounds(){
        return new Rectangle(position.x,position.y,bounds.width,bounds.height);

    }



}

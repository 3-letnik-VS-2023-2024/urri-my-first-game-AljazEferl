package com.mygdx2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Treasure  extends DynamicGameObject {
    public Treasure(float x, float y, float width, float height, Vector2 velocity, long createTime) {
        super(x, y, width, height, velocity, createTime);
    }


/*public static long CreateNextIntTime(){
        return
    }*/

    @Override
    public void render(SpriteBatch batch) {
      //  super.render(batch);
        batch.draw(Assets.treasureImg, position.x, position.y, bounds.width, bounds.height);
    }
    public Rectangle rectangleBounds(){
        return new Rectangle(position.x,position.y,bounds.width,bounds.height);

    }


    @Override
    public void update(float deltaTime) {
        position.y -= velocity.y * deltaTime;
    }
}

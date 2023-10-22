package com.mygdx2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Rock extends DynamicGameObject{


    public Rock(float x, float y, float width, float height, Vector2 velocity, long createTime) {
        super(x, y, width, height, velocity, createTime);
    }

    public Rectangle rectangleBounds(){
        return new Rectangle(position.x,position.y,bounds.width,bounds.height);

    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        batch.draw(Assets.rockImg, position.x, position.y, bounds.width, bounds.height);
    }
    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
        position.y -= velocity.y * deltaTime;
    }
}

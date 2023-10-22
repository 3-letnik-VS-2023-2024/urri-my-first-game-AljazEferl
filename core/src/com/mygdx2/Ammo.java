package com.mygdx2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Ammo  extends DynamicGameObject{
    public Ammo(float x, float y, float width, float height, Vector2 velocity, long createTime) {
        super(x, y, width, height, velocity, createTime);
    }
    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        batch.draw(Assets.ammoImg, position.x, position.y, bounds.width, bounds.height);
    }

    @Override
    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);

    }

    public Rectangle rectangleBounds(){
        return new Rectangle(position.x,position.y,bounds.width ,bounds.height);
    }
}

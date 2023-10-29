package com.mygdx2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class DynamicGameObject extends GameObject{
    public final Vector2 velocity;
    public final long createTime;

    public DynamicGameObject(float x, float y, float width, float height,Vector2 velocity, long createTime) {
        super(x, y, width, height);
        this.velocity =velocity ;
        this.createTime = createTime;
    }

    public  abstract void render(SpriteBatch batch);

    public abstract void update(float deltaTime);
}

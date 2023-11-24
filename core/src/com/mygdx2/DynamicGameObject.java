package com.mygdx2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class DynamicGameObject extends GameObject{
    public Vector2 velocity;
    public long createTime;
    public TextureAtlas.AtlasRegion atlas;
    public DynamicGameObject(float x, float y, float width, float height,Vector2 velocity, long createTime, TextureAtlas.AtlasRegion atlas) {
        super(x, y, width, height);
        this.velocity =velocity ;
        this.createTime = createTime;
        this.atlas = atlas;
    }
    public DynamicGameObject() {
    }


    public  abstract void render(SpriteBatch batch);

    public abstract void update(float deltaTime);

    public Rectangle rectangleBounds() {
        return new Rectangle(position.x, position.y, bounds.width, bounds.height);
    }
}

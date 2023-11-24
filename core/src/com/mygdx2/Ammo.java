package com.mygdx2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.mygdx2.assets.AssetPaths;
import com.mygdx2.assets.RegionNames;

public class Ammo  extends DynamicGameObject implements  Pool.Poolable{
    /*   public Ammo(float x, float y, float width, float height, Vector2 velocity, long createTime) {
            super(x, y, width, height, velocity, createTime);
        }*/
    public boolean alive;

    public static final Pool<Ammo> POOL_AMMO =
            Pools.get(Ammo.class, 20);

    @Override
    public void render(SpriteBatch batch) {
        //super.render(batch);
        batch.draw(Assets.ammoImg, position.x, position.y, bounds.width, bounds.height);
    }

    public Ammo() {
        alive = false;
    }

    public void init(float x, float y, float width, float height, Vector2 velocity, long createTime){
        this.position = new Vector2(x,y);
        this.bounds = new Rectangle(x,y,width,height);
        this.velocity = velocity;
        this.createTime = createTime;

        alive = true;
    }

    public void free(){POOL_AMMO.free(this);}

    @Override
    public void update(float deltaTime) {
        position.y += velocity.y * deltaTime;
        if(!isOutOfScreen()){
            alive = false;
        }

    }

    public boolean isAlive() {
        return alive;
    }
    private boolean isOutOfScreen() {
        return !(position.y > Gdx.graphics.getHeight());
    }

    public Rectangle rectangleBounds(){
        return new Rectangle(position.x,position.y,bounds.width ,bounds.height);
    }

    @Override
    public void reset() {
        position.set(0,0);
        bounds.set(0,0, bounds.width, bounds.height);
        velocity.set(0,20f);
        createTime = 0;

    }
}

package com.mygdx2;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

public class Rock extends DynamicGameObject implements Pool.Poolable {
    private static final long ROCK_SPAWN_TIME = 2 ;
    public boolean alive;
    /*public Rock(float x, float y, float width, float height, Vector2 velocity, long createTime) {
        super(x, y, width, height, velocity, createTime);
    }*/

    public Rock() {
        alive = false;
    }
    public static final Pool<Rock> POOL_ROCK =
            Pools.get(Rock.class, 5);
    public Rectangle rectangleBounds(){
        return new Rectangle(position.x,position.y,bounds.width,bounds.height);

    }
    public void init(float x, float y, float width, float height, Vector2 velocity, long createTime){
        this.position = new Vector2(x,y);
        this.bounds = new Rectangle(x,y,width,height);
        this.velocity = velocity;
        this.createTime = createTime;
        alive = true;
    }
    public void free(){POOL_ROCK.free(this);}

    @Override
    public void render(SpriteBatch batch) {
    //    super.render(batch);
        batch.draw(Assets.rockImg, position.x, position.y, bounds.width, bounds.height);
    }
    @Override
    public void update(float deltaTime) {
        position.y -= velocity.y * deltaTime;
        if(!isOutOfScreen()){
            alive = false;
        }
    }


    public boolean isAlive() {
        return alive;
    }
    private boolean isOutOfScreen() {
        return !(position.y < 0);
    }

    @Override
    public void reset() {
        init(MathUtils.random(0f, Gdx.graphics.getWidth() - Assets.rockImg.getWidth()),Gdx.graphics.getHeight(),Assets.rockImg.getWidth(),Assets.rockImg.getHeight(),new Vector2(0f, 150f),ROCK_SPAWN_TIME);
    }
}

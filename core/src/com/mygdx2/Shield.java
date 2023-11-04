package com.mygdx2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

public class Shield extends DynamicGameObject{
    public float rotate;
    public float rotateSpeed;
    public float duration;
    public boolean alive;
    public static final Pool<Shield> POOL_SHIELD =
            Pools.get(Shield.class, 1);
  /*  public Shield(float x, float y, float width, float height, Vector2 velocity, long createTime,float rotate, float rotateSpeed,float duration) {
        super(x, y, width, height, velocity, createTime);
        this.rotate = rotate;
        this.rotateSpeed = rotateSpeed;
        this.duration = duration;
    }*/

    public Shield() {
        alive = false;
    }
    public void init(float x, float y, float width, float height, Vector2 velocity, long createTime,float rotate, float rotateSpeed,float duration){
        this.position = new Vector2(x,y);
        this.bounds = new Rectangle(x,y,width,height);
        this.velocity = velocity;
        this.createTime = createTime;
        this.rotate = rotate;
        this.rotateSpeed = rotateSpeed;
        this.duration = duration;
        alive = true;
    }

    public void free(){POOL_SHIELD.free(this);}

    public Rectangle rectangleBounds(){
        return new Rectangle(position.x,position.y,bounds.width,bounds.height);

    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion textureRegion = new TextureRegion(Assets.powerUpImg);
        batch.draw(textureRegion, position.x, position.y, bounds.width / 2, bounds.height / 2, bounds.width, bounds.height, 1, 1, rotate);
    }
    public float getDuration(){
        return duration;
    }
    public void setDuration(float duration){
        this.duration = duration;
    }



    @Override
    public void update(float deltaTime){
        rotate += deltaTime * rotateSpeed;
        position.y -= velocity.y * deltaTime;
        position.x += velocity.x * deltaTime;
        if (rotate>360) rotate -= 360;
        if (rotate<-360) rotate += 360;
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

}

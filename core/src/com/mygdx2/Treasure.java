package com.mygdx2;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

public class Treasure  extends DynamicGameObject implements Pool.Poolable{
    public boolean alive;
    private ParticleEffect particleEffect;
    private static final int TREASURE_SPAWN_TIME = 4;

   /*public Treasure(float x, float y, float width, float height, Vector2 velocity, long createTime) {
        super(x, y, width, height, velocity, createTime);
    }*/

    public static final Pool<Treasure> POOL_TREASURE =
            Pools.get(Treasure.class, 5);
    public Treasure() {
        alive = false;
        particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal("particles/treasure3.pe"),Gdx.files.internal("particles"));


    }


    public void init(float x, float y, float width, float height, Vector2 velocity, long createTime){
        this.position = new Vector2(x,y);
        this.bounds = new Rectangle(x,y,width,height);
        this.velocity = velocity;
        this.createTime = createTime;
        alive = true;
    }
    public void free(){POOL_TREASURE.free(this);}

    @Override
    public void reset() {
      init(MathUtils.random(0f, Gdx.graphics.getWidth() - Assets.treasureImg.getWidth()),Gdx.graphics.getHeight(),Assets.treasureImg.getWidth(),Assets.treasureImg.getHeight(),new Vector2(0f, 100f),TREASURE_SPAWN_TIME);
    }



    @Override
    public void render(SpriteBatch batch) {
        batch.draw(Assets.treasureImg, position.x, position.y, bounds.width, bounds.height);
        particleEffect.draw(batch);
    }
    public Rectangle rectangleBounds(){
        return new Rectangle(position.x,position.y,bounds.width,bounds.height);

    }

    public boolean isAlive() {
        return alive;
    }

    @Override
    public void update(float deltaTime) {
        position.y -= velocity.y * deltaTime;
        if(!isOutOfScreen()){
            alive = false;
        }
        particleEffect.setPosition(position.x + bounds.width / 2, position.y + bounds.height / 2);
        particleEffect.update(deltaTime);
        if (particleEffect.isComplete()) {
            particleEffect.reset();
        }
    }

    private boolean isOutOfScreen() {
        return !(position.y < 0);
    }
}

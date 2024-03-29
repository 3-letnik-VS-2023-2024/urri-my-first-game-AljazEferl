package com.mygdx2;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx2.assets.RegionNames;


public class PirateShip extends DynamicGameObject {

    private final float AMMO_SPEED = 300f;
    private TextureAtlas ammoAtlas;


    private Array<Ammo> activeAmmo;
  //  private Array<DynamicGameObject> dynamicGameObjectArray;
    private Movement movement;

    public PirateShip(float x, float y, float width, float height, Vector2 velocity, long createTime, TextureAtlas.AtlasRegion atlas,TextureAtlas ammoAtlas) {
        super(x, y, width, height, velocity, createTime,atlas);
        activeAmmo = new Array<Ammo>();
        this.ammoAtlas = ammoAtlas;

    }

    public enum Movement {
        LEFT, RIGHT, UP, END
    }

    public void moveLeft(float delta) {
/*        position.x -= velocity.x * delta;
        if (position.x < 0)
            position.x = 0f; */
        movement = Movement.LEFT;
    }

    public void moveRight(float delta) {
       /* position.x += velocity.x * delta;
        if (position.x > Gdx.graphics.getWidth() - bounds.getWidth())
            position.x = (float) (Gdx.graphics.getWidth() - bounds.getWidth());*/
        movement = Movement.RIGHT;
    }

    public void shootAmmo(float delta) {
        movement = Movement.UP;
        /*Ammo ammo = new Ammo(position.x + bounds.width / 2 - Assets.ammoImg.getWidth() / 4, position.y + bounds.height, Assets.ammoImg.getWidth(), Assets.ammoImg.getHeight(), new Vector2(0, AMMO_SPEED), TimeUtils.nanoTime());
        ammoList.add(ammo);*/

    }
    public Array<Ammo> getAmmoList() {
        return activeAmmo;
    }

    @Override
    public void render(SpriteBatch batch) {
        //   super.render(batch);
        batch.draw(atlas, position.x, position.y, bounds.width, bounds.height);
    }
    public float getX(){
        return  position.x;
    }
    public float getY(){
        return position.y;   
    }
    public Rectangle rectangleBounds() {
        return new Rectangle(position.x, position.y, bounds.width, bounds.height);
    }

    @Override
    public void update(float deltaTime) {
        if (movement == Movement.RIGHT) {
            position.x += velocity.x * deltaTime;
            if (position.x > Gdx.graphics.getWidth() - bounds.getWidth())
                position.x = (float) (Gdx.graphics.getWidth() - bounds.getWidth());
        movement = Movement.END;
        }
        else if(movement == Movement.LEFT){
            position.x -= velocity.x * deltaTime;
            if (position.x < 0)
                position.x = 0f;

            movement = Movement.END;
        }
        else if(movement == Movement.UP){
           // Ammo ammo = new Ammo(position.x + bounds.width / 2 - Assets.ammoImg.getWidth() / 4, position.y + bounds.height, Assets.ammoImg.getWidth(), Assets.ammoImg.getHeight(), new Vector2(0, AMMO_SPEED), TimeUtils.nanoTime());
            //ammoList.add(ammo);
            Ammo ammo = Ammo.POOL_AMMO.obtain();


            ammo.init(position.x + bounds.width / 2 - ammoAtlas.findRegion(RegionNames.AMMO).getRegionWidth() / 2,
                    position.y + bounds.height,
                    ammoAtlas.findRegion(RegionNames.AMMO).getRegionWidth(), ammoAtlas.findRegion(RegionNames.AMMO).getRegionHeight(),
                    new Vector2(0, AMMO_SPEED), TimeUtils.nanoTime());
            activeAmmo.add(ammo);
            movement = Movement.END;
        }
    }
    public void reset() {
        position.set(Gdx.graphics.getWidth() / 2f - atlas.getRegionWidth() / 2f, 20f);
        velocity.set(250, 0);
        for(Ammo ammo1 : activeAmmo){
            ammo1.free();
        }
        activeAmmo.clear();
            }

}

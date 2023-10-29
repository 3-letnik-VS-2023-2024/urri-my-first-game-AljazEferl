package com.mygdx2;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class PirateShip extends DynamicGameObject {
    private int health = 100;
    private int treasureCollected = 0;
    private int rockHits = 0;
    private final float AMMO_SPEED = 300f;
    private Array<Ammo> ammoList;
    private Array<DynamicGameObject> dynamicGameObjectArray;
    private Movement movement;

    public PirateShip(float x, float y, float width, float height, Vector2 velocity, long createTime) {
        super(x, y, width, height, velocity, createTime);
        ammoList = new Array<Ammo>();
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
        return ammoList;
    }

    public int getRockHits() {
        return rockHits;
    }

    public void setRockHits(int rockHits) {
        this.rockHits = rockHits;
    }

    public int getTreasureCollected() {
        return treasureCollected;
    }

    public void setTreasureCollected(int treasureCollected) {
        this.treasureCollected = treasureCollected;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public void render(SpriteBatch batch) {
        //   super.render(batch);
        batch.draw(Assets.piratesShipImg, position.x, position.y, bounds.width + 20, bounds.height + 20);
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
            Ammo ammo = new Ammo(position.x + bounds.width / 2 - Assets.ammoImg.getWidth() / 4, position.y + bounds.height, Assets.ammoImg.getWidth(), Assets.ammoImg.getHeight(), new Vector2(0, AMMO_SPEED), TimeUtils.nanoTime());
            ammoList.add(ammo);
            movement = Movement.END;
        }


    }
}

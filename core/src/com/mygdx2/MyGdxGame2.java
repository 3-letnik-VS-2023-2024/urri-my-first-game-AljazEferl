package com.mygdx2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MyGdxGame2 extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private PirateShip pirateShip;
    private Treasure treasure;
    private Rock rock;
    private GameScore gameScore;
    private Array<DynamicGameObject> dynamicobjects;
    private GameOver gameOver;
    private static final int TREASURE_SPAWN_TIME = 4;
    private static final int  ROCK_SPAWN_TIME = 2;

    float width, height;
    private static final float TREASURE_SPAWN_INTERVAL = 3f;
    private static final float ROCK_SPAWN_INTERVAL = 1f;
    private float timeSinceLastTreasureSpawn = 0f;
    private float timeSinceLastRockSpawn = 0f;


    @Override
    public void create() {
        batch = new SpriteBatch();
        Assets.load();
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        batch = new SpriteBatch();

        pirateShip = new PirateShip(Gdx.graphics.getWidth() / 2f - Assets.piratesShipImg.getWidth() / 2f, 20f,Assets.piratesShipImg.getWidth(),Assets.piratesShipImg.getHeight(),new Vector2(250, 0),0 );
        dynamicobjects = new Array<DynamicGameObject>();
        gameOver = new GameOver(0,0, width,height);//(10f,Gdx.graphics.getHeight()-20f,100,30);
        gameScore = new GameScore(0,0, width,height, 0, 100,0);
        spawnTreasure();
        spawnRock();
    }
    private void spawnTreasure() {
        dynamicobjects.add(new Treasure(MathUtils.random(0f, Gdx.graphics.getWidth() - Assets.treasureImg.getWidth()),Gdx.graphics.getHeight(),Assets.treasureImg.getWidth(),Assets.treasureImg.getHeight(),new Vector2(0f, 100f),TREASURE_SPAWN_TIME));

    }
    private void spawnRock() {
        dynamicobjects.add(new Rock(MathUtils.random(0f, Gdx.graphics.getWidth() - Assets.rockImg.getWidth()),Gdx.graphics.getHeight(),Assets.rockImg.getWidth(),Assets.rockImg.getHeight(),new Vector2(0f, 150f),ROCK_SPAWN_TIME));

    }
    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) pirateShip.moveLeft(Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) pirateShip.moveRight(Gdx.graphics.getDeltaTime());
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) pirateShip.shootAmmo(Gdx.graphics.getDeltaTime());
    }
    @Override
    public void render() {

      if(gameScore.getShipHealth() > 0) {

          handleInput();
          update(Gdx.graphics.getDeltaTime());
          pirateShip.update(Gdx.graphics.getDeltaTime());
      }

      batch.begin();
        draw();

        timeSinceLastTreasureSpawn += Gdx.graphics.getDeltaTime();
        if (timeSinceLastTreasureSpawn >= TREASURE_SPAWN_INTERVAL) {
            spawnTreasure();
            timeSinceLastTreasureSpawn = 0f;
        }

        timeSinceLastRockSpawn += Gdx.graphics.getDeltaTime();
        if (timeSinceLastRockSpawn >= ROCK_SPAWN_INTERVAL) {
            spawnRock();
            timeSinceLastRockSpawn = 0f;
        }

      batch.end();
    }
    public void update(float delta) {
        for (DynamicGameObject object : dynamicobjects) {
            object.update(delta);
            if (object instanceof Rock) {
                Rock rock = (Rock) object;
                if (Intersector.overlaps(pirateShip.rectangleBounds(), rock.rectangleBounds())) {
                    Assets.playSound(Assets.damageShip);
                    gameScore.setShipHealth(gameScore.getShipHealth()-10);
                    dynamicobjects.removeValue(rock, true);
                }
            } else if (object instanceof Treasure) {
                Treasure treasure1 = (Treasure) object;
                if (Intersector.overlaps(pirateShip.rectangleBounds(), treasure1.rectangleBounds())) {
                    Assets.playSound(Assets.laughPirate);
                    gameScore.setScore(gameScore.getScore() + 1);
                    dynamicobjects.removeValue(treasure1, true);
                }
            }
        }

        for (int i = pirateShip.getAmmoList().size - 1; i >= 0; i--) {
            Ammo ammo = pirateShip.getAmmoList().get(i);
            ammo.update(delta);

            if (ammo.position.y > Gdx.graphics.getHeight()) {
                pirateShip.getAmmoList().removeIndex(i);
            }

            for (int j = dynamicobjects.size - 1; j >= 0; j--) {
                DynamicGameObject object2 = dynamicobjects.get(j);
                if (object2 instanceof Rock && Intersector.overlaps(ammo.rectangleBounds(), ((Rock) object2).rectangleBounds())) {
                    dynamicobjects.removeIndex(j);
                    pirateShip.getAmmoList().removeIndex(i);
                    gameScore.setRockHits(gameScore.getRockHits()+1);
                }
                else if(object2 instanceof  Treasure && Intersector.overlaps(ammo.rectangleBounds(),((Treasure)object2).rectangleBounds())){
                    pirateShip.getAmmoList().removeIndex(i);
                }
            }
        }
    }

    public void draw(){
        batch.draw(Assets.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if(gameScore.getShipHealth() <= 0){
            gameOver.render(batch);
            return;
        }

        for(DynamicGameObject object : dynamicobjects){
            object.render(batch);
        }
        for (Ammo ammo : pirateShip.getAmmoList()) {
            ammo.render(batch);
        }
        pirateShip.render(batch);
        gameScore.render(batch);
       // rockHits.render(batch);
    }


    @Override
    public void dispose() {
        Assets.dispose();
    }
}

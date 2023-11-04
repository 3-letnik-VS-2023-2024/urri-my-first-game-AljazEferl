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
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MyGdxGame2 extends ApplicationAdapter {
    private static final float SHIELD_DURATION = 5;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private PirateShip pirateShip;
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

    private float timeSinceLastShieldSpawn = 0f;
    private final float SHIELD_SPAWN_INTERVAL = 7f;
    private float remainingShieldTime = 0f;

    private boolean isGamePaused = false;

    private boolean hasShield = false;
    private final Array<Treasure> activeTreasures = new Array<Treasure>();
    private final Array<Rock> activeRocks = new Array<Rock>();
    private final Array<Shield> activeShield = new Array<Shield>();
    private static final int AMMO_POOL_SIZE = 20;

    private final Array<Ammo> activeAmmo = new Array<Ammo>();




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
       // dynamicobjects.add(new Treasure(MathUtils.random(0f, Gdx.graphics.getWidth() - Assets.treasureImg.getWidth()),Gdx.graphics.getHeight(),Assets.treasureImg.getWidth(),Assets.treasureImg.getHeight(),new Vector2(0f, 100f),TREASURE_SPAWN_TIME));
        Treasure item = Treasure.POOL_TREASURE.obtain();
        item.init(MathUtils.random(0f, Gdx.graphics.getWidth() - Assets.treasureImg.getWidth()),Gdx.graphics.getHeight(),Assets.treasureImg.getWidth(),Assets.treasureImg.getHeight(),new Vector2(0f, 100f),TREASURE_SPAWN_TIME);
        activeTreasures.add(item);

    }
    private void spawnRock() {
      //  dynamicobjects.add(new Rock(MathUtils.random(0f, Gdx.graphics.getWidth() - Assets.rockImg.getWidth()),Gdx.graphics.getHeight(),Assets.rockImg.getWidth(),Assets.rockImg.getHeight(),new Vector2(0f, 150f),ROCK_SPAWN_TIME));
     //   dynamicobjects.add(Rock.POOL_ROCK.obtain());
        Rock item = Rock.POOL_ROCK.obtain();
        item.init(MathUtils.random(0f, Gdx.graphics.getWidth() - Assets.rockImg.getWidth()),Gdx.graphics.getHeight(),Assets.rockImg.getWidth(),Assets.rockImg.getHeight(),new Vector2(0f, 150f),ROCK_SPAWN_TIME);
        activeRocks.add(item);
    }
    private void spawnShield() {
        float randomY = MathUtils.random(0f, Gdx.graphics.getHeight());
        float randomXVelocity = MathUtils.random(50f, 150f);
        boolean spawnFromLeft = MathUtils.randomBoolean();
        if (!spawnFromLeft) {
            randomXVelocity *= -1;
        }
       // dynamicobjects.add(new Shield(spawnFromLeft ? 0 : Gdx.graphics.getWidth() - Assets.powerUpImg.getWidth(), randomY, Assets.powerUpImg.getWidth(), Assets.powerUpImg.getHeight(), new Vector2(randomXVelocity, 150f), 7, 290f, 100,SHIELD_DURATION));
       Shield item = Shield.POOL_SHIELD.obtain();
        item.init(spawnFromLeft ? 0 : Gdx.graphics.getWidth() - Assets.powerUpImg.getWidth(), randomY, Assets.powerUpImg.getWidth(), Assets.powerUpImg.getHeight(), new Vector2(randomXVelocity, 150f), 7, 290f, 100,SHIELD_DURATION);
        activeShield.add(item);
        remainingShieldTime = SHIELD_DURATION;
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) pirateShip.moveLeft(Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) pirateShip.moveRight(Gdx.graphics.getDeltaTime());
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) pirateShip.shootAmmo(Gdx.graphics.getDeltaTime());
        if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
            isGamePaused = !isGamePaused;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            resetGame();
        }
    }
    @Override
    public void render() {
        if (gameScore.getShipHealth() > 0) {
            handleInput();
            update(Gdx.graphics.getDeltaTime());
            pirateShip.update(Gdx.graphics.getDeltaTime());
            if (Gdx.input.isKeyPressed(Input.Keys.R)) {
                resetGame();
            }
        }

        batch.begin();
        draw();
        if (isGamePaused) {
            drawPauseScreen();
        }

        batch.end();
    }

    private void drawPauseScreen() {
        float x = Gdx.graphics.getWidth() / 2f;
        float y = Gdx.graphics.getHeight() / 2f;

        Assets.font.draw(batch, "PAUSED", x, y);
    }


    public void update(float delta) {

        if (isGamePaused) {
            return;
        }

        timeSinceLastTreasureSpawn += delta;
        if (timeSinceLastTreasureSpawn >= TREASURE_SPAWN_INTERVAL) {
            spawnTreasure();
            timeSinceLastTreasureSpawn = 0f;
        }

        timeSinceLastRockSpawn += delta;
        if (timeSinceLastRockSpawn >= ROCK_SPAWN_INTERVAL) {
            spawnRock();
            timeSinceLastRockSpawn = 0f;
        }

        timeSinceLastShieldSpawn += delta;
        if (timeSinceLastShieldSpawn >= SHIELD_SPAWN_INTERVAL) {
            spawnShield();
            timeSinceLastShieldSpawn = 0f;
        }
        if (hasShield) {
            remainingShieldTime -= delta;

            if (remainingShieldTime <= 0) {
                hasShield = false;
            }
        }

        for(Treasure pool : activeTreasures){
            pool.update(delta);
            if(!pool.isAlive()){
                activeTreasures.removeValue(pool,true);
            }
            if(pirateShip.rectangleBounds().overlaps(pool.rectangleBounds())){
                gameScore.setScore(gameScore.getScore()+1);
                Assets.playSound(Assets.laughPirate);
                activeTreasures.removeValue(pool,true);
                pool.free();
            }

        }

        for(Rock pool : activeRocks){
            pool.update(delta);
            if(!pool.isAlive()){
                activeRocks.removeValue(pool,true);
            }
            if(pirateShip.rectangleBounds().overlaps(pool.rectangleBounds())){
                if (!hasShield) {
                    gameScore.setShipHealth(gameScore.getShipHealth() - 10);
                    Assets.playSound(Assets.damageShip);
                }
                activeRocks.removeValue(pool, true);
                pool.free();
            }

        }

        for(Shield pool : activeShield){
            pool.update(delta);
            if(!pool.isAlive()){
                activeShield.removeValue(pool,true);
            }
            if(pirateShip.rectangleBounds().overlaps(pool.rectangleBounds())){
                Shield shield1 = pool;
                shield1.setDuration(shield1.getDuration() - delta);
                hasShield = true;
                activeShield.removeValue(pool,true);
                pool.free();
                if (shield1.getDuration() <= 0) {
                    hasShield = false;
                }
            }

        }
        for (Ammo ammo : pirateShip.getAmmoList()) {
            ammo.update(delta);
            for (Rock rock : activeRocks) {
                if (Intersector.overlaps(ammo.rectangleBounds(), rock.rectangleBounds())) {
                    pirateShip.getAmmoList().removeValue(ammo, true);
                    activeRocks.removeValue(rock, true);
                    ammo.free();
                    rock.free();
                }
            }

            if (!ammo.isAlive()) {
                pirateShip.getAmmoList().removeValue(ammo, true);
            }

        }


    }
    private void resetGame() {
        gameScore.reset();
        pirateShip.reset();

        activeTreasures.clear();
        activeRocks.clear();
        activeShield.clear();

        hasShield = false;
        remainingShieldTime = 0f;

        spawnRock();
        spawnTreasure();
    }





    public void draw(){
        batch.draw(Assets.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if(gameScore.getShipHealth() <= 0){
            gameOver.render(batch);
            return;
        }

      /*  for(DynamicGameObject object : dynamicobjects){
            object.render(batch);
        }*/
      /*  for (Ammo ammo : pirateShip.getAmmoList()) {
            ammo.render(batch);
        }*/
        for (Ammo ammo : pirateShip.getAmmoList()) {
            ammo.render(batch);
        }

        for(Treasure pool : activeTreasures){
            pool.render(batch);
        }
        for(Rock pool : activeRocks){
            pool.render(batch);
        }
        for(Shield pool : activeShield){
            pool.render(batch);
        }
        pirateShip.render(batch);
        gameScore.render(batch);
        if (hasShield) {
            float x = Gdx.graphics.getWidth() / 2f;
            float y = Gdx.graphics.getHeight() - 20f;

            Assets.font.draw(batch, "SHIELD: " + String.format("%.1f", remainingShieldTime), x, y);
        }
       // Assets.font.draw(batch,"POOL"+ activeTreasures.)

    }


    @Override
    public void dispose() {
        Assets.dispose();
    }




}

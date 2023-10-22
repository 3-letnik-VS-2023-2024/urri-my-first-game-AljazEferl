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

public class MyGdxGame2 extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private PirateShip pirateShip;
    private Treasure treasure;
    private Rock rock;
    private Array<DynamicGameObject> dynamicobjects;
    private Health health;
    private GameOver gameOver;
    private Score score;
    private RockHits rockHits;
    private int shipHealth = 100;
    private int treasuresCollected = 0 ;

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
        score = new Score(0,0,width,height,PirateShip.getTreasureCollected());
        gameOver = new GameOver(0,0,width,height);
        batch = new SpriteBatch();

        pirateShip = new PirateShip(Gdx.graphics.getWidth() / 2f - Assets.piratesShipImg.getWidth() / 2f, 20f,Assets.piratesShipImg.getWidth(),Assets.piratesShipImg.getHeight(),new Vector2(250, 250f),0 );
        dynamicobjects = new Array<DynamicGameObject>();

        health = new Health(10f,Gdx.graphics.getHeight()-10f,100,30,shipHealth);
        score = new Score(10f,Gdx.graphics.getHeight()-60f,100,30,treasuresCollected);
        gameOver = new GameOver(10f,Gdx.graphics.getHeight()-20f,100,30);

        spawnTreasure();
        spawnRock();
    }
    private void spawnTreasure() {
        dynamicobjects.add(treasure = new Treasure(MathUtils.random(0f, Gdx.graphics.getWidth() - Assets.treasureImg.getWidth()),Gdx.graphics.getHeight(),Assets.treasureImg.getWidth(),Assets.treasureImg.getHeight(),new Vector2(0f, 100f),4));

    }
    private void spawnRock() {
        dynamicobjects.add(rock = new Rock(MathUtils.random(0f, Gdx.graphics.getWidth() - Assets.rockImg.getWidth()),Gdx.graphics.getHeight(),Assets.rockImg.getWidth(),Assets.rockImg.getHeight(),new Vector2(0f, 150f),4));

    }
    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) pirateShip.moveLeft(Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) pirateShip.moveRight(Gdx.graphics.getDeltaTime());
      //  if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) pirateShip.shootAmmo();
    }
    @Override
    public void render() {

      if(shipHealth > 0) {

          handleInput();
          update(Gdx.graphics.getDeltaTime());
      }

      batch.begin();
        draw();

        timeSinceLastTreasureSpawn += Gdx.graphics.getDeltaTime();
        if (timeSinceLastTreasureSpawn >= TREASURE_SPAWN_INTERVAL) {
            spawnTreasure();
            timeSinceLastTreasureSpawn = 0f; // Ponastavite časovnik
        }

        timeSinceLastRockSpawn += Gdx.graphics.getDeltaTime();
        if (timeSinceLastRockSpawn >= ROCK_SPAWN_INTERVAL) {
            spawnRock();
            timeSinceLastRockSpawn = 0f; // Ponastavite časovnik
        }

      batch.end();
    }
    public void update(float delta) {
        for (DynamicGameObject object : dynamicobjects) {
            object.update(delta);
            if (object instanceof Rock) {
                Rock Rock = (Rock) object;
                if (Intersector.overlaps(pirateShip.rectangleBounds(), Rock.rectangleBounds())) {
                    Assets.playSound(Assets.damageShip);
                    shipHealth -= 10;

                    dynamicobjects.removeValue(Rock, true);
                    pirateShip.setHealth(shipHealth);
                }
            } else if (object instanceof Treasure) {
                Treasure human = (Treasure) object;
                if (Intersector.overlaps(pirateShip.rectangleBounds(), human.rectangleBounds())) {
                    Assets.playSound(Assets.laughPirate);
                    treasuresCollected++;
                    dynamicobjects.removeValue(human, true);
                    pirateShip.setTreasureCollected(treasuresCollected);
                }
            }


        }
    }

    public void draw(){
        batch.draw(Assets.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if(shipHealth <= 0){
            gameOver.render(batch);
            return;
        }

        for(DynamicGameObject object : dynamicobjects){
            object.render(batch);
        }
        pirateShip.render(batch);

        health.render(batch);
        score.render(batch);
      //  rockHits.render(batch);
    }


    @Override
    public void dispose() {
        super.dispose();
    }
}
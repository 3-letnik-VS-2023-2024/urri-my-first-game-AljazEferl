package com.mygdx2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx2.assets.AssetDescriptors;
import com.mygdx2.assets.RegionNames;
import com.mygdx2.debug.DebugCameraController;
import com.mygdx2.debug.MemoryInfo;
import com.mygdx2.util.ViewportUtils;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
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
    private int obtainedAmmoCount = 0;


    private boolean hasShield = false;
    private final Array<Treasure> activeTreasures = new Array<Treasure>();
    private final Array<Rock> activeRocks = new Array<Rock>();
    private final Array<Shield> activeShield = new Array<Shield>();
    //private static final int AMMO_POOL_SIZE = 20;

    //private final Array<Ammo> activeAmmo = new Array<Ammo>();

    private DebugCameraController debugCameraController;
    private MemoryInfo memoryInfo;
    private boolean debug = false;
    private ShapeRenderer shapeRenderer;
    public Viewport viewport;
    private AssetManager assetManager;
    private TextureAtlas gameplayAtlas;
    private Sound laugh;
    private Sound damage;
    private BitmapFont newFont;
    private   ParticleEffect waterParticleEffect;
    private  ParticleEffect fireParticleEffect;





    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();
        Assets.load();

        assetManager = new AssetManager();
        assetManager.load(AssetDescriptors.UI_FONT);

        // Load gameplay atlas
        assetManager.load(AssetDescriptors.GAMEPLAY);
        assetManager.getLogger().setLevel(Logger.DEBUG);

        assetManager.load(AssetDescriptors.LAUGH_SOUNDS);
        assetManager.load(AssetDescriptors.DAMAGE_SOUNDS );
        assetManager.load(AssetDescriptors.WATER_PARTICLE_EFFECT);
        assetManager.load(AssetDescriptors.FIRE_PARTICLE_EFFECT);
        assetManager.finishLoading();

        assetManager.finishLoading();
        gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);
        laugh = assetManager.get((AssetDescriptors.LAUGH_SOUNDS));
        damage = assetManager.get(AssetDescriptors.DAMAGE_SOUNDS);
        newFont = assetManager.get(AssetDescriptors.UI_FONT);
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        //debug

        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        memoryInfo = new MemoryInfo(500);

        shapeRenderer = new ShapeRenderer();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        waterParticleEffect = assetManager.get(AssetDescriptors.WATER_PARTICLE_EFFECT);
        fireParticleEffect = assetManager.get(AssetDescriptors.FIRE_PARTICLE_EFFECT);

       // Assets.particleEffect.flipY();
        waterParticleEffect.flipY();

        pirateShip = new PirateShip(
                Gdx.graphics.getWidth() / 2f - gameplayAtlas.findRegion(RegionNames.PIRATE_SHIP).getRegionWidth() / 2f,
                20f,
                gameplayAtlas.findRegion(RegionNames.PIRATE_SHIP).getRegionWidth() ,
                gameplayAtlas.findRegion(RegionNames.PIRATE_SHIP).getRegionHeight() ,
                new Vector2(250, 0),
                0,gameplayAtlas.findRegion(RegionNames.PIRATE_SHIP),gameplayAtlas
        );
       // pirateShip = new PirateShip(Gdx.graphics.getWidth() / 2f - Assets.piratesShipImg.getWidth() / 2f, 20f,Assets.piratesShipImg.getWidth(),Assets.piratesShipImg.getHeight(),new Vector2(250, 0),0 );
      //  dynamicobjects = new Array<DynamicGameObject>();
        gameOver = new GameOver(0,0, width,height,newFont);//(10f,Gdx.graphics.getHeight()-20f,100,30);
        gameScore = new GameScore(0,0, width,height, 0, 100,0,newFont );
        spawnTreasure();
        spawnRock();


    }
    private void spawnTreasure() {
       // dynamicobjects.add(new Treasure(MathUtils.random(0f, Gdx.graphics.getWidth() - Assets.treasureImg.getWidth()),Gdx.graphics.getHeight(),Assets.treasureImg.getWidth(),Assets.treasureImg.getHeight(),new Vector2(0f, 100f),TREASURE_SPAWN_TIME));
        Treasure item = Treasure.POOL_TREASURE.obtain();
        item.init(MathUtils.random(0f, Gdx.graphics.getWidth() - gameplayAtlas.findRegion(RegionNames.TREASURE).getRegionWidth()),Gdx.graphics.getHeight(),gameplayAtlas.findRegion(RegionNames.TREASURE).getRegionWidth(),gameplayAtlas.findRegion(RegionNames.TREASURE).getRegionHeight(),new Vector2(0f, 100f),TREASURE_SPAWN_TIME,gameplayAtlas.findRegion(RegionNames.TREASURE),fireParticleEffect);
        activeTreasures.add(item);

    }
    private void spawnRock() {
      //  dynamicobjects.add(new Rock(MathUtils.random(0f, Gdx.graphics.getWidth() - Assets.rockImg.getWidth()),Gdx.graphics.getHeight(),Assets.rockImg.getWidth(),Assets.rockImg.getHeight(),new Vector2(0f, 150f),ROCK_SPAWN_TIME));
     //   dynamicobjects.add(Rock.POOL_ROCK.obtain());
        Rock item = Rock.POOL_ROCK.obtain();
        item.init(MathUtils.random(0f, Gdx.graphics.getWidth() - gameplayAtlas.findRegion(RegionNames.ROCK).getRegionWidth()),Gdx.graphics.getHeight(),gameplayAtlas.findRegion(RegionNames.ROCK).getRegionWidth(),gameplayAtlas.findRegion(RegionNames.ROCK).getRegionHeight(),new Vector2(0f, 100f),ROCK_SPAWN_TIME,gameplayAtlas.findRegion(RegionNames.ROCK));
       // item.init(MathUtils.random(0f, Gdx.graphics.getWidth() - Assets.rockImg.getWidth()),Gdx.graphics.getHeight(),Assets.rockImg.getWidth(),Assets.rockImg.getHeight(),new Vector2(0f, 150f),ROCK_SPAWN_TIME,gameplayAtlas.findRegion(RegionNames.ROCK));
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
        item.init(spawnFromLeft ? 0 : Gdx.graphics.getWidth() - Assets.powerUpImg.getWidth(), randomY, Assets.powerUpImg.getWidth(), Assets.powerUpImg.getHeight(), new Vector2(randomXVelocity, 150f), 7, 290f, 100,SHIELD_DURATION,gameplayAtlas.findRegion(RegionNames.SHIELD));
        activeShield.add(item);
        remainingShieldTime = SHIELD_DURATION;
    }

    private void handleInput() {
        if(!isGamePaused) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                pirateShip.moveLeft(Gdx.graphics.getDeltaTime());
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                pirateShip.moveRight(Gdx.graphics.getDeltaTime());
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
                pirateShip.shootAmmo(Gdx.graphics.getDeltaTime());
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
            isGamePaused = !isGamePaused;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            resetGame();
        }
    }
    @Override
    public void render() {
        ScreenUtils.clear(0,0.5f,0,1);
        if (gameScore.getShipHealth() > 0) {
            handleInput();
            update(Gdx.graphics.getDeltaTime());
            pirateShip.update(Gdx.graphics.getDeltaTime());
            if (Gdx.input.isKeyPressed(Input.Keys.R)) {
                resetGame();
            }
        }
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera
        batch.setProjectionMatrix(camera.combined);
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) debug = !debug;

        if (debug) {
            debugCameraController.handleDebugInput(Gdx.graphics.getDeltaTime());
            memoryInfo.update();
        }


        batch.begin();
        draw();
        batch.end();
        if (debug) {
            debugCameraController.applyTo(camera);
            batch.begin();
            {
                // the average number of frames per second
                GlyphLayout layout = new GlyphLayout(Assets.font, "FPS:" + Gdx.graphics.getFramesPerSecond());
               /* Assets.font.setColor(Color.YELLOW);
                Assets.font.draw(batch, layout, Gdx.graphics.getWidth() - layout.width, Gdx.graphics.getHeight() - 50);

                // number of rendering calls, ever; will not be reset unless set manually
                Assets.font.setColor(Color.YELLOW);
                Assets.font.draw(batch, "RC:" + batch.totalRenderCalls, Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() - 20);*/
                newFont.setColor(Color.YELLOW);
                newFont.draw(batch, layout, Gdx.graphics.getWidth() - layout.width, Gdx.graphics.getHeight() - 50);

                // number of rendering calls, ever; will not be reset unless set manually
                newFont.setColor(Color.YELLOW);
                newFont.draw(batch, "RC:" + batch.totalRenderCalls, Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() - 20);

                memoryInfo.render(batch, Assets.font);
            }
            batch.end();

            batch.totalRenderCalls = 0;
            ViewportUtils.drawGrid(viewport, shapeRenderer, 50);

            // print rectangles
            shapeRenderer.setProjectionMatrix(camera.combined);
            // https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/graphics/glutils/ShapeRenderer.html
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            {
                shapeRenderer.setColor(1, 1, 0, 1);
                for (Rock rock1 : activeRocks ) {
                    shapeRenderer.rect(rock1.position.x, rock1.position.y, rock1.bounds.getWidth(),rock1.bounds.getHeight());
                }
                for (Treasure treasure1 : activeTreasures ) {
                    shapeRenderer.rect(treasure1.position.x, treasure1.position.y, treasure1.bounds.getWidth(),treasure1.bounds.getHeight());
                }
                for (Shield shield1 : activeShield ) {
                    shapeRenderer.rect(shield1.position.x, shield1.position.y, shield1.bounds.getWidth(),shield1.bounds.getHeight());
                }
                for (Ammo ammo1 : pirateShip.getAmmoList() ) {
                    shapeRenderer.rect(ammo1.position.x, ammo1.position.y, ammo1.bounds.getWidth(),ammo1.bounds.getHeight());
                }
                shapeRenderer.rect(pirateShip.position.x, pirateShip.position.y, pirateShip.bounds.getWidth(), pirateShip.bounds.getHeight());
            }
            shapeRenderer.end();
        }
        if (isGamePaused) {
            batch.begin();
            drawPauseScreen();
            batch.end();
        }


    }

    private void drawPauseScreen() {
        float x = Gdx.graphics.getWidth() / 2f - 70;
        float y = Gdx.graphics.getHeight() / 2f;

       // Assets.font.draw(batch, "PAUSED", x, y);
        newFont.draw(batch, "PAUSED", x, y);
    }


    public void update(float delta) {
        //Assets.particleEffect.update(delta);
        //Assets.particleEffect.setPosition(pirateShip.position.x + pirateShip.bounds.getWidth()/2, pirateShip.position.y + 5);
        waterParticleEffect.update(delta);
        waterParticleEffect.setPosition(pirateShip.position.x + pirateShip.bounds.getWidth()/2, pirateShip.position.y + 5);
        if (isGamePaused) {
            return;
        }
       /* if (Assets.particleEffect.isComplete()){
                Assets.particleEffect.reset();
        }*/

        if(waterParticleEffect.isComplete()){
            waterParticleEffect.reset();
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
                pool.free();
            }
            if(pirateShip.rectangleBounds().overlaps(pool.rectangleBounds())){
                gameScore.setScore(gameScore.getScore()+1);
                laugh.play();
                activeTreasures.removeValue(pool,true);
                pool.free();
            }

        }

        for(Rock pool : activeRocks){
            pool.update(delta);
            if(!pool.isAlive()){
                activeRocks.removeValue(pool,true);
                pool.free();
            }
            if(pirateShip.rectangleBounds().overlaps(pool.rectangleBounds())){
                if (!hasShield) {
                    gameScore.setShipHealth(gameScore.getShipHealth() - 10);

                    // Assets.playSound(Assets.damageShip);
                    damage.play();
                   // pool.free();
                }
                activeRocks.removeValue(pool, true);
                pool.free();
            }
        }

        for(Shield pool : activeShield){
            pool.update(delta);
            if(!pool.isAlive()){
                activeShield.removeValue(pool,true);
                pool.free();
            }
            if(pirateShip.rectangleBounds().overlaps(pool.rectangleBounds())){
                Shield shield1 = pool;
                shield1.setDuration(shield1.getDuration() - delta);
                hasShield = true;
                activeShield.removeValue(pool,true);
                pool.free();
                if (shield1.getDuration() <= 0) {
                    hasShield = false;
                   // pool.free();
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
                    obtainedAmmoCount++;
                }
            }

            if (!ammo.isAlive()) {
                pirateShip.getAmmoList().removeValue(ammo, true);
                ammo.free();
                obtainedAmmoCount++;
            }

        }

    }
    private void resetGame() {
        gameScore.reset();
        pirateShip.reset();

        for (Treasure treasure : activeTreasures) {
            treasure.free();
        }
        activeTreasures.clear();

        for (Rock rock : activeRocks) {
            rock.free();
        }
        activeRocks.clear();

        for (Shield shield : activeShield) {
            shield.free();
        }
        activeShield.clear();

        hasShield = false;
        remainingShieldTime = 0f;

    }


    public void draw(){
        batch.draw(gameplayAtlas.findRegion(RegionNames.BACKGROUND), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //Assets.particleEffect.draw(batch);
        waterParticleEffect.draw(batch);
        if(gameScore.getShipHealth() <= 0){
            gameOver.render(batch);
            return;
        }

        for (Ammo ammo : pirateShip.getAmmoList()) {
            ammo.render(batch);
        }

        for(Treasure pool : activeTreasures){
            pool.update(Gdx.graphics.getDeltaTime());
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

            //Assets.font.draw(batch, "SHIELD: " + String.format("%.1f", remainingShieldTime), x, y);
            newFont.draw(batch, "SHIELD: " + String.format("%.1f", remainingShieldTime), x, y);
        }
        float textX = 10f;
        float textY = Gdx.graphics.getHeight() - 160f;

        /*Assets.font.draw(batch, "Active Ammo: " + pirateShip.getAmmoList().size, textX, textY);
        Assets.font.draw(batch, "Ammo in Pool: " + Ammo.POOL_AMMO.getFree(), textX, textY - 20f);
        Assets.font.draw(batch, "Obtained Ammo: " + obtainedAmmoCount, textX, textY - 40);*/
        newFont.draw(batch, "Active Ammo: " + pirateShip.getAmmoList().size, textX, textY);
        newFont.draw(batch, "Ammo in Pool: " + Ammo.POOL_AMMO.getFree(), textX, textY - 20f);
        newFont.draw(batch, "Obtained Ammo: " + obtainedAmmoCount, textX, textY - 40);
    }


    @Override
    public void dispose() {
       // Assets.dispose();
        assetManager.dispose();

    }




}

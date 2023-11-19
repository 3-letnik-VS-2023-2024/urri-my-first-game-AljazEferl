package com.game.dimensions;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


import java.util.Iterator;


public class GameDimensions extends ApplicationAdapter {
	SpriteBatch batch;
	private Texture piratesShipImg;
	private Texture rockImg;
	private Texture treasureImg;
	private Texture ammoImg;
	private Texture background;
	private  Sound  damageShip;
	private Sound laughPirate;

	private BitmapFont font;

	private Rectangle pirateShip;
	private Array<Rectangle> rocks;
	private Array<Rectangle> treasures;
	private Array<Rectangle> ammos;

	private static final float PIRATE_SHIP_SPEED = 300f;
	private static final float ROCK_SPEED = 150f;
	private static final float ROCK_DAMAGE = 10f;
	private static final float ROCK_SPAWN_TIME = 2f;

	private static final float AMMO_SHOOT_INTERVAL = 1f;

	private static final float AMMO_SPEED = 200f;
	private static final float TREASURE_SPAWN_TIME = 4f;
	private static final float TREASURE_SPEED = 100;

	private int health;
	private int treasureCollected;
	private int rockHits;
	private float treasureSpawnTime;
	private float rockSpawnTime;
	private float ammoshootTime;
	private FitViewport fitViewport;
	private OrthographicCamera camera;
	private Viewport hudViewport;
	private  float WORLD_WIDTH = 600f;
	private  float WORLD_HEIGHT = 300f;

	private float worldUnitX;

	private float worldUnitY;
	int screenWidth ;
	int screenHeight  ;
	float worldWidth;
	float worldHeight;

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		fitViewport = new FitViewport(WORLD_WIDTH,WORLD_HEIGHT,camera);
		hudViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


		background = new Texture("images/background2.png");
		piratesShipImg = new Texture("images/piratesShip.png");
		rockImg = new Texture("images/rock.png");
		treasureImg = new Texture("images/treasure.png");
		ammoImg = new Texture("images/ammo.png");

		laughPirate = Gdx.audio.newSound(Gdx.files.internal("sounds/laugh.mp3"));
		damageShip = Gdx.audio.newSound(Gdx.files.internal("sounds/damage.mp3"));

		font = new BitmapFont(Gdx.files.internal("fonts/arial-32.fnt"));
		worldUnitX = Gdx.graphics.getWidth()/fitViewport.getWorldWidth();
		worldUnitY = Gdx.graphics.getHeight()/fitViewport.getWorldHeight();
		pirateShip = new Rectangle();
		pirateShip.x = (/*Gdx.graphics.getWidth()*/WORLD_WIDTH / 2f - piratesShipImg.getWidth() / 2f);
		pirateShip.y = 20f;
		pirateShip.width = piratesShipImg.getWidth()/(Gdx.graphics.getWidth()/fitViewport.getWorldWidth());
		//System.out.println("Pirate Ship Width: " + pirateShip.width);


		pirateShip.height = piratesShipImg.getHeight()/(Gdx.graphics.getHeight()/ fitViewport.getWorldHeight());

		treasures = new Array<>();
		treasureCollected = 0;
		rockHits = 0;
		spawnTreasure();

		rocks = new Array<>();
		health = 100;
		spawnRock();

		ammos = new Array<>();
	}

	@Override
	public void render () {
		ScreenUtils.clear(Color.BLUE);
		fitViewport.apply();
		batch.setProjectionMatrix(camera.combined);

		if (health > 0) {
			handleInput();
			update(Gdx.graphics.getDeltaTime());
		}

		hudViewport.apply();
		batch.setProjectionMatrix(hudViewport.getCamera().combined);

		batch.begin();

		draw();


		drawHud();

		batch.end();
	}

	private void drawHud() {

		screenWidth = Gdx.graphics.getWidth();
		 screenHeight = Gdx.graphics.getHeight();
		worldWidth = fitViewport.getWorldWidth();
		 worldHeight = fitViewport.getWorldHeight();

		String screenSize = "Screen/Window size: " + screenWidth + " x " + screenHeight + " px";
		String worldSize = "World size: " + (int) worldWidth + " x " + (int) worldHeight + " world units";
		String oneWorldUnit = "One world unit: " + (screenWidth / worldWidth) + " x " + (screenHeight / worldHeight) + " px";

		font.draw(batch,
				screenSize,
				20f,
				hudViewport.getWorldHeight() - 20f);

		font.draw(batch,
				worldSize,
				20f,
				hudViewport.getWorldHeight() - 50f);

		font.draw(batch,
				oneWorldUnit,
				20f,
				hudViewport.getWorldHeight() - 80f);
	}

	@Override
	public void resize(int width, int height) {
		fitViewport.update(width, height,true);
		hudViewport.update(width, height, true);
		//pirateShip.width = piratesShipImg.getWidth()/(Gdx.graphics.getWidth()/fitViewport.getWorldWidth());
		//pirateShip.height = piratesShipImg.getHeight()/(Gdx.graphics.getHeight()/ fitViewport.getWorldHeight());
		//System.out.println("Pirate Ship Width: " + pirateShip.width);

	}
	private void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveLeft(Gdx.graphics.getDeltaTime());
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveRight(Gdx.graphics.getDeltaTime());
		if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) shootAmmo();
	}


	private void moveLeft(float delta){
		pirateShip.x -= PIRATE_SHIP_SPEED * delta;
		if(pirateShip.x < 0){
			pirateShip.x =  0f;
		}
	}
	private void moveRight(float delta){
		pirateShip.x += PIRATE_SHIP_SPEED * delta;
		if(pirateShip.x>= fitViewport.getWorldWidth()-pirateShip.getWidth()/*Gdx.graphics.getWidth()*/ ){
			pirateShip.x = fitViewport.getWorldWidth()-pirateShip.getWidth()/*Gdx.graphics.getWidth()*/;
		}
	}
	private void update(float delta) {
		float elapsedTime = (TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f);
		if (elapsedTime - treasureSpawnTime > TREASURE_SPAWN_TIME) spawnTreasure();
		if (elapsedTime - rockSpawnTime > ROCK_SPAWN_TIME) spawnRock();

		moveAmmo(delta);

		for (Iterator<Rectangle> it = treasures.iterator(); it.hasNext(); ) {
			Rectangle treasure = it.next();
			treasure.y -= TREASURE_SPEED * delta;
			if (treasure.y + treasureImg.getHeight() < 0) {
				it.remove();
			}
			if (treasure.overlaps(pirateShip)) {
				treasureCollected++;
				laughPirate.play();
				it.remove();
			}
		}

		for (Iterator<Rectangle> it = rocks.iterator(); it.hasNext(); ) {
			Rectangle rock = it.next();
			rock.y -= ROCK_SPEED * delta;
			if (rock.y + rockImg.getHeight() < 0) {
				it.remove();
			}
			if (rock.overlaps(pirateShip)) {
				health -= ROCK_DAMAGE;
				damageShip.play();
				it.remove();
			}
		}
	}
	private void draw() {
		if (health <= 0) {
			batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			font.setColor(Color.BLUE);
			font.draw(batch,
					"GAME OVER",
					20f, /*Gdx.graphics.getHeight()*/WORLD_HEIGHT - 20f
			);
			return;
		}
		batch.draw(background, 0, 0, WORLD_WIDTH,WORLD_HEIGHT/*Gdx.graphics.getWidth(), Gdx.graphics.getHeight()*/);
		for (Rectangle treasure : treasures) {
			batch.draw(treasureImg, treasure.x, treasure.y,treasure.width,treasure.height);
		}
		for (Rectangle rock : rocks) {
			batch.draw(rockImg, rock.x, rock.y,rock.width,rock.height);
		}
		for(Rectangle ammo : ammos){
			batch.draw(ammoImg, ammo.x,ammo.y,ammo.width,ammo.height);
		}
		batch.draw(piratesShipImg, pirateShip.x, pirateShip.y, pirateShip.width, pirateShip.height);


		font.setColor(Color.RED);
		font.draw(batch,
				"HEALTH: " + health,
				20f, /*Gdx.graphics.getHeight()*/WORLD_HEIGHT - 20f
		);

		font.setColor(Color.YELLOW);
		font.draw(batch,
				"SCORE: " + treasureCollected ,
				20f, /*Gdx.graphics.getHeight()*/WORLD_HEIGHT - 60f
		);
		font.setColor(Color.YELLOW);
		font.draw(batch,
				"HITS: " + rockHits,
				20f, /*Gdx.graphics.getHeight()*/WORLD_HEIGHT - 100f
		);
	}
	private void shootAmmo() {
		if (TimeUtils.timeSinceMillis((long) ammoshootTime) > AMMO_SHOOT_INTERVAL) {
			Rectangle ammo = new Rectangle();

			ammo.x = pirateShip.x + pirateShip.width / 2 - 6 ;

			ammo.y = pirateShip.y + pirateShip.height;

			ammo.width = ammoImg.getWidth()/(Gdx.graphics.getWidth()/fitViewport.getWorldWidth());
			ammo.height = ammoImg.getHeight()/(Gdx.graphics.getHeight()/fitViewport.getWorldHeight());

			ammos.add(ammo);
		}
	}


	private void moveAmmo(float delta) {
		for (Iterator<Rectangle> it = ammos.iterator(); it.hasNext(); ) {
			Rectangle ammo = it.next();
			ammo.y += AMMO_SPEED * delta;
			if (ammo.y > WORLD_HEIGHT/*Gdx.graphics.getHeight()*/) {
				it.remove();
			}

			for (int i = 0; i < rocks.size; i++) {
				if (rocks.get(i).overlaps(ammo)) {
					rocks.removeIndex(i);
					rockHits++;
				}
			}
			for(int i = 0; i<treasures.size; i++){
				if(treasures.get(i).overlaps(ammo)){
					it.remove();
				}
			}
		}
	}
	private void spawnTreasure() {
		Rectangle treasure = new Rectangle();
		treasure.x = MathUtils.random(0f, WORLD_WIDTH/*Gdx.graphics.getWidth()*/ - treasureImg.getWidth());
		treasure.y = WORLD_HEIGHT;//Gdx.graphics.getHeight();
		treasure.width = treasureImg.getWidth()/(Gdx.graphics.getWidth()/fitViewport.getWorldWidth());
		treasure.height = treasureImg.getHeight()/(Gdx.graphics.getHeight()/ fitViewport.getWorldHeight());
		treasures.add(treasure);
		treasureSpawnTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
	}
	private void spawnRock() {
		Rectangle rock = new Rectangle();
		rock.x = MathUtils.random(0f, WORLD_WIDTH/*Gdx.graphics.getWidth()*/ - rockImg.getWidth());
		rock.y = WORLD_HEIGHT;//Gdx.graphics.getHeight();
		rock.width = rockImg.getWidth()/(Gdx.graphics.getWidth()/ fitViewport.getWorldWidth());
		rock.height = rockImg.getHeight()/(Gdx.graphics.getHeight()/fitViewport.getWorldHeight());
		rocks.add(rock);
		rockSpawnTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		piratesShipImg.dispose();
		treasureImg.dispose();
		rockImg.dispose();
		laughPirate.dispose();
		damageShip.dispose();
	}
}

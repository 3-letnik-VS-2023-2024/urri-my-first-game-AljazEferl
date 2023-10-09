package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;


public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	private Texture img;
	private Texture piratesShipImg;
	private Texture rockImg;
	private Texture treasureImg;
	private Texture mapImg;
	private Texture background;
	private  Sound  damageShip;
	private Sound laughPirate;

	private BitmapFont font;

	private Rectangle pirateShip;
	private Array<Rectangle> rocks;
	private Array<Rectangle> treasures;
	
	private static final float PIRATE_SHIP_SPEED = 300f;
	private static final float ROCK_SPEED = 150f;
	private static final float ROCK_DAMAGE = 10f;
	private static final float ROCK_SPAWN_TIME = 2f;

	private static final float TREASURE_SPAWN_TIME = 4f;
	private static final float TREASURE_SPEED = 100;

	private int health;
	private int treasureCollected;
	private float treasureSpawnTime;
	private float rockSpawnTime;




	@Override
	public void create () {
		batch = new SpriteBatch();

		background = new Texture("images/background2.png");
		piratesShipImg = new Texture("images/piratesShip.png");
		rockImg = new Texture("images/rock.png");
		treasureImg = new Texture("images/treasure.png");

		laughPirate = Gdx.audio.newSound(Gdx.files.internal("sounds/laugh.mp3"));
		damageShip = Gdx.audio.newSound(Gdx.files.internal("sounds/damage.mp3"));

		font = new BitmapFont(Gdx.files.internal("fonts/arial-32.fnt"));

		pirateShip = new Rectangle();
		pirateShip.x = (Gdx.graphics.getWidth() / 2f - piratesShipImg.getWidth() / 2f);
		pirateShip.y = 20f;
		pirateShip.width = piratesShipImg.getWidth();
		pirateShip.height = piratesShipImg.getHeight();

		treasures = new Array<>();
		treasureCollected = 0;
		spawnTreasure();

		rocks = new Array<>();
		health = 100;
		spawnRock();
	}



	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);

		if (health > 0) {
			handleInput();
			update(Gdx.graphics.getDeltaTime());
		}

		batch.begin();

		draw();

		batch.end();
	}
	private void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveLeft(Gdx.graphics.getDeltaTime());
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveRight(Gdx.graphics.getDeltaTime());
	}
	private void moveLeft(float delta){
		pirateShip.x -= PIRATE_SHIP_SPEED * delta;
		if(pirateShip.x < 0){
			pirateShip.x =  0f;
		}
	}
	private void moveRight(float delta){
		pirateShip.x += PIRATE_SHIP_SPEED * delta;
		if(pirateShip.x> Gdx.graphics.getWidth() - piratesShipImg.getWidth()-piratesShipImg.getWidth()){
			pirateShip.x = Gdx.graphics.getWidth()-piratesShipImg.getWidth();
		}
	}
	private void update(float delta) {
		float elapsedTime = (TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f);
		if (elapsedTime - treasureSpawnTime > TREASURE_SPAWN_TIME) spawnTreasure();
		if (elapsedTime - rockSpawnTime > ROCK_SPAWN_TIME) spawnRock();

		for (Iterator<Rectangle> it = treasures.iterator(); it.hasNext(); ) {
			Rectangle coin = it.next();
			coin.y -= TREASURE_SPEED * delta;
			if (coin.y + treasureImg.getHeight() < 0) {
				it.remove();
			}
			if (coin.overlaps(pirateShip)) {
				treasureCollected++;
				laughPirate.play();
				it.remove();
			}
		}

		for (Iterator<Rectangle> it = rocks.iterator(); it.hasNext(); ) {
			Rectangle hammer = it.next();
			hammer.y -= ROCK_SPEED * delta;
			if (hammer.y + rockImg.getHeight() < 0) {
				it.remove();
			}
			if (hammer.overlaps(pirateShip)) {
				health -= ROCK_DAMAGE;
				damageShip.play();
				it.remove();
			}
		}
	}
	private void draw() {
		if (health <= 0) {
			font.setColor(Color.RED);
			font.draw(batch,
					"GAME OVER",
					20f, Gdx.graphics.getHeight() - 20f
			);
			return;
		}
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		for (Rectangle coin : treasures) {
			batch.draw(treasureImg, coin.x, coin.y);
		}
		for (Rectangle hammer : rocks) {
			batch.draw(rockImg, hammer.x, hammer.y);
		}
		batch.draw(piratesShipImg, pirateShip.x, pirateShip.y);

		font.setColor(Color.RED);
		font.draw(batch,
				"HEALTH: " + health,
				20f, Gdx.graphics.getHeight() - 20f
		);

		font.setColor(Color.YELLOW);
		font.draw(batch,
				"SCORE: " + treasureCollected,
				20f, Gdx.graphics.getHeight() - 60f
		);
	}

	private void spawnTreasure() {
		Rectangle treasure = new Rectangle();
		treasure.x = MathUtils.random(0f, Gdx.graphics.getWidth() - treasureImg.getWidth());
		treasure.y = Gdx.graphics.getHeight();
		treasure.width = treasureImg.getWidth();
		treasure.height = treasureImg.getHeight();
		treasures.add(treasure);
		treasureSpawnTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
	}
	private void spawnRock() {
		Rectangle rock = new Rectangle();
		rock.x = MathUtils.random(0f, Gdx.graphics.getWidth() - rockImg.getWidth());
		rock.y = Gdx.graphics.getHeight();
		rock.width = rockImg.getWidth();
		rock.height = rockImg.getHeight();
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

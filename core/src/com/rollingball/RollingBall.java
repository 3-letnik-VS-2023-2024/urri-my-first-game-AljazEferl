package com.rollingball;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RollingBall extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture texture;
    private Sprite sprite;
    private boolean movingRight = true;

    @Override
    public void create() {
        batch = new SpriteBatch();
        texture = new Texture("images/ball.png");
        sprite = new Sprite(texture);
        sprite.setPosition(0, 0);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float speed = 200f;
        if (movingRight) {
            sprite.setX(sprite.getX() + Gdx.graphics.getDeltaTime() * speed);

            if (sprite.getX() + sprite.getWidth() > Gdx.graphics.getWidth()) {
                sprite.setX(Gdx.graphics.getWidth() - sprite.getWidth());
                movingRight = false;
            }
        } else {
            sprite.setX(sprite.getX() - Gdx.graphics.getDeltaTime() * speed);

            if (sprite.getX() < 0) {
                sprite.setX(0);
                movingRight = true;
            }
        }

        float distanceMoved = Gdx.graphics.getDeltaTime() * speed;
        float circumference = 2 * (float) Math.PI * sprite.getWidth();
        float rotationAmount = (distanceMoved / circumference) * 360f;

        if (movingRight) {
            sprite.setRotation(sprite.getRotation() - rotationAmount);
        } else {
            sprite.setRotation(sprite.getRotation() + rotationAmount);
        }

        batch.begin();
        sprite.draw(batch);
        batch.end();
    }


    @Override
    public void dispose() {
        batch.dispose();
        texture.dispose();
    }
}
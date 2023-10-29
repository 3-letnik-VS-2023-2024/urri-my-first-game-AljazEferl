package com.jumping.ball;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Ball {
    private static final float RADIUS = 20;
    private static final float GRAVITY = -1500;

    private Vector2 position;
    private Vector2 velocity;
    private Color color;

    public Ball(float x, float y, Color color) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(MathUtils.random(-200, 200), MathUtils.random(200, 400));
        this.color = color;
    }

    public void update(float deltaTime) {
        velocity.y += GRAVITY * deltaTime;
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;

        // Bounce off the walls
        if (position.x - RADIUS < 0 || position.x + RADIUS > Gdx.graphics.getWidth()) {
            velocity.x = -velocity.x;
        }

        if (position.y - RADIUS < 0) {
            velocity.y = -velocity.y;
            position.y = RADIUS;
        }
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(color);
        shapeRenderer.circle(position.x, position.y, RADIUS);
    }
}

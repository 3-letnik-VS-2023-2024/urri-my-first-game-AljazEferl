package com.jumping.ball;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Ball {
    private static final float GRAVITY = -1500;

    private Vector2 position;
    private float radius;
    private Vector2 velocity;
    private Color color;
    private static final float RESTITUTION = 0.8f;
    public Ball(float x, float y, Color color,float radius) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(MathUtils.random(-200, 200), MathUtils.random(200, 400));
        this.color = color;
        this.radius = radius;
    }

    public void update(float deltaTime) {
        velocity.y += GRAVITY * deltaTime;
        position.y += velocity.y * deltaTime;


        if (position.y - radius < 0) {
            velocity.y = -velocity.y * RESTITUTION;
            position.y = radius;
        }
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(color);
        shapeRenderer.circle(position.x, position.y, radius);
    }
}

package com.example.paletdaov1;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Ball {
    private float x;
    private float y;
    private float speedX;
    private float speedY;
    private float radius;
    private Paint paint;

    private float friction = 0.99f;
    private float speed;

    private boolean isRecovering;

    public Ball(float startX, float startY, float startRadius, int color) {
        this.x = startX;
        this.y = startY;
        this.speedX = 10f;
        this.speedY = 10f;
        this.speed = 10f;
        this.radius = startRadius;

        paint = new Paint();
        paint.setColor(color);
    }

    public float getX() {
        return x;
    }
    public void setX(float x){this.x = x;}
    public float getY() {
        return y;
    }
    public void setY(float y){this.y = y;}
    public void setSpeedX(float speedX) {
        this.speedX =  speedX;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public float getRadius() {
        return radius;
    }

    public void updatePosition() {
        speedY *= friction;
        speedX *= friction;

        x += speedX;
        y += speedY;

        if (speedX < 0.01f && speedX> -0.01f){
            speedX=0;
        }

        if (speedY < 0.01f && speedY > -0.01f){
            speedY=0;
        }
    }

    public void addImpulse(float impulse, float directionX, float directionY) {
        float produitVitesse = this.speedX * directionX + this.speedY * directionY;

        this.speedX = this.speedX - 2 * produitVitesse * directionX;
        this.speedY = this.speedY - 2 * produitVitesse * directionY;

        this.speedX += impulse * directionX;
        this.speedY += impulse * directionY;

        this.speedX *= 0.6f;
        this.speedY *= 0.6f;
    }


    public void draw(Canvas canvas) {
        canvas.drawCircle(x, y, radius, paint);
    }

    public void reverseDirectionX() {
        speedX = -speedX;
    }

    public void reverseDirectionY() {
        speedY = -speedY;
    }

    public float getWidth() {
        return radius * 2;
    }

    public float getHeight() {
        return radius * 2;
    }

    public boolean intersects(Paddle paddle) {
        float paddleCenterX = paddle.getX() + paddle.getRadius();
        float paddleCenterY = paddle.getY() + paddle.getRadius();
        float distanceX = Math.abs(x - paddleCenterX);
        float distanceY = Math.abs(y - paddleCenterY);
        float distanceCenterToCenter = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        // Rayon de collision en fonction de la vitesse du palet
        float collisionRadius = radius + Math.abs(speed);

        return distanceCenterToCenter <= (collisionRadius + paddle.getRadius());
    }

    public boolean isRecovering() {return isRecovering;}

    public void setRecovering(boolean recovering){this.isRecovering = recovering;}
}

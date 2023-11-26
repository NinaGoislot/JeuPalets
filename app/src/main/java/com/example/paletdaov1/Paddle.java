package com.example.paletdaov1;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Paddle {
    private float x, y, radius, prevX, prevY, speed;
    private int  pointerId=-1, lastPointerId, numeroPlayer;

    private long prevTime, lastHitTime;
    private static final int MAX_POSITIONS = 3;
    private View.OnTouchListener touchListener;
    private boolean isMoving=false;
    private ArrayDeque<PointF> positions = new ArrayDeque<>(MAX_POSITIONS);
    private ArrayDeque<Long> times = new ArrayDeque<>(MAX_POSITIONS);
    private Paint paint;


    public Paddle(float startX, float startY, float paddleRadius, int color) {
        this.x = startX;
        this.y = startY;
        this.prevX = startX;
        this.prevY = startY;
        this.radius = paddleRadius;

        paint = new Paint();
        paint.setColor(color);
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    public void setX(float x){this.x = x;}

    public void setY(float y){this.y = y;}

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
    public int getNumeroPlayer() {
        return numeroPlayer;
    }

    public void setNumeroPlayer(int numeroPlayer) {
        this.numeroPlayer = numeroPlayer;
    }

    public void updatePosition(float touchX, float touchY, int pointerId) {
        if (this.pointerId == pointerId) {
            // Mise à jour des positions et du temps
            this.prevX = this.x;
            this.prevY = this.y;
            this.x = touchX - this.radius;
            this.y = touchY - this.radius;

            this.lastPointerId = pointerId;
        }
    }

    public void updateSpeed(float touchX, float touchY, int pointerId) {
        long currentTime = System.currentTimeMillis();
        float distance = 0;
        long timeElapsed = 0;

        // Si le paddle est touché, mettre à jour la vitesse en fonction du mouvement
        if (this.pointerId == pointerId) {
            // Ajouter la nouvelle position et le temps actuel à la file d'attente
            positions.add(new PointF(touchX, touchY));
            times.add(currentTime);

            // Si nous avons trop de positions, supprimer la plus ancienne
            if (positions.size() > MAX_POSITIONS) {
                positions.removeFirst();
                times.removeFirst();
            }

            // Si nous avons suffisamment de positions, calculer la distance et le temps écoulé
            if (positions.size() == MAX_POSITIONS) {
                PointF firstPosition = positions.getFirst();
                PointF lastPosition = positions.getLast();
                distance = (float) Math.sqrt(Math.pow(lastPosition.x - firstPosition.x, 2) + Math.pow(lastPosition.y - firstPosition.y, 2));
                timeElapsed = currentTime - times.getFirst();
            }

            // Calcul de la vitesse : distance / temps
            if (timeElapsed > 0 && distance > 0) {
                this.speed = distance / timeElapsed;
            }
        }
        // Si le paddle n'est pas touché, diminuer progressivement la vitesse
        else {
            float friction = 0.9f; // constante de friction
            this.speed *= friction;

            // Si la vitesse est très faible, la mettre à zéro
            if (this.speed < 0.01f) {
                this.speed = 0;
            }
        }
    }


    public View.OnTouchListener getTouchListener() {
        return touchListener;
    }

    public int getPointerId() {
        return pointerId;
    }

    public void setPointerId(int pointerId) {
        this.pointerId = pointerId;
    }

    public float getSpeed(){return speed;}

    public long getLastHitTime(){return lastHitTime;}


    public int getLastPointerId(){return lastPointerId;}

    public float getPrevX(){return prevX;}

    public float getPrevY(){return prevY;}

    public void draw(Canvas canvas) {
        //canvas.drawRect(x, y, x + width, y + height, paint);
        //canvas.drawCircle(x, y, radius, paint);
        canvas.drawCircle(x + radius, y + radius, radius, paint);
    }

}

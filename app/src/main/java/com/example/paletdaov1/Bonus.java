package com.example.paletdaov1;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;


import java.util.List;

public class Bonus {
    private static final int SIZE = 100;
    private static final int MARGIN = 20;
    private float x,y,xInvincibility, yInvincibility;
    private int colorBonus, colorInvicibility, currentBonus, numeroPlayer, screenHeight,screenWidth;
    private Paint paint;
    private boolean isDisponibleBonus, isDisponibleInvicibility, activedBonus;
    private long bonusStartTime;
    private List<Paddle> paddleList;
    private Paddle currentPaddle;



    public Bonus(int screenWidth, int screenHeight, int numPlayer) {
        this.isDisponibleInvicibility = false;
        this.isDisponibleBonus = false;
        this.activedBonus = false;
        this.numeroPlayer = numPlayer;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.bonusStartTime = System.currentTimeMillis();
        this.currentBonus = -1;

        paint = new Paint();


        switch (numPlayer) {
            case 1 :
                this.colorBonus = Color.rgb(177, 72, 255);
                this.colorInvicibility = Color.rgb(177, 72, 255);
                this.x = screenWidth - 2 * SIZE - MARGIN;
                this.y = screenHeight - SIZE;
                this.xInvincibility = screenWidth - 2 * SIZE - 3 * MARGIN - SIZE;
                this.yInvincibility = screenHeight - SIZE;
                break;
            case 2 :
                this.colorBonus = Color.rgb(255, 179, 65);
                this.colorInvicibility = Color.rgb(255, 179, 65);
                this.x = SIZE + MARGIN;
                this.y = 0;
                this.xInvincibility = SIZE + 8 * MARGIN;
                this.yInvincibility = 0;

                break;
            default :
                break;
            }
        }

    public void draw(Canvas canvas) {

        switch (this.numeroPlayer) {
            case 1 :
                if (isDisponibleBonus) {
                    RectF bonusSquare = new RectF(x, y, x + SIZE, screenHeight);
                    paint.setColor(colorBonus);
                    canvas.drawRect(bonusSquare, paint);
                }
                if (isDisponibleInvicibility) {
                    RectF invicibleSquare = new RectF(xInvincibility, yInvincibility, xInvincibility + SIZE, screenHeight);
                    paint.setColor(colorInvicibility);
                    canvas.drawRect(invicibleSquare, paint);
                }
                break;

            case 2 :
                if (isDisponibleBonus) {
                    RectF bonusSquare = new RectF(x, y, x + SIZE, SIZE);
                    paint.setColor(colorBonus);
                    canvas.drawRect(bonusSquare, paint);
                }
                if (isDisponibleInvicibility) {
                    RectF invicibleSquare = new RectF(xInvincibility, yInvincibility, xInvincibility + SIZE, SIZE);
                    paint.setColor(colorInvicibility);
                    canvas.drawRect(invicibleSquare, paint);
                }
                break;

            default:
                break;
        }

        update();

     }

    public int getNumeroPlayer() {
        return numeroPlayer;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getxInvincibility() {
        return xInvincibility;
    }

    public float getyInvincibility() {
        return yInvincibility;
    }

    public int getSize(){
        return SIZE;
    }

    public List<Paddle> getPaddleList() {
        return paddleList;
    }

    public void setPaddleList(List<Paddle> paddleList) {
        this.paddleList = paddleList;
    }

    public void update() {
        if (!isDisponibleBonus && !activedBonus && System.currentTimeMillis() - bonusStartTime >= 30000) { // Aucun bonus n'est actif et le temps écoulé est supérieur ou égal à 30 secondes
            isDisponibleBonus = true;
        }
    }

    public void activateRandomBonus() {
        currentBonus = (int) (0);
        switch (currentBonus) {
            case 0:
                paddleSizeUp();
                break;
            case 1:
                ennemyGoalBigger();
                break;
            case 2:
                goalSmaller();
                break;
            default:
                break;
        }
    }

    public void handleBonusClick() {
        if (isDisponibleBonus && !activedBonus) {
            isDisponibleBonus = false;
            activateRandomBonus();
            bonusStartTime = System.currentTimeMillis();
        }
    }

    public void paddleSizeUp() {
        currentPaddle=null;
        for (Paddle paddle : paddleList) {
            if (this.numeroPlayer == paddle.getNumeroPlayer()) {
                currentPaddle = paddle;
            }
        }

        if (currentPaddle != null) {
            currentPaddle.setRadius(120);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    resetSize();
                }
            }, 10000); // Planifie la réinitialisation de la taille après 10 secondes
        }


    }

    public void ennemyGoalBigger() {
        // But de l'adversaire plus grand
    }

    public void goalSmaller() {
        // Propre but plus petit
    }
    public void resetSize() {
        currentPaddle.setRadius(80);
        activedBonus = false;
    }

}

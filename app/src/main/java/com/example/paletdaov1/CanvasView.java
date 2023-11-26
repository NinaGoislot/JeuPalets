package com.example.paletdaov1;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.content.Context;
import android.util.AttributeSet;

import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.List;

public class CanvasView extends SurfaceView implements SurfaceHolder.Callback {

    private Ball ball;
    private List<Paddle> paddles;
    private List<Bonus> bonus;
    private SurfaceHolder surfaceHolder;
    private int screenWidth,screenHeight, rectWidth,rectHeight;
    private float factor = 45f;
    private Paint paintText, goalPaint;
    private Rect goal1, goal2;
    private Partie partie;
    private MediaPlayer sonPaddle, sonMur, sonBut;

    private boolean partieFinie = false;


    public CanvasView(Context context, List<Paddle> paddles, List<Bonus> bonus,Ball ball, int screenWidth, int screenHeight, Partie partie) {
        super(context);

        this.paddles = paddles;
        this.bonus = bonus;
        this.ball = ball;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.partie = partie;

        init(context);

        setFocusable(true);
        setZOrderOnTop(true);
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
    }

    public CanvasView(Context context, List<Paddle> paddles, Ball ball, int screenWidth, int screenHeight, Partie partie) {
        super(context);

        this.paddles = paddles;
        this.ball = ball;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.partie = partie;

        init(context);

        setFocusable(true);
        setZOrderOnTop(true);
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
    }

    public void init(Context context) {
        //Sons
        sonPaddle = MediaPlayer.create(context, R.raw.sonpaddle);
        sonMur = MediaPlayer.create(context, R.raw.sonmur);
        sonBut = MediaPlayer.create(context, R.raw.sonbut);

        // Obtention de l'objet SurfaceHolder et ajout du callback
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        // Buts
        int goalWidth = 40;
        int goalHeight = screenHeight / 3;
        goal1 = new Rect(0, screenHeight / 2 - goalHeight / 2, goalWidth, screenHeight / 2 + goalHeight / 2);
        goal2 = new Rect(screenWidth - goalWidth, screenHeight / 2 - goalHeight / 2, screenWidth, screenHeight / 2 + goalHeight / 2);
        goalPaint = new Paint();
        goalPaint.setColor(Color.WHITE);

        // Initialisation de l'objet Paint pour le texte
        paintText = new Paint();
        paintText.setColor(Color.BLACK);
        paintText.setTextSize(50);
        paintText.setTypeface(Typeface.DEFAULT_BOLD);
        paintText.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Paddle paddle : paddles) {
            paddle.draw(canvas);
        }
        if (bonus != null) {
            for (Bonus bonus : bonus) {
                bonus.draw(canvas);
            }
        }
        ball.draw(canvas);
    }

    public void updateGame() {
        ball.updatePosition();

        // Vérification si le palet est dans un but
        if (goal1.contains((int) ball.getX(), (int) ball.getY())) {
            sonBut.start();
            partie.setScore2(partie.getScore2() + 1);
            if (partie.getScore2() == 2) {
                Log.d("Game", "L'équipe 2 a atteint 10 buts !");
                partieFinie = true;
            }
            resetGame();
        } else if (goal2.contains((int) ball.getX(), (int) ball.getY())) {
            sonBut.start();
            partie.setScore1(partie.getScore1() + 1);
            if (partie.getScore1() == 2) {
                Log.d("Game", "L'équipe 1 a atteint 10 buts !");
                partieFinie= true;
            }
            resetGame();
        }

        // Vérification des collisions avec les bords de l'écran
        if (ball.getX() - ball.getRadius() <= 0) {
            // Collision avec le bord gauche
            sonMur.start();
            ball.reverseDirectionX();
            // Ajustement pour empêcher la balle de rester coincée dans le mur
            ball.setX(ball.getRadius());
        } else if (ball.getX() + ball.getRadius() >= screenWidth) {
            // Collision avec le bord droit
            sonMur.start();
            ball.reverseDirectionX();
            // Ajustement pour empêcher la balle de rester coincée dans le mur
            ball.setX(screenWidth - ball.getRadius());
        }
        if (ball.getY() - ball.getRadius() <= 0) {
            // Collision avec le bord supérieur
            sonMur.start();
            ball.reverseDirectionY();
            // Ajustement pour empêcher la balle de rester coincée dans le mur
            ball.setY(ball.getRadius());
        } else if (ball.getY() + ball.getRadius() >= screenHeight) {
            // Collision avec le bord inférieur
            sonMur.start();
            ball.reverseDirectionY();
            // Ajustement pour empêcher la balle de rester coincée dans le mur
            ball.setY(screenHeight - ball.getRadius());
        }

        // Vérification des collisions avec les raquettes des joueurs
        boolean collided = false;
        for (Paddle paddle : paddles) {
            if (ball.intersects(paddle)) {
                sonPaddle.start();
                long currentTime = System.currentTimeMillis();
                if (currentTime - paddle.getLastHitTime() >= 200) {
                    collided = true;
                    if (!ball.isRecovering()) {
                        // Calcul de l'angle de réflexion
                        float paddleCenterX = paddle.getX() + paddle.getRadius();
                        float paddleCenterY = paddle.getY() + paddle.getRadius();
                        float distanceX = ball.getX() - paddleCenterX;
                        float distanceY = ball.getY() - paddleCenterY;
                        double angleR = Math.atan2(distanceY, distanceX);
                        angleR += Math.PI;
                        float newDirectionX = (float) Math.cos(angleR);
                        float newDirectionY = (float) Math.sin(angleR);

                        //Vitesse de la balle en fonction de la vitesse du poussoir
                        ball.addImpulse(paddle.getSpeed() * factor, -newDirectionX, -newDirectionY);

                        ball.setRecovering(true);
                    }
                }
            }
        }

        if (!collided) {
            ball.setRecovering(false);
        }

        // Redessiner les éléments
        drawGameElements();
    }

    private void resetGame(){
        //Réini position et vitesse balle
        ball.setX(screenWidth/2);
        ball.setY(screenHeight/2);
        ball.setSpeedX(0);
        ball.setSpeedY(0);

        //Réini paddles
        if(paddles.size()<=2){
            for (int i = 0; i < paddles.size(); i++) {
                Paddle paddle = paddles.get(i);
                if(i==0){
                    paddle.setX(screenWidth - screenWidth/6);
                    paddle.setY(screenHeight/2 - 100);
                } else if (i==1) {
                    paddle.setX(0 + screenWidth/8);
                    paddle.setY(screenHeight/2 -100);
                }
            }
        } else {
            for (int i = 0; i < paddles.size(); i++) {
                Paddle paddle = paddles.get(i);
                if (i == 0) {
                    paddle.setX(screenWidth - screenWidth/7);
                    paddle.setY(screenHeight/2 - paddle.getRadius() + screenHeight/4 );
                } else if (i == 2) {
                    paddle.setX(screenWidth - screenWidth/7);
                    paddle.setY(screenHeight/2 - paddle.getRadius() - screenHeight/4);
                } else if (i == 1) {
                    paddle.setX(0 + screenWidth/20);
                    paddle.setY(screenHeight/2 - paddle.getRadius() + screenHeight/4 );
                } else if (i == 3) {
                    paddle.setX(0 + screenWidth/20);
                    paddle.setY(screenHeight/2 - paddle.getRadius() - screenHeight/4);
                }
            }
        }

    }

    protected void drawGameElements() {
        Canvas canvas = getHolder().lockCanvas();

        if (canvas != null) {
            //Fond
            canvas.drawColor(Color.rgb(28,28,28));

            //Ligne de milieu de terrain
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(5);
            canvas.drawLine(screenWidth / 2, 0, screenWidth / 2, screenHeight, paint);

            //Bonus
            if (bonus != null) {
                for (Bonus bonus : bonus) {
                    bonus.draw(canvas);
                }
            }

            //Balle
            ball.draw(canvas);

            // Buts
            canvas.drawRect(goal1, goalPaint);
            canvas.drawRect(goal2, goalPaint);

            // Paddles
            for (Paddle paddle : paddles) {
                paddle.draw(canvas);
            }

            //Scores
            paintText.setColor(Color.rgb(255,179,65));
            canvas.drawText(String.valueOf(partie.getScore1()), screenWidth / 4, 50, paintText);
            paintText.setColor(Color.rgb(177,72,255));
            canvas.drawText(String.valueOf(partie.getScore2()), 3 * screenWidth / 4, 50, paintText);

            ///////////////////////////////////////
            //            DEBUG                 //
            /////////////////////////////////////

            // Dessiner la vitesse du premier paddle et de la balle
            /*String speedTextPaddle = "Vitesse du paddle 1 : " + paddles.get(0).getSpeed();
            String speedTextBallX = "Vitesse de la balle (X) : " + ball.getSpeedX();
            String speedTextBallY = "Vitesse de la balle (Y) : " + ball.getSpeedY();
            canvas.drawText(speedTextPaddle, screenWidth / 2, screenHeight / 2 - 60, paintText);
            canvas.drawText(speedTextBallX, screenWidth / 2, screenHeight / 2, paintText);
            canvas.drawText(speedTextBallY, screenWidth / 2, screenHeight / 2 + 60, paintText);*/

            //String speedText = "Vitesse du paddle 1 : " + paddles.get(0).getSpeed();
            //canvas.drawText(speedText, screenWidth / 2, screenHeight / 2, paintText);

            surfaceHolder.unlockCanvasAndPost(canvas); // Utilisez la variable surfaceHolder pour déverrouiller et poster le Canvas
        }

        //getHolder().unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        updateGame();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public boolean partieFinie() {
        return partieFinie;
    }
}

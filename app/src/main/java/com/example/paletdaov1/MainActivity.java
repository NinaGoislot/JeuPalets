package com.example.paletdaov1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.view.View;
import android.view.SurfaceView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener{

    private static final float PADDLE_RADIUS = 80;
    private static final float BALL_RADIUS = 40;

    private LinearLayout zoneDeJeu;
    private int screenWidth, screenHeight, nbPlayers, numeroPlayer =0,partieid;
    private Ball ball;
    private CanvasView canvasView;
    private boolean gameRunning = true;
    private List<Paddle> paddleList;
    private List<Bonus> bonusList;
    private Partie partie;
    boolean partieFini = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mettre l'application en plein écran
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Création de la partie
        partie = (Partie) getIntent().getSerializableExtra("partie");
        partieid = partie.getId();
        nbPlayers = partie.getNbPlayers();
        init(nbPlayers);

        setContentView(canvasView);

        // Obtenir le SurfaceHolder et ajouter le callback
        SurfaceHolder surfaceHolder = canvasView.getHolder();
        surfaceHolder.addCallback(canvasView);


    }

    private void init(int numberPlayers) {
        // Obtenir les dimensions de l'écran
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        // Créer d'une instance de Ball
        ball = new Ball(screenWidth / 2f, screenHeight / 2f, BALL_RADIUS, Color.WHITE);

        // Création d'une liste de paddles pour les joueurs
        paddleList = new ArrayList<>();

        // Générer les paddles en fonction du nombre de joueurs
        for (int i = 0; i < numberPlayers; i++) {
            //float paddleY = (i % 2 == 0) ? screenHeight - 400 : 2 * PADDLE_RADIUS;

            int couleur; // Variable pour stocker la couleur du paddle
            float paddleX;
            float paddleY;

            switch (i) {
                case 0:
                    couleur = Color.rgb(177,72,255);
                    if (nbPlayers <= 2){
                        paddleX = screenWidth - (screenWidth/7);
                        paddleY = screenHeight / 2 - PADDLE_RADIUS;
                        numeroPlayer = 1;
                    } else {
                        paddleX = screenWidth - (screenWidth/7);
                        paddleY = (screenHeight / 2 - PADDLE_RADIUS) + screenHeight/4 ;
                    }
                    break;
                case 1:
                    couleur = Color.rgb(255,179,65);
                    if (nbPlayers <= 2){
                        paddleX = 0 + screenWidth/20;
                        paddleY = screenHeight / 2 - PADDLE_RADIUS;
                        numeroPlayer = 2;
                    } else {
                        paddleX = 0 + screenWidth/20;
                        paddleY = (screenHeight / 2 - PADDLE_RADIUS) + screenHeight/4 ;
                    }
                    break;
                case 2:
                    couleur = Color.rgb(177,72,255);;
                    paddleX = screenWidth - (screenWidth/7);
                    paddleY = (screenHeight / 2 - PADDLE_RADIUS) - screenHeight/4 ;
                    break;
                case 3:
                    couleur = Color.rgb(255,179,65);
                    paddleX = 0 + screenWidth/20;
                    paddleY = (screenHeight / 2 - PADDLE_RADIUS) - screenHeight/4 ;
                    break;
                default:
                    couleur = Color.BLACK; // Couleur par défaut si i n'est pas dans les cas spécifiés
                    paddleX = screenWidth/2;
                    paddleY = PADDLE_RADIUS;
                    break;
            }

            Paddle paddle = new Paddle(paddleX, paddleY, PADDLE_RADIUS, couleur);
            if(numeroPlayer != 0){
                paddle.setNumeroPlayer(numeroPlayer);
            }
            paddleList.add(paddle);

            /*Paddle paddle = new Paddle((screenWidth - 2 * PADDLE_RADIUS) / 2, paddleY, PADDLE_RADIUS, couleur);
            paddleList.add(paddle);*/
        }

        // Création d'une liste pour les bonus des joueurs
        if(nbPlayers <= 2) {
            bonusList = new ArrayList<>();

            // Création des bonus du joueur
            Bonus bonusPlayer1 = new Bonus(screenWidth, screenHeight, 1);
            bonusPlayer1.setPaddleList(paddleList);
            bonusList.add(bonusPlayer1);

            Bonus bonusPlayer2 = new Bonus(screenWidth, screenHeight, 2);
            bonusPlayer2.setPaddleList(paddleList);
            bonusList.add(bonusPlayer2);
        }

        // Créer une instance de CanvasView
        if (bonusList != null) {
            canvasView = new CanvasView(this, paddleList, bonusList, ball, screenWidth, screenHeight, partie);
        } else {
            canvasView = new CanvasView(this, paddleList, ball, screenWidth, screenHeight, partie);
        }
        canvasView.setOnTouchListener(this);
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        float touchX = event.getX(pointerIndex);
        float touchY = event.getY(pointerIndex);
        int zone1Height = screenWidth / 2;
        int currentZone = 0;

        // Vérifier dans quelle zone se trouve le toucher
        if (touchX < zone1Height) {
            currentZone = 2;
        } else {
            currentZone = 1;
        }

        Log.d("test", String.valueOf(currentZone));
        Log.d("test", String.valueOf(touchX));
        Log.d("test", String.valueOf(zone1Height));

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                Paddle paddle = getPaddleByTouch(touchX, touchY);
                if (paddle != null && paddle.getPointerId() == -1) { // si le paddle n'est pas associé à un autre pointeur
                    paddle.setPointerId(pointerId);
                } else {
                    Bonus bonus = getBonusByZone(currentZone);
                    if (bonus != null) {
                        if (isInsideBonus(touchX, touchY, bonus)) {
                            bonus.handleBonusClick();
                        }
                    }
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                for (int i = 0; i < event.getPointerCount(); i++) {
                    pointerId = event.getPointerId(i);
                    touchX = event.getX(i);
                    touchY = event.getY(i);
                    Paddle paddle = getPaddleByPointerId(pointerId);
                    if (paddle != null) {
                        paddle.updatePosition(touchX, touchY, pointerId);
                    }
                }
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP: {
                Paddle paddle = getPaddleByPointerId(pointerId);
                if (paddle != null) {
                    paddle.setPointerId(-1); // dissocier le paddle du pointeur
                }
                break;
            }
        }
        canvasView.drawGameElements();

        return true;
    }

    private boolean isInsidePaddle(float touchX, float touchY, Paddle paddle) {
        float paddleX = paddle.getX();
        float paddleY = paddle.getY();
        float radius = paddle.getRadius();
        boolean isInside = false;

        if (touchX >= paddleX && touchX <= paddleX + 2 * radius &&
                touchY >= paddleY && touchY <= paddleY + 2 * radius) {
            isInside = true;
        }

        return isInside;
    }

    private Paddle getPaddleByTouch(float touchX, float touchY) {
        for (Paddle paddle : paddleList) {
            if (isInsidePaddle(touchX, touchY, paddle)) {
                return paddle;
            }
        }
        return null;
    }

    private Paddle getPaddleByPointerId(int pointerId) {
        for (Paddle paddle : paddleList) {
            if (paddle.getPointerId() == pointerId) {
                return paddle;
            }
        }
        return null;
    }

    private boolean isInsideBonus(float touchX, float touchY, Bonus bonus) {
        float bonusX = bonus.getX();
        float bonusY = bonus.getY();
        int size = bonus.getSize();
        boolean isInside = false;

        if (touchX >= bonusX && touchX <= bonusX + size &&
                touchY >= bonusY && touchY <= bonusY + size) {
            isInside = true;
        }

        return isInside;
    }

    private Bonus getBonusByZone(int zone) {
        for (Bonus bonus : bonusList) {
            if (bonus.getNumeroPlayer() == zone) {
                return bonus;
            }
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameRunning = true;
        startGameLoop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameRunning = false;
    }


    private void startGameLoop() {
        Runnable gameLoop = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for(Paddle paddle : paddleList){
                        paddle.updateSpeed(paddle.getPrevX(), paddle.getPrevY(), paddle.getLastPointerId());
                    }
                    canvasView.updateGame();
                    partieFini = canvasView.partieFinie();
                    if(partieFini){

                        if (partie.getScore1() == 2) {
                            // L'équipe 1 a gagné
                            if (partie.getNbPlayers() == 2) {
                                // Mode 1vs1
                                Player winningPlayer = AppDataBase.getAppDataBase(getApplicationContext()).getMyDao().getPlayerByPseudo(partie.getPlayer1());
                                winningPlayer.setGameWin(winningPlayer.getGameWin() + 1);
                                winningPlayer.setTotalGames(winningPlayer.getTotalGames() + 1);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AppDataBase.getAppDataBase(getApplicationContext()).getMyDao().updatePlayer(winningPlayer);
                                    }
                                }).start();
                            } else {
                                // Mode 2vs2
                                Player winningPlayer1 = AppDataBase.getAppDataBase(getApplicationContext()).getMyDao().getPlayerByPseudo(partie.getTeam1joueur1());
                                Player winningPlayer2 = AppDataBase.getAppDataBase(getApplicationContext()).getMyDao().getPlayerByPseudo(partie.getTeam1joueur2());
                                winningPlayer1.setGameWin(winningPlayer1.getGameWin() + 1);
                                winningPlayer1.setTotalGames(winningPlayer1.getTotalGames() + 1);
                                winningPlayer2.setGameWin(winningPlayer2.getGameWin() + 1);
                                winningPlayer2.setTotalGames(winningPlayer2.getTotalGames() + 1);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AppDataBase.getAppDataBase(getApplicationContext()).getMyDao().updatePlayer(winningPlayer1);
                                        AppDataBase.getAppDataBase(getApplicationContext()).getMyDao().updatePlayer(winningPlayer2);
                                    }
                                }).start();
                            }

                            partie.setScore1(2);
                            partie.setScore2(partie.getScore2());

                        } else if (partie.getScore2() == 2) {
                            // L'équipe 2 a gagné
                            if (partie.getNbPlayers() == 2) {
                                // Mode 1vs1
                                Player winningPlayer = AppDataBase.getAppDataBase(getApplicationContext()).getMyDao().getPlayerByPseudo(partie.getPlayer2());
                                winningPlayer.setGameWin(winningPlayer.getGameWin() + 1);
                                winningPlayer.setTotalGames(winningPlayer.getTotalGames() + 1);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AppDataBase.getAppDataBase(getApplicationContext()).getMyDao().updatePlayer(winningPlayer);
                                    }
                                }).start();
                            } else {
                                Player winningPlayer1 = AppDataBase.getAppDataBase(getApplicationContext()).getMyDao().getPlayerByPseudo(partie.getTeam2joueur1());
                                Player winningPlayer2 = AppDataBase.getAppDataBase(getApplicationContext()).getMyDao().getPlayerByPseudo(partie.getTeam2joueur2());
                                winningPlayer1.setGameWin(winningPlayer1.getGameWin() + 1);
                                winningPlayer1.setTotalGames(winningPlayer1.getTotalGames() + 1);
                                winningPlayer2.setGameWin(winningPlayer2.getGameWin() + 1);
                                winningPlayer2.setTotalGames(winningPlayer2.getTotalGames() + 1);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AppDataBase.getAppDataBase(getApplicationContext()).getMyDao().updatePlayer(winningPlayer1);
                                        AppDataBase.getAppDataBase(getApplicationContext()).getMyDao().updatePlayer(winningPlayer2);
                                    }
                                }).start();
                            }
                            partie.setScore2(2);
                            partie.setScore1(partie.getScore1());
                        }

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                AppDataBase.getAppDataBase(getApplicationContext()).getMyDao().updatePartie(partie);
                            }
                        }).start();

                        // FinPartie
                        Intent intent = new Intent(MainActivity.this, finPartie.class);
                        if (partie.getScore1() == 2) {
                            // L'équipe 1 a gagné
                            if (partie.getNbPlayers() == 2) {
                                // Mode 1vs1
                                intent.putExtra("winnerNames", partie.getPlayer1());
                            } else {
                                // Mode 2vs2
                                intent.putExtra("winnerNames", partie.getTeam1joueur1() + ", " + partie.getTeam1joueur2());
                                intent.putExtra("teamName", partie.getTeam1());
                            }
                        } else if (partie.getScore2() == 2) {
                            // L'équipe 2 a gagné
                            if (partie.getNbPlayers() == 2) {
                                // Mode 1vs1
                                intent.putExtra("winnerNames", partie.getPlayer2());
                            } else {
                                // Mode 2vs2
                                intent.putExtra("winnerNames", partie.getTeam2joueur1() + ", " + partie.getTeam2joueur2());
                                intent.putExtra("teamName", partie.getTeam2());
                            }
                        }
                        startActivity(intent);
                        break;
                    }

                    try {
                        Thread.sleep(16); // 16 millisecondes entre chaque mise à jour (environ 60 FPS)
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(gameLoop).start();
    }


}
package com.example.paletdaov1;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class PseudoAssigner extends AppCompatActivity {

    private TextView[] playerTextViews;
    boolean isAllFieldsFilled = false;
    boolean textfilled = false;
    int numTextView;
    int numPlayers;
    boolean[] teamExistAlready = new boolean[2];
    int changehint =0;
    int assignTeam=0;
    String team1;
    String team2;
    ArrayList<String> playerList = new ArrayList<>();
    Partie partie;
    private String[] listePseudo ;

    private AppDataBase dbAccess;
    private MyDao daoQuery;

    public void accessDataBase() {
        dbAccess = AppDataBase.getAppDataBase(this);
        daoQuery = dbAccess.getMyDao();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pseudo_assigner);

        numPlayers = getIntent().getIntExtra("playerType", 0);
        createPlayerTextViews(numPlayers);
        EditText pseudoJoueur = findViewById(R.id.pseudoJoueur);
        Button submitPseudo = findViewById(R.id.submitPseudo);

        switch (numPlayers) {
            case 2:
                pseudoJoueur.setHint("Pseudo Joueur 1");
                break;
            case 4:
                pseudoJoueur.setHint("Nom Team 1");
                break;
        }

        listePseudo = new String[10];

        submitPseudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Vérifie si tous les champs ont été remplie

                String pseudo = pseudoJoueur.getText().toString();
                if (pseudo.isEmpty()) {
                    pseudo = generateRandomString(6);
                }
                listePseudo[changehint] = pseudo;
                pseudoJoueur.setText("");

                for (TextView textView : playerTextViews) {
                    if (textView != null) {
                        if (textView.getText().toString().isEmpty()) {
                            textView.setText(pseudo);
                            break;
                        }
                    }
                }
                changehint++;
                boolean isAllFieldsFilled = checkAllFieldsFilled();
                switch (numPlayers) {
                    case 2:
                        switch (changehint) {
                            case 1:
                                pseudoJoueur.setHint("Pseudo Joueur 2");
                                break;
                            case 2:
                                pseudoJoueur.setVisibility(View.GONE);
                                break;
                        }
                        break;
                    case 4:
                        switch (changehint) {
                            case 1:
                                pseudoJoueur.setHint("Nom Team 2");
                                break;
                            case 2:
                                isTeamExists();
                                pseudoJoueur.setHint("Pseudo Joueur");
                                break;
                            case 3:
                                pseudoJoueur.setHint("Pseudo Joueur");
                                break;
                            case 4:
                                pseudoJoueur.setHint("Pseudo Joueur");
                                break;
                            case 5:
                                pseudoJoueur.setHint("Pseudo Joueur");
                                break;
                            case 6:
                                pseudoJoueur.setVisibility(View.GONE);
                                break;
                        }
                        break;
                }

                if (isAllFieldsFilled) {
                    if (textfilled) {
                        switch (numPlayers) {
                            case 2:

                                createPlayerThread(playerTextViews[0].getText().toString());
                                createPlayerThread(playerTextViews[1].getText().toString());

                                break;
                            case 4:

                                createPlayerThread(playerTextViews[2].getText().toString());
                                createPlayerThread(playerTextViews[3].getText().toString());
                                createPlayerThread(playerTextViews[4].getText().toString());
                                createPlayerThread(playerTextViews[5].getText().toString());


                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                createTeamThread();
                                break;
                        }
                        Log.d("TAG", "create");
                        partie = createPartie();
                        Log.d("DEBUG", "Partie: " + partie);
                        Intent intent = new Intent(PseudoAssigner.this, MainActivity.class);
                        intent.putExtra("playerType", numPlayers);
                        intent.putExtra("playerList", playerList);
                        intent.putExtra("partie", partie);
                        startActivity(intent);
                    } else {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) submitPseudo.getLayoutParams();
                        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        params.addRule(RelativeLayout.CENTER_VERTICAL);
                        submitPseudo.setLayoutParams(params);
                        submitPseudo.setText("Lancer la Partie");
                        textfilled = true;
                    }
                }
            }
        });
    }

    private void createTeamThread(){
        accessDataBase();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Team existingTeam1 = daoQuery.getTeamByName(listePseudo[0]);
                if (existingTeam1 == null) {
                    daoQuery.insertTeam(new Team(listePseudo[0],listePseudo[2],listePseudo[3]));
                }
                Team existingTeam2 = daoQuery.getTeamByName(listePseudo[1]);
                if (existingTeam2 == null) {
                    daoQuery.insertTeam(new Team(listePseudo[1],listePseudo[4],listePseudo[5]));
                }
            }
        }).start();
    }

    // Méthode pour générer une chaîne de caractères aléatoires
    public String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            result.append(characters.charAt(index));
        }
        return result.toString();
    }

    //méthode pour créer la Partie
    private Partie createPartie(){
        Log.d("TAG", "create partie");
        accessDataBase();
        Thread newThread = new Thread(new Runnable() {
            @Override
            public void run() {
                switch (numPlayers){
                    case 2:
                        for (int i = 0; i < 2; i++) {
                            playerList.add(playerTextViews[i].getText().toString());
                        }
                        partie = new Partie(playerList.get(0),playerList.get(1));
                        daoQuery.insertPartie(partie);
                        Log.d("TAG", "liste player 1v1: ");
                        break;
                    case 4:
                        for (int i = 0; i < 6; i++) {
                            playerList.add(playerTextViews[i].getText().toString());
                        }
                        partie = new Partie(playerList.get(0),playerList.get(1),playerList.get(2),playerList.get(3),playerList.get(4),playerList.get(5));
                        daoQuery.insertPartie(partie);
                        Log.d("TAG", "liste player team: ");
                        break;
                }
            }
        });
        newThread.start();
        try {
            newThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return partie;
    }


    //méthode pour créer les Player
    private void createPlayerThread(final String pseudo) {
        accessDataBase();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Player existingPlayer = daoQuery.getPlayerByPseudo(pseudo);
                if (existingPlayer == null) {
                    daoQuery.insertPlayer(new Player(pseudo));
                }
            }
        }).start();
    }

    private boolean checkAllFieldsFilled() {
        for (TextView textView : playerTextViews) {
            if (textView != null) {
                if (textView.getText().toString().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }


    //méthode pour afficher les Player dans les textViews
    private void createPlayerTextViews(int numPlayers) {
        if (numPlayers == 4) {
            numTextView = numPlayers+2;
        } else {
            numTextView = numPlayers;
        }
        playerTextViews = new TextView[numTextView];
        RelativeLayout joueurLayout = findViewById(R.id.joueurLayout);

        switch (numPlayers) {
            case 4:
                for (int i = 0; i < numPlayers + 2; i++) {
                    TextView textView = new TextView(this);
                    textView.setId(View.generateViewId());
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    // Set position based on player index
                    switch (i) {
                        case 0:
                            textView.setTextColor(Color.BLUE);
                            textView.setTextSize(32); // Remplacez la valeur 16 par la taille souhaitée en pixels
                            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            params.addRule(RelativeLayout.CENTER_VERTICAL);
                            break;
                        case 1:
                            textView.setTextColor(Color.RED);
                            textView.setTextSize(32); // Remplacez la valeur 16 par la taille souhaitée en pixels
                            params.addRule(RelativeLayout.CENTER_VERTICAL);
                            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                            break;
                        case 2:
                            textView.setTextColor(Color.BLUE);
                            textView.setTextSize(24); // Remplacez la valeur 16 par la taille souhaitée en pixels
                            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                            break;
                        case 3:
                            textView.setTextColor(Color.BLUE);
                            textView.setTextSize(24); // Remplacez la valeur 16 par la taille souhaitée en pixels
                            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                            break;
                        case 4:
                            textView.setTextColor(Color.RED);
                            textView.setTextSize(24); // Remplacez la valeur 16 par la taille souhaitée en pixels
                            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                            break;
                        case 5:
                            textView.setTextColor(Color.RED);textView.setTextSize(24); // Remplacez la valeur 16 par la taille souhaitée en pixels
                            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                            break;
                    }
                    textView.setLayoutParams(params);
                    joueurLayout.addView(textView);
                    playerTextViews[i] = textView;
                }
                break;
            case 2:
                for (int i = 0; i < numPlayers; i++) {
                    TextView textView = new TextView(this);
                    textView.setId(View.generateViewId());
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    switch (i) {
                        case 0:
                            textView.setTextColor(Color.BLUE);
                            textView.setTextSize(24); // Remplacez la valeur 16 par la taille souhaitée en pixels
                            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            params.addRule(RelativeLayout.CENTER_VERTICAL);
                            break;
                        case 1:
                            textView.setTextColor(Color.RED);
                            textView.setTextSize(24); // Remplacez la valeur 16 par la taille souhaitée en pixels
                            params.addRule(RelativeLayout.CENTER_VERTICAL);
                            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                            break;
                    }
                    textView.setLayoutParams(params);
                    joueurLayout.addView(textView);
                    playerTextViews[i] = textView;
                }
                break;
        }

    }


    //Si une team exist on insert les pseudos des joueurs
    public void isTeamExists() {
        teamExist(listePseudo[0],listePseudo[1]);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        accessDataBase();
        boolean team1Exist = teamExistAlready[0];
        boolean team2Exist = teamExistAlready[1];
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(team1Exist == true){
                    changehint=changehint+2;
                    String player1Pseudo = daoQuery.getPlayer1PseudoByTeamName(listePseudo[0]);
                    String player2Pseudo = daoQuery.getPlayer2PseudoByTeamName(listePseudo[0]);
                    listePseudo[2] = player1Pseudo;
                    listePseudo[3] = player2Pseudo;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Mettre à jour les TextView avec les pseudos des joueurs de team1
                            playerTextViews[2].setText(player1Pseudo);
                            playerTextViews[3].setText(player2Pseudo);
                        }
                    });
                }
                if (team2Exist==true) {
                    String player3Pseudo = daoQuery.getPlayer1PseudoByTeamName(listePseudo[1]);
                    listePseudo[4] = player3Pseudo;
                    String player4Pseudo = daoQuery.getPlayer2PseudoByTeamName(listePseudo[1]);
                    listePseudo[5] = player4Pseudo;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Mettre à jour les TextView avec les pseudos des joueurs de team2
                            playerTextViews[4].setText(player3Pseudo);
                            playerTextViews[5].setText(player4Pseudo);
                        }
                    });
                }

            }
        }).start();
    }

    //vérif si une team exist
    public void teamExist(String team1, String team2) {
        accessDataBase();
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean teamverif1 = daoQuery.isTeamExists(team1);
                boolean teamverif2 = daoQuery.isTeamExists(team2);
                teamExistAlready[0] = teamverif1;
                teamExistAlready[1] = teamverif2;
            }
        }).start();
    }



}

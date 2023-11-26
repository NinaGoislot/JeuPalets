package com.example.paletdaov1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    Button buttonmenu;
    private AppDataBase dbAccess;
    private MyDao daoQuery;
    private ListView listView;
    private ListView listTeamView;

    private ListView historique;


    public void accessDataBase() {
        dbAccess = AppDataBase.getAppDataBase(this);
        daoQuery = dbAccess.getMyDao();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        buttonmenu = findViewById(R.id.buttonmenu);


        buttonmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, Menu.class);
                startActivity(intent);
            }
        });


        listView = (ListView) findViewById(R.id.topScoreList);
        listTeamView = (ListView) findViewById(R.id.listTeam);
        historique = (ListView) findViewById(R.id.historique);

        updateHistorique();

    }

    private void updateHistorique() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                accessDataBase();

                List<Player> players = daoQuery.getAllPlayers();
                List<String> playerInfoList = new ArrayList<>();
                List<Team> teams = daoQuery.getAllTeams();
                List<String> teaminfoList = new ArrayList<>();
                List<Partie> parties = daoQuery.getAllParties();
                List<String> partieinfoList = new ArrayList<>();

                for (int i = 0; i < players.size(); i++) {
                    Player player = players.get(i);
                    String playerInfo = (i + 1) + " - " + player.getPseudo() + " " + player.winPercentage() + "% de victoire";
                    playerInfoList.add(playerInfo);
                }

                for (int i = 0; i < teams.size(); i++) {
                    Team team = teams.get(i);
                    String teaminfo = (i + 1) + " - " + team.getNameTeam() + " - " + team.getPlayer1Pseudo() + "-" + team.getPlayer2Pseudo();
                    teaminfoList.add(teaminfo);
                }

                for (int i = 0; i < parties.size(); i++) {
                    Partie partie = parties.get(i);
                    String partieInfo;
                    if (partie.getNbPlayers() == 2) {
                        partieInfo = "" + partie.getId() + "-" + partie.getPlayer1() + "-" + partie.getScore1() + ":" + partie.getScore2() + "-" + partie.getPlayer2();
                    } else {
                        partieInfo = "" + partie.getId() + "-" + partie.getTeam1() + "-" + partie.getTeam1joueur1() + "|" + partie.getTeam1joueur2() + "-" + partie.getScore1() + ":" + partie.getScore2() + "-" + partie.getTeam2joueur1() + "|" + partie.getTeam2joueur2() + "-" + partie.getTeam2();
                    }
                    partieinfoList.add(partieInfo);
                    Log.d("Historique", "Partie " + (i + 1) + ": " + partieInfo);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                HistoryActivity.this,
                                R.layout.afficheinfojoueur,
                                R.id.affichePseudo,
                                playerInfoList
                        );
                        listView.setAdapter(adapter);

                        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                                HistoryActivity.this,
                                R.layout.afficheinfojoueur,
                                R.id.affichePseudo,
                                teaminfoList
                        );
                        listTeamView.setAdapter(adapter2);

                        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(
                                HistoryActivity.this,
                                R.layout.afficheinfojoueur,
                                R.id.affichePseudo,
                                partieinfoList
                        );
                        historique.setAdapter(adapter3);
                    }
                });
            }
        }).start();
    }
}
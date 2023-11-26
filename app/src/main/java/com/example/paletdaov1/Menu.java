package com.example.paletdaov1;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class Menu extends AppCompatActivity {

    private AppDataBase dbAccess;
    private MyDao daoQuery;
    private ListView listView;
    Cursor cursor;
    private SimpleCursorAdapter adapter;
    private RadioGroup radioGroup;
    private Button demarrer;
    private Button history;

    private Button regles;

    public void accessDataBase() {
        dbAccess = AppDataBase.getAppDataBase(this);
        daoQuery = dbAccess.getMyDao();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        demarrer = findViewById(R.id.demarrer);
        history = findViewById(R.id.history);
        regles = findViewById(R.id.regles);
        radioGroup = findViewById(R.id.radioGroup);
        demarrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();

                if (selectedId == -1) {
                    // Aucun bouton sélectionné, afficher un message d'erreur
                    Toast.makeText(Menu.this, "Sélectionnez votre mode de jeu", Toast.LENGTH_SHORT).show();
                } else {
                    int playerType = 0;

                    if (selectedId == R.id.deuxjoueurs) {
                        playerType = 2;
                    } else if (selectedId == R.id.quatrejoueurs) {
                        playerType = 4;
                    }

                    Intent intent = new Intent(Menu.this, PseudoAssigner.class);
                    intent.putExtra("playerType", playerType);
                    startActivity(intent);
                }
            }
        });

        history.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Menu.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        regles.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Menu.this, regleActivity.class);
                startActivity(intent);
            }
        });

        listView = (ListView) findViewById(R.id.topScoreList);

        new Thread(() -> {
            accessDataBase();
            daoQuery.insertPlayer(new Player("blup", 248,47));
            daoQuery.insertPlayer(new Player("blop", 5,1));
            daoQuery.insertPlayer(new Player("blip", 6,2));
            daoQuery.insertPlayer(new Player("blap", 1,2));
            daoQuery.insertTeam(new Team("Test", "Test1","Test2"));

            List<Player> players = daoQuery.getTopPlayers();
            List<String> playerInfoList = new ArrayList<>();

            // Sélectionnez les trois premiers joueurs
            for (int i = 0; i < Math.min(players.size(), 3); i++) {
                Player player = players.get(i);
                String playerInfo = (i + 1) + " - " + player.getPseudo() + " " + player.winPercentage() + "% de victoire";
                playerInfoList.add(playerInfo);
            }
            //affiche les trois premier joueurs
            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this,
                        R.layout.afficheinfojoueur,
                        R.id.affichePseudo,
                        playerInfoList
                );
                listView.setAdapter(adapter);
            });
        }).start();



    }
}

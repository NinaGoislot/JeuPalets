package com.example.paletdaov1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class finPartie extends AppCompatActivity {

    Button buttonmenu;
    Button buttonrelancer;
    TextView winnerMessage;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin_partie);

        buttonmenu = findViewById(R.id.menu);
        buttonrelancer = findViewById(R.id.relancer);
        buttonmenu.setGravity(Gravity.CENTER_HORIZONTAL);
        buttonrelancer.setGravity(Gravity.CENTER_HORIZONTAL);

        // Affichage des gagnants
        winnerMessage = findViewById(R.id.winnerMessage);
        String gagnants = getIntent().getStringExtra("winnerNames");
        String equipe = getIntent().getStringExtra("teamName");

        // On adapte en fonction du nombre de joueurs
        if(equipe !=null){
            // 2c2
            winnerMessage.setText("Bravo à " + gagnants + " de l'équipe " + equipe + "! Vous avez remporté la partie !");
        } else {
            //1c1
            winnerMessage.setText("Bravo à " + gagnants + "! Tu as remporté la partie !");
        }


        buttonmenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(finPartie.this, Menu.class);
                startActivity(intent);
            }
        });

        buttonrelancer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(finPartie.this, Menu.class);
                startActivity(intent);
            }
        });
    }
}

package com.example.paletdaov1;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Partie implements Serializable {
    @PrimaryKey (autoGenerate = true)
    private int id;
    private int nbPlayers;
    private int score1;
    private int score2;
    private String team1joueur1;
    private String team1joueur2;
    private String team2joueur1;
    private String team2joueur2;
    private String team1;
    private String team2;
    private String player1;
    private String player2;

    @Ignore
    public Partie(String team1, String team2 ,String team1joueur1,String team1joueur2,String team2joueur1, String team2joueur2) {
        this.nbPlayers = 4;
        this.score1 = 0;
        this.score2 = 0;
        this.team1 = team1;
        this.team2 = team2;
        this.team1joueur1 = team1joueur1;
        this.team1joueur2 = team1joueur2;
        this.team2joueur1 = team2joueur1;
        this.team2joueur2 = team2joueur2;
        this.player1 = "";
        this.player2 = "";
    }

    public Partie(String player1, String player2) {
        this.nbPlayers = 2;
        this.score1 = 0;
        this.score2 = 0;
        this.team1 = "";
        this.team2 = "";
        this.team1joueur1 = "";
        this.team1joueur2 = "";
        this.team2joueur1 = "";
        this.team2joueur2 = "";
        this.player1 = player1;
        this.player2 = player2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getNbPlayers() {
        return nbPlayers;
    }

    public void setNbPlayers(int nbPlayers) {
        this.nbPlayers = nbPlayers;
    }

    public int getScore1() {
        return score1;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public String getTeam1joueur1() {
        return team1joueur1;
    }

    public void setTeam1joueur1(String team1joueur1) {
        this.team1joueur1 = team1joueur1;
    }

    public String getTeam1joueur2() {
        return team1joueur2;
    }

    public void setTeam1joueur2(String team1joueur2) {
        this.team1joueur2 = team1joueur2;
    }

    public String getTeam2joueur1() {
        return team2joueur1;
    }

    public void setTeam2joueur1(String team2joueur1) {
        this.team2joueur1 = team2joueur1;
    }

    public String getTeam2joueur2() {
        return team2joueur2;
    }

    public void setTeam2joueur2(String team2joueur2) {
        this.team2joueur2 = team2joueur2;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }
}


package com.example.paletdaov1;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Team")
public class Team {
    @PrimaryKey
    @NonNull
    private String nameTeam;

    private String player1Pseudo;
    private String player2Pseudo;

    private int gameWon;
    private int gameLoose;

    public Team(@NonNull String nameTeam, String player1Pseudo, String player2Pseudo) {
        this.nameTeam = nameTeam;
        this.player1Pseudo = player1Pseudo;
        this.player2Pseudo = player2Pseudo;
        this.gameWon = 0;
        this.gameLoose = 0;
    }


    // Getters and setters

    @NonNull
    public String getNameTeam() {
        return nameTeam;
    }

    public void setNameTeam(@NonNull String nameTeam) {
        this.nameTeam = nameTeam;
    }

    public String getPlayer1Pseudo() {
        return player1Pseudo;
    }

    public void setPlayer1Pseudo(String player1Pseudo) {
        this.player1Pseudo = player1Pseudo;
    }

    public String getPlayer2Pseudo() {
        return player2Pseudo;
    }

    public void setPlayer2Pseudo(String player2Pseudo) {
        this.player2Pseudo = player2Pseudo;
    }

    public int getGameWon() {
        return gameWon;
    }

    public void setGameWon(int gameWon) {
        this.gameWon = gameWon;
    }

    public int getGameLoose() {
        return gameLoose;
    }

    public void setGameLoose(int gameLoose) {
        this.gameLoose = gameLoose;
    }
}

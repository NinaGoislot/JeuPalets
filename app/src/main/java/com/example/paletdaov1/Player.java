package com.example.paletdaov1;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Player", indices = {@Index(value = "pseudo", unique = true)})
public class Player {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") // Spécifiez le nom de la colonne comme "id"
    private int playerId;
    @NonNull
    private String pseudo;
    private int gameWin;
    private int gameLoose;
    private int totalGames;

    public Player(@NonNull String pseudo) {
        this.pseudo = pseudo;
        this.gameWin = 0;
        this.gameLoose = 0;
        this.totalGames = 0;
    }

    public Player(@NonNull String pseudo,int win,int loose) {
        this.pseudo = pseudo;
        this.gameWin = win;
        this.gameLoose = loose;
        this.totalGames = gameWin+gameLoose;
    }
    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    @NonNull
    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(@NonNull String pseudo) {
        this.pseudo = pseudo;
    }

    public int getGameWin() {
        return gameWin;
    }

    public void setGameWin(int gameWin) {
        this.gameWin = gameWin;
    }

    public int getGameLoose() {
        return gameLoose;
    }

    public void setGameLoose(int gameLoose) {
        this.gameLoose = gameLoose;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(int totalGames) {
        this.totalGames = totalGames;
    }

    public int winPercentage() {
        if (totalGames == 0) {
            return 0;
        } else {
            return (int) Math.round((double) gameWin / totalGames * 100);
        }
    }

}
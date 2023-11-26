package com.example.paletdaov1;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertPlayer(Player player);


    @Update
    void updatePlayer(Player player);

    @Delete
    void deletePlayer(Player player);

    @Query("SELECT * FROM Player")
    List<Player> getAllPlayers();

    @Query("SELECT * FROM Player ORDER BY (gameWin * 100 / totalGames) DESC LIMIT 3")
    List<Player> getTopPlayers();

    @Query("SELECT * FROM Player WHERE pseudo = :pseudo")
    Player getPlayerByPseudo(String pseudo);

    @Query("SELECT * FROM Team WHERE nameTeam = :nameTeam")
    Team getTeamByName(String nameTeam);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTeam(Team team);
    @Query("SELECT * FROM Team")
    List<Team> getAllTeams();

    @Update
    void updateTeam(Team team);

    @Update
    void updatePartie(Partie partie);

    @Query("SELECT player1Pseudo FROM Team WHERE nameTeam = :teamName")
    String getPlayer1PseudoByTeamName(String teamName);

    @Query("SELECT player2Pseudo FROM Team WHERE nameTeam = :teamName")
    String getPlayer2PseudoByTeamName(String teamName);

    @Query("SELECT EXISTS(SELECT 1 FROM Team WHERE nameTeam = :teamName LIMIT 1)")
    boolean isTeamExists(String teamName);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertPartie(Partie partie);

    @Query("SELECT * FROM Partie WHERE id = :partieId")
    Partie getPartieById(int partieId);

    @Query("SELECT * FROM Partie ORDER BY id DESC")
    List<Partie> getAllParties();
}

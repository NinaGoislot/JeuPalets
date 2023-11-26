package com.example.paletdaov1;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Player.class,Team.class, Partie.class}, version = 39)
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase bddInstance = null;

    public abstract MyDao getMyDao();

    public static AppDataBase getAppDataBase(Context context) {
        if (bddInstance == null) {
            synchronized (AppDataBase.class) {
                if (bddInstance == null) {
                    bddInstance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDataBase.class, "JeuPalet")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return bddInstance;
    }
}

package com.example.gestionetudiant.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.gestionetudiant.dao.EtudiantDao;
import com.example.gestionetudiant.model.Etudiant;

@Database(entities = {Etudiant.class}, version = 1, exportSchema = false)
public abstract class EtudiantDatabase extends RoomDatabase {
    private static volatile EtudiantDatabase INSTANCE;
    private static final String DATABASE_NAME = "etudiant_database.db";

    public abstract EtudiantDao etudiantDao();

    public static EtudiantDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (EtudiantDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    EtudiantDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
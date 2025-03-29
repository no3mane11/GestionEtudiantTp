package com.example.gestionetudiant.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.gestionetudiant.model.Etudiant;
import java.util.List;

@Dao
public interface EtudiantDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long ajouterEtudiant(Etudiant etudiant);

    @Update
    int modifierEtudiant(Etudiant etudiant);

    @Delete
    int supprimerEtudiant(Etudiant etudiant);

    @Query("DELETE FROM etudiant_table WHERE id = :id")
    int supprimerEtudiantParId(int id);

    @Query("SELECT * FROM etudiant_table WHERE id = :id")
    LiveData<Etudiant> chercherEtudiant(int id);

    @Query("SELECT * FROM etudiant_table ORDER BY nom ASC, prenom ASC")
    LiveData<List<Etudiant>> getAllEtudiants();

    @Query("SELECT * FROM etudiant_table WHERE nom LIKE :query OR prenom LIKE :query")
    LiveData<List<Etudiant>> rechercherEtudiants(String query);

    @Query("SELECT COUNT(*) FROM etudiant_table")
    LiveData<Integer> getCount();
}
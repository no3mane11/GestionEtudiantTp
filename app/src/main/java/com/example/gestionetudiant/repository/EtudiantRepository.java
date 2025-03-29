package com.example.gestionetudiant.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.gestionetudiant.dao.EtudiantDao;
import com.example.gestionetudiant.database.EtudiantDatabase;
import com.example.gestionetudiant.model.Etudiant;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EtudiantRepository {
    private final EtudiantDao etudiantDao;
    private final LiveData<List<Etudiant>> allEtudiants;
    private final Executor executor;

    public EtudiantRepository(Application application) {
        EtudiantDatabase database = EtudiantDatabase.getInstance(application);
        etudiantDao = database.etudiantDao();
        allEtudiants = etudiantDao.getAllEtudiants();
        executor = Executors.newFixedThreadPool(2);
    }

    public LiveData<List<Etudiant>> getAllEtudiants() {
        return allEtudiants;
    }

    public LiveData<Etudiant> chercherEtudiant(int id) {
        return etudiantDao.chercherEtudiant(id);
    }

    public LiveData<List<Etudiant>> rechercherEtudiants(String query) {
        return etudiantDao.rechercherEtudiants("%" + query + "%");
    }

    public LiveData<Integer> getCount() {
        return etudiantDao.getCount();
    }

    public void ajouterEtudiant(Etudiant etudiant, RepositoryCallback<Long> callback) {
        executor.execute(() -> {
            try {
                long id = etudiantDao.ajouterEtudiant(etudiant);
                callback.onSuccess(id);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void modifierEtudiant(Etudiant etudiant, RepositoryCallback<Integer> callback) {
        executor.execute(() -> {
            try {
                int rowsAffected = etudiantDao.modifierEtudiant(etudiant);
                callback.onSuccess(rowsAffected);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void supprimerEtudiant(int id, RepositoryCallback<Integer> callback) {
        executor.execute(() -> {
            try {
                int rowsAffected = etudiantDao.supprimerEtudiantParId(id);
                callback.onSuccess(rowsAffected);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public interface RepositoryCallback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }
}
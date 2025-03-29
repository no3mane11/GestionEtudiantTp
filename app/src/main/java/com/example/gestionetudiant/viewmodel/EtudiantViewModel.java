package com.example.gestionetudiant.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.gestionetudiant.model.Etudiant;
import com.example.gestionetudiant.repository.EtudiantRepository;
import java.util.List;

public class EtudiantViewModel extends AndroidViewModel {
    private final EtudiantRepository repository;
    private final LiveData<List<Etudiant>> allEtudiants;
    private final MutableLiveData<Integer> operationResult = new MutableLiveData<>();

    public EtudiantViewModel(@NonNull Application application) {
        super(application);
        repository = new EtudiantRepository(application);
        allEtudiants = repository.getAllEtudiants();
    }

    public LiveData<List<Etudiant>> getAllEtudiants() {
        return allEtudiants;
    }

    public LiveData<Etudiant> chercherEtudiant(int id) {
        return repository.chercherEtudiant(id);
    }

    public LiveData<List<Etudiant>> rechercherEtudiants(String query) {
        return repository.rechercherEtudiants(query);
    }

    public LiveData<Integer> getCount() {
        return repository.getCount();
    }

    public LiveData<Integer> ajouterEtudiant(Etudiant etudiant) {
        MutableLiveData<Integer> result = new MutableLiveData<>();
        repository.ajouterEtudiant(etudiant, new EtudiantRepository.RepositoryCallback<Long>() {
            @Override
            public void onSuccess(Long id) {
                result.postValue(id > 0 ? 1 : 0);
            }

            @Override
            public void onError(Exception e) {
                result.postValue(-1);
            }
        });
        return result;
    }

    public LiveData<Integer> modifierEtudiant(Etudiant etudiant) {
        MutableLiveData<Integer> result = new MutableLiveData<>();
        repository.modifierEtudiant(etudiant, new EtudiantRepository.RepositoryCallback<Integer>() {
            @Override
            public void onSuccess(Integer rowsAffected) {
                result.postValue(rowsAffected > 0 ? 1 : 0);
            }

            @Override
            public void onError(Exception e) {
                result.postValue(-1);
            }
        });
        return result;
    }

    public LiveData<Integer> supprimerEtudiant(int id) {
        MutableLiveData<Integer> result = new MutableLiveData<>();
        repository.supprimerEtudiant(id, new EtudiantRepository.RepositoryCallback<Integer>() {
            @Override
            public void onSuccess(Integer rowsAffected) {
                result.postValue(rowsAffected > 0 ? 1 : 0);
            }

            @Override
            public void onError(Exception e) {
                result.postValue(-1);
            }
        });
        return result;
    }

    public void refreshEtudiants() {
        // Cette méthode déclenchera un rafraîchissement des données LiveData
        allEtudiants.getValue();
    }
}
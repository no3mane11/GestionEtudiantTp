package com.example.gestionetudiant.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "etudiant_table")
public class Etudiant {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nom;
    private String prenom;
    private String dateNaissance;
    private String photoUri;

    public Etudiant(String nom, String prenom, String dateNaissance, String photoUri) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.photoUri = photoUri;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Etudiant etudiant = (Etudiant) o;
        return id == etudiant.id &&
                nom.equals(etudiant.nom) &&
                prenom.equals(etudiant.prenom) &&
                dateNaissance.equals(etudiant.dateNaissance) &&
                photoUri.equals(etudiant.photoUri);
    }

    @Override
    public String toString() {
        return "Etudiant{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", dateNaissance='" + dateNaissance + '\'' +
                ", photoUri='" + photoUri + '\'' +
                '}';
    }
}
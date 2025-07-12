package com.example.model;

public class Competence {
    private String nom;

    public Competence() {}

    public Competence(String nom) { this.nom = nom; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    @Override
    public String toString() { return nom; }
}
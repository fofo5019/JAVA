package com.example.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Personnage {
    private String nom;
    private int pointsDeVie;
    private int pointsDeVieMax;
    private int doublonsOr;
    private List<Objet> inventaire;
    private Set<String> competences;
    private Set<String> motsDePasse;

    public Personnage() {
        this.inventaire = new ArrayList<>();
        this.competences = new HashSet<>();
        this.motsDePasse = new HashSet<>();
    }

    public Personnage(String nom) {
        this();
        this.nom = nom;
        // Valeurs par défaut selon le livre
        this.pointsDeVie = 10;
        this.pointsDeVieMax = 10;
        this.doublonsOr = 10;
    }

    // Getters et Setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public int getPointsDeVie() { return pointsDeVie; }
    public void setPointsDeVie(int pointsDeVie) { this.pointsDeVie = pointsDeVie; }
    public int getPointsDeVieMax() { return pointsDeVieMax; }
    public void setPointsDeVieMax(int pointsDeVieMax) { this.pointsDeVieMax = pointsDeVieMax; }
    public int getDoublonsOr() { return doublonsOr; }
    public void setDoublonsOr(int doublonsOr) { this.doublonsOr = doublonsOr; }
    public List<Objet> getInventaire() { return inventaire; }
    public void setInventaire(List<Objet> inventaire) { this.inventaire = inventaire; }
    public Set<String> getCompetences() { return competences; }
    public void setCompetences(Set<String> competences) { this.competences = competences; }
    public Set<String> getMotsDePasse() { return motsDePasse; }
    public void setMotsDePasse(Set<String> motsDePasse) { this.motsDePasse = motsDePasse; }

    // Méthodes utilitaires
    public void modifierPointsDeVie(int delta) {
        this.pointsDeVie += delta;
        if (this.pointsDeVie > this.pointsDeVieMax) {
            this.pointsDeVie = this.pointsDeVieMax;
        }
        if (this.pointsDeVie < 0) {
            this.pointsDeVie = 0;
        }
    }

    public void ajouterObjet(Objet objet) {
        if (inventaire.size() < 8) {
            inventaire.add(objet);
        }
    }
    
    public boolean hasCompetence(String nom) {
        return competences.contains(nom);
    }

    public boolean hasObjet(String nom) {
        return inventaire.stream().anyMatch(o -> o.getNom().equalsIgnoreCase(nom));
    }
}
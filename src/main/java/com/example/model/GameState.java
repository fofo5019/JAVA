package com.example.model;

public class GameState {
    private int chapitreActuel;
    private Personnage joueur;

    public GameState() {}
    public int getChapitreActuel() { return chapitreActuel; }
    public void setChapitreActuel(int id) { this.chapitreActuel = id; }
    public Personnage getJoueur() { return joueur; }
    public void setJoueur(Personnage j) { this.joueur = j; }
}
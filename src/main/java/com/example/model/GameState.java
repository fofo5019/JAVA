package com.example.model;

import java.util.HashSet;
import java.util.Set;

public class GameState {
    private int chapitreActuel;
    private Personnage joueur;
    // Cet ensemble gardera en mémoire les chapitres dont les effets ont été appliqués
    private Set<Integer> chapitresVisites = new HashSet<>();

    public GameState() {}

    // Getters et Setters
    public int getChapitreActuel() { return chapitreActuel; }
    public void setChapitreActuel(int id) { this.chapitreActuel = id; }

    public Personnage getJoueur() { return joueur; }
    public void setJoueur(Personnage j) { this.joueur = j; }

    public Set<Integer> getChapitresVisites() { return chapitresVisites; }
    public void setChapitresVisites(Set<Integer> chapitresVisites) { this.chapitresVisites = chapitresVisites; }
}
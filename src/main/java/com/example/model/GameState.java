/*
╔══════════════════════════════════════════════════════╗
║  Projet Java – Le livre dont vous êtes le héros      ║
║  Le Pirate des 7 Mers                               ║
║                                                      ║
║  ESGI 2 – Franck Giordano & Louis Dalet – 2025      ║
╚══════════════════════════════════════════════════════╝
*/

package com.example.model;

import java.util.HashSet;
import java.util.Set;

public class GameState {
    // Identifiant du chapitre où se trouve actuellement le joueur
    private int chapitreActuel;
    // Référence au personnage principal (son état, inventaire, etc.)
    private Personnage joueur;

    // Ensemble des chapitres déjà visités, pour ne pas réappliquer leurs effets
    private Set<Integer> chapitresVisites = new HashSet<>();

    // Constructeur vide, utilisé par la désérialisation ou pour initialiser un nouvel état
    public GameState() {}

    // Donne l’ID du chapitre en cours
    public int getChapitreActuel() {
        return chapitreActuel;
    }

    // Définit l’ID du chapitre actuel (déplacement dans l’histoire)
    public void setChapitreActuel(int id) {
        this.chapitreActuel = id;
    }

    // Récupère l’objet Personnage représentant le joueur
    public Personnage getJoueur() {
        return joueur;
    }

    // Associe un Personnage à cet état de partie
    public void setJoueur(Personnage j) {
        this.joueur = j;
    }

    // Renvoie la liste des chapitres déjà visités
    public Set<Integer> getChapitresVisites() {
        return chapitresVisites;
    }

    // Remplace l’ensemble des chapitres visités (utile pour recharger une partie)
    public void setChapitresVisites(Set<Integer> chapitresVisites) {
        this.chapitresVisites = chapitresVisites;
    }
}

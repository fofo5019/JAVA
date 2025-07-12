/*
╔══════════════════════════════════════════════════════╗
║  Projet Java – Le livre dont vous êtes le héros      ║
║  Le Pirate des 7 Mers                               ║
║                                                      ║
║  ESGI 2 – Franck Giordano & Louis Dalet – 2025      ║
╚══════════════════════════════════════════════════════╝
*/

package com.example.model;

public class Competence {
    // Nom lisible de la compétence que le héros peut posséder
    private String nom;

    // Constructeur vide nécessaire pour la désérialisation ou certains frameworks
    public Competence() {}

    // Permet d'initialiser une compétence avec un nom spécifique
    public Competence(String nom) {
        this.nom = nom;
    }

    // Récupère le nom de la compétence
    public String getNom() {
        return nom;
    }

    // Définit ou modifie le nom de la compétence
    public void setNom(String nom) {
        this.nom = nom;
    }

    // Retourne le nom de la compétence, pratique pour l'affichage direct
    @Override
    public String toString() {
        return nom;
    }
}

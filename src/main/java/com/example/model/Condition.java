/*
╔══════════════════════════════════════════════════════╗
║  Projet Java – Le livre dont vous êtes le héros      ║
║  Le Pirate des 7 Mers                               ║
║                                                      ║
║  ESGI 2 – Franck Giordano & Louis Dalet – 2025      ║
╚══════════════════════════════════════════════════════╝
*/

package com.example.model;

public class Condition {
    // Type de condition à vérifier (par ex. compétence, objet, mot de passe)
    private String type;
    // Valeur précise requise pour cette condition (nom de la compétence, de l'objet, du mot de passe…)
    private String valeur;

    // Constructeur vide utile pour la désérialisation JSON ou certains frameworks
    public Condition() {}

    // Récupère le type de condition (COMPETENCE, OBJET, MOTDEPASSE…)
    public String getType() {
        return type;
    }

    // Définit le type de condition à appliquer
    public void setType(String type) {
        this.type = type;
    }

    // Renvoie la valeur attendue pour satisfaire la condition
    public String getValeur() {
        return valeur;
    }

    // Assigne la valeur nécessaire pour cette condition
    public void setValeur(String valeur) {
        this.valeur = valeur;
    }
}

/*
╔══════════════════════════════════════════════════════╗
║  Projet Java – Le livre dont vous êtes le héros      ║
║  Le Pirate des 7 Mers                               ║
║                                                      ║
║  ESGI 2 – Franck Giordano & Louis Dalet – 2025      ║
╚══════════════════════════════════════════════════════╝
*/

package com.example.model;

import java.util.Objects;

public class Objet {
    // Nom de l’objet tel qu’il apparaîtra dans l’inventaire
    private String nom;
    // Catégorie ou type de l’objet (ex. arme, clé, artefact…)
    private String type;

    // Constructeur vide pour la désérialisation ou l’instanciation sans détails
    public Objet() {}

    // Initialise un objet avec son nom et son type pour l’inventaire du joueur
    public Objet(String nom, String type) {
        this.nom = nom;
        this.type = type;
    }

    // Retourne le nom de l’objet, pratique pour l’affichage
    public String getNom() {
        return nom;
    }

    // Modifie le nom de l’objet, utile si vous souhaitez renommer ou personnaliser
    public void setNom(String nom) {
        this.nom = nom;
    }

    // Renvoie la catégorie de l’objet pour savoir comment l’utiliser ou le décrypter
    public String getType() {
        return type;
    }

    // Définit ou change la catégorie de l’objet
    public void setType(String type) {
        this.type = type;
    }

    // Pour afficher simplement l’objet partout où un String est attendu
    @Override
    public String toString() {
        return nom;
    }

    // Deux objets sont considérés identiques si leurs noms correspondent
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Objet objet = (Objet) o;
        return Objects.equals(nom, objet.nom);
    }

    // Hash code basé sur le nom, pour usage dans les collections (Set, Map…)
    @Override
    public int hashCode() {
        return Objects.hash(nom);
    }
}

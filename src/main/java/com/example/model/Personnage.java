/*
╔══════════════════════════════════════════════════════╗
║  Projet Java – Le livre dont vous êtes le héros      ║
║  Le Pirate des 7 Mers                               ║
║                                                      ║
║  ESGI 2 – Franck Giordano & Louis Dalet – 2025      ║
╚══════════════════════════════════════════════════════╝
*/

package com.example.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Personnage {
    // Nom du héros choisi par le joueur
    private String nom;
    // Points de vie actuels du personnage
    private int pointsDeVie;
    // Points de vie maximum que le personnage peut avoir
    private int pointsDeVieMax;
    // Quantité d'or (doublons) possédée
    private int doublonsOr;

    // Liste des objets que le joueur transporte
    private List<Objet> inventaire;
    // Ensemble des compétences apprises par le personnage
    private Set<String> competences;
    // Ensemble des mots de passe ou indices récupérés
    private Set<String> motsDePasse;

    // Initialisation par défaut : inventaire et ensembles prêts à l'emploi
    public Personnage() {
        this.inventaire = new ArrayList<>();
        this.competences = new HashSet<>();
        this.motsDePasse = new HashSet<>();
    }

    // Création d'un nouveau personnage avec un nom, PV et or de départ
    public Personnage(String nom) {
        this();
        this.nom = nom;
        this.pointsDeVie = 10;
        this.pointsDeVieMax = 10;
        this.doublonsOr = 10;
    }

    // Donne le nom actuel du personnage
    public String getNom() {
        return nom;
    }

    // Change ou définit le nom du personnage
    public void setNom(String nom) {
        this.nom = nom;
    }

    // Récupère le nombre de points de vie restants
    public int getPointsDeVie() {
        return pointsDeVie;
    }

    // Définit directement les points de vie (attention aux bornes)
    public void setPointsDeVie(int pointsDeVie) {
        this.pointsDeVie = pointsDeVie;
    }

    // Récupère le maximum de points de vie possible
    public int getPointsDeVieMax() {
        return pointsDeVieMax;
    }

    // Modifie la capacité maximale de points de vie
    public void setPointsDeVieMax(int pointsDeVieMax) {
        this.pointsDeVieMax = pointsDeVieMax;
    }

    // Affiche la quantité d'or actuelle
    public int getDoublonsOr() {
        return doublonsOr;
    }

    // Ajuste la quantité d'or du personnage
    public void setDoublonsOr(int doublonsOr) {
        this.doublonsOr = doublonsOr;
    }

    // Renvoie la liste d'objets que le joueur porte
    public List<Objet> getInventaire() {
        return inventaire;
    }

    // Remplace l'inventaire par une nouvelle liste d'objets
    public void setInventaire(List<Objet> inventaire) {
        this.inventaire = inventaire;
    }

    // Renvoie l'ensemble des compétences acquises
    public Set<String> getCompetences() {
        return competences;
    }

    // Définit ou remplace les compétences du personnage
    public void setCompetences(Set<String> competences) {
        this.competences = competences;
    }

    // Renvoie tous les mots de passe ou indices collectés
    public Set<String> getMotsDePasse() {
        return motsDePasse;
    }

    // Met à jour l'ensemble des mots de passe collectés
    public void setMotsDePasse(Set<String> motsDePasse) {
        this.motsDePasse = motsDePasse;
    }

    // Vérifie si le personnage est encore en vie (au moins 1 PV)
    public boolean estVivant() {
        return this.pointsDeVie > 0;
    }

    // Ajuste les points de vie en tenant compte des minimums et maximums
    public void modifierPointsDeVie(int delta) {
        this.pointsDeVie += delta;
        if (this.pointsDeVie > this.pointsDeVieMax) {
            this.pointsDeVie = this.pointsDeVieMax;
        }
        if (this.pointsDeVie < 0) {
            this.pointsDeVie = 0;
        }
    }

    // Tente d'ajouter un objet à l'inventaire si la capacité n'est pas dépassée
    public void ajouterObjet(Objet objet) {
        if (inventaire.size() < 8) {
            inventaire.add(objet);
        }
    }

    // Vérifie si le personnage possède une compétence (insensible à la casse)
    public boolean hasCompetence(String nom) {
        if (nom == null || competences == null) {
            return false;
        }
        return competences.stream()
                .anyMatch(c -> c.equalsIgnoreCase(nom));
    }

    // Vérifie si un objet, par son nom, est dans l'inventaire (insensible à la casse)
    public boolean hasObjet(String nom) {
        if (nom == null || inventaire == null) {
            return false;
        }
        return inventaire.stream()
                .anyMatch(o -> o.getNom().equalsIgnoreCase(nom));
    }
}

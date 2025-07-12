/*
╔══════════════════════════════════════════════════════╗
║  Projet Java – Le livre dont vous êtes le héros      ║
║  Le Pirate des 7 Mers                               ║
║                                                      ║
║  ESGI 2 – Franck Giordano & Louis Dalet – 2025      ║
╚══════════════════════════════════════════════════════╝
*/

package com.example.model;

import java.util.List;
import java.util.Map;

public class Chapitre {
    // Identifiant unique du chapitre pour naviguer dans l’histoire
    private int id;
    // Texte narratif affiché au joueur quand il entre dans ce chapitre
    private String texte;
    // Liste des choix possibles depuis ce chapitre
    private List<Choix> choix;

    // Effets appliqués automatiquement à l’entrée du chapitre (PV, or, objets…)
    private Map<String, Object> effets;

    // Conditions globales à remplir pour débloquer ce chapitre (par ex. mots de passe)
    private Map<String, List<String>> conditions;

    // Constructeur vide utile pour la désérialisation JSON
    public Chapitre() {}

    // Renvoie l’identifiant du chapitre
    public int getId() {
        return id;
    }

    // Définit l’identifiant du chapitre
    public void setId(int id) {
        this.id = id;
    }

    // Récupère le texte principal du chapitre
    public String getTexte() {
        return texte;
    }

    // Met à jour le texte narratif du chapitre
    public void setTexte(String texte) {
        this.texte = texte;
    }

    // Renvoie la liste des choix disponibles pour ce chapitre
    public List<Choix> getChoix() {
        return choix;
    }

    // Définit les options de choix pour ce chapitre
    public void setChoix(List<Choix> choix) {
        this.choix = choix;
    }

    // Récupère les effets à déclencher à l’entrée
    public Map<String, Object> getEffets() {
        return effets;
    }

    // Assigne les effets à appliquer à l’entrée du chapitre
    public void setEffets(Map<String, Object> effets) {
        this.effets = effets;
    }

    // Récupère les conditions requises pour accéder au chapitre
    public Map<String, List<String>> getConditions() {
        return conditions;
    }

    // Définit les conditions globales pour débloquer ce chapitre
    public void setConditions(Map<String, List<String>> conditions) {
        this.conditions = conditions;
    }
}

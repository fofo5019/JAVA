/*
╔══════════════════════════════════════════════════════╗
║  Projet Java – Le livre dont vous êtes le héros      ║
║  Le Pirate des 7 Mers                               ║
║                                                      ║
║  ESGI 2 – Franck Giordano & Louis Dalet – 2025      ║
╚══════════════════════════════════════════════════════╝
*/

package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class Choix {
    // Texte affiché au joueur pour présenter ce choix
    private String texte;
    // Chapitre qui suit lorsque ce choix est sélectionné
    private int destination;

    // Liste des conditions nécessaires pour débloquer ce choix (compétences, objets…)
    private List<Condition> conditions = new ArrayList<>();

    // Effets appliqués au personnage quand on prend ce choix (PV, or, inventaire…)
    private Map<String, Object> effets;

    // Constructeur vide requis pour la désérialisation JSON
    public Choix() {}

    // Récupère le texte du choix
    public String getTexte() {
        return texte;
    }

    // Définit le texte à afficher pour ce choix
    public void setTexte(String texte) {
        this.texte = texte;
    }

    // Récupère l’ID du chapitre de destination
    public int getDestination() {
        return destination;
    }

    // Associe l’ID du chapitre cible ; lu depuis le champ "vers" dans le JSON
    @JsonProperty("vers")
    public void setDestination(int destination) {
        this.destination = destination;
    }

    // Renvoie la liste des conditions à satisfaire pour que ce choix soit actif
    public List<Condition> getConditions() {
        return conditions;
    }

    // Met à jour les conditions de disponibilité du choix
    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    // Récupère la map des effets à appliquer quand on choisit cette option
    public Map<String, Object> getEffets() {
        return effets;
    }

    // Définit les effets (gain/perte de PV, or, objets…) de ce choix
    public void setEffets(Map<String, Object> effets) {
        this.effets = effets;
    }
}

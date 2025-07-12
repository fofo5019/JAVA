/*
╔══════════════════════════════════════════════════════╗
║  Projet Java – Le livre dont vous êtes le héros      ║
║  Le Pirate des 7 Mers                               ║
║                                                      ║
║  ESGI 2 – Franck Giordano & Louis Dalet – 2025      ║
╚══════════════════════════════════════════════════════╝
*/

package com.example.model;

import java.util.Map;

public class Action {
    // Le type d’action à réaliser (attaque, défense, dialogue, etc.)
    private ActionType type;
    // Paramètres associés à l’action (dégâts, cible, message, etc.)
    private Map<String, Object> params;

    // Constructeur vide, utile pour la désérialisation ou les frameworks
    public Action() {}

    // Récupère le type d’action défini
    public ActionType getType() {
        return type;
    }

    // Définit le type d’action à exécuter
    public void setType(ActionType type) {
        this.type = type;
    }

    // Récupère la collection de paramètres pour cette action
    public Map<String, Object> getParams() {
        return params;
    }

    // Affecte les paramètres nécessaires à l’action
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}

/*
╔══════════════════════════════════════════════════════╗
║  Projet Java – Le livre dont vous êtes le héros      ║
║  Le Pirate des 7 Mers                               ║
║                                                      ║
║  ESGI 2 – Franck Giordano & Louis Dalet – 2025      ║
╚══════════════════════════════════════════════════════╝
*/

package com.example.model;

// Types d’actions possibles dans le jeu, pour gérer gains, pertes et objets
public enum ActionType {
    GAIN_PV,           // Ajouter des points de vie au personnage
    PERTE_PV,          // Retirer des points de vie (dommages)
    AJOUT_OBJET,       // Ajouter un objet à l’inventaire
    RETRAIT_OBJET,     // Enlever un objet de l’inventaire
    AJOUT_MOTDEPASSE,  // Ajouter un mot de passe ou indice secret
    GAIN_ARGENT,       // Augmenter le solde d’or du personnage
    PERTE_ARGENT,      // Diminuer le solde d’or (dépenses, vol…)
    AUTRE              // Tout autre type d’action non standard
}

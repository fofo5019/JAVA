/*
╔══════════════════════════════════════════════════════╗
║  Projet Java – Le livre dont vous êtes le héros      ║
║  Le Pirate des 7 Mers                               ║
║                                                      ║
║  ESGI 2 – Franck Giordano & Louis Dalet – 2025      ║
╚══════════════════════════════════════════════════════╝
*/

package com.example.controller;

import com.example.model.*;
import com.example.util.SaveManager;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameController {

    private final Scenario scenario;
    private GameState gameState;

    public GameController(Scenario scenario, Personnage joueur) {
        // Initialise une nouvelle partie avec le scénario et le personnage fournis
        this.scenario = scenario;
        this.gameState = new GameState();
        this.gameState.setJoueur(joueur);
        this.reset();
    }

    public GameController(Scenario scenario, GameState gameState) {
        // Reprend une partie existante à partir de l’état de jeu fourni
        this.scenario = scenario;
        this.gameState = gameState;
    }

    public void reset() {
        // Réinitialise le jeu au premier chapitre et vide l’historique des visites
        if (this.gameState != null && this.scenario != null) {
            gameState.setChapitreActuel(scenario.getChapitreInitial().getId());
            gameState.getChapitresVisites().clear();
            appliquerEffets(getChapitreCourant());
        }
    }

    public Chapitre getChapitreCourant() {
        // Renvoie le chapitre où se trouve actuellement le joueur
        return scenario.getChapitre(gameState.getChapitreActuel());
    }

    public Personnage getJoueur() {
        // Donne accès au personnage principal de la partie
        return gameState.getJoueur();
    }

    public boolean isGameOver() {
        // Indique si le joueur est décédé (PV à zéro)
        return !getJoueur().estVivant();
    }

    public List<Choix> getChoixDisponibles() {
        // Liste les choix possibles, en filtrant ceux inaccessibles pour le joueur
        if (isGameOver()) {
            return Collections.emptyList();
        }

        Chapitre chapitre = getChapitreCourant();
        if (chapitre == null || chapitre.getChoix() == null) {
            return Collections.emptyList();
        }

        return chapitre.getChoix().stream()
                .filter(this::checkConditions)
                .collect(Collectors.toList());
    }

    public void choisir(Choix choix) {
        // Applique les effets du choix et passe au chapitre désigné
        appliquerEffets(choix);

        if (isGameOver()) {
            return;
        }

        gameState.setChapitreActuel(choix.getDestination());
        appliquerEffets(getChapitreCourant());
    }

    private boolean checkConditions(Choix choix) {
        // Vérifie que le joueur remplit toutes les conditions requises pour un choix
        List<Condition> conditions = choix.getConditions();
        if (conditions != null) {
            for (Condition condition : conditions) {
                if ("competences_requises".equals(condition.getType())) {
                    String comp = condition.getValeur();
                    if (!getJoueur().hasCompetence(comp)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private void appliquerEffets(Choix choix) {
        // Met à jour les points de vie, l’or, l’inventaire et les mots de passe selon les effets du choix
        if (choix == null || choix.getEffets() == null) {
            return;
        }

        var effets = choix.getEffets();
        Personnage joueur = getJoueur();

        if (effets.containsKey("points_de_vie")) {
            joueur.modifierPointsDeVie((Integer) effets.get("points_de_vie"));
        }
        if (effets.containsKey("or")) {
            joueur.setDoublonsOr(joueur.getDoublonsOr() + (Integer) effets.get("or"));
        }
        if (effets.containsKey("inventaire")) {
            ((List<String>) effets.get("inventaire"))
                    .forEach(item -> joueur.ajouterObjet(new Objet(item, "Inconnu")));
        }
        if (effets.containsKey("mots_de_passe")) {
            joueur.getMotsDePasse().addAll((List<String>) effets.get("mots_de_passe"));
        }
    }

    @SuppressWarnings("unchecked")
    private void appliquerEffets(Chapitre chapitre) {
        // Applique une seule fois les effets définis à l’entrée d’un chapitre
        if (chapitre == null || gameState.getChapitresVisites().contains(chapitre.getId())) {
            return;
        }

        gameState.getChapitresVisites().add(chapitre.getId());

        if (chapitre.getEffets() != null) {
            var effets = chapitre.getEffets();
            Personnage joueur = getJoueur();

            if (effets.containsKey("points_de_vie")) {
                joueur.modifierPointsDeVie((Integer) effets.get("points_de_vie"));
            }
            if (effets.containsKey("or")) {
                joueur.setDoublonsOr(joueur.getDoublonsOr() + (Integer) effets.get("or"));
            }
            if (effets.containsKey("inventaire")) {
                ((List<String>) effets.get("inventaire"))
                        .forEach(item -> joueur.ajouterObjet(new Objet(item, "Inconnu")));
            }
            if (effets.containsKey("mots_de_passe")) {
                joueur.getMotsDePasse().addAll((List<String>) effets.get("mots_de_passe"));
            }
        }
    }

    private String getNomFichierSauvegarde() {
        // Crée un nom de fichier valide pour la sauvegarde basé sur le nom du héros
        String nomHeros = getJoueur().getNom();
        String nomFichierValide = nomHeros.trim().replaceAll("[^a-zA-Z0-9.-]", "_");
        return "sauvegarde_" + nomFichierValide + ".json";
    }

    public void sauvegarderPartie() {
        // Enregistre l’état actuel du jeu dans un fichier JSON
        try {
            SaveManager.save(gameState, Paths.get(getNomFichierSauvegarde()));
            System.out.println("Partie sauvegardée !");
        } catch (Exception e) {
            System.err.println("Erreur de sauvegarde : " + e.getMessage());
        }
    }
}

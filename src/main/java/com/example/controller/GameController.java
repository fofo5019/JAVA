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
        this.scenario = scenario;
        this.gameState = new GameState();
        this.gameState.setJoueur(joueur);
        this.reset();
    }

    public GameController(Scenario scenario, GameState gameState) {
        this.scenario = scenario;
        this.gameState = gameState;
    }

    public void reset() {
        if (this.gameState != null && this.scenario != null) {
            this.gameState.setChapitreActuel(scenario.getChapitreInitial().getId());
            this.gameState.getChapitresVisites().clear();
            appliquerEffets(getChapitreCourant());
        }
    }

    public Chapitre getChapitreCourant() {
        return scenario.getChapitre(gameState.getChapitreActuel());
    }

    public Personnage getJoueur() {
        return gameState.getJoueur();
    }

    public boolean isGameOver() {
        return !getJoueur().estVivant();
    }

    public List<Choix> getChoixDisponibles() {
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
        appliquerEffets(choix); // MODIFICATION : Appliquer les effets du choix
        if (isGameOver()) {
            return; // Arrêter si le joueur meurt suite au choix
        }
        gameState.setChapitreActuel(choix.getDestination());
        appliquerEffets(getChapitreCourant());
    }

    private boolean checkConditions(Choix choix) {
        List<Condition> conditions = choix.getConditions();
        if (conditions != null) {
            for (Condition condition : conditions) {
                if ("competences_requises".equals(condition.getType())) {
                    String comp = condition.getValeur();
                    if (!getJoueur().hasCompetence(comp)) {
                        return false;
                    }
                }
                // Vous pouvez ajouter d'autres types de conditions ici
            }
        }
        return true;
    }

    // MODIFICATION : Nouvelle méthode pour appliquer les effets d'un choix
    @SuppressWarnings("unchecked")
    private void appliquerEffets(Choix choix) {
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
            ((List<String>) effets.get("inventaire")).forEach(item -> joueur.ajouterObjet(new Objet(item, "Inconnu")));
        }
        if (effets.containsKey("mots_de_passe")) {
            joueur.getMotsDePasse().addAll((List<String>) effets.get("mots_de_passe"));
        }
    }

    @SuppressWarnings("unchecked")
    private void appliquerEffets(Chapitre chapitre) {
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
                ((List<String>) effets.get("inventaire")).forEach(item -> joueur.ajouterObjet(new Objet(item, "Inconnu")));
            }
            if (effets.containsKey("mots_de_passe")) {
                joueur.getMotsDePasse().addAll((List<String>) effets.get("mots_de_passe"));
            }
        }
    }

    private String getNomFichierSauvegarde() {
        String nomHeros = getJoueur().getNom();
        String nomFichierValide = nomHeros.trim().replaceAll("[^a-zA-Z0-9.-]", "_");
        return "sauvegarde_" + nomFichierValide + ".json";
    }

    public void sauvegarderPartie() {
        try {
            SaveManager.save(gameState, Paths.get(getNomFichierSauvegarde()));
            System.out.println("Partie sauvegardée !");
        } catch (Exception e) {
            System.err.println("Erreur de sauvegarde : " + e.getMessage());
        }
    }
}
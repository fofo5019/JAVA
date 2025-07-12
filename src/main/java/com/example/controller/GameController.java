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
        }
    }

    public Chapitre getChapitreCourant() {
        if (gameState == null) return null;
        return scenario.getChapitre(gameState.getChapitreActuel());
    }

    public Personnage getJoueur() {
        if (gameState == null) return null;
        return gameState.getJoueur();
    }

    public List<Choix> getChoixDisponibles() {
        Chapitre chapitreCourant = getChapitreCourant();
        if (chapitreCourant == null || chapitreCourant.getChoix() == null) {
            return Collections.emptyList();
        }
        return chapitreCourant.getChoix().stream()
                .filter(this::checkConditions)
                .collect(Collectors.toList());
    }

    public void choisir(Choix choix) {
        appliquerEffets(getChapitreCourant());
        gameState.setChapitreActuel(choix.getDestination());
    }

    private boolean checkConditions(Choix choix) {
        if (choix.getConditions() == null || choix.getConditions().isEmpty()) {
            return true;
        }
        Personnage joueur = getJoueur();
        for (Condition condition : choix.getConditions()) {
            String type = condition.getType().toUpperCase();
            String valeur = condition.getValeur();
            switch (type) {
                case "COMPETENCE":
                    if (!joueur.hasCompetence(valeur)) return false;
                    break;
                case "OBJET":
                    if (!joueur.hasObjet(valeur)) return false;
                    break;
                case "MOTDEPASSE":
                    if (!joueur.getMotsDePasse().contains(valeur)) return false;
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private void appliquerEffets(Chapitre chapitre) {
        if (chapitre.getEffets() == null || chapitre.getEffets().isEmpty()) return;

        Personnage joueur = getJoueur();
        Map<String, Object> effets = chapitre.getEffets();

        if (effets.containsKey("points_de_vie")) {
            joueur.modifierPointsDeVie((Integer) effets.get("points_de_vie"));
        }
        if (effets.containsKey("or")) {
            joueur.setDoublonsOr(joueur.getDoublonsOr() + (Integer) effets.get("or"));
        }
        if (effets.containsKey("inventaire")) {
            List<String> items = (List<String>) effets.get("inventaire");
            for (String item : items) {
                joueur.ajouterObjet(new Objet(item, "Inconnu"));
            }
        }
        if (effets.containsKey("mots_de_passe")) {
            List<String> passwords = (List<String>) effets.get("mots_de_passe");
            joueur.getMotsDePasse().addAll(passwords);
        }
    }


    private String getNomFichierSauvegarde() {
        String nomHeros = getJoueur().getNom();
        String nomFichierValide = nomHeros.trim().replaceAll("[^a-zA-Z0-9.-]", "_");
        return "sauvegarde_" + nomFichierValide + ".json";
    }

    public void sauvegarderPartie() {
        String nomFichier = getNomFichierSauvegarde();
        try {
            SaveManager.save(gameState, Paths.get(nomFichier));
            System.out.println("Partie sauvegardée avec succès dans " + nomFichier);
        } catch (Exception e) {
            System.err.println("Erreur lors de la sauvegarde de la partie : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
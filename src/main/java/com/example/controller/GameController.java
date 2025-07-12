package com.example.controller;

import com.example.model.*;
import com.example.util.SaveManager;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class GameController {

    private Scenario scenario;
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
        if (this.gameState != null) {
            this.gameState.setChapitreActuel(scenario.getChapitreInitial().getId());
        }
    }

    public Chapitre getChapitreCourant() {
        return scenario.getChapitre(gameState.getChapitreActuel());
    }

    public Personnage getJoueur() {
        return gameState.getJoueur();
    }

    public List<Choix> getChoixDisponibles() {
        Chapitre chapitreCourant = getChapitreCourant();
        if (chapitreCourant == null || chapitreCourant.getChoix() == null) {
            return List.of();
        }
        return chapitreCourant.getChoix().stream()
                .filter(this::checkConditions)
                .collect(Collectors.toList());
    }

    public void choisir(Choix choix) {
        gameState.setChapitreActuel(choix.getDestination());
    }
    
    private boolean checkConditions(Choix choix) {
        // La logique de vérification des conditions (compétences, objets) irait ici
        return true;
    }

    private String getNomFichierSauvegarde() {
        String nomHeros = getJoueur().getNom();
        String nomFichierValide = nomHeros.trim().replaceAll("[^a-zA-Z0-9.-]", "_");
        return "sauvegarde_" + nomFichierValide + ".json";
    }

    /**
     * Sauvegarde la partie en utilisant le nom du héros actuel.
     */
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
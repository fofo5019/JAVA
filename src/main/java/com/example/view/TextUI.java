package com.example.view;

import com.example.controller.GameController;
import com.example.model.*;
import com.example.util.SaveManager;

import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class TextUI {
    private GameController controller;
    private final Scanner scanner;
    private final Scenario scenario;

    public TextUI(Scanner scanner, Scenario scenario) {
        this.scanner = scanner;
        this.scenario = scenario;
    }

    public void start() {
        System.out.println("\n=== Le Pirate des Sept Mers ===");
        while (true) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Nouvelle Partie");
            System.out.println("2. Charger une Partie");
            System.out.println("3. Quitter");
            String choix = askForInput("Votre choix : ");
            switch (choix) {
                case "1" -> actionNouvellePartie();
                case "2" -> actionChargerPartie();
                case "3" -> { return; }
                default -> System.out.println("Choix invalide.");
            }
        }
    }
    
    private void actionNouvellePartie() {
        System.out.print("Entrez le nom de votre héros : ");
        String nomHeros = scanner.nextLine();
        if(nomHeros.trim().isEmpty()) nomHeros = "L'Aventurier Anonyme";

        Personnage joueur = new Personnage(nomHeros);
        joueur.getCompetences().add("Escrime");
        joueur.ajouterObjet(new Objet("Épée", "arme"));
        
        this.controller = new GameController(scenario, joueur);
        boucleDeJeu();
    }

    private void actionChargerPartie() {
        List<String> sauvegardes = SaveManager.listerSauvegardes();
        if (sauvegardes.isEmpty()) {
            System.out.println("-> Aucune sauvegarde trouvée.");
            return;
        }

        System.out.println("Sauvegardes disponibles :");
        for (int i = 0; i < sauvegardes.size(); i++) {
            System.out.printf("%d) %s\n", i + 1, sauvegardes.get(i));
        }
        
        int choix = askInt("Quelle partie charger ? ", 1, sauvegardes.size()) - 1;
        String nomFichier = sauvegardes.get(choix);
        
        try {
            GameState etatCharge = SaveManager.load(Paths.get(nomFichier));
            this.controller = new GameController(scenario, etatCharge);
            System.out.println("Partie chargée !");
            boucleDeJeu();
        } catch (Exception e) {
            System.out.println("-> Erreur lors du chargement de la sauvegarde.");
        }
    }

    private void boucleDeJeu() {
        while (true) {
            Chapitre c = controller.getChapitreCourant();
            if (c == null) {
                System.out.println("Erreur : Le chapitre actuel est introuvable.");
                break;
            }
            System.out.printf("\n--- Chapitre %d ---\n", c.getId());
            System.out.println(c.getTexte());

            List<Choix> choixDisponibles = controller.getChoixDisponibles();
            if (choixDisponibles.isEmpty()) {
                System.out.println("\n--- FIN DE L'AVENTURE ---");
                break;
            }

            System.out.println("\nVos choix :");
            for (int i = 0; i < choixDisponibles.size(); i++) {
                System.out.printf("%d) %s%n", i + 1, choixDisponibles.get(i).getTexte());
            }
            System.out.println("-> Entrez 'sauver' pour sauvegarder votre progression.");

            String input = askForInput("Votre choix : ");

            if ("sauver".equalsIgnoreCase(input.trim())) {
                controller.sauvegarderPartie();
            } else {
                try {
                    int sel = Integer.parseInt(input);
                    if (sel >= 1 && sel <= choixDisponibles.size()) {
                        controller.choisir(choixDisponibles.get(sel - 1));
                    } else {
                        System.out.println("-> Choix invalide.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("-> Entrée non reconnue.");
                }
            }
        }
    }
    
    private String askForInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
    
    private int askInt(String prompt, int min, int max) {
         while (true) {
            String input = askForInput(prompt);
            try {
                int v = Integer.parseInt(input.trim());
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException ignored) {}
            System.out.printf("Veuillez entrer un nombre entre %d et %d.%n", min, max);
        }
    }
}

/*
 ╔════════════════════════════════════════╗
 ║  Projet Java – Le livre dont vous      ║
 ║  êtes le héros : Le Pirate des 7 Mers  ║
 ║                                        ║
 ║  ESGI 2                                ║
 ║  Franck Giordano & Louis Dalet, 2025   ║
 ╚════════════════════════════════════════╝
 */

package com.example.app;

import com.example.model.Scenario;
import com.example.controller.ScenarioLoader;
import com.example.view.GameView;
import com.example.view.TextUI;

import javax.swing.SwingUtilities;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // On récupère tous les chapitres depuis notre fichier JSON d’aventure
        List<com.example.model.Chapitre> chapitres =
                ScenarioLoader.chargerScenario("Le_Pirate_des_Sept_Mers_SCENARIO.json");

        // On construit le scénario en partant du tout premier chapitre
        Scenario scenario = new Scenario(1, chapitres);

        // Scanner pour lire les choix de l’utilisateur
        Scanner scanner = new Scanner(System.in);

        // Petite introduction dans la console
        System.out.println("--- Le Pirate des Sept Mers ---");
        System.out.print("Voulez-vous lancer en mode (1) Console ou (2) Graphique ? ");
        String mode = scanner.nextLine();

        // Si l’utilisateur préfère l’interface graphique…
        if ("2".equals(mode)) {
            // …on crée la fenêtre Swing de jeu
            SwingUtilities.invokeLater(() -> new GameView(scenario));
        } else {
            // Sinon, on reste en mode texte pour vivre l’aventure dans la console
            TextUI textUI = new TextUI(scanner, scenario);
            textUI.start();
        }
    }
}

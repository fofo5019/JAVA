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

        // **CORRECTION :** On vérifie si un argument "gui" est passé au lancement.
        // C'est la méthode la plus fiable pour un lancement par script.
        if (args.length > 0 && "gui".equalsIgnoreCase(args[0])) {
            // Si oui, on lance directement l'interface graphique.
            SwingUtilities.invokeLater(() -> new GameView(scenario));
        } else {
            // Sinon, on garde le comportement normal avec le menu dans la console.
            Scanner scanner = new Scanner(System.in);
            System.out.println("--- Le Pirate des Sept Mers ---");
            System.out.print("Voulez-vous lancer en mode (1) Console ou (2) Graphique ? ");
            String mode = scanner.nextLine();

            if ("2".equals(mode.trim())) {
                // L'utilisateur a choisi le mode graphique.
                SwingUtilities.invokeLater(() -> new GameView(scenario));
            } else {
                // L'utilisateur a choisi le mode console.
                TextUI textUI = new TextUI(scanner, scenario);
                textUI.start();
            }
        }
    }
}
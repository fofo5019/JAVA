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
        List<com.example.model.Chapitre> chapitres = ScenarioLoader.chargerScenario("Le_Pirate_des_Sept_Mers_SCENARIO.json");
        Scenario scenario = new Scenario(1, chapitres);
        
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- Le Pirate des Sept Mers ---");
        System.out.print("Voulez-vous lancer en mode (1) Console ou (2) Graphique ? ");
        String mode = scanner.nextLine();

        if ("2".equals(mode)) {
             SwingUtilities.invokeLater(() -> new GameView(scenario));
        } else {
            TextUI textUI = new TextUI(scanner, scenario);
            textUI.start();
        }
    }
}

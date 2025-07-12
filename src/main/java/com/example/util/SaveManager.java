/*
╔══════════════════════════════════════════════════════╗
║  Projet Java – Le livre dont vous êtes le héros      ║
║  Le Pirate des 7 Mers                               ║
║                                                      ║
║  ESGI 2 – Franck Giordano & Louis Dalet – 2025      ║
╚══════════════════════════════════════════════════════╝
*/

package com.example.util;

import com.example.model.GameState;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SaveManager {

    // Objet Jackson configuré pour écrire et lire le JSON de façon claire
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Enregistre l'état du jeu dans un fichier JSON joliment formaté.
     * @param gameState l'état de la partie à sauvegarder
     * @param path le chemin du fichier où écrire la sauvegarde
     * @throws Exception en cas de problème d'écriture
     */
    public static void save(GameState gameState, Path path) throws Exception {
        objectMapper.writerWithDefaultPrettyPrinter()
                .writeValue(path.toFile(), gameState);
    }

    /**
     * Tente de charger un état de jeu depuis un fichier JSON.
     * @param path le chemin du fichier de sauvegarde
     * @return l'état de jeu chargé, ou null si aucun fichier n'existe
     * @throws Exception en cas de problème de lecture
     */
    public static GameState load(Path path) throws Exception {
        if (Files.exists(path)) {
            return objectMapper.readValue(path.toFile(), GameState.class);
        }
        return null;
    }

    /**
     * Recherche tous les fichiers de sauvegarde dans le dossier courant
     * (fichiers commençant par "sauvegarde_" et finissant par ".json").
     * @return une liste des noms de fichiers de sauvegarde trouvés
     */
    public static List<String> listerSauvegardes() {
        List<String> sauvegardes = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get("."))) {
            paths.map(Path::getFileName)
                    .map(Path::toString)
                    .filter(f -> f.startsWith("sauvegarde_") && f.endsWith(".json"))
                    .forEach(sauvegardes::add);
        } catch (Exception e) {
            System.err.println("Impossible de rechercher les sauvegardes : " + e.getMessage());
            e.printStackTrace();
        }
        return sauvegardes;
    }
}

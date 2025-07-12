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

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void save(GameState gameState, Path path) throws Exception {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), gameState);
    }

    public static GameState load(Path path) throws Exception {
        if (Files.exists(path)) {
            return objectMapper.readValue(path.toFile(), GameState.class);
        }
        return null;
    }

    /**
     * NOUVEAU : Scanne le répertoire courant pour trouver les fichiers de sauvegarde.
     * @return Une liste des noms de fichiers de sauvegarde (ex: "sauvegarde_MonHeros.json").
     */
    public static List<String> listerSauvegardes() {
        List<String> resultats = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get("."))) {
            paths
                .map(Path::getFileName)
                .map(Path::toString)
                .filter(f -> f.startsWith("sauvegarde_") && f.endsWith(".json"))
                .forEach(resultats::add);
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche des sauvegardes.");
            e.printStackTrace();
        }
        return resultats;
    }
}

/*
╔══════════════════════════════════════════════════════╗
║  Projet Java – Le livre dont vous êtes le héros      ║
║  Le Pirate des 7 Mers                               ║
║                                                      ║
║  ESGI 2 – Franck Giordano & Louis Dalet – 2025      ║
╚══════════════════════════════════════════════════════╝
*/

package com.example.controller;

import com.example.model.Chapitre;
import com.example.model.ScenarioDto;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;

public class ScenarioLoader {

    public static List<Chapitre> chargerScenario(String cheminFichier) throws Exception {
        // On prépare l’objet Jackson pour lire le JSON
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // On tente de récupérer le fichier JSON depuis le classpath
        try (InputStream is = ScenarioLoader.class
                .getClassLoader()
                .getResourceAsStream(cheminFichier)) {

            // Si le flux est null, c’est que le fichier est introuvable
            if (is == null) {
                throw new IllegalArgumentException("Fichier introuvable: " + cheminFichier);
            }

            // On désérialise le JSON dans un DTO intermédiaire
            ScenarioDto scenarioDto = mapper.readValue(is, ScenarioDto.class);

            // On renvoie juste la liste des chapitres pour construire le scénario
            return scenarioDto.getChapitres();
        }
    }

    public static Chapitre getChapitreParId(List<Chapitre> chapitres, int id) {
        // On parcourt les chapitres pour trouver celui dont l’ID correspond
        return chapitres.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }
}

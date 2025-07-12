package com.example.controller;

import com.example.model.Chapitre;
import com.example.model.ScenarioDto;
import com.fasterxml.jackson.databind.DeserializationFeature; // Importez cette classe
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;

public class ScenarioLoader {

    public static List<Chapitre> chargerScenario(String cheminFichier) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        // Ajoutez cette ligne pour ignorer les propriétés inconnues
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try (InputStream is = ScenarioLoader.class.getClassLoader().getResourceAsStream(cheminFichier)) {
            if (is == null) {
                throw new IllegalArgumentException("Fichier introuvable: " + cheminFichier);
            }
            // Lire le fichier dans notre classe DTO
            ScenarioDto scenarioDto = mapper.readValue(is, ScenarioDto.class);
            // Retourner uniquement la liste des chapitres
            return scenarioDto.getChapitres();
        }
    }

    public static Chapitre getChapitreParId(List<Chapitre> chapitres, int id) {
        return chapitres.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
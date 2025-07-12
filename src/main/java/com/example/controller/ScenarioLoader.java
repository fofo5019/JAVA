package com.example.controller;

import com.example.model.Chapitre;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;

public class ScenarioLoader {

    public static List<Chapitre> chargerScenario(String cheminFichier) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = ScenarioLoader.class.getClassLoader().getResourceAsStream(cheminFichier)) {
            if (is == null) throw new IllegalArgumentException("Fichier introuvable: " + cheminFichier);
            return mapper.readValue(is, new TypeReference<List<Chapitre>>() {});
        }
    }

    public static Chapitre getChapitreParId(List<Chapitre> chapitres, int id) {
        return chapitres.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }
}

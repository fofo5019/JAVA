package com.example.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ScenarioDto {
    private String titre;
    private List<Chapitre> chapitres;
    // Vous pouvez ajouter les classes pour "regles" et "heros_predefinis" si vous en avez besoin

    // Getters et Setters
    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public List<Chapitre> getChapitres() {
        return chapitres;
    }

    public void setChapitres(List<Chapitre> chapitres) {
        this.chapitres = chapitres;
    }
}
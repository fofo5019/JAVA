/*
╔══════════════════════════════════════════════════════╗
║  Projet Java – Le livre dont vous êtes le héros      ║
║  Le Pirate des 7 Mers                               ║
║                                                      ║
║  ESGI 2 – Franck Giordano & Louis Dalet – 2025      ║
╚══════════════════════════════════════════════════════╝
*/

package com.example.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ScenarioDto {
    // Titre du scénario tel qu’il figure dans le fichier JSON
    private String titre;
    // Liste des chapitres décrits dans le scénario
    private List<Chapitre> chapitres;

    // Renvoie le titre du scénario, utile pour l’affichage ou la validation
    public String getTitre() {
        return titre;
    }

    // Définit le titre du scénario, lu depuis le JSON
    public void setTitre(String titre) {
        this.titre = titre;
    }

    // Récupère la liste des chapitres du scénario
    public List<Chapitre> getChapitres() {
        return chapitres;
    }

    // Assigne la liste des chapitres après désérialisation
    @JsonProperty("chapitres")
    public void setChapitres(List<Chapitre> chapitres) {
        this.chapitres = chapitres;
    }
}

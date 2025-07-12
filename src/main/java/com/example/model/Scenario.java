/****************************************************************************
 * Projet Java, le livre dont vous êtes le héros, le pirate des 7 mers
 * ESGI 2 — Franck Giordano et Louis Dalet — 2025
 ****************************************************************************/

package com.example.model;

import java.util.*;

public class Scenario {
    private Map<Integer, Chapitre> chapitres = new HashMap<>();
    private int initial;

    public Scenario(int initial, List<Chapitre> liste) {
        this.initial = initial;

        // On transforme la liste de chapitres en map pour un accès rapide par ID
        for (Chapitre c : liste) {
            chapitres.put(c.getId(), c);
        }
    }

    // Renvoie le chapitre de départ de l'aventure
    public Chapitre getChapitreInitial() {
        return chapitres.get(initial);
    }

    // Permet de récupérer n'importe quel chapitre via son identifiant
    public Chapitre getChapitre(int id) {
        return chapitres.get(id);
    }
}

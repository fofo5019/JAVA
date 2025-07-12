package com.example.model;
import java.util.*;
public class Scenario {
    private Map<Integer, Chapitre> chapitres = new HashMap<>();
    private int initial;
    public Scenario(int initial, List<Chapitre> liste) {
        this.initial = initial;
        for (Chapitre c : liste) chapitres.put(c.getId(), c);
    }
    public Chapitre getChapitreInitial() { return chapitres.get(initial); }
    public Chapitre getChapitre(int id) { return chapitres.get(id); }
}
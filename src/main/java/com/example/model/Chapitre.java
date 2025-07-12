package com.example.model;

import java.util.List;
import java.util.ArrayList;

public class Chapitre {
    private int id;
    private String texte;
    private List<Choix> choix;
    // CORRECTION : La liste est initialisée ici pour éviter les NullPointerException.
    private List<Action> actions = new ArrayList<>();

    public Chapitre() {}

    // Getters et Setters...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTexte() { return texte; }
    public void setTexte(String texte) { this.texte = texte; }
    public List<Choix> getChoix() { return choix; }
    public void setChoix(List<Choix> choix) { this.choix = choix; }
    public List<Action> getActions() { return actions; }
    public void setActions(List<Action> actions) { this.actions = actions; }
}
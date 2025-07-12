package com.example.model;

import java.util.List;
import java.util.ArrayList;

public class Choix {
    private String texte;
    private int destination;
    // CORRECTION : La liste est initialisée ici pour éviter les NullPointerException.
    private List<Condition> conditions = new ArrayList<>();

    public Choix() {}

    // Getters et Setters...
    public String getTexte() { return texte; }
    public void setTexte(String texte) { this.texte = texte; }
    public int getDestination() { return destination; }
    public void setDestination(int destination) { this.destination = destination; }
    public List<Condition> getConditions() { return conditions; }
    public void setConditions(List<Condition> conditions) { this.conditions = conditions; }
}
package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.ArrayList;

public class Choix {
    private String texte;
    private int destination;
    private List<Condition> conditions = new ArrayList<>();

    public Choix() {}

    // Getters et Setters...
    public String getTexte() { return texte; }
    public void setTexte(String texte) { this.texte = texte; }

    public int getDestination() { return destination; }

    // On indique que la propriété JSON "vers" correspond au champ "destination"
    @JsonProperty("vers")
    public void setDestination(int destination) { this.destination = destination; }

    public List<Condition> getConditions() { return conditions; }
    public void setConditions(List<Condition> conditions) { this.conditions = conditions; }
}
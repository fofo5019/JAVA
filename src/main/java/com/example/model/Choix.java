package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class Choix {
    private String texte;
    private int destination;
    private List<Condition> conditions = new ArrayList<>();
    private Map<String, Object> effets; // Champ ajouté

    public Choix() {}

    // Getters et Setters...
    public String getTexte() { return texte; }
    public void setTexte(String texte) { this.texte = texte; }

    public int getDestination() { return destination; }

    @JsonProperty("vers")
    public void setDestination(int destination) { this.destination = destination; }

    public List<Condition> getConditions() { return conditions; }
    public void setConditions(List<Condition> conditions) { this.conditions = conditions; }

    // Getter et Setter pour les effets
    public Map<String, Object> getEffets() { return effets; }
    public void setEffets(Map<String, Object> effets) { this.effets = effets; }
}
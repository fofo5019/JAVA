package com.example.model;

import java.util.List;
import java.util.Map;

public class Chapitre {
    private int id;
    private String texte;
    private List<Choix> choix;
    // On change la List<Action> en Map<String, Object> pour correspondre au JSON
    private Map<String, Object> effets;
    private Map<String, List<String>> conditions;


    public Chapitre() {}

    // Getters et Setters...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTexte() { return texte; }
    public void setTexte(String texte) { this.texte = texte; }

    public List<Choix> getChoix() { return choix; }
    public void setChoix(List<Choix> choix) { this.choix = choix; }

    public Map<String, Object> getEffets() { return effets; }
    public void setEffets(Map<String, Object> effets) { this.effets = effets; }

    public Map<String, List<String>> getConditions() { return conditions; }
    public void setConditions(Map<String, List<String>> conditions) { this.conditions = conditions; }
}
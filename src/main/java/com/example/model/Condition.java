package com.example.model;

public class Condition {
    private String type; // Ex: "COMPETENCE", "OBJET", "MOTDEPASSE"
    private String valeur;

    public Condition() {}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }
}
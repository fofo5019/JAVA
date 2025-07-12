package com.example.model;

import java.util.Objects;

public class Objet {
    private String nom;
    private String type; // Ex: "arme", "magique", "cle"

    public Objet() {}

    public Objet(String nom, String type) {
        this.nom = nom;
        this.type = type;
    }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    @Override
    public String toString() { return nom; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Objet objet = (Objet) o;
        return Objects.equals(nom, objet.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom);
    }
}
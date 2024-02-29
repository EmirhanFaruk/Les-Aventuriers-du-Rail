package com.model;


import com.model.config.Ville;
import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteWagon.Couleur;

public class Route {

    private Ville ville1;
    private Ville ville2;
    private int longueur;
    private Couleur couleur; //couleur de la route
    private Joueur proprietaire; //joueur qui a construit la route
    private CarteDestination carte;

    public Route(Ville ville1, Ville ville2, int longueur, Couleur couleur,CarteDestination carte) {
        this.ville1 = ville1;
        this.ville2 = ville2;
        this.longueur = longueur;
        this.couleur = couleur;
        this.carte = carte;
        proprietaire = null;
    }

    public Ville getVille1() {
        return ville1;
    }

    public Ville getVille2() {
        return ville2;
    }

    public int getLongueur() {
        return longueur;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public Joueur getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(Joueur proprietaire) {
        this.proprietaire = proprietaire;
    }

}

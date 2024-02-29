package com.model.config;

import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteWagon;
import com.model.Player;

public class Route {

    private Ville ville1;
    private Ville ville2;
    private int longueur;
    private CarteWagon.Couleur couleur; //couleur de la route
    private Player proprietaire; //joueur qui a construit la route
    private CarteDestination carte;

    public Route(Ville ville1, Ville ville2, int longueur, CarteWagon.Couleur couleur,CarteDestination carte) {
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

    public CarteWagon.Couleur getCouleur() {
        return couleur;
    }

    public Player getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(Player proprietaire) {
        this.proprietaire = proprietaire;
    }

}

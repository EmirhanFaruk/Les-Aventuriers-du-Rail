package com.model;


import java.util.ArrayList;
import com.model.config.Rail;
import com.model.config.Ville;
import com.model.config.carte.CarteWagon.Couleur;

public class Route {

    private Ville ville1;
    private Ville ville2;
    private int longueur;
    private Couleur couleur; //couleur de la route
    private Player proprietaire; //joueur qui a construit la route
    private ArrayList<Rail> routeDeRail = new ArrayList<>();

    public Route(Ville ville1, Ville ville2, int longueur, Couleur couleur) {
        this.ville1 = ville1;
        this.ville2 = ville2;
        this.longueur = longueur;
        this.couleur = couleur;
        proprietaire = null;
    }
    
    public Route(Ville ville1, Ville ville2, int longueur, Couleur couleur, Player p) {
        this.ville1 = ville1;
        this.ville2 = ville2;
        this.longueur = longueur;
        this.couleur = couleur;
        this.proprietaire = p;
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

    public Player getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(Player proprietaire) {
        this.proprietaire = proprietaire;
    }

	public ArrayList<Rail> getRouteDeRail() {
		return routeDeRail;
	}
}

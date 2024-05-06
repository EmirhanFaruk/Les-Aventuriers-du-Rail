package com.model.config;

import java.util.ArrayList;
import com.model.Player;
import com.model.config.carte.CarteWagon.Couleur;
import com.model.config.Rail.Content;

public class Route {
    private Ville ville1;
    private Ville ville2;
    private int longueur; //la longueur des rails
    private Rail.Content couleur; //couleur de la route
    private Player proprietaire; //joueur qui a construit la route
    private Route cousin; // null si cette route n'est pas un de double route, l'autre route sinon
    private int nombrePoint; //nombre de point que raporte la route
    private ArrayList<Rail> railsRoute; //rails qui forme la route


    public Route(Ville v1, Ville v2, int nombrePointDistance) {
		this.ville1 = v1;
		this.ville2 = v2;
		this.nombrePoint = nombrePointDistance;
	}

    public Route(Ville ville1, Ville ville2, int longueur, Rail.Content couleur)
    {
        this.ville1 = ville1;
        this.ville2 = ville2;
        this.longueur = longueur;
        this.couleur = couleur;
        this.proprietaire = null;
        this.railsRoute = new ArrayList<>();
        nombrePointsDestination(); //initialise le nombre de point que donne cette route
    }

    public Couleur traducteurCouleur(){
        //Pour Carte : BLEU, VIOLET, MARRON, NOIRE, VERT, JAUNE, BLANC, ROUGE, LOC
        if(this.getCouleur() == Content.BLEU)return Couleur.BLEU;
        if(this.getCouleur() == Content.VIOLET)return Couleur.VIOLET;
        if(this.getCouleur() == Content.MARRON)return Couleur.MARRON;
        if(this.getCouleur() == Content.NOIRE)return Couleur.NOIRE;
        if(this.getCouleur() == Content.VERT)return Couleur.VERT;
        if(this.getCouleur() == Content.JAUNE)return Couleur.JAUNE;
        if(this.getCouleur() == Content.BLANC)return Couleur.BLANC;
        if(this.getCouleur() == Content.ROUGE)return Couleur.ROUGE;
        if(this.getCouleur() == Content.JOKERETOILEE)return Couleur.JOKERETOILEE;
        return Couleur.LOC;
    }

    public void nombrePointsDestination(){
        //Fonction qui dit le nombre de point pour la destination entre 2 villes

        switch (longueur){
            //1 wagon = 1 point
            case 1 :
                nombrePoint = 1;
                break;

            //2 wagon = 2 point
            case 2 :
                nombrePoint = 2;
                break;

            //3 wagon = 4 point
            case 3 :
                nombrePoint = 4;
                break;

            //4 wagon = 7 point
            case 4 :
                nombrePoint = 7;
                break;
            //5 wagon = 10 point
            case 5 :
                nombrePoint = 10;
                break;

            //6 wagon = 15 point
            case 6 :
                nombrePoint = 15;
                break;

            //7 wagon = 15 point (exeption pour la map longue)
            case 7 :
                nombrePoint = 20;
                break;

            default :
                nombrePoint = 0;
                break;
        }

    }

    public boolean links(Ville ville1, Ville ville2){
        boolean possibility1 = ville1 == this.getVille1() && ville2 == this.getVille2();
        boolean possibility2 = ville1 == this.getVille2() && ville2 == this.getVille1();

        return possibility1 || possibility2;
    }

    public String toString()
    {
        String res = "\n=================\n";
        res += "Route: \nVille1: " + ville1.getNom() + "\nVille2: " + ville2.getNom() + "\nLongueur: " + longueur + "\nCouleur: " + getCouleur();
        if (proprietaire != null)
        {
            res += "\nProp: " + proprietaire.getName();
        }
        else
        {
            res += "\nProp: null";
        }
        if (cousin != null)
        {
            res += "\nCousin: " + cousin.getCouleur();
        }
        else
        {
            res += "\nCousin: null";
        }
        return res;
    }


    /* getteurs et setteurs */

    public Ville getVille1() {
        return ville1;
    }

    public Ville getVille2() {
        return ville2;
    }

    public int getLongueur() {
        return longueur;
    }

    public Rail.Content getCouleur() {
        return couleur;
    }
    public Player getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(Player proprietaire) {
        this.proprietaire = proprietaire;
    }

    public int getNombrePoint() {
        return nombrePoint;
    }

    /**
     * Getter for cousin
     * @return cousin
     */
    public Route getCousin() { return cousin; }

    /**
     * Setter for cousin
     * @param cousin cousin to set
     */
    public void setCousin(Route cousin) { this.cousin = cousin; }

    public ArrayList<Rail> getRailsRoute() {
        return railsRoute;
    }
    
    public void enleverProprio() {
    	this.proprietaire = null;

    	for(int i = 0; i < this.railsRoute.size(); i++) {
    		this.railsRoute.get(i).setOccuperPar(null);
    		this.railsRoute.get(i).setOccuper(false);
    	}    	
    }

}

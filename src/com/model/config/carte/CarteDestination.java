package com.model.config.carte;

import com.model.config.Route;
import com.model.config.Ville;

public class CarteDestination {

    private Ville premiereVille;
    private Ville deuxiemeVille;
    private int nombrePoints;
    private boolean complete; //savoir si le joueur a complété ou non la mission


    //Pour les testes on va utiliser ce constructeur
    public CarteDestination(Route route){
        this.premiereVille = route.getVille1();
        this.deuxiemeVille = route.getVille2();
        this.nombrePoints = route.getLongueur();
        this.complete = false;
    }

    public CarteDestination(Ville v1,Ville v2,int nbpoint){
        this.premiereVille = v1;
        this.deuxiemeVille = v2;
        this.nombrePoints = nbpoint;
        this.complete = false;
    }

    /* getteurs et setteurs */
	public String getDescription() {
		return this.premiereVille.getNom() + " - "+ this.deuxiemeVille.getNom() 
		+ " | " + " nombre de points : " + this.nombrePoints;
	}

    public Ville getPremiereVille() {
        return premiereVille;
    }
    public Ville getDeuxiemeVille() {
        return deuxiemeVille;
    }
    public int getNombrePoints() {
        return nombrePoints;
    }
    public boolean getComplete(){
        return complete;
    }
    public void setComplete(){
        complete = true;
    }


}

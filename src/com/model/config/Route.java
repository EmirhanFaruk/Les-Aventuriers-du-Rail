package com.model.config;

import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteWagon;
import com.model.Player;

public class Route {

    private Ville ville1;
    private Ville ville2;
    private int longueur; //la longueur des rails
    private Rail.Content couleur; //couleur de la route
    private Player proprietaire; //joueur qui a construit la route
    private int nombrePoint; //nombre de point que raporte la route

    public Route(Ville ville1, Ville ville2, int longueur,Rail.Content couleur) {
        this.ville1 = ville1;
        this.ville2 = ville2;
        this.longueur = longueur;
        this.couleur = couleur;
        this.proprietaire = null;
        nombrePointsDestination(); //initialise le nombre de point que donne cette route
    }


    public Route(Ville ville, Ville ville1, int longueur) {
        this.ville1 = ville1;
        this.ville2 = ville2;
        this.longueur = longueur;
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

            //5 wagon = 15 point
            case 6 :
                nombrePoint = 15;
                break;

            default :
                nombrePoint = 0;
                break;


            }

    }


}

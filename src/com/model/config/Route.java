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
    private int nombrePoint; //nombre de point que raporte la route
    private ArrayList<Rail> railsRoute; //rails qui forme la route

    public Route(Ville ville1, Ville ville2, int longueur, Rail.Content couleur, Plateau p) {
        this.ville1 = ville1;
        this.ville2 = ville2;
        this.longueur = longueur;
        this.couleur = couleur;
        this.proprietaire = null;
        RouteFinder rf =  new  RouteFinder(p, ville1, ville2, couleur, this);
        this.railsRoute = rf.trouverChemin();
        nombrePointsDestination(); //initialise le nombre de point que donne cette route
    }

    public Route(Ville v1, Ville v2, int nombrePointDistance) {
		this.ville1 = v1;
		this.ville2 = v2;
		this.nombrePoint = nombrePointDistance;
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
        return Couleur.LOC;
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


	public ArrayList<Rail> getRailsRoute() {
		return railsRoute;
	}


	public void setRailsRoute(ArrayList<Rail> railsRoute) {
		this.railsRoute = railsRoute;
	}

	class RouteFinder {
	    private Plateau plateau;
	    private Ville villeDepart;
	    private Ville villeArrivee;
	    private Rail.Content couleurRoute;
	    private Route saRoute;

	    public RouteFinder(Plateau plateau, Ville villeDepart, Ville villeArrivee, Rail.Content couleurRoute, Route r) {
	        this.plateau = plateau;
	        this.villeDepart = villeDepart;
	        this.villeArrivee = villeArrivee;
	        this.couleurRoute = couleurRoute;
	        this.saRoute = r;
	    }

	    public ArrayList<Rail> trouverChemin() {
	        ArrayList<Rail> chemin = new ArrayList<>();

	        // Commence par le rail le plus proche de villeDepart et se déplace vers villeArrivee
	        int startX = this.villeDepart.getX();
	        int startY = this.villeDepart.getY();
	        int endX = this.villeArrivee.getX();
	        int endY = this.villeArrivee.getY();

	        int deltaX = Integer.compare(endX, startX); // Donne -1, 0 ou 1
	        int deltaY = Integer.compare(endY, startY); // Donne -1, 0 ou 1

	        int x = startX;
	        int y = startY;

	        while (x != endX || y != endY) {
	            if (this.plateau.positionValide(x, y) && this.plateau.getPlateau()[x][y] instanceof Rail) {
	                Rail rail = (Rail) this.plateau.getPlateau()[x][y];
	                if (rail.getInitialContent() == this.couleurRoute) {
	                    chemin.add(rail);
	                    System.out.println(this.saRoute); 
	                    ((Rail) this.plateau.getPlateau()[x][y]).setSaRoute(this.saRoute); // Associe chaque rail trouvé à la route
	                }
	            }

	            x += deltaX;
	            y += deltaY;
	        }
	        
	        //Debug : Etat de la liste
	        for(int i = 0; i < chemin.size(); i++) {
	        	Rail r = chemin.get(i);
	        	System.out.println(r + " " + r.getInitialContent());
	        }

	        return chemin;
	    }
	}
}

package com.model.config;

import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteWagon;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.model.Player;
import com.model.config.carte.CarteWagon.Couleur;
import com.model.config.Rail.Content;

@SuppressWarnings("unused")
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


    public Route(Ville ville1, Ville ville2, int longueur, Rail.Content couleur, Player proprio, ArrayList<Rail> r) {
        this.ville1 = ville1;
        this.ville2 = ville2;
        this.longueur = longueur;
        this.couleur = couleur;
        this.railsRoute = r;
        this.proprietaire = proprio;
        nombrePointsDestination();
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

	    private boolean estRailValideEtDeLaCouleur(Rail rail) {
	        return rail != null && rail.getInitialContent() == couleurRoute;
	    }

	    public ArrayList<Rail> trouverChemin() {
	        Queue<Rail> queue = new LinkedList<>();
	        ArrayList<Rail> chemin = new ArrayList<>();
	        boolean[][] visite = new boolean[plateau.getLongueur()][plateau.getLargeur()];

	        Rail railDepart = (Rail) plateau.getPlateau()[villeDepart.getY()][villeDepart.getX()];
	        if (!estRailValideEtDeLaCouleur(railDepart)) return chemin; // Vérifie le rail de départ

	        queue.offer(railDepart);
	        visite[villeDepart.getY()][villeDepart.getX()] = true;

	        while (!queue.isEmpty()) {
	            Rail railCourant = queue.poll();
	            int x = railCourant.getX();
	            int y = railCourant.getY();

	            if (x == villeArrivee.getX() && y == villeArrivee.getY()) {
	                chemin.add(railCourant);
	                return chemin; // Chemin trouvé
	            }

	            // Parcours les voisins
	            int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {0, -1}, {-1, 0}, {-1, -1}, {1, 1}, {1 ,-1}, {-1, 1}}; // Haut, Droite, Bas, Gauche
	            for (int[] direction : directions) {
	                int voisinX = x + direction[0];
	                int voisinY = y + direction[1];

	                if (plateau.positionValide(voisinX, voisinY) && !visite[voisinY][voisinX]) {
	                    Rail railVoisin = (Rail) plateau.getPlateau()[voisinY][voisinX];
	                    if (estRailValideEtDeLaCouleur(railVoisin)) {
	                        queue.offer(railVoisin);
	                        visite[voisinY][voisinX] = true;
	                        chemin.add(railVoisin); // Ajoute au chemin si valide
	                        railVoisin.setSaRoute(this.saRoute);
	                    }
	                }
	            }
	        }

	        return chemin; // Retourne le chemin, vide si aucun chemin trouvé
	    }
	}
}

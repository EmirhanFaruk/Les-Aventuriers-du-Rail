package com.model.config;

import java.util.ArrayList;
import com.model.Player;
import com.model.config.carte.CarteWagon.Couleur;

/**
 * La classe Route représente une route entre deux villes sur le plateau de jeu.
 * Elle contient des informations sur les villes qu'elle relie, sa longueur, sa couleur,
 * son propriétaire, son cousin (si elle fait partie d'une double route), et les rails qui la composent.
 */
public class Route {
    private Ville ville1;
    private Ville ville2;
    private int longueur; // La longueur des rails
    private Rail.Content couleur; // La couleur de la route
    private Player proprietaire; // Le joueur qui a construit la route
    private Route cousin; // null si cette route n'est pas une double route, l'autre route sinon
    private int nombrePoint; // Le nombre de points que rapporte la route
    private ArrayList<Rail> railsRoute; // Les rails qui forment la route

    /**
     * Constructeur de la classe Route.
     *
     * @param v1                La première ville
     * @param v2                La deuxième ville
     * @param nombrePointDistance Le nombre de points de distance
     */
    public Route(Ville v1, Ville v2, int nombrePointDistance) {
        this.ville1 = v1;
        this.ville2 = v2;
        this.nombrePoint = nombrePointDistance;
    }

    /**
     * Constructeur de la classe Route.
     *
     * @param ville1  La première ville
     * @param ville2  La deuxième ville
     * @param longueur La longueur de la route
     * @param couleur  La couleur de la route
     */
    public Route(Ville ville1, Ville ville2, int longueur, Rail.Content couleur) {
        this.ville1 = ville1;
        this.ville2 = ville2;
        this.longueur = longueur;
        this.couleur = couleur;
        this.proprietaire = null;
        this.railsRoute = new ArrayList<>();
        nombrePointsDestination(); // Initialise le nombre de points que donne cette route
    }

    /**
     * Traduit la couleur de la route en couleur de carte.
     *
     * @return La couleur de la carte correspondant à la couleur de la route
     */
    public Couleur traducteurCouleur() {
        switch (this.getCouleur()) {
            case BLEU:
                return Couleur.BLEU;
            case VIOLET:
                return Couleur.VIOLET;
            case MARRON:
                return Couleur.MARRON;
            case NOIRE:
                return Couleur.NOIRE;
            case VERT:
                return Couleur.VERT;
            case JAUNE:
                return Couleur.JAUNE;
            case BLANC:
                return Couleur.BLANC;
            case ROUGE:
                return Couleur.ROUGE;
            case JOKERETOILEE:
                return Couleur.JOKERETOILEE;
            default:
                return Couleur.LOC;
        }
    }

    /**
     * Initialise le nombre de points que rapporte la route en fonction de sa longueur.
     */
    public void nombrePointsDestination() {
        switch (longueur) {
            case 1:
                nombrePoint = 1;
                break;
            case 2:
                nombrePoint = 2;
                break;
            case 3:
                nombrePoint = 4;
                break;
            case 4:
                nombrePoint = 7;
                break;
            case 5:
                nombrePoint = 10;
                break;
            case 6:
                nombrePoint = 15;
                break;
            case 7:
                nombrePoint = 20;
                break;
            default:
                nombrePoint = 0;
                break;
        }
    }

    /**
     * Vérifie si cette route relie deux villes données.
     *
     * @param ville1 La première ville
     * @param ville2 La deuxième ville
     * @return true si la route relie les deux villes, sinon false
     */
    public boolean links(Ville ville1, Ville ville2) {
        boolean possibility1 = ville1 == this.getVille1() && ville2 == this.getVille2();
        boolean possibility2 = ville1 == this.getVille2() && ville2 == this.getVille1();

        return possibility1 || possibility2;
    }

    @Override
    public String toString() {
        String res = "\n=================\n";
        res += "Route: \nVille1: " + ville1.getNom() + "\nVille2: " + ville2.getNom() + "\nLongueur: " + longueur + "\nCouleur: " + getCouleur();
        res += "\nProp: " + (proprietaire != null ? proprietaire.getName() : "null");
        res += "\nCousin: " + (cousin != null ? cousin.getCouleur() : "null");
        return res;
    }

    /**
     * Réinitialise le propriétaire de la route et libère les rails associés.
     */
    public void resetProprietaire() {
        this.proprietaire = null;
        resetRails();
    }

    /**
     * Libère les rails associés à cette route.
     */
    private void resetRails() {
        for (Rail rail : railsRoute) {
            rail.setOccuperPar(null);
        }
    }

    /* Getters et Setters */

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

    public Route getCousin() {
        return cousin;
    }

    public void setCousin(Route cousin) {
        this.cousin = cousin;
    }

    public ArrayList<Rail> getRailsRoute() {
        return railsRoute;
    }

    /**
     * Retire le propriétaire de la route et libère les rails associés.
     */
    public void enleverProprio() {
        this.proprietaire = null;
        for (Rail rail : this.railsRoute) {
            rail.setOccuperPar2(null);
        }
    }
}

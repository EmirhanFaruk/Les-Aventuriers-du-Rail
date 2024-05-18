package com.model.config.carte;

import com.model.config.Route;
import com.model.config.Ville;

/**
 * La classe CarteDestination représente une carte de destination dans le jeu,
 * qui relie deux villes et attribue des points lorsqu'elle est complétée par un joueur.
 */
public class CarteDestination {

    private Ville premiereVille;
    private Ville deuxiemeVille;
    private int nombrePoints;
    private boolean complete; // Indique si le joueur a complété ou non la mission

    /**
     * Constructeur pour tester une carte de destination avec une route.
     *
     * @param route La route reliant les deux villes.
     */
    public CarteDestination(Route route) {
        this.premiereVille = route.getVille1();
        this.deuxiemeVille = route.getVille2();
        this.nombrePoints = route.getLongueur();
        this.complete = false;
    }

    /**
     * Constructeur d'une carte de destination reliant deux villes avec un certain nombre de points.
     *
     * @param v1 Première ville de la carte.
     * @param v2 Deuxième ville de la carte.
     * @param nbpoint Le nombre de points attribués pour relier les villes.
     */
    public CarteDestination(Ville v1, Ville v2, int nbpoint) {
        this.premiereVille = v1;
        this.deuxiemeVille = v2;
        this.nombrePoints = nbpoint;
        this.complete = false;
    }

    /**
     * Retourne la description de la carte de destination.
     *
     * @return Une chaîne de caractères décrivant la carte de destination.
     */
    public String getDescription() {
        return this.premiereVille.getNom() + " - " + this.deuxiemeVille.getNom() + " | " + " nombre de points : " + this.nombrePoints;
    }

    /**
     * Retourne la première ville de la carte de destination.
     *
     * @return La première ville de la carte.
     */
    public Ville getPremiereVille() {
        return premiereVille;
    }

    /**
     * Retourne la deuxième ville de la carte de destination.
     *
     * @return La deuxième ville de la carte.
     */
    public Ville getDeuxiemeVille() {
        return deuxiemeVille;
    }

    /**
     * Retourne le nombre de points de la carte de destination.
     *
     * @return Le nombre de points de la carte.
     */
    public int getNombrePoints() {
        return nombrePoints;
    }

    /**
     * Vérifie si la carte de destination est complète.
     *
     * @return true si la carte est complète, sinon false.
     */
    public boolean getComplete() {
        return complete;
    }

    /**
     * Marque la carte de destination comme complète.
     */
    public void setComplete() {
        complete = true;
    }
}

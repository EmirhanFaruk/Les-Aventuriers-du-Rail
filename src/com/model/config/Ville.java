package com.model.config;

import com.model.Player;
import java.util.ArrayList;

/**
 * La classe Ville représente une case de type ville
 * Elle hérite de la classe Case et ajoute des fonctionnalités spécifiques
 * aux villes telles que la possibilité de définir si une ville possède une gare.
 */
public class Ville extends Case {

    /** Le nom de la ville. */
    private String nom;

    /**
     * Un indicateur indiquant si la ville possède une gare.
     * Par défaut, la valeur est false.
     */
    private Player isOccuped = null;


    private ArrayList<Route> routes;



    /**
     * Constructeur de la classe Ville.
     * @param x La position horizontale de la ville sur le plateau de jeu.
     * @param y La position verticale de la ville sur le plateau de jeu.
     * @param nom Le nom de la ville.
     */
    public Ville(int x, int y, String nom) {
        super(x, y);
        this.nom = nom;
        routes = new ArrayList<>();
    }


    /**
     * Met isOccupied en null, aussi appelle resetRoutes(). Sera utilisé pour le mode nuke.
     */
    public void resetOccuped()
    {
        this.isOccuped = null;
        resetRoutes();
    }

    /**
     * Met proprietaire de ses routes en null. Sera utilisé pour le mode nuke.
     */
    private void resetRoutes()
    {
        for (Route route : routes)
        {
            route.resetProprietaire();
        }
    }


    /* getteurs et setteurs */

    /**
     * Indicateur de présence d'une gare pour cette ville.
     * @return affiche un joueur si elle est occupé par un joueur, sinon false;
     */
    public Player getIsOccuped() {
        return isOccuped;
    }

    /**
     * Définit si la ville possède une gare ou non.
     * @param isOccuped on set a un joueur la gare si elle est prise.
     */
    public void setIsOccuped(Player isOccuped) {
        this.isOccuped = isOccuped;
    }


    /**
     * Obtient le nom de la ville.
     * @return Le nom de la ville.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Obtient la liste des routes concerné par cette ville
     * @return la liste des routes concerné par cette ville
     */
    public ArrayList<Route> getRoutes()
    {
        return routes;
    }

	@Override
	public boolean estUneCaseGare() {
		return true;
	}
}

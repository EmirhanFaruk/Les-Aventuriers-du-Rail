package com.model.config;

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
    private boolean gare;

    /**
     * Constructeur de la classe Ville.
     * @param x La position horizontale de la ville sur le plateau de jeu.
     * @param y La position verticale de la ville sur le plateau de jeu.
     * @param nom Le nom de la ville.
     */
    public Ville(int x, int y, String nom) {
        super(x, y);
        this.nom = nom;
    }
    
    /**
     * Indicateur de présence d'une gare pour cette ville.
     * @return true si la ville possède une gare, sinon false.
     */
    public boolean getGare() {
        return this.gare;
    }
    
    /**
     * Définit si la ville possède une gare ou non.
     * @param estUneGare true si la ville possède une gare, sinon false.
     */
    public void setGare(boolean estUneGare) {
        this.gare = estUneGare;
    }

    /**
     * Obtient le nom de la ville.
     * @return Le nom de la ville.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom de la ville.
     * @param nom Le nom à attribuer à la ville.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public boolean estUneCaseGare() {
        return false;
    }
}

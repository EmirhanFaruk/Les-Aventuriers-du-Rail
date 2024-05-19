package com.model.config;

/**
 * La classe Paysage représente une case de type paysage sur le plateau de jeu.
 */
public class Paysage extends Case {

    /**
     * Constructeur de la classe Paysage.
     *
     * @param x Coordonnée x de la case
     * @param y Coordonnée y de la case
     */
    public Paysage(int x, int y) {
        super(x, y);
    }

    /**
     * Indique si la case est une gare.
     *
     * @return false car une case Paysage n'est pas une gare
     */
    @Override
    public boolean estUneCaseGare() {
        return false;
    }
}
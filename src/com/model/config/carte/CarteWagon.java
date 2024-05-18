package com.model.config.carte;

import javax.swing.*;

/**
 * La classe CarteWagon représente une carte de wagon dans le jeu.
 * Elle hérite de JPanel, permettant ainsi une représentation graphique de la carte.
 */
public class CarteWagon extends JPanel {
    /**
     * Énumération représentant les différentes couleurs de cartes de wagon disponibles dans le jeu.
     */
    public enum Couleur {
        BLEU, VIOLET, MARRON, NOIRE, VERT, JAUNE, BLANC, ROUGE, NUKE, LOC, JOKERETOILEE
    }

    // Couleur initiale de la carte de wagon.
    private Couleur initialCouleur;
    // Largeur de la carte dans le panneau.
    private int widthInPanel;
    // Hauteur de la carte dans le panneau.
    private int heightInPanel;

    /**
     * Constructeur de la carte de wagon avec une couleur et des dimensions.
     *
     * @param couleur La couleur initiale de la carte de wagon.
     * @param w       La largeur de la carte dans le panneau.
     * @param h       La hauteur de la carte dans le panneau.
     */
    public CarteWagon(Couleur couleur, int w, int h) {
        this.initialCouleur = couleur;
        this.widthInPanel = w;
        this.heightInPanel = h;
    }

    /**
     * Constructeur de la carte de wagon avec uniquement une couleur.
     *
     * @param couleur La couleur initiale de la carte de wagon.
     */
    public CarteWagon(Couleur couleur) {
        this.initialCouleur = couleur;
    }

    /* Getteurs */

    /**
     * Retourne la couleur initiale de la carte de wagon.
     *
     * @return La couleur initiale de la carte de wagon.
     */
    public Couleur getInitialCouleur() {
        return initialCouleur;
    }

    /**
     * Retourne la largeur de la carte dans le panneau.
     *
     * @return La largeur de la carte dans le panneau.
     */
    public int getWidthInPanel() {
        return widthInPanel;
    }

    /**
     * Retourne la hauteur de la carte dans le panneau.
     *
     * @return La hauteur de la carte dans le panneau.
     */
    public int getHeightInPanel() {
        return heightInPanel;
    }
}

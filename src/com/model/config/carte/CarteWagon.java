package com.model.config.carte;

import javax.swing.*;

public class CarteWagon extends JPanel {
    public enum Couleur {BLEU, VIOLET, MARRON, NOIRE, VERT, JAUNE, BLANC, ROUGE, NUKE, LOC, JOKERETOILEE}
    private Couleur initialCouleur ;
    private int widthInPanel  , heightInPanel ;

	public CarteWagon(Couleur couleur , int w , int h ) {
		this.initialCouleur = couleur;
        this.widthInPanel = w ;
        this.heightInPanel = h ;
	}

    public CarteWagon(Couleur couleur)
    {
        this.initialCouleur = couleur;
    }

    /* getteurs et setteurs */
	public Couleur getInitialCouleur() {
        return initialCouleur;
    }

    public int getWidthInPanel() {
        return widthInPanel;
    }

    public int getHeightInPanel() {
        return heightInPanel;
    }
}

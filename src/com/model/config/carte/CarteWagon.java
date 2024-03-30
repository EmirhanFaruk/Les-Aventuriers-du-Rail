package com.model.config.carte;
import com.model.config.Rail;

import javax.swing.*;
import java.util.Random;
import static com.model.config.carte.CarteWagon.Couleur.*;

public class CarteWagon extends JPanel {
    public enum Couleur {BLEU, VIOLET, MARRON, NOIRE, VERT, JAUNE, BLANC, ROUGE, LOC, JOKERETOILEE}

    private Couleur initialCouleur ;

    private Couleur[] couleurCarte = new Couleur[]
            {LOC, BLEU, VIOLET, MARRON, BLANC, VERT, JAUNE, NOIRE, ROUGE};

    private int widthInPanel  , heightInPanel ;
	public CarteWagon(Couleur couleur , int w , int h ) {
		this.initialCouleur = couleur;
        this.widthInPanel = w ;
        this.heightInPanel = h ;
	}

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

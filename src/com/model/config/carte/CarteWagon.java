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

	public CarteWagon(Couleur couleur) {
		this.initialCouleur = couleur;
	}

	public Couleur getInitialCouleur() {
        return initialCouleur;
    }
}

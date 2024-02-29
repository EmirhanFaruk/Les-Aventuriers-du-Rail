package com.model.config.carte;
import com.model.config.Rail;

import java.util.Random;
import static com.model.config.carte.CarteWagon.Couleur.*;

public class CarteWagon {
    public enum Couleur {LOC, BLEU, VIOLET, MARRON, BLANC, VERT, JAUNE, NOIRE, ROUGE}

    private Couleur initialCouleur ;

    private Couleur[] couleurCarte = new Couleur[]
            {LOC, BLEU, VIOLET, MARRON, BLANC, VERT, JAUNE, NOIRE, ROUGE};




    public Couleur getInitialCouleur() {
        return initialCouleur;
    }
}

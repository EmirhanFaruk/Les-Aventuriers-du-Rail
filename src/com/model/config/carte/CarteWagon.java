package com.model.config.carte;
import com.model.config.Rail;

import java.util.Random;
import static com.model.config.carte.CarteWagon.Couleur.*;

public class CarteWagon {
    public enum Couleur {LOC, BLEU, VIOLET, MARRON, BLANC, VERT, JAUNE, NOIRE, ROUGE}

    private Couleur initialCouleur ;

    private Couleur[] couleurCarte = new Couleur[]
            {LOC, BLEU, VIOLET, MARRON, BLANC, VERT, JAUNE, NOIRE, ROUGE};


    public Couleur getPioche(){
        //Comme il y a 110 cartes au total, on fait un random qui va nous donner un chiffre entre 0 et 109

        Random carte = new Random(110);
        int pioche = carte.nextInt();

        //En fonction du chiffre qu'on a obtenu, on renvoit une Couleur
        if(pioche >= 0 && pioche <= 11){
            return BLEU;
        }
        if(pioche >= 12 && pioche <= 23){
            return VIOLET;
        }
        if(pioche >= 24 && pioche <= 35){
            return MARRON;
        }
        if(pioche >= 36 && pioche <= 47){
            return NOIRE;
        }
        if(pioche >= 48 && pioche <= 59){
            return VERT;
        }
        if(pioche >= 60 && pioche <= 71){
            return JAUNE;
        }
        if(pioche >= 72 && pioche <= 83){
            return BLANC;
        }
        if(pioche >= 84 && pioche <= 95){
            return ROUGE;
        }

        return LOC;


    }

    public Couleur getInitialCouleur() {
        return initialCouleur;
    }
}

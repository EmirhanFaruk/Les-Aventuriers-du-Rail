package com.model.config.carte;

import javax.swing.*;
import java.util.Random;

import static com.model.config.carte.CarteDestination.Ville.*;

public class CarteDestination extends JPanel {
    private Ville premiereVille;
    private Ville deuxiemeVille;
    private int nombrePoints;
    public enum Ville
    {MONAN,EMIRHANDOME,SALAME,AMAZIGHSPIDERMAN,
        ALEXICOLE,YAPADEPANO,WOKUWOKU,CHEDELYON,
        WOKANDA,FFQUATORZE,DUCKDUCK,CATLAND,
        PARTPARTPART,BEINGCHILLING,ADOMINATION}

    private Ville[] villes = new Ville[]{MONAN, EMIRHANDOME, SALAME, AMAZIGHSPIDERMAN,
            ALEXICOLE, YAPADEPANO, WOKUWOKU, CHEDELYON,
            WOKANDA, FFQUATORZE, DUCKDUCK, CATLAND,
            PARTPARTPART, BEINGCHILLING, ADOMINATION};


    public Ville getPremiereVille() {
        return premiereVille;
    }
    public Ville getDeuxiemeVille() {
        return deuxiemeVille;
    }
    public int getNombrePoints() {
        return nombrePoints;
    }

    public Ville[] getVilles() {
        return villes;
    }

    //Pour les testes on va utiliser ce constructeur

    public CarteDestination(){
        this.premiereVille = WOKUWOKU;
        this.deuxiemeVille = ADOMINATION;
        this.nombrePoints = 5;
    }

    public CarteDestination(Ville v1, Ville v2, int nombrePoints){
        this.premiereVille = v1;
        this.deuxiemeVille = v2;
        this.nombrePoints = nombrePoints;
    }


    /*
    public CarteDestination(){
        getDestination();
    }
    */





}

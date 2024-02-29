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

    private void getDestination(){
        //Fonction qui choisit au hasard les déstinations

        //On prends 2 Random qui donne un nombre qui représente la position dans le tableau des villes
        Random ville1RANDOM = new Random(villes.length);
        Random ville2RANDOM = new Random(villes.length);
        int ville1 = ville1RANDOM.nextInt();
        int ville2 = ville1RANDOM.nextInt();

        //Si on a la meme ville en alors on relance ville2 jusqu'a en avoir un différent
        while(ville1 == ville2){
            ville2 = ville1RANDOM.nextInt();
        }

        //On initialise la premiere ville et la deuxieme ville et le nombre de point
        this.premiereVille = villes[ville1];
        this.deuxiemeVille = villes[ville2];
        this.nombrePoints = nombrePointsDestination(this.premiereVille,this.deuxiemeVille);

    }

    //Pour les testes on va utiliser ce constructeur

    public CarteDestination(){
        this.premiereVille = WOKUWOKU;
        this.deuxiemeVille = ADOMINATION;
        this.nombrePoints = 5;
    }

    /*
    public CarteDestination(){
        getDestination();
    }
    */


    public int nombrePointsDestination(Ville v1, Ville v2){
        //Fonction qui dit le nombre de point pour la destination entre 2 villes

        int nombreDePoint = 0; //variable qui détermine le nombre de point

        //TODO : Add a fonction that add a int in nombrePoints

        /*
        switch (nombreDePoint){
            //1 wagon = 1 point
            case 1 -> {
                return 1;
            }
            //2 wagon = 2 point
            case 2 -> {
                return 2;
            }
            //3 wagon = 4 point
            case 3 -> {
                return 4;
            }
            //4 wagon = 7 point
            case 4 -> {
                return 7;
            }
            //5 wagon = 10 point
            case 5 -> {
                return 10;
            }
            default -> {
                return 0;
            }

            }
        */
        return 0;
    }



}

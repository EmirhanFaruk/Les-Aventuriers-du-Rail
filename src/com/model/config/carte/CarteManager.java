package com.model.config.carte;

import com.model.config.Plateau;
import com.model.config.Plateau.Ville;
import com.model.config.Route;

import java.util.Random;

import static com.model.config.carte.CarteWagon.Couleur.*;

public class CarteManager {

    //Le tableau des cartes Wagon du jeu
    private CarteWagon.Couleur[] trainCards = new CarteWagon.Couleur[5];
    //Le tableau des cartes Destination du jeu
    private CarteDestination[] destinationsCards = new CarteDestination[3];
    private Plateau plateau = new Plateau();


    public CarteManager(){
        //Pour initialiser les wagons
        for(int i = 0; i< trainCards.length;i++){
            trainCards[i] = getPioche();
        }

    }

    public CarteDestination[] getDestinationsCards() {
        return destinationsCards;
    }

    public CarteWagon.Couleur[] getTrainCards() {
        return trainCards;
    }

    public void setDestinationsCards(CarteDestination[] destinationsCards) {
        this.destinationsCards = destinationsCards;
    }

    public void setTrainCards(CarteWagon.Couleur[] trainCards) {
        this.trainCards = trainCards;
    }

    public CarteWagon.Couleur takeWagon(int position){
        //Fonction qui prends une carte
        CarteWagon.Couleur renvoie = trainCards[position]; //On renvoie l'ancienne carte
        trainCards[position] = getPioche(); //On met une nouvelle carte qui remplace l'ancienne

        return renvoie;

    }

    public CarteDestination takeDestination(int position){
        //Fonction qui prends prends une carte destination
        return destinationsCards[position];
    }

    public void rerollDestination(){
        //Fonction qui remets de nouvelles mission

        //On va remplacer chaque élément par une nouvelle destination
        for(int i = 0; i<destinationsCards.length;i++){
            destinationsCards[i] = new CarteDestination();

        }
    }

    public CarteWagon.Couleur getPioche(){
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

    public CarteDestination getDestination(){
        //Fonction qui choisit au hasard les déstinations

        //On prends 2 Random qui donne un nombre qui représente la position dans le tableau des villes
        Random ville1RANDOM = new Random(plateau.getVilles().length);
        Random ville2RANDOM = new Random(plateau.getVilles().length);
        int ville1 = ville1RANDOM.nextInt();
        int ville2 = ville1RANDOM.nextInt();

        //Si on a la meme ville en alors on relance ville2 jusqu'a en avoir un différent
        while(ville1 == ville2){
            ville2 = ville1RANDOM.nextInt();
        }
        Plateau.Ville v1 = plateau.getVilles()[ville1];
        Plateau.Ville v2 = plateau.getVilles()[ville2];

        //On initialise la premiere ville et la deuxieme ville et le nombre de point
        return  new CarteDestination(new Route(v1,v2,nombrePointDistance(v1,v2)));


    }


    public int nombrePointDistance(Plateau.Ville v1, Plateau.Ville v2){
        //TODO
        return 2;
    }


    public CarteWagon.Couleur drawTrain(){
        return getPioche();
    }

    //TODO




}

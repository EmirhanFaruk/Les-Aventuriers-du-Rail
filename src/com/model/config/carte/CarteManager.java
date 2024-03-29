package com.model.config.carte;

import com.model.Game;
import com.model.config.Plateau;
import com.model.config.Route;
import com.model.config.Ville;
import com.model.config.carte.CarteWagon.Couleur;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.model.config.carte.CarteWagon.Couleur.*;

public class CarteManager {

    //Le tableau des cartes Wagon du jeu
    private CarteWagon.Couleur[] trainCards = new CarteWagon.Couleur[5];
    //Le tableau des cartes Destination du jeu
    private CarteDestination[] destinationsCards = new CarteDestination[3];
    //La pile de cartes Wagon
    public ArrayList<CarteWagon> PileCarteWagon = new ArrayList<>(110);
    //La pile de cartes Destination
    public ArrayList<CarteDestination> PileCarteDestination = new ArrayList<>(46);

    public CarteManager(){
        //Pour initialiser les wagons
        initPileCarteWagon();

    }


    private void initPileCarteWagon() {
        for(int i=0;i<8;i++){ //8 couleurs de carteWagon
            for(int j=0;j<12;j++) //12 wagons de chaque couleur
            PileCarteWagon.add(new CarteWagon(Couleur.values()[i]));
        }
        for(int i =0;i<14;i++){
            PileCarteWagon.add(new CarteWagon(LOC)); // 14 Locomotive
        }
        Collections.shuffle(PileCarteWagon); // Mélange de cartes.
    }

    public void initPileCarteDestination(Game g) {
        ArrayList<Route> gameRoutes = g.getRoutes();
        for(Route r : gameRoutes){
            PileCarteDestination.add(new CarteDestination(r)); //Les cartes destinations à courte distance (une route)
        }
        for(int i=0; i<21; i++){
            int nbRoutes = 1;//ThreadLocalRandom.current().nextInt(1, 4);
            Route depart = gameRoutes.get(i);
            Ville v1 = depart.getVille1();
            int nbpoint = depart.getLongueur();
            for(int j=nbRoutes;j>0;j--){
                depart = trouverUneCorrespondance(gameRoutes,depart);
                nbpoint += depart.getLongueur();
            }
            Ville v2 = depart.getVille2();
            PileCarteDestination.add(new CarteDestination(v1, v2, nbpoint));
        }
        //Collections.shuffle(PileCarteDestination);
    }

    private Route trouverUneCorrespondance(ArrayList<Route> gameRoutes, Route depart) {
        for(Route r:gameRoutes){
            if(r.getVille1()==depart.getVille2() ||r.getVille2()==depart.getVille2() && r != depart)
                return r;
        }
        return null;
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

    public boolean possibleTakeWagon(int action, int position){
        if(trainCards[position] == LOC){
            return action == 2;
        }else{
            return true;
        }
    }

    public CarteWagon.Couleur takeWagon(int position){
        //Fonction qui prends une carte
        CarteWagon.Couleur renvoie = trainCards[position]; //On renvoie l'ancienne carte
        trainCards[position] = drawCard(); //On met une nouvelle carte qui remplace l'ancienne

        return renvoie;

    }

    public CarteDestination[] takeDestination(int[] position,Game game){
        CarteDestination [] renvoie = new CarteDestination[position.length];
        //Fonction qui prends prends une carte destination

        for(int i = 0; i<position.length;i++){


            renvoie[i] = destinationsCards[position[i]];
        }
        rerollDestination(game);

        return renvoie;
    }

    public void rerollDestination(Game game){

        //Fonction qui remets de nouvelles mission

        //On va remplacer chaque élément par une nouvelle destination
        for(int i = 0; i<destinationsCards.length;i++){

            destinationsCards[i] = getDestination(game);

        }
    }

    public CarteWagon.Couleur drawCard(){
        //Comme il y a 110 cartes au total, on fait un random qui va nous donner un chiffre entre 0 et 109

        Random carte = new Random();
        int pioche = carte.nextInt(110);

        //En fonction du chiffre qu'on a obtenu, on renvoit une Couleur
        if(pioche >= 0 && pioche <= 11){
            return Couleur.BLEU;
        }
        if(pioche >= 12 && pioche <= 23){
            return Couleur.VIOLET;
        }
        if(pioche >= 24 && pioche <= 35){
            return Couleur.MARRON;
        }
        if(pioche >= 36 && pioche <= 47){
            return Couleur.NOIRE;
        }
        if(pioche >= 48 && pioche <= 59){
            return Couleur.VERT;
        }
        if(pioche >= 60 && pioche <= 71){
            return Couleur.JAUNE;
        }
        if(pioche >= 72 && pioche <= 83){
            return Couleur.BLANC;
        }
        if(pioche >= 84 && pioche <= 95){
            return Couleur.ROUGE;
        }

        return Couleur.LOC;

    }

    public CarteDestination getDestination(Game game){
        //Fonction qui choisit au hasard les déstinations

        //On prend un Random qui donne un nombre qui représente la position dans le tableau des villes
        Random villeRANDOM = new Random();

        //Si on a la meme ville en alors on relance ville2 jusqu'a en avoir un différent


        Ville v1 = game.getVilles()[villeRANDOM.nextInt(game.getVilles().length)];
        Ville v2 = game.getVilles()[villeRANDOM.nextInt(game.getVilles().length)];

        while (v1 == v2){
            v2 = game.getVilles()[villeRANDOM.nextInt(game.getVilles().length)];

        }

        //On initialise la premiere ville et la deuxieme ville et le nombre de point
        return new CarteDestination(new Route(v1,v2,nombrePointDistance(v1,v2)));


    }


    public int nombrePointDistance(Ville v1, Ville v2){
        //TODO
        return 2;
    }

    public void afficherCartesDestination(){
        for(CarteDestination c : PileCarteDestination){
            System.out.println("V1: "+c.getPremiereVille().getNom()+" V2: "+c.getDeuxiemeVille().getNom()+" Points: "+c.getNombrePoints());
        }
    }




    
}

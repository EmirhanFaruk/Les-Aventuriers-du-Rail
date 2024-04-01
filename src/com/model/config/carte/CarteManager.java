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
        for (int i=0; i< 21;i++){
            Random r = new Random();
            int Vlength = g.getVilles().length;
            Ville v1 = g.getVilles()[r.nextInt(Vlength)];
            Ville v2 = g.getVilles()[r.nextInt(Vlength)];
            while (v2.getNom().equals(v1.getNom())){
                v2 = g.getVilles()[r.nextInt(Vlength)];
            }
            while(carteDestExistante(v1,v2)){
                v1 = g.getVilles()[r.nextInt(Vlength)];
                v2 = g.getVilles()[r.nextInt(Vlength)];
                while (v2.getNom().equals(v1.getNom())){
                    v2 = g.getVilles()[r.nextInt(Vlength)];
                }
            }

            PileCarteDestination.add(new CarteDestination(v1,v2,nombrePointDistance(v1, v2)));
        }
        Collections.shuffle(PileCarteDestination);
        for(int i=0; i< destinationsCards.length;i++){
            destinationsCards[i]= PileCarteDestination.remove(0);
        }
    }


    private boolean carteDestExistante(Ville v1, Ville v2) {
        String cV1 = v1.getNom(); //Le nom de la première ville
        String cV2 = v2.getNom(); //Le nom de la deuxieme ville
        for(CarteDestination carte : PileCarteDestination){
            if(carte.getPremiereVille().getNom().equals(cV1) && carte.getDeuxiemeVille().getNom().equals(cV2) || carte.getPremiereVille().getNom().equals(cV2) && carte.getDeuxiemeVille().getNom().equals(cV1))
                return true;
        }
        return false;
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
        return PileCarteWagon.remove(0).getInitialCouleur();

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
}

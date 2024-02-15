package config.carte;

import java.util.ArrayList;

public class CarteManager {

    private CarteWagon cw = new CarteWagon();

    //Le tableau des cartes Wagon du jeu
    private CarteWagon.Couleur[] trainCards = new CarteWagon.Couleur[4];
    //Le tableau des cartes Destination du jeu
    private CarteDestination[] destinationsCards = new CarteDestination[3];


    public CarteManager(){
        //Pour initialiser les wagons
        for(int i = 0; i< trainCards.length;i++){
            trainCards[i] = cw.getPioche();
        }

    }

    public CarteWagon.Couleur takeWagon(int position){
        //Fonction qui prends une carte
        CarteWagon.Couleur renvoie = trainCards[position]; //On renvoie l'ancienne carte
        trainCards[position] = cw.getPioche(); //On met une nouvelle carte qui remplace l'ancienne

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

    public CarteWagon.Couleur drawTrain(){
        return cw.getPioche();
    }






}

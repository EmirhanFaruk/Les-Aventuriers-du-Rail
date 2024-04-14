package com.model.config.carte;

import com.model.Game;
import com.model.Player;
import com.model.ai.Node;
import com.model.config.Plateau;
import com.model.config.Route;
import com.model.config.Ville;
import com.model.config.carte.CarteWagon.Couleur;

import java.util.Random;
import java.util.ArrayList;
import java.util.*;

import static com.model.config.carte.CarteWagon.Couleur.*;

public class CarteManager {

    //Le tableau des cartes Wagon du jeu
    private CarteWagon.Couleur[] trainCards = new CarteWagon.Couleur[3];
    //Le tableau des cartes Destination du jeu
    private CarteDestination[] destinationsCards = new CarteDestination[3];
    //La pile de cartes Wagon
    public ArrayList<CarteWagon> PileCarteWagon = new ArrayList<>(110);
    //La pile de cartes Destination
    public ArrayList<CarteDestination> PileCarteDestination = new ArrayList<>(46);
    //Liste des cartes missions déjà crée
    private ArrayList<CarteDestination> createdMissionCard = new ArrayList<>();

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
        for(int i=0; i<trainCards.length;i++){
            trainCards[i] = PileCarteWagon.remove(0).getInitialCouleur();
        }
        verifAllDifferent();
    }

    public void initPileCarteDestination(Game g) {
        ArrayList<Route> gameRoutes = g.getRoutes();
        for(Route r : gameRoutes){
            PileCarteDestination.add(new CarteDestination(r)); //Les cartes destinations à courte distance (une route)
        }
        for (int i=0; i< 21;i++){
            Random r = new Random();
            int Vlength = g.getVilles().size();
            Ville v1 = g.getVilles().get(r.nextInt(Vlength));
            Ville v2 = g.getVilles().get(r.nextInt(Vlength));
            while (v2.getNom().equals(v1.getNom())){
                v2 = g.getVilles().get(r.nextInt(Vlength));
            }
            while(carteDestExistante(v1,v2)){
                v1 = g.getVilles().get(r.nextInt(Vlength));
                v2 = g.getVilles().get(r.nextInt(Vlength));
                while (v2.getNom().equals(v1.getNom())){
                    v2 = g.getVilles().get(r.nextInt(Vlength));
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
        
        verifAllDifferent();

        return renvoie;
    }

    private void verifAllDifferent() {
        while((trainCards[0] == trainCards[1] && trainCards[0] == trainCards[2]) &&
                trainCards[0] == CarteWagon.Couleur.LOC) {
            trainCards[0] = drawCard();
            trainCards[1] = drawCard();
            trainCards[2] = drawCard();
        }
    }

    public CarteWagon.Couleur showWagon(int position){
        return trainCards[position];
    }

    
    public CarteDestination[] takeDestination(int[] position){    
        //Fonction qui prends prends une carte destination
        CarteDestination [] renvoie = new CarteDestination[position.length];
        for(int i = 0; i<position.length;i++){
            renvoie[i] = destinationsCards[position[i]];
            destinationsCards[position[i]] = null;
        }
        rerollDestination();

        return renvoie;
    }

    public void rerollDestination(){
        //Fonction qui remets de nouvelles mission
        for(int i = 0; i<destinationsCards.length;i++){
            if(destinationsCards[i]!= null){
                PileCarteDestination.add(destinationsCards[i]);
            }
            destinationsCards[i] = getDestination();
        }
    }

    public CarteWagon.Couleur drawCard(){
        if(!PileCarteWagon.isEmpty())
        return PileCarteWagon.remove(0).getInitialCouleur();
        return null;
    }

    public CarteDestination getDestination(){
        return PileCarteDestination.remove(0);
    }


    private int cheminLongueur(ArrayList<Ville> chemin)
    {
        int res = 0;
        while(chemin.size() > 1)
        {
            Ville current = chemin.get(0);
            Ville next = chemin.get(1);
            res += findLink(current, next).getLongueur();
            chemin.remove(0);
        }

        return res;
    }

    private Route findLink(Ville v1, Ville v2)
    {
        ArrayList<Route> routes = v1.getRoutes();
        int i = 0;
        while (i < routes.size())
        {
            if (links(v1, v2, routes.get(i)))
            {
                return v1.getRoutes().get(i);
            }
            i++;
        }
        return routes.get(0);
    }

    private boolean links(Ville v1, Ville v2, Route route)
    {
        boolean possibility1 = route.getVille1() == v1 && route.getVille2() == v2;
        boolean possibility2 = route.getVille1() == v2 && route.getVille2() == v1;
        return possibility1 || possibility2;
    }


    public int nombrePointDistance(Ville v1, Ville v2){
        ArrayList<Ville> chemin = Node.findClosestPath(v1, v2, null);

        int longueur = cheminLongueur(chemin);

        return longueur;
    }

    public boolean trainCardisEmpty(){
        for(CarteWagon.Couleur c : trainCards){
            if(c != null) return false;
        }
        return true;
    }

    public boolean sameMission(CarteDestination carteDestination1, CarteDestination carteDestination2){
        //Fonction qui compare 2 cartes missions si ce sont les memes

        boolean sameVille1 = carteDestination1.getPremiereVille() == carteDestination2.getPremiereVille();
        boolean sameVille2 = carteDestination1.getDeuxiemeVille() == carteDestination2.getDeuxiemeVille();

        boolean sameMission1 = sameVille1 && sameVille2;

        boolean sameVille3 = carteDestination1.getPremiereVille() == carteDestination2.getDeuxiemeVille();
        boolean sameVille4 = carteDestination1.getDeuxiemeVille() == carteDestination2.getPremiereVille();

        boolean sameMission2 = sameVille3 && sameVille4;

        return  sameMission1 || sameMission2;

    }

    public boolean doubleMission(CarteDestination carteDestination){
        //Fonction qui regarde dans la liste des missions deja créés (prises par les joueur) et dit si la mission existe deja ou non

        for(int i = 0; i< this.createdMissionCard.size();i++){

            if(sameMission(createdMissionCard.get(i),carteDestination)){
                return true;
            }

        }
        return false;

    }


}

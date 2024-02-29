package com.main;

import com.model.Player;
import com.model.config.Route;
import com.model.config.Plateau;
import com.model.config.Ville;
import com.model.config.carte.CarteManager;

import java.util.ArrayList;
import java.util.List;

public class Game implements Runnable {
    private Plateau plateau;
    private List<Player> joueurs;
    private Player joueurCourant;
    private List<Ville> villes;
    private List<Route> routes;
    private CarteManager cm;

    //Le board des cartes
    private CarteManager carteManager = new CarteManager();

    private ArrayList<Route> route = new ArrayList<>();

    public Game(Plateau plateau, List<Ville> villes,List<Route> routes) {
        // création des cartes
        //...
        // création des villes et des routes
        this.plateau = plateau;
        this.villes = villes;
        this.routes = routes;
    }

    public List<Ville> getVilles() {
        return villes;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public List<Player> getJoueurs() {
        return joueurs;
    }

    public Player getJoueurCourant() {
        return joueurCourant;
    }

    private void initBoard(){
        //Fonction qui initialise le jeu


        //Initialisation des cartes wagon sur le board
        for(int i = 0; i< carteManager.getTrainCards().length; i++){
            carteManager.getTrainCards()[i] = carteManager.getPioche();
        }

        //Initialisation des cartes destination du premier tour du board que le joueur choisit
        for(int y = 0; y < carteManager.getDestinationsCards().length;y++){
            carteManager.getDestinationsCards()[y] = carteManager.getDestination();
        }

    }


    private void initRoad(){




    }

    public void run() {

        while (true) {
            //game loop
        }
    }


}

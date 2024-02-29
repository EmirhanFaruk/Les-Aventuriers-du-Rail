package com.main;

import com.model.Route;
import com.model.config.Plateau;
import com.model.config.Ville;
import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteManager;

import java.util.List;

public class Game implements Runnable {
    private Plateau plateau;
    private List<Joueur> joueurs;
    private Joueur joueurCourant;
    private List<Ville> villes;
    private List<Route> routes;
    private CarteManager cm;

    //Le board des cartes
    private CarteManager carteManager = new CarteManager();

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

    public List<Joueur> getJoueurs() {
        return joueurs;
    }

    public Joueur getJoueurCourant() {
        return joueurCourant;
    }

    private void initBoard(){
        for(int i = 0; i< carteManager.getDestinationsCards().length; i++){
            carteManager.getTrainCards()[i] = carteManager.getPioche();
        }

        for(int y = 0; y < carteManager.getDestinationsCards().length;y++){
            carteManager.getDestinationsCards()[y] = carteManager.getDestination();
        }

    }

    public void run() {

        while (true) {
            //game loop
        }
    }


}

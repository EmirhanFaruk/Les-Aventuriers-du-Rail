package com.main;

import com.model.config.Plateau;
import com.model.config.carte.CarteManager;
import com.model.config.Ville;
import com.model.Route;
import com.model.Joueur;
import java.util.List;

public class Game implements Runnable {
    private Plateau plateau;
    private List<Joueur> joueurs;
    private Joueur joueurCourant;
    private List<Ville> villes;
    private List<Route> routes;
    private CarteManager cm;

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

    public void run() {

        while (true) {
            //game loop
        }
    }


}

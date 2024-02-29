package com.model;

import com.model.config.Plateau;
import com.model.config.carte.CarteManager;
import com.model.config.Ville;

import java.util.ArrayList;
import java.util.List;

public class Game
{
    private Plateau plateau;
    private List<Joueur> joueurs;
    private Joueur joueurCourant;
    private Ville[] villes;
    private ArrayList<Route> routes;
    private CarteManager cm;

    public Game() {}

    public void makeGame(String nomMap)
    {
        this.cm = new CarteManager();
        this.plateau = Plateau.makePlateau(nomMap, this.routes, this.villes);
    }

    public Ville[] getVilles() {
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

    public void update(double deltaTime)
    {
        //game loop
    }


}

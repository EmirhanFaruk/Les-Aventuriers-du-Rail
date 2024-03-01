package com.model;

import com.model.config.Plateau;
import com.model.config.carte.CarteManager;
import com.model.config.Ville;

import java.util.ArrayList;
import java.util.List;

public class Game
{
    private Plateau plateau;
    private List<Player> joueurs;
    private Player joueurCourant;
    private Ville[] villes;
    private ArrayList<Route> routes;
    private CarteManager cm;

    public Game() {}

    public void makeGame(String nomMap)
    {
        this.cm = new CarteManager();
        this.plateau = Plateau.makePlateau(nomMap, this);
    }

    public Ville[] getVilles() {
        return villes;
    }

    public void setVilles(Ville[] villes) { this.villes = villes; }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<Route> routes) { this.routes = routes; }

    public List<Player> getJoueurs() {
        return joueurs;
    }

    public Player getJoueurCourant() {
        return joueurCourant;
    }

    public void update(double deltaTime)
    {
        //game loop
    }


}

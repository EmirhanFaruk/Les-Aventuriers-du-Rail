package com.model;

import com.model.config.Plateau;
import com.model.config.Route;
import com.model.config.carte.CarteManager;
import com.model.config.Ville;

import java.util.ArrayList;
import java.util.List;

public class Game
{
    private Plateau plateau;
    private ArrayList<Player> listPlayer;
    private Ville[] villes;
    private ArrayList<Route> routes;
    private CarteManager cm;
    private Round round;

    public Game() {}

    public void makeGame(String nomMap)
    {
        this.cm = new CarteManager(plateau);
        this.plateau = Plateau.makePlateau(nomMap, this);
        this.round = new Round();
    }

    /*
    getteurs et setteurs
     */
    public Plateau getPlateau() {
        return plateau;
    }

    public Ville[] getVilles() {
        return villes;
    }

    public void setVilles(Ville[] villes) { this.villes = villes; }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<Route> routes) { this.routes = routes; }

    public ArrayList<Player> getListPlayer() {
        return listPlayer;
    }

    public void setListPlayer(ArrayList<Player> listPlayer) {
        this.listPlayer = listPlayer;
    }

    private void initBoard(){
        //Fonction qui initialise le jeu


        //Initialisation des cartes wagon sur le board
        for(int i = 0; i< cm.getTrainCards().length; i++){
            cm.getTrainCards()[i] = cm.drawCard();
        }

        //Initialisation des cartes destination du premier tour du board que le joueur choisit
        for(int y = 0; y < cm.getDestinationsCards().length;y++){
            cm.getDestinationsCards()[y] = cm.getDestination(this);
        }

    }





    public void updateGame(double deltaTime)
    {
        //game loop
        if(round.roundFinished()){

            round.round(this,cm);

        }




    }
}

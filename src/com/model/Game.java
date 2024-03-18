package com.model;

import com.model.config.Plateau;
import com.model.config.Route;
import com.model.config.carte.CarteManager;
import com.model.config.Ville;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class Game
{
    private Plateau plateau;
    private ArrayList<Player> listPlayer;
    private Ville[] villes;
    private ArrayList<Route> routes;
    private CarteManager cm;
    private Round round;

    public Game() {}

    public void makeGame(String nomMap,String[] player_names, String[] player_types,Color[] player_colors)
    {
        this.cm = new CarteManager(plateau);
        this.plateau = Plateau.makePlateau(nomMap, this);
        listPlayer = initPlayers(player_names,player_types,player_colors);
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

    private ArrayList<Player> initPlayers(String[] player_names, String[] player_types,Color[] player_colors){
        ArrayList<Player> playerlist = new ArrayList<>();
        Player p1 = new Player(colorToString(player_colors[0]),player_names[0],StringToNiveau(player_types[0]));
        Player p2 = new Player(colorToString(player_colors[1]),player_names[1],StringToNiveau(player_types[1]));
        Player p3 = new Player(colorToString(player_colors[2]),player_names[2],StringToNiveau(player_types[2]));
        Player p4 = new Player(colorToString(player_colors[3]),player_names[3],StringToNiveau(player_types[3]));
        playerlist.add(p1);playerlist.add(p2);playerlist.add(p3);playerlist.add(p4);
        return playerlist;
    }

    private String colorToString(Color c){
        if(c.equals(Color.red)) return "ROUGE";
        if(c.equals(Color.blue)) return "BLEU";
        if(c.equals(Color.green)) return "VERT";
        if(c.equals(Color.yellow)) return "JAUNE";
        return "Error";
    }

    private int StringToNiveau(String s){
        if (s.equals("PLAYER")) return 0;
        if (s.equals("CPU")) return 1; // pour l'instant en attendant une implémentation complète.
        return -1;
    }


    /**
     * Verifie s'il y a un joueur qui a moins de 3 wagons
     * @return true si nbrWagon est inferieur a 3
     */
    public boolean endGame( ){
        for (Player p : listPlayer){
            if ( p.getNbrWagon() <=2 ){
                return true ;
            }
        }
        return false ;
    }


    public void updateGame(double deltaTime)
    {
        //game loop
        if(round.roundFinished()){

            round.round(this,cm);

        }




    }
}

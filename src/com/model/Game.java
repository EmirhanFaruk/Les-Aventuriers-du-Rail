package com.model;

import com.model.config.Plateau;
import com.model.config.Route;
import com.model.config.carte.CarteManager;
import com.model.config.Ville;
import com.view.GameFrame;
import com.view.GameScreen;

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

    private GameFrame gameFrame ;

    public Game(GameFrame gameFrame) {
        this.gameFrame = gameFrame ;
        listPlayer = new ArrayList<>();
        listPlayer.add(new Player("BLEU")) ;
    }

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

    public void  dinumueCarte(){
        for ( Player p : listPlayer ){
            p.setNbrWagon( p.getNbrWagon() - 1 );
        }
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


    public void updateGame( )
    {
        //game loop
        if(round.roundFinished()){

            round.round(this,cm);

        }

        if ( endGame() && this.gameFrame.getGameScreen() != null) {
            System.err.println("la partie est terminée");
            this.gameFrame.getGameScreen().getGameManagerScreen().update();
        }

    }
}

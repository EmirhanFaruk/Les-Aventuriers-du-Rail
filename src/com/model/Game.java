package com.model;

import com.model.config.Plateau;
import com.model.config.Route;
import com.model.config.carte.CarteManager;
import com.model.config.carte.CarteWagon;
import com.model.config.carte.CarteWagon.Couleur;
import com.view.GameFrame;
import com.view.GameScreen;
import com.model.config.Ville;
import com.view.GameFrame;
import com.view.GameScreen;

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

    private GameFrame gameFrame ;

    public Game(GameFrame gameFrame) {
        this.gameFrame = gameFrame ;
    }

    public void makeGame( String nomMap , String[] player_names , String[] player_types , Color[] player_colors )
    {
        this.cm = new CarteManager();
        this.plateau = Plateau.makePlateau(nomMap, this);
        this.listPlayer = initPlayers(player_names,player_types,player_colors,cm);
        initBoard();
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

    public ArrayList<Route> getRoutes() {
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

        // Donner des cartes aux joueurs au debut de la partie (chacun en reçoit 4)
        for (int i = 0; i < listPlayer.size(); i++)
        {
            for (int j = 0; j < 5; j++)
            {
                listPlayer.get(i).piocher(cm);
            }
        }

    }

    private ArrayList<Player> initPlayers(String[] player_names, String[] player_types ,Color[] player_colors,  CarteManager carteManager){
        ArrayList<Player> playerlist = new ArrayList<>();

        for(int i = 0; i< player_types.length;i++){

            switch (player_types[i]){

                case "PLAYER" :
                    playerlist.add(new Player(colorToString(player_colors[i]),player_names[i],0,carteManager));
                    break;

                case "WEAK" :
                    playerlist.add(new Player(colorToString(player_colors[i]),player_names[i],1,carteManager));
                    break;

                case "NORMAL" :
                    playerlist.add(new Player(colorToString(player_colors[i]),player_names[i],2,carteManager));
                    break;

                case "STRONG" :
                    playerlist.add(new Player(colorToString(player_colors[i]),player_names[i],3,carteManager));
                    break;

                default: break;

            }



        }

        return playerlist;
    }

    private String colorToString(Color c){
        if(c.equals(Color.red)) return "ROUGE";
        if(c.equals(Color.blue)) return "BLEU";
        if(c.equals(Color.green)) return "VERT";
        if(c.equals(Color.yellow)) return "JAUNE";
        return "Error";
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
    

    public void updateGame( double deltaTime ) {
        //game loop
        if (!round.roundFinished()) {

            round.round(this, cm, deltaTime);

        }

        if (endGame() && this.gameFrame.getGameScreen() != null) {
            System.err.println("la partie est terminée");
            this.gameFrame.getGameScreen().getGameManagerScreen().update();
        }
    }

}

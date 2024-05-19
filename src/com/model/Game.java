package com.model;

import com.model.ai.LongestFinder;
import com.model.config.Plateau;
import com.model.config.Route;
import com.model.config.carte.CarteManager;
import com.view.GameFrame;
import com.model.config.Ville;
import com.view.GameMapPanel;
import com.view.MapScreen;

import java.util.ArrayList;
import java.awt.*;

public class Game
{
    private Plateau plateau;
    private ArrayList<Player> listPlayer;
    private ArrayList<Ville> villes;
    private ArrayList<Route> routes;
    private CarteManager cm;
    private Round round;
    private GameFrame gameFrame ;

    private boolean gaveBonusPoints = false;

    public Game(GameFrame gameFrame) {
        this.gameFrame = gameFrame ;
    }

    public void makeGame( String nomMap , String[] player_names , String[] player_types , Color[] player_colors, boolean music )
    {
        this.cm = new CarteManager(gameFrame.getMode());
        this.plateau = Plateau.makePlateau(nomMap, this);
        this.listPlayer = initPlayers(player_names,player_types,player_colors);
        initBoard();
        this.round = new Round();
        if(music)
        {
            playSound("INGAME" , "inGame.wav");
        }
    }

    private void initBoard(){
        //Fonction qui initialise le jeu
        cm.initPileCarteDestination(this);

        // Donner des cartes aux joueurs au debut de la partie (chacun en reçoit 4)
        for (int i = 0; i < listPlayer.size(); i++)
        {
            for (int j = 0; j < 10; j++)
            {
                listPlayer.get(i).piocher(cm);
            }
        }

    }

    private ArrayList<Player> initPlayers(String[] player_names, String[] player_types ,Color[] player_colors){
        ArrayList<Player> playerlist = new ArrayList<>();

        for(int i = 0; i< player_types.length;i++){

            switch (player_types[i]){

                case "PLAYER" :
                    playerlist.add(new Player(colorToString(player_colors[i]),player_names[i],0,this));
                    break;

                case "WEAK" :
                    playerlist.add(new Player(colorToString(player_colors[i]),player_names[i],1,this));
                    break;

                case "NORMAL" :
                    playerlist.add(new Player(colorToString(player_colors[i]),player_names[i],2,this));
                    break;

                case "STRONG" :
                    playerlist.add(new Player(colorToString(player_colors[i]),player_names[i],3,this));
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
     * Vérifie s'il y a un joueur qui a moins de 3 wagons.
     * @return true si nbrWagon est inférieur à 3.
     */
    public boolean endGame(){
        for (Player p : listPlayer){
            if ( p.getNbrWagon() <=2 ){
                if (!gaveBonusPoints)
                {
                    giveLongestRouteBonus();
                    gaveBonusPoints = true;
                }

                return true ;
            }
        }
        return cm.trainCardisEmpty();
    }

    private void giveLongestRouteBonus()
    {
        int max = 0, maxi = 0;

        for (int i = 0;i < listPlayer.size(); i++)
        {
            ArrayList<Route> tempLongestWay = LongestFinder.findLongestWayAll(villes, listPlayer.get(i));
            int tempMax = LongestFinder.wayLength(tempLongestWay);
            if (tempMax > max)
            {
                max = tempMax;
                maxi = i;
            }
        }

        if (listPlayer.get(maxi) != null)
        {
            listPlayer.get(maxi).addLongestWayScore();
        }
    }

    /**
     * Une fonction qui met a jour le jeu
     * @param deltaTime le temps
     */
    public void updateGame( double deltaTime ) {
        //game loop
        if (!round.roundFinished()) {
            round.round(this, deltaTime);
        }

        if (endGame() && this.gameFrame.getGameScreen() != null) {
            // DEBUG : System.err.println("la partie est terminée");
            this.gameFrame.getGameScreen().getGameManagerScreen().showEndGame();
        }
    }

    public void playSound(String screen, String sound_name)
    {
        gameFrame.playSound(screen, sound_name);
    }


    /* getteurs et setteurs */
    public Plateau getPlateau() {
        return plateau;
    }

    public ArrayList<Ville> getVilles() {
        return villes;
    }

    public void setVilles(ArrayList<Ville> villes) { this.villes = villes; }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<Route> routes) { this.routes = routes; }

    public ArrayList<Player> getListPlayer() {
        return listPlayer;
    }

    public GameFrame getGameFrame() {
        return gameFrame;
    }

    public MapScreen getMapScreen()
    {
        if (gameFrame != null)
        {
            return gameFrame.getMapScreen();
        }

        return null;
    }

    public GameMapPanel getGameMapPanel()
    {
        if (gameFrame != null)
        {
            return gameFrame.getGameMapPanel();
        }

        return null;
    }

    public CarteManager getCarteManager() {
    	return this.cm;
    }

    public Round getRound() {
        return this.round;
    }

    public Player getJoueurCourant(){
        return listPlayer.get(round.getWhoIsPlaying());
    }
}

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
    private final GameFrame gameFrame ;

    private boolean gaveBonusPoints = false;

    /**
     * Constructeur de game
     * @param gameFrame gameFrame pour utiliser a plusieurs places
     */
    public Game(GameFrame gameFrame) {
        this.gameFrame = gameFrame ;
    }

    /**
     * Initialiser game.
     * @param nomMap nom de map pour produire la map
     * @param player_names nom des joueurs
     * @param player_types type des joueurs
     * @param player_colors couleur des joueurs
     */
    public void makeGame( String nomMap , String[] player_names , String[] player_types , Color[] player_colors )
    {
        this.cm = new CarteManager(gameFrame.getMode());
        this.plateau = Plateau.makePlateau(nomMap, this);
        this.listPlayer = initPlayers(player_names,player_types,player_colors);
        initBoard();
        this.round = new Round();
    }

    /**
     * Initialiser la pile de carte destination et faire piocher des cartes wagons aux joueurs.
     */
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

    /**
     * Initialiser les joueurs
     * @param player_names nom des joueurs
     * @param player_types type des joueurs
     * @param player_colors couleurs des joueurs
     * @return arraylist des joueurs produits par les parametres
     */
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

    /**
     * Convertir le couleur à string.
     * @param c couleur donné
     * @return l'équivalence de couleur en string
     */
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

    /**
     * Trouver la plus longue route et donne 10 points de plus a son proprietaire
     */
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


    /**
     * Jouer le son dans le screen données dans les parametres en checkant le click de son.
     * Faire appel de gameFrame.
     * @param screen le screen dit
     * @param sound_name le nom de son dit
     */
    public void playSoundClick(String screen, String sound_name)
    {
        gameFrame.playSoundClick(screen, sound_name);
    }

    /**
     * Changer le chanson dans le screen données dans les parametres.
     * Faire appel de gameFrame.
     * @param screen le screen dit
     * @param sound_name le nom de musique dit
     */
    public void changeMusic(String screen, String sound_name)
    {
        gameFrame.changeMusic(screen, sound_name);
    }

    /**
     * Changer le chanson dans le screen données dans les parametres en verifiant si le sound.getMusic() est vrai.
     * Faire appel de gameFrame.
     * @param screen le screen dit
     * @param sound_name le nom de musique dit
     */
    public void changeMusicIsMusic(String screen, String sound_name)
    {
        if (gameFrame.getSound().getMusic())
        {
            changeMusic(screen, sound_name);
        }
    }


    /* getteurs et setteurs */

    /**
     * Renvoyer plateau.
     * @return plateau
     */
    public Plateau getPlateau() {
        return plateau;
    }

    /**
     * Renvoyer villes.
     * @return villes
     */
    public ArrayList<Ville> getVilles() {
        return villes;
    }

    /**
     * Set villes a celle de parametres.
     * @param villes villes données
     */
    public void setVilles(ArrayList<Ville> villes) { this.villes = villes; }

    /**
     * Renvoyer routes.
     * @return routes
     */
    public ArrayList<Route> getRoutes() {
        return routes;
    }

    /**
     * Set routes a celle de parametres.
     * @param routes routes données
     */
    public void setRoutes(ArrayList<Route> routes) { this.routes = routes; }

    /**
     * Renvoyer listPlayer.
     * @return listPlayer
     */
    public ArrayList<Player> getListPlayer() {
        return listPlayer;
    }

    /**
     * Renvoyer gameFrame.
     * @return gameFrame
     */
    public GameFrame getGameFrame() {
        return gameFrame;
    }

    /**
     * Renvoyer MapScreen depuis gameFrame.
     * @return MapScreen
     */
    public MapScreen getMapScreen()
    {
        if (gameFrame != null)
        {
            return gameFrame.getMapScreen();
        }

        return null;
    }

    /**
     * Renvoyer gameMapPanel depuis gameFrame.
     * @return gameMapPanel
     */
    public GameMapPanel getGameMapPanel()
    {
        if (gameFrame != null)
        {
            return gameFrame.getGameMapPanel();
        }

        return null;
    }

    /**
     * Renvoyer si mode est egal a "NUKE".
     * @return resultat
     */
    public boolean isModeNuke()
    {
        return getGameFrame().getMode().equals("NUKE");
    }

    /**
     * Getter pour carte manager.
     * @return carte manager
     */
    public CarteManager getCarteManager() {
    	return this.cm;
    }

    /**
     * Getter pour round.
     * @return round
     */
    public Round getRound() {
        return this.round;
    }

    /**
     * Renvoyer joueur courant.
     * @return joueur courant
     */
    public Player getJoueurCourant(){
        return listPlayer.get(round.getWhoIsPlaying());
    }
}

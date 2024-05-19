package com.model;

import com.model.config.Plateau;
import com.model.config.Route;
import com.model.config.Ville;
import com.model.config.carte.CarteManager;
import com.view.GameFrame;
import com.view.GameMapPanel;
import com.view.MapScreen;

import java.awt.*;
import java.util.ArrayList;

/**
 * Classe représentant le jeu.
 */
public class Game {
    private Plateau plateau;
    private ArrayList<Player> listPlayer;
    private ArrayList<Ville> villes;
    private ArrayList<Route> routes;
    private CarteManager cm;
    private Round round;
    private GameFrame gameFrame;

    /**
     * Constructeur de la classe Game.
     *
     * @param gameFrame La fenêtre du jeu.
     */
    public Game(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    /**
     * Initialise et configure une nouvelle partie.
     *
     * @param nomMap         Le nom de la carte.
     * @param player_names   Les noms des joueurs.
     * @param player_types   Les types des joueurs.
     * @param player_colors  Les couleurs des joueurs.
     * @param music          Indique si la musique doit être jouée.
     */
    public void makeGame(String nomMap, String[] player_names, String[] player_types, Color[] player_colors, boolean music) {
        this.cm = new CarteManager(gameFrame.getMode());
        this.plateau = Plateau.makePlateau(nomMap, this);
        this.listPlayer = initPlayers(player_names, player_types, player_colors);
        initBoard();
        this.round = new Round();
        if (music) gameFrame.getSound().playMusic("INGAME", "inGame.wav");
    }

    /**
     * Initialise le plateau de jeu et distribue les cartes aux joueurs.
     */
    private void initBoard() {
        // Fonction qui initialise le jeu
        cm.initPileCarteDestination(this);

        // Donner des cartes aux joueurs au début de la partie (chacun en reçoit 4)
        for (Player player : listPlayer) {
            for (int j = 0; j < 10; j++) {
                player.piocher(cm);
            }
        }
    }

    /**
     * Initialise les joueurs.
     *
     * @param player_names  Les noms des joueurs.
     * @param player_types  Les types des joueurs.
     * @param player_colors Les couleurs des joueurs.
     * @return              La liste des joueurs initialisée.
     */
    private ArrayList<Player> initPlayers(String[] player_names, String[] player_types, Color[] player_colors) {
        ArrayList<Player> playerlist = new ArrayList<>();

        for (int i = 0; i < player_types.length; i++) {
            switch (player_types[i]) {
                case "PLAYER":
                    playerlist.add(new Player(colorToString(player_colors[i]), player_names[i], 0, this));
                    break;
                case "WEAK":
                    playerlist.add(new Player(colorToString(player_colors[i]), player_names[i], 1, this));
                    break;
                case "NORMAL":
                    playerlist.add(new Player(colorToString(player_colors[i]), player_names[i], 2, this));
                    break;
                case "STRONG":
                    playerlist.add(new Player(colorToString(player_colors[i]), player_names[i], 3, this));
                    break;
                default:
                    break;
            }
        }

        return playerlist;
    }

    /**
     * Convertit une couleur en chaîne de caractères.
     *
     * @param c La couleur.
     * @return  La chaîne de caractères représentant la couleur.
     */
    private String colorToString(Color c) {
        if (c.equals(Color.red)) return "ROUGE";
        if (c.equals(Color.blue)) return "BLEU";
        if (c.equals(Color.green)) return "VERT";
        if (c.equals(Color.yellow)) return "JAUNE";
        return "Error";
    }

    /**
     * Vérifie s'il y a un joueur qui a moins de 3 wagons.
     *
     * @return true si le nombre de wagons est inférieur ou égal à 2.
     */
    public boolean endGame() {
        for (Player p : listPlayer) {
            if (p.getNbrWagon() <= 2) {
                return true;
            }
        }
        return cm.trainCardisEmpty();
    }

    /**
     * Met à jour le jeu.
     *
     * @param deltaTime Le temps écoulé depuis la dernière mise à jour.
     */
    public void updateGame(double deltaTime) {
        // game loop
        if (!round.roundFinished()) {
            round.round(this, deltaTime);
        }

        if (endGame() && this.gameFrame.getGameScreen() != null) {
            // DEBUG : System.err.println("la partie est terminée");
            this.gameFrame.getGameScreen().getGameManagerScreen().showEndGame();
        }
    }

    /* Getters et Setters */

    /**
     * Retourne le plateau de jeu.
     *
     * @return Le plateau de jeu.
     */
    public Plateau getPlateau() {
        return plateau;
    }

    /**
     * Retourne la liste des villes.
     *
     * @return La liste des villes.
     */
    public ArrayList<Ville> getVilles() {
        return villes;
    }

    /**
     * Définit la liste des villes.
     *
     * @param villes La nouvelle liste des villes.
     */
    public void setVilles(ArrayList<Ville> villes) {
        this.villes = villes;
    }

    /**
     * Retourne la liste des routes.
     *
     * @return La liste des routes.
     */
    public ArrayList<Route> getRoutes() {
        return routes;
    }

    /**
     * Définit la liste des routes.
     *
     * @param routes La nouvelle liste des routes.
     */
    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }

    /**
     * Retourne la liste des joueurs.
     *
     * @return La liste des joueurs.
     */
    public ArrayList<Player> getListPlayer() {
        return listPlayer;
    }

    /**
     * Retourne la fenêtre du jeu.
     *
     * @return La fenêtre du jeu.
     */
    public GameFrame getGameFrame() {
        return gameFrame;
    }

    /**
     * Retourne l'écran de la carte du jeu.
     *
     * @return L'écran de la carte du jeu.
     */
    public MapScreen getMapScreen() {
        if (gameFrame != null) {
            return gameFrame.getMapScreen();
        }
        return null;
    }

    /**
     * Retourne le panneau de la carte du jeu.
     *
     * @return Le panneau de la carte du jeu.
     */
    public GameMapPanel getGameMapPanel() {
        if (gameFrame != null) {
            return gameFrame.getGameMapPanel();
        }
        return null;
    }

    /**
     * Retourne le gestionnaire de cartes.
     *
     * @return Le gestionnaire de cartes.
     */
    public CarteManager getCarteManager() {
        return this.cm;
    }

    /**
     * Retourne le round en cours.
     *
     * @return Le round en cours.
     */
    public Round getRound() {
        return this.round;
    }

    /**
     * Retourne le joueur courant.
     *
     * @return Le joueur courant.
     */
    public Player getJoueurCourant() {
        return listPlayer.get(round.getWhoIsPlaying());
    }
}

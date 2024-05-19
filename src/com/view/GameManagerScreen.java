package com.view;

import com.model.Game;
import com.model.Player;
import com.model.config.Plateau;
import com.view.endgame.EndGameScreen;

import javax.swing.*;
import java.awt.*;

/**
 * La classe GameManagerScreen gère l'affichage et la gestion des différents écrans de jeu, y compris la carte du jeu, l'écran de pause et l'écran de fin de jeu.
 */
public class GameManagerScreen extends JPanel {
    GameFrame frame ;
    private GameMapPanel gameMapPanel ;
    private Game game ;

    private final CardLayout cardLayout = new CardLayout() ;
    private EndGameScreen endGameScreen ;
    private PauseScreen pauseScreen ;
    private int width , height ;
    private GameScreen gameScreen ;


    /**
     * Constructeur de GameManagerScreen
     * @param frame gameframe
     * @param gameScreen l'ecran
     * @param map le nom de la map
     * @param width width
     * @param height height
     * @param player le joueur
     * @param game le jeu
     */
    public GameManagerScreen ( GameFrame frame , GameScreen gameScreen ,  String map , int width , int height , Player player , Game game ){
        this.frame = frame ;
        this.gameScreen = gameScreen ;
        setSize(width , height );
        this.width = width ;
        this.height = height ;
        this.gameMapPanel = new GameMapPanel(frame , map , width , height  , player , game ) ;
        this.game = this.frame.getMain().getGame();
        setLayout( cardLayout );
        add( frame.getIngame_screen_s() , gameMapPanel ) ;
        cardLayout.show(this, frame.getIngame_screen_s());
        frame.setCurrentCard(frame.getEndgame_screen_s());
    }

    /**
     * Une fonction qui permet de faire la map à partir du plateau
     * @param plateau Plateau
     */
    public void make( Plateau plateau ){
        this.gameMapPanel.make(plateau);
    }

    /**
     * Une fonction qui verfie si la partie est fini et affiche le panel de la fin de jeu
     */
    public void showEndGame(){
        if ( this.game.endGame()){
            if ( this.frame.getSound().getMusic() ) this.frame.getSound().playMusic("END" ,"end.wav");
            this.endGameScreen = new EndGameScreen( gameScreen , width ,height ) ;
            add( frame.getEndgame_screen_s() , endGameScreen ) ;
            cardLayout.show(this , frame.getEndgame_screen_s());
            frame.setCurrentCard(frame.getEndgame_screen_s());
            this.frame.getMain().setRunning( false );
            //DEBUG : System.err.println("la partie est terminée");
        }

        this.gameMapPanel.repaint();
    }

    /**
     * Une fonction qui affiche le panel pause
     */
    public void showPause (){
        this.pauseScreen = new PauseScreen( gameScreen , width , height ) ;
        // DEBUG : System.err.println("Un nouveau pause ") ;
        add( frame.getPause_screen_s() , pauseScreen ) ;
        cardLayout.show(this , frame.getPause_screen_s() );
        frame.setCurrentCard(frame.getPause_screen_s());
        this.gameMapPanel.repaint();
    }

    /**
     * Une fonction qui retire le panneau de pause
     */
    public void removePause() {
        if (pauseScreen != null) {
            remove(pauseScreen);
            // DEBUG : System.err.println("Pause effacer");
            pauseScreen = null;
        }
    }

    /* getteurs et setteurs */

    public GameMapPanel getGameMapPanel() {
        return gameMapPanel;
    }

    public MapScreen getMapScreen()
    {
        if (gameMapPanel != null)
        {
            return gameMapPanel.getMapScreen();
        }

        return null;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

}

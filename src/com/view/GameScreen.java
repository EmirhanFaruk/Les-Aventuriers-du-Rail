package com.view;


import com.model.Game;
import com.model.config.Plateau;
import com.view.graphics.* ;

import javax.swing.*;
import java.awt.*;

public class GameScreen extends JPanel {

    GameFrame frame ;
    GameManagerScreen gameManagerScreen ;
    private Game game ;
    private EndGameScreen endGameScreen ;
    private final String ingame_screen_s = "INGAME" , endgame_sceen = "ENDGAME";
    private boolean playing = true ;

    /**
     * Constructeur de la classe GameScreen
     * @param frame fenetre
     * @param map quelle map
     * @param width width
     * @param height height
     */
    public GameScreen ( GameFrame frame , String map , int width , int height ){
        this.frame = frame ;
        setSize(width , height );
        this.gameManagerScreen = new GameManagerScreen(frame , map , width , height ) ;

        setLayout(new BorderLayout());
        add( gameManagerScreen , BorderLayout.CENTER ) ;
    }

    /**
     * Une fonction qui permet de faire la map à partir du plateau
     * @param plateau Plateau
     */
    public void make( Plateau plateau ){
        this.gameManagerScreen.make(plateau);
    }


    public void update( double deltaTime ){
        if (playing){
            this.game.updateGame( deltaTime ) ;
            if ( this.game.endGame()){
                playing = false ;
                add(endGameScreen , BorderLayout.CENTER) ;
            }
        }
        repaint();
    }
}

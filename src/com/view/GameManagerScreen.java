package com.view;

import com.model.Game;
import com.model.config.Plateau;

import javax.swing.*;
import java.awt.*;

public class GameManagerScreen extends JPanel {
    GameFrame frame ;
    GameMapPanel gameMapPanel ;
    private Game game ;
    private EndGameScreen endGameScreen ;

    private GameScreen gameScreen ;

    public GameManagerScreen ( GameFrame frame , GameScreen gameScreen ,  String map , int width , int height ){
        this.frame = frame ;
        this.gameScreen = gameScreen ;
        setSize(width , height );
        this.gameMapPanel = new GameMapPanel(frame , map , width , height ) ;
        this.game = this.frame.getMain().getGame();
        this.endGameScreen = new EndGameScreen( gameScreen , width ,height ) ;

        setLayout(new CardLayout());
        add( gameMapPanel ) ;
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
    public void update(){
        if ( this.game.endGame()){
            gameMapPanel.add( endGameScreen , BorderLayout.EAST) ;
            this.frame.getMain().setRunning( false );
        }
        repaint();
    }

    /* getteurs et setteurs */
    public GameFrame getFrame() {
        return frame;
    }
}

package com.view;

import com.model.Game;
import com.model.Player;
import com.model.config.Plateau;
import com.model.Game ;
import com.view.PlayerHandPanel ;

import javax.swing.*;
import java.awt.*;

public class GameManagerScreen extends JPanel {
    GameFrame frame ;
    private GameMapPanel gameMapPanel ;
    private Game game ;

    private final CardLayout cardLayout = new CardLayout() ;
    private EndGameScreen endGameScreen ;
    private int width , height ;
    private GameScreen gameScreen ;

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
            this.endGameScreen = new EndGameScreen( gameScreen , width ,height ) ;
            add( frame.getEndgame_screen_s() , endGameScreen ) ;
            cardLayout.show(this , frame.getEndgame_screen_s());
            this.frame.getMain().setRunning( false );
        }
        this.gameMapPanel.repaint();
    }

    /* getteurs et setteurs */
    public GameFrame getFrame() {
        return frame;
    }

    public GameMapPanel getGameMapPanel() {
        return gameMapPanel;
    }

    public EndGameScreen getEndGameScreen() {
        return endGameScreen;
    }


    /*
    public void setPlayerCourant(Player playerCourant) {
		this.playerHandPanel.setPlayer(playerCourant);
	}
	*/
}

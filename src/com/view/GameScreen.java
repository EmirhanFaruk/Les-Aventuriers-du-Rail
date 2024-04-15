package com.view;

import com.model.Player;
import com.model.Game;
import com.model.config.Plateau;
import com.view.graphics.*;
import com.view.mainmenu.Play;

import javax.swing.*;
import java.awt.*;

public class GameScreen extends JPanel {

    private GameFrame frame;
    private GameManagerScreen gameManagerScreen;

    /**
     * Constructor for GameScreen class
     * @param frame window
     * @param map which map
     * @param width width
     * @param height height
     * @param game game
     */
    public GameScreen (GameFrame frame , String map , int width , int height , Player player, Game game ){
        this.frame = frame ;
        setSize(width , height );
        this.gameManagerScreen = new GameManagerScreen(frame , this , map , width , height , player , game ) ;
        setLayout(new BorderLayout());
        add( gameManagerScreen ) ;
    }

    public GameManagerScreen getGameManagerScreen() {
        return gameManagerScreen;
    }

    public MapScreen getMapScreen()
    {
        if (gameManagerScreen != null)
        {
            return gameManagerScreen.getMapScreen();
        }

        return null;
    }

    public GameMapPanel getGameMapPanel()
    {
        if (gameManagerScreen != null)
        {
            return gameManagerScreen.getGameMapPanel();
        }

        return null;
    }

    public GameFrame getFrame() {
        return frame;
    }
    
    public GameManagerScreen getGMScreen() {
    	return this.gameManagerScreen;
    }
}

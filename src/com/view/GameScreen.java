package com.view;

import com.model.Player;
import com.model.config.Plateau;
import com.view.graphics.*;

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
     * @param plateau 
     */
    public GameScreen(GameFrame frame, String map, int width, int height, Player joueur, Plateau plateau) {
        this.frame = frame;
        setSize(width, height);
        this.gameManagerScreen = new GameManagerScreen(frame, map, width, height, joueur, plateau);      

        setLayout(new BorderLayout());
        add(gameManagerScreen);
    }

    /**
     * A method to make the map from the plateau
     * @param plateau Plateau
     */
    public void make(Plateau plateau) {
        this.gameManagerScreen.make(plateau);
    }
    
    public GameManagerScreen getGMScreen() {
    	return this.gameManagerScreen;
    }
}

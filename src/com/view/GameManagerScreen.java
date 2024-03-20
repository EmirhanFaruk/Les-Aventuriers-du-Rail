package com.view;

import com.model.Player;
import com.model.config.Plateau;

import javax.swing.*;
import java.awt.*;

public class GameManagerScreen extends JPanel {
    GameFrame frame ;
    MapScreen mapScreen ;
    private PlayerHandPanel playerHandPanel;
    private static int tile_width , tile_height ;
    private int width , height ;

    /**
     * Constructeur de la classe GameManagerScreen
     * @param frame
     * @param map
     * @param width
     * @param height
     * @param p 
     */
    public GameManagerScreen ( GameFrame frame , String map , int width , int height, Player joueur, Plateau plateau){
        this.frame = frame ;
        setSize(width , height );
        this.height = height ;
        this.width = width ;
        tile_height = getHeight() / 24 ;
        tile_width = getWidth() / 24 ;
        this.mapScreen = new MapScreen(map ,width ,height ,tile_width , tile_height, joueur, plateau) ;
        this.playerHandPanel = new PlayerHandPanel(joueur);
        

        setLayout(new BorderLayout());
        //Split le layout en deux parties : La map au milieu et la main du joueur en bas
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mapScreen, playerHandPanel);
        splitPane.setResizeWeight(0.8); // Donne plus d'espace à la map
        
        // Ajout du splitPane au JFrame
        add(splitPane, BorderLayout.CENTER);

    }

    /**
     * Une fonction qui permet de faire la map à partir du plateau
     * @param plateau Plateau
     */
    public void make( Plateau plateau ){
        mapScreen.makeMap( plateau );
    }
    
    public void setPlayerCourant(Player playerCourant) {
		this.playerHandPanel.setPlayer(playerCourant);
	}
}

package com.view;

import com.model.Player;
import com.model.config.Plateau;

import javax.swing.*;
import java.awt.*;

public class GameMapPanel extends JPanel {
    GameFrame frame ;
    private MapScreen mapScreen ;
    private PlayerHandPanel playerHandPanel ;
    private PiochePanel pioche;
    private static int tile_width , tile_height ;
    private int width , height ;

    /**
     * Constructeur de la classe GameManagerScreen
     * @param frame
     * @param map
     * @param width
     * @param height
     */
    public GameMapPanel (GameFrame frame , String map , int width , int height , Player player , Plateau plateau ){
        this.frame = frame ;
        setSize(width , height );
        this.height = height ;
        this.width = width ;
        tile_height = (int) (getHeight() * 0.8 / 24);
        tile_width = (int) (getWidth() * 0.8 / 24);
        
        this.playerHandPanel = new PlayerHandPanel(player, width, height);
        this.mapScreen = new MapScreen( map , width , height ,tile_width , tile_height  , player , plateau, this.playerHandPanel) ;
        this.playerHandPanel = new PlayerHandPanel(player, width, (int) (height * 0.2)) ;
        this.pioche = new PiochePanel(width, height, player, this.playerHandPanel);
        
        setLayout(new BorderLayout());
        
        add(mapScreen, BorderLayout.CENTER) ;
        add(playerHandPanel , BorderLayout.SOUTH ) ;
        add(pioche, BorderLayout.EAST);
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

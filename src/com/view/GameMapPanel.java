package com.view;

import com.model.Game;
import com.model.Player;
import com.model.config.Plateau;
import com.model.controller.GameController;

import javax.swing.*;
import java.awt.*;

public class GameMapPanel extends JPanel {
    GameFrame frame ;
    private MapScreen mapScreen ;
    private PlayerHandPanel playerHandPanel ;
    private PiochePanel pioche;
    private static int tile_width , tile_height ;
    private int width , height ;
    private GameController gameController = new GameController();

    /**
     * Constructeur de la classe GameManagerScreen
     * @param frame
     * @param map
     * @param width
     * @param height
     */
    public GameMapPanel (GameFrame frame , String map , int width , int height , Player player , Game game ){
        this.frame = frame ;
        setSize(width , height );
        this.height = height ;
        this.width = width ;
        tile_height = (int) (getHeight() * 0.8 / 24);
        tile_width = (int) (getWidth() * 0.8 / 24);

        this.playerHandPanel = new PlayerHandPanel() ;
        this.mapScreen = new MapScreen( map , (int) (width*0.85), (int) (height*0.8),tile_width , tile_height  , player , game, this.playerHandPanel , gameController ) ;
        this.playerHandPanel.make(gameController,game,width,height,mapScreen);
        this.pioche = new PiochePanel(width, height, player, this.playerHandPanel, frame.getMain().game.getCarteManager());
        setLayout(new BorderLayout());

        add( mapScreen , BorderLayout.CENTER ) ;
        add(pioche, BorderLayout.EAST);
        add( playerHandPanel , BorderLayout.SOUTH ) ;


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

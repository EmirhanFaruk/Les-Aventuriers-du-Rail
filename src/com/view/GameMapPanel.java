package com.view;

import com.model.Player;
import com.model.config.Plateau;

import javax.swing.*;
import java.awt.*;

public class GameMapPanel extends JPanel {
    GameFrame frame ;
    MapScreen mapScreen ;
    private static int tile_width , tile_height ;
    private int width , height ;

    /**
     * Constructeur de la classe GameManagerScreen
     * @param frame
     * @param map
     * @param width
     * @param height
     */
    public GameMapPanel ( GameFrame frame , String map , int width , int height){
        this.frame = frame ;
        setSize(width , height );
        this.height = height ;
        this.width = width ;
        tile_height = getHeight() / 24 ;
        tile_width = getWidth() / 24 ;
        this.mapScreen = new MapScreen( map , width , height ,tile_width , tile_height , new Player(), new Plateau()) ;

        setLayout(new BorderLayout());
        add( mapScreen , BorderLayout.CENTER ) ;

    }

    /**
     * Une fonction qui permet de faire la map à partir du plateau
     * @param plateau Plateau
     */
    public void make( Plateau plateau ){
        mapScreen.makeMap( plateau );
    }
}

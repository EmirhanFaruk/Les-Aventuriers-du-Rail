package com.view;


import com.model.config.Plateau;
import com.view.graphics.* ;

import javax.swing.*;
import java.awt.*;

public class GameScreen extends JPanel {

    GameFrame frame ;
    MapScreen mapScreen ;
    private static int tile_width , tile_height ;
    private int tile_offset_width , tile_offset_height ;

    public GameScreen ( GameFrame frame  , int width , int height){
        this.frame = frame ;
        setSize(width , height );
        tile_height = getHeight() / 24 ;
        tile_width = getWidth() / 24 ;
        this.mapScreen = new MapScreen( tile_width , tile_height ) ;

        setLayout(new BorderLayout());
        add( mapScreen , BorderLayout.CENTER ) ;

    }

    public void make( Plateau plateau ){
        mapScreen.makeMap( plateau );
    }
}

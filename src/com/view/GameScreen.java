package com.view;


import com.model.config.Plateau;

import javax.swing.*;

public class GameScreen extends JPanel {

    GameFrame frame ;
    MapScreen mapScreen ;
    private static int tile_width , tile_height ;
    private int tile_offset_width , tile_offset_height ;

    public GameScreen ( GameFrame frame ){
        this.frame = frame ;
        this.mapScreen = new MapScreen() ;
    }

    public void make( Plateau plateau ){
        tile_height = getHeight() / 24 ;
        tile_width = getWidth() / 24 ;

        tile_offset_width = (getWidth() - (tile_width * 24));
        tile_offset_height = (getHeight() - (tile_height * 24));

        mapScreen.makeMap( plateau , tile_width , tile_height );
    }
}

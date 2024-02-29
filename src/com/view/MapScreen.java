package com.view;

import com.model.config.Case;
import com.model.config.Plateau;
import com.view.graphics.* ;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MapScreen extends JPanel {
    ArrayList<MapGraphics> map = new ArrayList<>() ;

    public MapScreen(){

    }


    public void makeMap ( Plateau plateau , int tile_width , int tile_height ){
        map( plateau );
    }

    private void map( Plateau plateau ) {
        Case[][] tab = plateau.getPlateau();
        for ( Case[] cases : tab ) {
            for ( Case c : cases ) {
                map.add( new MapGraphics( c , c.getX() , c.getY() ) );
            }
        }
    }

    protected void paintComponent (Graphics g ){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Everything to draw goes here using g2
        for (MapGraphics m : map)
        {
            m.paint( g2 , m.getaCase() );
        }
        g2.dispose();
    }
}

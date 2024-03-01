package com.view;

import com.model.config.Case;
import com.model.config.Plateau;
import com.view.graphics.* ;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MapScreen extends JPanel {
    ArrayList<MapGraphics> map = new ArrayList<>() ;

    final int width , height ;

    public MapScreen( int width , int height){
        this.width = width ;
        this.height = height ;
    }


    public void makeMap ( Plateau plateau){
        map( plateau );
    }

    private void map( Plateau plateau ) {
        Case[][] tab = plateau.getPlateau();
        for ( Case[] cases : tab ) {
            for ( Case c : cases ) {
                map.add( new MapGraphics( c , width , height ) );
            }
        }
    }

    protected void paintComponent (Graphics g ){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Everything to draw goes here using g2
        for (MapGraphics m : map)
        {
            m.draw( g2 );
        }
        g2.dispose();
    }
}

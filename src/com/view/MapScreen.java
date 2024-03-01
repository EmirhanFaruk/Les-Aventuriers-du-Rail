package com.view;

import com.model.config.Case;
import com.model.config.Plateau;
import com.view.graphics.* ;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MapScreen extends JPanel {
    ArrayList<MapGraphics> map = new ArrayList<>() ;
    final String mapName ;
    final int width , height ;
    final int tileWidth , tileHeight ;

    public MapScreen( String mapName , int width , int height , int tileWidth , int tileHeight ){
        this.mapName = mapName+".png" ;
        this.width = width ;
        this.height = height ;
        this.tileWidth = tileWidth ;
        this.tileHeight = tileHeight ;
    }


    public void makeMap ( Plateau plateau ){

        map( plateau );
        RailGraphics.setWH(tileWidth, tileHeight);
        VilleGraphics.setWH(tileWidth, tileHeight);
    }

    private void map( Plateau plateau ) {
        Case[][] tab = plateau.getPlateau();
        for ( Case[] cases : tab ) {
            for ( Case c : cases ) {
                map.add( new MapGraphics( c , tileWidth , tileHeight ) );
            }
        }
    }

    protected void paintComponent (Graphics g ){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g.drawImage( MapGraphics.backgroundImage( mapName ), 0 ,0 , width , height , null ) ;

        // Everything to draw goes here using g2
        for (MapGraphics m : map)
        {
            m.draw( g2 );
        }
        MapGraphics.getCmp();
        g2.dispose();
    }
}

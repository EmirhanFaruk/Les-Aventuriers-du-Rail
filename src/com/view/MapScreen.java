package com.view;

import com.model.Game;
import com.model.Player;
import com.model.Round;
import com.model.config.Case;
import com.model.config.Plateau;
import com.model.controller.GameController;
import com.view.graphics.* ;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MapScreen extends JPanel {
    ArrayList<MapGraphics> map = new ArrayList<>() ;
    final String mapName ;
    final int width , height ;
    final int tileWidth , tileHeight ;
    private Player player ;
    private Plateau plateau ;
    GameController gameController ;

    /**
     * Constructeur de MapScreen
     * @param mapName nom de la map
     * @param width width du panel
     * @param height height du panel
     * @param tileWidth width de l'image
     * @param tileHeight height de l'image
     * @param playerHandPanel 
     */
    public MapScreen(String mapName , int width , int height , int tileWidth , int tileHeight,
                     Player joueur, Game game, PlayerHandPanel playerHandPanel , GameController gameController){
        this.mapName = mapName+".png" ;
        this.width = width ;
        this.height = height ;
        this.tileWidth = tileWidth ;
        this.tileHeight = tileHeight ;
        this.player = joueur ;
        this.gameController = gameController ;

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	gameController.mouseClicked(e, tileWidth, tileHeight, game, player );
            	repaintAll(playerHandPanel);
            }
        });
    }

    /**
     * Une fonciton qui crée la map
     * @param plateau
     */
    public void makeMap ( Plateau plateau ){
        map( plateau );
        RailGraphics.setWH(tileWidth , tileHeight ) ;
        VilleGraphics.setWH(tileWidth , tileHeight ) ;
        TrainGraphics.setWH(tileWidth , tileHeight ) ;
    }

    /**
     * Une fonction qui ajoute dans l'attribut map les Mapgraphics de chaque case
     * @param plateau Plateau
     */
    private void map( Plateau plateau ) {
        Case[][] tab = plateau.getPlateau();
        for ( Case[] cases : tab ) {
            for ( Case c : cases ) {
                map.add( new MapGraphics(  plateau , c , tileWidth , tileHeight ) );
            }
        }
    }
    
    public void repaintAll(PlayerHandPanel php) {
        php.getDrawPlayerHand().repaint();
    	php.repaint();
    	this.repaint();
    }

    /**
     * Une fonction affcihe les element de case
     * @param g the <code>Graphics</code> object to protect
     */
    protected void paintComponent (Graphics g ){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g.drawImage( MapGraphics.backgroundImage( mapName ), 0 ,0 , width , height , null ) ;

        // Everything to draw goes here using g2
        for (MapGraphics m : map)
        {
            m.draw( g2 );
        }
        g2.dispose();
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}

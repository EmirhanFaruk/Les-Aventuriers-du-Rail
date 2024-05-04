package com.view;

import com.model.Game;
import com.model.Player;
import com.model.config.Case;
import com.model.config.Plateau;
import com.model.controller.GameController;
import com.view.graphics.* ;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class MapScreen extends JPanel {
    ArrayList<MapGraphics> map = new ArrayList<>();
    final String mapName;
    final int width, height;
    final int tileWidth, tileHeight;
    private Player player;
    GameController gameController;
    private Game game ;
    private PlayerHandPanel playerHandPanel ;
    private double scale = 1.0;
    private double zoomSpeed = 0.1;
    private int mouseX, mouseY;
    private int baseWidth, baseHeight;


    /**
     * Constructeur de MapScreen
     * @param mapName nom de la map
     * @param width width du panel
     * @param height height du panel
     * @param tileWidth width de l'image
     * @param tileHeight height de l'image
     * @param playerHandPanel la main du joueur
     */
    public MapScreen(String mapName, int width, int height, int tileWidth, int tileHeight,
                     Player joueur, Game game, PlayerHandPanel playerHandPanel, GameController gameController) {
        this.mapName = mapName + ".png";
        this.width = width;
        this.height = height;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.player = joueur;
        this.gameController = gameController;
        this.game = game ;
        this.playerHandPanel = playerHandPanel ;
        this.baseWidth = width;
        this.baseHeight = height;

        mouseListener();
        mouseWheelListener() ;
    }

    /**
     * Une fonction qui crée la map
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

    /**
     * Une fonction qui permet au panel d'avoir un mouse listener
     */
    private void mouseListener( ) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gameController.mouseClicked(e, tileWidth, tileHeight, game, player);
                repaintAll(playerHandPanel);
            }
        });
    }

    /**
     * Une fonction qui permet au panel d'avoir un zoom et un dézoom
     */
    private void mouseWheelListener( ) {
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();

                int notches = e.getWheelRotation();
                if (notches < 0) {
                    zoomIn();
                } else {
                    if (getWidth() * scale > baseWidth && getHeight() * scale > baseHeight) {
                        zoomOut();
                    }
                }

                repaint();
            }
        });
    }

    /**
     * Une fonction zoom
     */
    private void zoomIn() {
        scale += zoomSpeed;
    }

    /**
     * Une fonction dézoom
     */
    private void zoomOut() {
        scale -= zoomSpeed;
        scale = Math.max(0.1, scale);
    }

    /**
     * Une fonction qui repaint tout
     * @param php PlayerHandPanel
     */
    public void repaintAll(PlayerHandPanel php) {
    	php.repaint();
    	this.repaint();
    }

    /**
     * Une fonction affiche les element de case
     * @param g the <code>Graphics</code> object to protect
     */
    protected void paintComponent (Graphics g ){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Permet de dessiner avec les zooms et dézooms
        AffineTransform tx = new AffineTransform();
        tx.translate(mouseX, mouseY);
        tx.scale(scale, scale);
        tx.translate(-mouseX, -mouseY);
        g2.transform(tx);

        g.drawImage( MapGraphics.backgroundImage( mapName ), 0 ,0 , width , height , null ) ;
        // Everything to draw goes here using g2
        for (MapGraphics m : map)
        {
            m.draw( g2 );
        }

        // Draw ville names
        for (MapGraphics m : map)
        {
            drawVilleNames(g2, m);
        }
        g2.dispose();
    }

    private void drawVilleNames(Graphics2D g2, MapGraphics m)
    {
        if (m.isVille())
        {
            m.drawVilleNames(g2);
        }
    }

    /* getters et setters */
    public void setPlayer(Player player) {
        this.player = player;
    }
}

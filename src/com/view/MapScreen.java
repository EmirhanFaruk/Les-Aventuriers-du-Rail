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
    private double zoomSpeed = 0.5 ;
    private double zoomSpeedMax ;
    private int mouseX, mouseY;
    private int mapOffsetX = 0;
    private int mapOffsetY = 0;
    private int lastMouseX, lastMouseY;
    private boolean zoomed = false;
    private boolean isDragging = false;
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
        this.zoomSpeedMax = zoomMax() ;
        mouseListener();
        mouseMotionListener();
        mouseWheelListener() ;
    }

    /**
     * Une fonction qui crée la map
     * @param plateau Plateau
     */
    public void makeMap ( Plateau plateau ){
        map( plateau );
        RailGraphics.setWH(tileWidth , tileHeight ) ;
        VilleGraphics.setWH(tileWidth , tileHeight ) ;
        TrainGraphics.setWH(tileWidth , tileHeight ) ;
    }

    /**
     * Une fonction qui ajoute dans l'attribut map les Map graphics de chaque case
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
                gameController.mouseClicked(e, tileWidth, tileHeight, game, player , zoomed );
                repaintAll(playerHandPanel);
            }
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                isDragging = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isDragging = false;
                // Enregistrer la dernière position de la souris lorsque le bouton est relâché
                lastMouseX = mouseX;
                lastMouseY = mouseY;
            }
        });
    }

    /**
     * Une fonction qui permet au panel d'avoir un zoom et un dézoome
     */
    private void mouseWheelListener( ) {
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int previousMouseX = mouseX;
                int previousMouseY = mouseY;

                mouseX = e.getX();
                mouseY = e.getY();

                int notches = e.getWheelRotation();
                if (notches < 0 ) {
                    zoomIn(previousMouseX, previousMouseY); // Passer les anciennes coordonnées de la souris
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
     * Une fonction qui ajoute une motion au panel
     */
    private void mouseMotionListener () {
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging) {
                    int newMouseX = e.getX();
                    int newMouseY = e.getY();
                    int deltaX = newMouseX + mouseX;
                    int deltaY = newMouseY + mouseY;
                    mouseX = newMouseX;
                    mouseY = newMouseY;

                    // Déplacer la carte horizontalement et verticalement en fonction du mouvement de la souris
                    mapOffsetX -= deltaX;
                    mapOffsetY -= deltaY;
                    repaint();
                }
            }
        });
    }

    /**
     * Une fonction zoom
     */
    private void zoomIn(int zoomX, int zoomY) { // Prendre les coordonnées de la souris pour zoomer
        if ( scale < zoomSpeedMax ) {
            scale += zoomSpeed;
            mapOffsetX += (int) (zoomX / scale - zoomX / (scale - zoomSpeed));
            mapOffsetY += (int) (zoomY / scale - zoomY / (scale - zoomSpeed));
            zoomed = true;
        } else {
            mouseX = zoomX ;
            mouseY = zoomY ;
            JOptionPane.showMessageDialog(new JFrame(),
                    "Le zoom est maximal.","Instructions",JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Une fonction dézoome
     */
    private void zoomOut() {
        if ( zoomed ) {
            scale -= zoomSpeed;
            scale = Math.max(0.1, scale);
            if (scale <= 1.0) {
                scale = 1.0;
                mapOffsetX = 0;
                mapOffsetY = 0;
                zoomed = false;
            }
        }
    }

    /**
     * Une fonction qui donne le zoom max de chaque map
     * @return zoom max
     */
    private int zoomMax (){
        String map  = game.getGameFrame().getMain().getMap() ;
        if ( map.equals("LongMap") ) {
            return 6 ;
        } else if ( map.equals("NormalMap") ) {
            return 4 ;
        } else if ( map.equals("QuickMap") ) {
            return 2 ;
        }
        return 0 ;
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

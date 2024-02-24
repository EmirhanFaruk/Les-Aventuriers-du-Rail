package com.view.graphics;

import com.model.config.Rail;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RailGraphics {
    private static final String path = System.getProperty("user.dir");
    private static final String s = findSlash(path);
    private static final BufferedImage RailBlue = loadImage( "RailBleu.png" ) ;
    private static final BufferedImage RailBrown = loadImage( "RailMarron.png" ) ;
    private static final BufferedImage RailDark = loadImage( "RailNoir.png" ) ;
    private static final BufferedImage RailGreen = loadImage( "RailVert.png" ) ;
    private static final BufferedImage RailRed = loadImage( "RailRouge.png" ) ;
    private static final BufferedImage RailViolet = loadImage( "RailViolet.png" ) ;
    private static final BufferedImage RailWhite = loadImage( "RailBlanc.png" ) ;
    private static final BufferedImage RailYellow = loadImage( "RailJaune.png" ) ;
    private static final BufferedImage RailJoker = loadImage( "RailLRainbow.png") ;
    private static final BufferedImage RailJokerEtoilee = loadImage("RailLEtoile.png" ) ;

    private static int width , height ;

    public RailGraphics( ) {
    }


    /**
     * Une fonction qui renvoie une image
     * @param fileName String
     * @return bufferedImage
     */
    private static BufferedImage loadImage(String fileName) {
        try {
            String imagePath = path + s + "resources" + s + "Rail" + s + fileName;
            return ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Une fonction qui donne le bon slash
     * @param p String
     * @return String
     */
    private static String findSlash(String p) {
        for (int i = 0; i < p.length(); i++) {
            switch (p.charAt(i)) {
                case '/':
                    return "/";
                case '\\':
                    return "\\";
            }
        }
        return "/";
    }

    /**
     * TODO : faire une fonction qui puisse orienter les rails en fonction du chemin entre les villes
     *  et mettre en lien avec getImage
     */

    /**
     * Renvoie la bonne image
     * @param rail Rail
     * @return bufferedImage
     */
    public static BufferedImage getImage(Rail rail) {
        switch ( rail.getInitialContent()){
            case BLEU -> {
                return RailBlue ;
            }
            case NOIR -> {
                return RailDark ;
            }
            case VERT -> {
                return RailGreen ;
            }
            case JAUNE -> {
                return RailYellow ;
            }
            case ROUGE -> {
                return RailRed ;
            }
            case MARRON -> {
                return RailBrown ;
            }
            case VIOLET -> {
                return RailViolet ;
            }
            case BLANC -> {
                return RailWhite ;
            }
            case JOKER -> {
                return RailJoker ;
            }
            case JOKERETOILEE -> {
                return RailJokerEtoilee ;
            }
        }
        return null ;
    }

    /**
     * Affiche l'image
     * @param g Graphics
     * @param rail Rail
     */
    public static void paint (Graphics2D g , Rail rail ){
        BufferedImage image = getImage(rail) ;
        g.drawImage( image , rail.getX() , rail.getY() , width , height , null ) ;
    }

    /*
    getters et setters
     */
    public static void setWH(int w, int h)
    {
        width = w;
        height = h;
    }

}

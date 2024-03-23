package com.view.graphics;

import com.model.config.* ;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MapGraphics {
    private Case aCase ;
    final int tileWidth , tileHeight ;
    private Plateau plateau ;
    private static final String path = System.getProperty("user.dir");
    private static final String s = findSlash(path);

    /**
     * Constructeur de la classe MapGraphics
     * @param c une case
     * @param tileWidth width de l'image
     * @param tileHeight height de l'image
     */
    public MapGraphics(  Plateau plateau, Case c , int tileWidth , int tileHeight ){
        this.plateau = plateau ;
        this.aCase = c ;
        this.tileWidth = tileWidth ;
        this.tileHeight = tileHeight ;
    }


    /**
     * Une fonction qui renvoie une image
     * @param fileName String
     * @return bufferedImage
     */
    private static BufferedImage loadImage(String fileName) {
        try {
            String imagePath = path + s + "ressources" + s + "Map" + s + fileName;
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
     * Unr focntion qui permet de savoir sur quoi on joue
     * @param mapName
     * @return
     */
    public static BufferedImage backgroundImage ( String mapName ){
        return loadImage(mapName ) ;
    }

    /**
     * Une fonction qui affiche la bonne image
     * @param g graphics
     */
    public void draw(Graphics2D g) {
    	if(aCase != null) {
    		if (aCase instanceof Ville ) {
    			VilleGraphics.paint( g , ( Ville ) aCase );
    		} else if (aCase instanceof Rail ) {
    			if(((Rail) aCase).getOccuper() == true) {
    				TrainGraphics.paint(g, (Rail) aCase);
    			}else {
    				RailGraphics.paint( g, (Rail) aCase );
    			}
    		}
    	}
    }


    /*
   getteurs et setteurs
    */
    public Case getaCase() {
        return aCase;
    }

    public int getHeight() {
        return tileHeight;
    }

    public int getWidth() {
        return tileWidth;
    }
}

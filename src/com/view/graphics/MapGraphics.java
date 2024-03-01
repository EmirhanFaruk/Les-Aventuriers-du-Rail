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


    private static final String path = System.getProperty("user.dir");
    private static final String s = findSlash(path);

    public MapGraphics( Case c , int tileWidth , int tileHeight ){
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

    public static BufferedImage backgroundImage ( String mapName ){
        return loadImage(mapName ) ;
    }


    public void draw(Graphics2D g) {
        if (aCase instanceof Ville ville) {
            VilleGraphics.setWH(tileWidth, tileHeight);
            VilleGraphics.paint( g , ville );
            System.err.println("La ville est dessiner");
        } else if (aCase instanceof Rail rail) {
            RailGraphics.setWH(tileWidth, tileHeight);
            RailGraphics.paint( g, rail );
            System.err.println("La rail est dessiner");
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

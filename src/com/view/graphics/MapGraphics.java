package com.view.graphics;

import com.model.config.* ;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MapGraphics {
    private Case aCase ;
    int width , height ;

    private static final String path = System.getProperty("user.dir");
    private static final String s = findSlash(path);
    private final BufferedImage backGround = loadImage("") ;

    public MapGraphics( Case c , int width , int height ){
        this.aCase = c ;
        this.width = width ;
        this.height = height ;
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



    public void draw(Graphics2D g) {
        g.drawImage( backGround , 0 ,0 , getWidth() , getHeight() , null ) ;
        if (aCase instanceof Ville ville) {
            VilleGraphics.setWH(width, height);
            VilleGraphics.paint( g , ville );
            System.err.println("La ville est dessiner");
        } else if (aCase instanceof Rail rail) {
            RailGraphics.setWH(width, height);
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
        return height;
    }

    public int getWidth() {
        return width;
    }
}

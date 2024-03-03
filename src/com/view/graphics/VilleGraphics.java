package com.view.graphics;

import com.model.config.Ville;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class VilleGraphics {
    private static final String path = System.getProperty("user.dir");
    private static final String s = findSlash(path);
    private static final BufferedImage villeImage= loadImage( "Ville.png" ) ;
    private static final BufferedImage gareImage = loadImage( "Gare.png" ) ;
    private static int width , height ;


    public VilleGraphics(){
    }

    /**
     * Une fonction qui renvoie une image
     * @param fileName String
     * @return bufferedImage
     */
    private static BufferedImage loadImage(String fileName) {
        try {
            String imagePath = path + s + "ressources" + s + "Batiment" + s + fileName;
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
     * Renvoie la bonne image
     * @param ville Ville
     * @return bufferedImage
     */
    public static BufferedImage getImage( Ville ville ){
        if (ville.estUneCaseGare()){
            return gareImage ;
        } else {
            return villeImage ;
        }
    }

    /**
     * Affiche l'image
     * @param g graphics2D
     * @param ville Ville
     */
    public static void paint (Graphics2D g  , Ville ville ){
        BufferedImage image = getImage( ville ) ;
        g.drawImage( image , ville.getX() * width , ville.getY() * height ,  width , height , null) ;
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

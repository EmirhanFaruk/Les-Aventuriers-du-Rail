package com.view.graphics;

import com.model.Player;
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
    private static final BufferedImage gareBleu = loadImage( "GareBleu.png" ) ;
    private static final BufferedImage gareJaune = loadImage( "GareJaune.png" ) ;
    private static final BufferedImage gareRouge = loadImage( "GareRouge.png" ) ;
    private static final BufferedImage gareVert = loadImage( "GareVert.png" ) ;
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
            String imagePath =null ;
            if (findColor(fileName)) {
                imagePath = path + s + "ressources" + s + "Batiment" + s + "Gare" +  s+ fileName;
            } else {
                imagePath = path + s + "ressources" + s + "Batiment" + s + fileName;
            }
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
     * Cherche si dans le string possede une couleur
     * @param s nom du fichier
     * @return true si il y a une couleur sinon non
     */
    private static boolean findColor ( String s ) {
        return s.contains("Jaune") || ( s.contains("Bleu")) ||( s.contains("Noir"))||  ( s.contains("Rouge")) || (s.contains("Vert")) ;
    }

    /**
     * Renvoie la bonne image
     * @param ville Ville
     * @return bufferedImage
     */
    public static BufferedImage getImage(Ville ville ){
        if (ville.estUneCaseGare() && ville.getIsOccuped() != null ){
            switch (ville.getIsOccuped().getPlayerCouleur()){
                case "JAUNE" : return gareJaune ;
                case "ROUGE" : return gareRouge ;
                case "BLEU" : return gareBleu ;
                case "VERT" : return gareVert ;
            }
        } else {
            return villeImage ;
        }
        return null ;
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

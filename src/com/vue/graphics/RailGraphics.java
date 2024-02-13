package com.vue.graphics;

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

    private static int width , height ;

    public RailGraphics( ) {
    }


    /**
     * Une fonction qui renvoie une image
     * @param fileName
     * @return bufferedImage
     */
    private static BufferedImage loadImage(String fileName) {
        try {
            String imagePath = path + s + "resources" + s + "images" + s + "Monster" + s + fileName;
            return ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Une fonction qui donne le bon slash
     * @param p
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
     * TODO : faire la fonction getImage quand on aura la classe Carte ( ou Couleur )  finit
     * Donne la bonne image
     * @return bufferedImage
     */
    public static BufferedImage getImage( ){
        return null ;
    }

    public static void paint (Graphics2D g ){
        BufferedImage image = getImage() ;
        g.drawImage( image , width , height , null) ;
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

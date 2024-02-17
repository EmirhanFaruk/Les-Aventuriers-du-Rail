package com.vue.graphics;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CardGraphics {
    private static final String path = System.getProperty("user.dir");
    private static final String s = findSlash(path);
    private static final BufferedImage CardObjectif = loadImage( "OjectifCard.png" ) ;
    private static final BufferedImage CardBack= loadImage( "CardWagonBack.png" ) ;
    private static final BufferedImage CardLocomotive = loadImage( "CardWagonLocomotive.png" ) ;
    private static final BufferedImage CardBlue = loadImage( "CardWagonBlue.png" ) ;
    private static final BufferedImage CardBrown = loadImage( "CardWagonBrown.png" ) ;
    private static final BufferedImage CardDark = loadImage( "CardWagonDark.png" ) ;
    private static final BufferedImage CardGreen = loadImage( "CardWagonGreen.png" ) ;
    private static final BufferedImage CardRed = loadImage( "CardWagonRed.png" ) ;
    private static final BufferedImage CardViolet = loadImage( "CardWagonViolet.png" ) ;
    private static final BufferedImage CardWhite = loadImage( "CardWagonWhite.png" ) ;
    private static final BufferedImage CardYellow = loadImage( "CardWagonYellow.png" ) ;

    private static int width , height ;

    public CardGraphics( ) {
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

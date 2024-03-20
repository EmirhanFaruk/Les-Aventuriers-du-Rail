package com.view.graphics;


import com.model.config.carte.CarteWagon;

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
    private static final BufferedImage CardLocomotive = loadImage( "CardLocomotive.png" ) ;
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
     * @param fileName String
     * @return bufferedImage
     */
    private static BufferedImage loadImage(String fileName) {
        try {
            String imagePath = path + s + "ressources" + s + "Card" + s + fileName;
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
     * @param carteWagon CarteWagon
     * @return bufferedImage
     */
    public static BufferedImage getImage(CarteWagon carteWagon){
        switch ( carteWagon.getInitialCouleur()){
            case BLANC:
                return CardWhite;
            case VIOLET:
                return CardViolet;

            case MARRON:
                return CardBrown;

            case NOIRE:
                return CardDark;

            case JAUNE:
                return CardYellow;

            case VERT:
                return CardGreen;

            case LOC:
                return CardLocomotive;

            case BLEU:
                return CardBlue;

            case ROUGE:
                return CardRed;

        }
        return null ;
    }

    /**
     * Affiche l'image
     * @param g graphics2D
     * @param carteWagon CarteWagon
     */
    public static void paint (Graphics2D g  , CarteWagon carteWagon){
        BufferedImage image = getImage( carteWagon ) ;
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

package com.view.graphics;

import com.model.config.Case;
import com.model.config.Rail;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RailGraphics {
    private static final String path = System.getProperty("user.dir");
    private static final String s = findSlash(path);
    private static final BufferedImage[] RailBlue = createListImage( "RailBleu.png" ) ;
    private static final BufferedImage[] RailBrown = createListImage( "RailMarron.png" ) ;
    private static final BufferedImage[] RailDark = createListImage( "RailNoir.png" ) ;
    private static final BufferedImage[] RailGreen = createListImage( "RailVert.png" ) ;
    private static final BufferedImage[] RailRed = createListImage( "RailRouge.png" ) ;
    private static final BufferedImage[] RailViolet =createListImage( "RailViolet.png" ) ;
    private static final BufferedImage[] RailWhite = createListImage( "RailBlanc.png" ) ;
    private static final BufferedImage[] RailYellow = createListImage( "RailJaune.png" ) ;
    private static final BufferedImage[] RailJoker = createListImage( "RailLRainbow.png") ;
    private static final BufferedImage[] RailJokerEtoilee =createListImage("RailLEtoile.png" ) ;
    private static int[] angle = { 0 , 45 , 90 , 135 } ;
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
            String imagePath = path + s + "ressources" + s + "Rail" + s + fileName;
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
     * Une fonction qui permet de faire la rotation de l'image
     * @param image image
     * @return bufferedImage
     */
    public static BufferedImage putRotation ( BufferedImage image , int angle ) {
        AffineTransform transform = new AffineTransform() ;
        transform.rotate( Math.toRadians(angle) , (double) image.getWidth() / 2, (double) image.getHeight() / 2 );
        AffineTransformOp transformOp = new AffineTransformOp(transform , AffineTransformOp.TYPE_BILINEAR) ;
        image = transformOp.filter( image , null ) ;
        return image ;
    }

    /**
     * Une fonction qui crée une liste de BufferedImage
     * @param s string
     * @return BufferedImage[]
     */
    public static BufferedImage[] createListImage(String s) {
        if (angle == null) {
            angle = new int[]{0, 45, 90, 135};
        }
        BufferedImage[] list = new BufferedImage[4];
        BufferedImage image = loadImage(s);
        if (image != null) {
            list[0] = image;
            for (int i = 1 ; i < list.length; i++) {
                BufferedImage rotatedImage = putRotation(image, angle[i]);
                if (rotatedImage != null) {
                    list[i] = rotatedImage;
                }
            }
        }
        return list;
    }

    /**
     * UNe fonction qui me donne l'index de l'élément de ma liste angle
     * @param a Integer
     * @return int
     */
    public static int indexOf ( int a ){
        for ( int i =0 ; i < angle.length ; i++){
            if ( angle[i] == a ) return i ;
        }
        return -1 ;
    }

    /**
     * Renvoie la bonne image
     * @param rail Rail
     * @return bufferedImage
     */
    public static BufferedImage getImage(Rail rail) {
        int index = indexOf( rail.getAngle() ) ;
        switch ( rail.getInitialContent()){
            case BLEU:
                return RailBlue[index] ;

            case NOIR:
                return RailDark[index] ;

            case VERT:
                return RailGreen[index] ;

            case JAUNE:
                return RailYellow[index] ;

            case ROUGE:
                return RailRed[index] ;

            case MARRON:
                return RailBrown[index] ;

            case VIOLET:
                return RailViolet[index] ;

            case BLANC:
                return RailWhite[index] ;

            case JOKER:
                return RailJoker[index] ;

            case JOKERETOILEE:
                return RailJokerEtoilee[index] ;

        }
        return null ;
    }

    /**
     * Affiche l'image
     * @param g Graphics
     * @param rail Rail
     */
    public static void paint(Graphics2D g, Rail rail){
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

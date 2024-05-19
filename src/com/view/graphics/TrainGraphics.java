package com.view.graphics;

import com.model.config.Rail;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * La classe TrainGraphics gère l'affichage graphique des trains sur les rails.
 */
public class TrainGraphics {
    private static final String path = System.getProperty("user.dir");
    private static final String s = findSlash(path);
    private static final BufferedImage[] TrainYellow = createListImage( "TrainJauneCorp.png" ) ;
    private static final BufferedImage[] TrainlRed = createListImage( "TrainRougeCorp.png" ) ;
    private static final BufferedImage[] TrainDark = createListImage( "TrainNoirCorp.png" ) ;
    private static final BufferedImage[] TrainBlue = createListImage( "TrainBleuCorp.png" ) ;
    private static final BufferedImage[] TrainGreen = createListImage( "TrainVertCorp.png" ) ;
    private static int[] angle = { 90 , 45 , 0 , 135 } ;
    private static int width , height ;


    /**
     * Une fonction qui renvoie une image
     * @param fileName String
     * @return bufferedImage
     */
    private static BufferedImage loadImage(String fileName) {
        try {
            String imagePath = path + s + "ressources" + s + "Train" + s + findColor(fileName) + s + fileName;
            return ImageIO.read(new File(imagePath));
        } catch (Exception ignored) {
            // DEBUG : System.out.println( "Pas d'image.  ° _ ° " );
            return null;
        }
    }

    /**
     * Cherche si dans le string possede une couleur
     * @param s nom du fichier
     * @return true si il y a une couleur sinon non
     */
    private static String findColor ( String s ) {
        if ( s.contains("Jaune")) return "Jaune" ;
        else if ( s.contains("Bleu")) return "Bleu" ;
        else if ( s.contains("Noir")) return "Noir" ;
        else if ( s.contains("Rouge")) return "Rouge" ;
        else return "Vert" ;
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
        int newWidth = image.getHeight() ;
        int newHeight = image.getWidth() ;
        double radians = Math.toRadians(angle);

        Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = rotatedImage.createGraphics();
        g2d.translate((newWidth - image.getWidth()) / 2, (newHeight - image.getHeight()) / 2);
        g2d.rotate(radians, (double) newWidth / 2, (double) newHeight / 2);
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        return rotatedImage;
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
            for (int i = 0 ; i < list.length; i++) {
                BufferedImage rotatedImage = putRotation(image, angle[i]);
                list[i] = rotatedImage;
            }
        }
        return list;
    }

    /**
     * UNe fonction qui me donne l'index de l'élément de ma liste angle
     * @param a Integer qui est un angle
     * @return index
     */
    public static int indexOf ( int a ){
        for ( int i = 0 ; i < angle.length ; i++){
            if ( angle[i] == a ) return i ;
        }
        return 0 ;
    }

    /**
     * Renvoie la bonne image
     * @param rail Rail
     * @return bufferedImage
     */
    public static BufferedImage getImage( Rail rail ){
        switch ( rail.getOccuperPar().getPlayerCouleur()){
            case "BLEU" : return TrainBlue[indexOf(rail.getAngle())] ;
            case "VERT" : return TrainGreen[indexOf(rail.getAngle())] ;
            case "JAUNE" : return TrainYellow[indexOf(rail.getAngle())] ;
            case "ROUGE" : return TrainlRed[indexOf(rail.getAngle())] ;
            case "NOIR" : return TrainDark[indexOf(rail.getAngle())] ;
        }
        return null ;
    }

    /**
     * Affiche l'image du train
     * @param g Graphics
     * @param rail Rail
     */
    public static void paint (Graphics2D g , Rail rail , int width, int height ){
        BufferedImage image = getImage(rail) ;
        g.drawImage( image , rail.getX() * width , rail.getY() * height , width , height , null ) ;
    }

    /*    getters et setters     */

    public static void setWH(int w, int h) {
        width = w;
        height = h;
    }

}

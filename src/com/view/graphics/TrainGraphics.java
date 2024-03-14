package com.view.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TrainGraphics {
    private static final String path = System.getProperty("user.dir");
    private static final String s = findSlash(path);
    private static int width , height ;

    /**
     * TODO : ajouter les variables
     */

    /**
     * Une fonction qui renvoie une image
     * @param fileName String
     * @return bufferedImage
     */
    private static BufferedImage loadImage(String fileName) {
        try {
            String imagePath = path + s + "ressources" + s + "Train" + s + findColor(fileName) + s + fileName;
            return ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
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
     * TODO : faire une fonction qui  qui puisse orienter les wagons en fonction du chemin entre les villes
     *  et mettre en lien avec la fonction getImage()
     */

    /*
    getters et setters
     */
    public static void setWH(int w, int h)
    {
        width = w;
        height = h;
    }

}

package com.view.graphics;

import com.model.config.* ;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class MapGraphics {
    private Case aCase ;
    int tileWidth;
    int tileHeight ;
    private Plateau plateau ;
    private static final String path = System.getProperty("user.dir");
    private static final String s = findSlash(path);

    /**
     * Constructeur de la classe MapGraphics
     * @param c une case
     * @param tileWidth width de l'image
     * @param tileHeight height de l'image
     */
    public MapGraphics(  Plateau plateau, Case c , int tileWidth , int tileHeight ){
        this.plateau = plateau ;
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
        } catch (Exception ignored) {
            // DEBUG : System.out.println( "Pas d'image.  ° _ ° " );
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
     * Unr focntion qui permet de savoir sur quoi on joue
     * @param mapName nom de la map
     * @return l'image de fond
     */
    public static BufferedImage backgroundImage ( String mapName ){
        return loadImage(mapName+".png" ) ;
    }

    /**
     * Une fonction qui affiche la bonne image
     * @param g graphics
     */
    public void draw(Graphics2D g) {
        if (aCase != null) {
            if (aCase instanceof Ville) {
                VilleGraphics.paint(g, (Ville) aCase, tileWidth, tileHeight);
            } else if (aCase instanceof Rail) {
                if (((Rail) aCase).getOccuper()) {
                    TrainGraphics.paint(g, (Rail) aCase, tileWidth, tileHeight);
                } else {
                    RailGraphics.paint(g, (Rail) aCase, tileWidth, tileHeight);
                }
            }
        }
    }


    public void drawVilleNames(Graphics2D g)
    {
        if(aCase != null)
        {
            if (aCase instanceof Ville)
            {
                Ville ville = (Ville) aCase;
                Rectangle2D r = getNameSize(g, ville);
                RectangleGraphics rectangle = new RectangleGraphics(r);
                rectangle.paint(g);
                putsNameVille(g, ville);
            }
        }
    }

    public void putsNameVille( Graphics2D g , Ville ville){

        int x = ville.getX() * tileWidth ;
        int y = ville.getY() * tileHeight ;

        Rectangle2D r = getNameSize(g, ville);

        // Calcul des coordonnées de texte
        x = ( x + (tileWidth / 2) ) - ( (int) (r.getWidth() / 2) );
        y = y - ( (int) (r.getHeight() / 2) );

        g.setColor(Color.BLACK);
        // Affichage de texte
        g.drawString(ville.getNom(), x, y);

    }


    public Rectangle2D getNameSize(Graphics2D g, Ville ville)
    {
        int fontSize = tileHeight/2; // Calculating the size of the font
        Font f = new Font(Font.SERIF, Font.BOLD, fontSize);
        g.setFont(f);

        String nom = ville.getNom() ;
        Rectangle2D rectangle = g.getFontMetrics().getStringBounds(nom, g); // Calcul de size de texte au total

        int x = ville.getX() * tileWidth ;
        int y = ville.getY() * tileHeight ;
        // Calcul des coordonnées de texte
        x = ( x + (tileWidth / 2) ) - ( (int) (rectangle.getWidth() / 2) );
        y = y - ( (int) (rectangle.getHeight()) );

        Rectangle2D res = new Rectangle2D.Double(x , y , rectangle.getWidth() , rectangle.getHeight());

        return res;
    }

    /* getteurs et setteurs */
    public void setTileSize(int tileWidth, int tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public int getHeight() {
        return tileHeight;
    }

    public int getWidth() {
        return tileWidth;
    }

    public boolean isVille() { return aCase instanceof Ville; }

}

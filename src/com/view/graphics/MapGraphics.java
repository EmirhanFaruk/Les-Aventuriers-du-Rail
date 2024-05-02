package com.view.graphics;

import com.model.config.* ;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MapGraphics {
    private Case aCase ;
    final int tileWidth , tileHeight ;
    private Plateau plateau ;
    private static final String path = System.getProperty("user.dir");
    private static final String s = findSlash(path);
    private final int[] listx = { -1 , 0 , 1 , 0 } ;
    private final int[] listy = { 0 , 1 , 0 , -1 } ;

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
        return loadImage(mapName ) ;
    }

    /**
     * Une fonction qui affiche la bonne image
     * @param g graphics
     */
    public void draw(Graphics2D g) {
    	if(aCase != null) {
    		if (aCase instanceof Ville ) {
    			VilleGraphics.paint( g , ( Ville ) aCase ) ;
    		} else if (aCase instanceof Rail ) {
    			if(((Rail) aCase).getOccuper()) {
    				TrainGraphics.paint(g, (Rail) aCase);
    			}else {
    				RailGraphics.paint( g, (Rail) aCase );
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
                putsNameVille(g, (Ville) aCase);
            }
        }
    }

    public void putsNameVille( Graphics2D g , Ville ville){
        int fontSize = tileHeight/2; //( tileWidth * 2 ) / (ville.getNom().length()/2); // Trying to find the best size of font(?)
        Font f = new Font(Font.SERIF, Font.BOLD, fontSize);
        g.setFont(f);


        String nom = ville.getNom() ;
        Rectangle2D r = g.getFontMetrics().getStringBounds(nom, g);

        System.out.println(r.getWidth() + " - " + r.getHeight());

        int x = ( ville.getX() + (tileWidth / 2) ) - ( (int) (r.getWidth() / 2) );
        int y = ville.getY() - ( (int) (r.getHeight() / 2) ); ;


        g.fillRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());
        g.drawString( ville.getNom() , x , y );

        /*
        for ( int i = 0  ; i < listx.length ; i++ ){
            if ( x - listx[i] > -1  && y - listy[i] > -1 ){
                x = x  - listx[i] ;
                y = y  - listy[i] ;
                if ( ! (this.plateau.getPlateau() [ x ][ y ] instanceof Rail ) ){
                    g.drawString( ville.getNom() , x * tileWidth , y * tileHeight );
                    break;
                }
            }
        }
         */
    }

    /*
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // draw the rectangle here
        g.drawRect(RECT_X, RECT_Y, RECT_WIDTH, RECT_HEIGHT);
    }


    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int fontSize = 20;
        Font f = new Font("Comic Sans MS", Font.BOLD, fontSize);
        g2.setFont(f);
        g2.drawString("Hello World", 300, 300);
    }
     */

    /* getteurs et setteurs */

    public int getHeight() {
        return tileHeight;
    }

    public int getWidth() {
        return tileWidth;
    }
}

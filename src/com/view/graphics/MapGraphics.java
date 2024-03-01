package com.view.graphics;

import com.model.config.* ;

import java.awt.*;

public class MapGraphics {
    private Case aCase ;
    int width , height ;

    public MapGraphics( Case c , int width , int height ){
        this.aCase = c ;
        this.width = width ;
        this.height = height ;
    }

    public void draw(Graphics2D g) {
        if (aCase instanceof Ville ville) {
            VilleGraphics.setWH(width, height);
            //VilleGraphics.paint( g , ville );
            System.err.println("La ville est dessiner");
        } else if (aCase instanceof Rail rail) {
            RailGraphics.setWH(width, height);
            RailGraphics.paint( g, rail );
            System.err.println("La rail est dessiner");
        }
    }


    /*
   getteurs et setteurs
    */
    public Case getaCase() {
        return aCase;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}

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

   public void paint ( Graphics2D g , Case c ){
        if ( c instanceof Ville ){
            VilleGraphics.paint( g , (Ville) c );
        } else if ( c instanceof Rail ) {
            RailGraphics.paint(g, (Rail) c);
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

package com.view.graphics;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Had to make a whole new class lmaoooo
 * Didn't do a subclass because it looks messy
 */
public class RectangleGraphics
{
    private int x;
    private int y;
    private int width;
    private int height;

    public RectangleGraphics(Rectangle2D r)
    {
        this.x = (int) r.getX();
        this.y = (int) r.getY();
        this.width = (int) r.getWidth();
        this.height = (int) r.getHeight();
        System.out.println("RectangleGraphics x=" + x + ", y=" + y + ", width=" + width + ", height=" + height);
    }

    public void paint(Graphics g)
    {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
    }
}

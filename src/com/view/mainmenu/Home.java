package com.view.mainmenu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Home extends JPanel
{
    private int width, height;
    private BufferedImage home_image_file;

    public Home(int width, int height)
    {
        this.width = width;
        this.height = height;
        makeHome();
    }

    @Override
    public void setSize(int width, int height)
    {
        this.width = width;
        this.height = height;
        makeHome();
    }

    private String findSlash(String p)
    {
        for(int i = 0; i < p.length(); i++)
        {
            switch (p.charAt(i))
            {
                case '/' : return "/";
                case '\\' : return "\\";
            }
        }
        return "/";
    }

    private void setHome_image_file()
    {
        String path = System.getProperty("user.dir");
        String s = findSlash(path);
        try
        {
            home_image_file = ImageIO.read(new File(path + s + "ressources" + s + "Main_Menu" + s + "Menu_Image.png"));
        }
        catch (Exception ignored)
        {
            System.out.println("Couldn't read file.");
        }
    }

    /**
     * Produire JPanel pour home: Image de menu.
     */
    public void makeHome()
    {
        // Getting home image
        setHome_image_file();

        Image scaled_home_image = home_image_file.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        removeAll();
        add(new JLabel(new ImageIcon(scaled_home_image)));
    }
}

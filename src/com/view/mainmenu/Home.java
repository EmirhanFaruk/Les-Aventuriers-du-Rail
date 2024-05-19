package com.view.mainmenu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * La classe Home représente l'écran d'accueil du menu principal du jeu, affichant une image de fond.
 */
public class Home extends JPanel {
    private int width, height;
    private BufferedImage home_image_file;

    /**
     * Constructeur de la classe Home
     * @param width la largeur du panneau
     * @param height la hauteur du panneau
     */
    public Home(int width, int height) {
        this.width = width;
        this.height = height;
        makeHome();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                setSize(getWidth(), getHeight());
                repaint();
            }
        });
    }

    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        makeHome();
    }

    /**
     * Trouve le bon slash pour le chemin en fonction du système d'exploitation
     * @param p chemin du fichier
     * @return le bon slash sous forme de chaîne de caractères
     */
    private String findSlash(String p) {
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
     * Définit le fichier image de l'écran d'accueil
     */
    private void setHome_image_file() {
        String path = System.getProperty("user.dir");
        String s = findSlash(path);
        try {
            home_image_file = ImageIO.read(new File(path + s + "ressources" + s + "Main_Menu" + s + "Menu_Image.png"));
        } catch (Exception ignored) {
            System.out.println("Couldn't read file.");
        }
    }

    /**
     * Crée le JPanel pour l'écran d'accueil avec l'image de menu.
     */
    public void makeHome() {
        // Getting home image
        setHome_image_file();

        Image scaled_home_image = home_image_file.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        removeAll();
        add(new JLabel(new ImageIcon(scaled_home_image)));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        double scaleX = (double) getWidth() / home_image_file.getWidth();
        double scaleY = (double) getHeight() / home_image_file.getHeight();

        AffineTransform at = AffineTransform.getScaleInstance(scaleX, scaleY);
        g2.drawRenderedImage(home_image_file, at);
    }
}

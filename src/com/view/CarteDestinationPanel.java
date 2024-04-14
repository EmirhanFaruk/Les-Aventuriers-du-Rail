package com.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.*;

import com.model.Player;
import com.model.config.carte.CarteManager;
import com.model.controller.GameController;
import com.view.graphics.CardGraphics;

public class CarteDestinationPanel extends JPanel {
    private GameController gameController = new GameController();
    private Player player;
    private boolean[] isCardSelected;
    private Rectangle[] piocheVisibleBounds; // Pour gérer plusieurs cartes visibles
    private CarteManager carteDestination;
    private PlayerHandPanel mainDuJoueur;

    
    public CarteDestinationPanel(int width, int height, PlayerHandPanel playerHandPanel, CarteManager carteManager) {
        setBackground(Color.CYAN);
        setPreferredSize(new Dimension((int) (width * 0.15), height));
        this.mainDuJoueur = playerHandPanel;
        this.player = playerHandPanel.getPlayer();
        
        // Initialisation des rectangles pour les cartes visibles
        this.carteDestination = carteManager;
        this.isCardSelected = new boolean[3];

        // Initialisation des rectangles dans une méthode dédiée pour plus de clarté
        initRectangles(width, height);
        setupMouseAdapter(); // Installer l'écouteur de souris
    }

    
    private void initRectangles(int width, int height) {
        // Définition des dimensions et positions des rectangles
        int rectWidth = width; // Exemple de largeur
        int rectHeight = height / 5; // Exemple de hauteur
        int startX = (getWidth() - rectWidth) / 2;
        int startY = 20; // Marge du haut
        
        piocheVisibleBounds = new Rectangle[3]; // Pour 3 cartes visibles
        
        for (int i = 0; i < piocheVisibleBounds.length; i++) {
            startY += rectHeight + 10; // Marge entre les cartes
            piocheVisibleBounds[i] = new Rectangle(startX, startY, rectWidth, rectHeight);
        }
    }
    
    private void setupMouseAdapter() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	for (int i = 0; i < piocheVisibleBounds.length; i++) {
                    if (piocheVisibleBounds[i].contains(e.getPoint())) {
                        // Actualiser la carte visible après l'avoir piochée et la met dans la main du joueur
                        if(gameController.piocherCarteDestination(player, carteDestination, i)){
                        	isCardSelected[i] = true;
                            mainDuJoueur.getParent().revalidate();
                            mainDuJoueur.getParent().repaint();
        					repaint();
                        }
                        break; // Quitte la boucle si une correspondance est trouvée
                    }
                }
            }
        });
    }
    
    
    //Méthode qui rend l'image grise après sélection de la carte
    public static BufferedImage desaturateImage(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color color = new Color(image.getRGB(x, y));
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                int avg = (r + g + b) / 3; // Calcul de la moyenne des composantes RGB
                int gray = (avg << 16) | (avg << 8) | avg; // Conversion en niveau de gris
                result.setRGB(x, y, (color.getAlpha() << 24) | gray); // Appliquer le niveau de gris
            }
        }

        return result;
    }

    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        initRectangles(getWidth(), getHeight());

        // Dessin des cartes visibles
        for (int i = 0; i < piocheVisibleBounds.length; i++) {
            Rectangle rect = piocheVisibleBounds[i];
            BufferedImage carteVisible = CardGraphics.getCardObjectif();
            
            if (carteVisible != null) {
                if (isCardSelected[i]) { // Vérifier si la carte est sélectionnée
                	
                    // Dessiner la carte en gris en appliquant une désaturation
                    BufferedImage carteGris = desaturateImage(carteVisible);
                    g2d.drawImage(carteGris, rect.x, rect.y, rect.width, rect.height, null);
                    
                } else {
                    // Dessiner la carte normalement
                    g2d.drawImage(carteVisible, rect.x, rect.y, rect.width, rect.height, null);
                }
            }
        }
    }
    
    //Remet tout à false pour mettre la bonne couleur (ça évite que les cartes soient grises)
    public void setAllDefault() {
    	for(int i = 0; i < this.isCardSelected.length; i++)this.isCardSelected[i] = false;
    }


    public void setPlayer(Player player) {
        this.player = player;
    }
}

    
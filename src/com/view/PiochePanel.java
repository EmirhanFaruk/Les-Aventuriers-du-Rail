package com.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.model.Player;
import com.model.config.carte.CarteManager;
import com.model.config.carte.CarteWagon;
import com.model.controller.GameController;
import com.view.graphics.CardGraphics;

public class PiochePanel extends JPanel {
    private GameController gameController = new GameController();
    private Player player;
    private Rectangle piocheHiddenBounds;
    private Rectangle[] piocheVisibleBounds; // Pour gérer plusieurs cartes visibles
    private CarteWagon.Couleur[] imagePiocheVisible;
    private PlayerHandPanel mainDuJoueur;

    
    public PiochePanel(int width, int height, Player player, PlayerHandPanel playerHandPanel) {
        setBackground(Color.CYAN);
        setPreferredSize(new Dimension((int) (width * 0.15), height));
        this.player = player;
        this.mainDuJoueur = playerHandPanel;
        
        // Initialisation des rectangles pour les cartes visibles
        imagePiocheVisible = new CarteWagon.Couleur[3]; 
        this.setupAllCard();

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

        // Initialisation du rectangle pour la pioche cachée
        piocheHiddenBounds = new Rectangle(startX, startY, rectWidth, rectHeight);
        
        piocheVisibleBounds = new Rectangle[3]; // Pour 3 cartes visibles
        
        for (int i = 0; i < piocheVisibleBounds.length; i++) {
            startY += rectHeight + 10; // Marge entre les cartes
            piocheVisibleBounds[i] = new Rectangle(startX, startY, rectWidth, rectHeight);
        }
    }
    
    
    private void checkCard() {
    	if((imagePiocheVisible[0] == imagePiocheVisible[1] && imagePiocheVisible[0] == imagePiocheVisible[2]) && 
    			imagePiocheVisible[0] == CarteWagon.Couleur.LOC) {
    		this.setupCard(0);
    		this.setupCard(1);
    		this.setupCard(2);
    	} 	
    }
    
    
    private void setupAllCard() {
    	this.setupCard(0);
    	this.setupCard(1);
    	this.setupCard(2);
    }
    
    
    private void setupCard(int i) {
        CarteManager cm = new CarteManager();
        this.imagePiocheVisible[i] = cm.drawCard();
        this.checkCard();
    }

    
    private void setupMouseAdapter() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (piocheHiddenBounds.contains(e.getPoint())) {
                    gameController.piocherCarteInvisible(player);
                    mainDuJoueur.repaint();
                    repaint();
                } else {
                    for (int i = 0; i < piocheVisibleBounds.length; i++) {
                        if (piocheVisibleBounds[i].contains(e.getPoint())) {
                            gameController.piocherCarteVisible(player, imagePiocheVisible[i]);
                            setupCard(i); // Actualiser la carte visible après l'avoir piochée
                            mainDuJoueur.repaint();
                            repaint();
                            break; // Quitte la boucle si une correspondance est trouvée
                        }
                    }
                }
            }
        });
    }

    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        initRectangles(getWidth(), getHeight());

        // Dessin de la pioche cachée
        BufferedImage carteCachee = CardGraphics.getCardCache(); // Supposons que cette méthode existe et renvoie l'image de la pioche cachée
        if (carteCachee != null) {
            g2d.drawImage(carteCachee, piocheHiddenBounds.x, piocheHiddenBounds.y, piocheHiddenBounds.width, piocheHiddenBounds.height, null);
        }

        // Dessin des cartes visibles
        for (int i = 0; i < piocheVisibleBounds.length; i++) {
            Rectangle rect = piocheVisibleBounds[i];
            BufferedImage carteVisible = CardGraphics.getImageFromColor(imagePiocheVisible[i]);
            
            if (carteVisible != null) {
                g2d.drawImage(carteVisible, rect.x, rect.y, rect.width, rect.height, null);
            }
        }
    }
}

    
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

import com.model.Game;
import com.model.Player;
import com.model.config.carte.CarteManager;
import com.model.controller.GameController;
import com.view.graphics.CardGraphics;

public class PiochePanel extends JPanel {
    private GameController gameController = new GameController();
    private Player player;
    private int hoveredCardIndex = -1; // -1 signifie qu'aucune carte n'est survolée
    private Rectangle piocheHiddenBounds;
    private Rectangle[] piocheVisibleBounds; // Pour gérer plusieurs cartes visibles
    private CarteManager imagePiocheVisible;
    private PlayerHandPanel mainDuJoueur;
    private Game game;

    
    public PiochePanel(int width, int height, PlayerHandPanel playerHandPanel, Game game){
        setBackground(Color.orange);
        setPreferredSize(new Dimension((int) (width * 0.10), height));
        this.mainDuJoueur = playerHandPanel;
        this.player = playerHandPanel.getPlayer();
        this.game = game;
        
        // Initialisation des rectangles pour les cartes visibles
        this.imagePiocheVisible = game.getCarteManager();

        // Initialisation des rectangles dans une méthode dédiée pour plus de clarté
        initRectangles(width, height);
        setupMouseAdapter(); // Installer l'écouteur de souris
        setupMouseMotionListener();
    }

    
    private void initRectangles(int width, int height) {
        // Définition des dimensions et positions des rectangles
        int rectWidth = width; // Exemple de largeur
        int rectHeight = height / 5; // Exemple de hauteur
        int startX = (getWidth() - rectWidth) / 2;
        int startY = 20; // Marge du haut

        // Initialisation du rectangle pour la pioche cachée
        piocheHiddenBounds = new Rectangle(startX + 2, startY, rectWidth - 4, rectHeight);
        
        piocheVisibleBounds = new Rectangle[3]; // Pour 3 cartes visibles
        
        for (int i = 0; i < piocheVisibleBounds.length; i++) {
            startY += rectHeight + 10; // Marge entre les cartes
            piocheVisibleBounds[i] = new Rectangle(startX + 2, startY, rectWidth - 4, rectHeight);
        }
    }
    
    private void setupMouseAdapter() {
        addMouseListener(new MouseAdapter() { 	
            @Override
            public void mouseClicked(MouseEvent e) {
            	if(player.getFirstTurnOver() && imagePiocheVisible.alreadyPickedACard()) {
                    if (game.getGameFrame().getSound().getclick() ) game.getGameFrame().getSound().playSound("INGAME" , "popUp.wav");
            		 JOptionPane.showMessageDialog( player.getGame().getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel()
                             ,"TU CROIS M'AVOIR SALE FOU T'AS DEJA PRIS UNE CARTE DESTINATION !","INFORMATION", JOptionPane.INFORMATION_MESSAGE ) ;
            	}else {
            		if(player.getCanPlay() ) {
    	                if (piocheHiddenBounds.contains(e.getPoint())) {
    	                    gameController.piocherCarteInvisible(player);
    	                    mainDuJoueur.getParent().revalidate();
    	                    mainDuJoueur.getParent().repaint();
    	                    repaint();
    	                } else {
    	                    for (int i = 0; i < piocheVisibleBounds.length; i++) {
    	                        if (piocheVisibleBounds[i].contains(e.getPoint())) {
    	                            // Actualiser la carte visible après l'avoir piochée et la met dans la main du joueur
    	                            if(gameController.piocherCarteVisible(player, imagePiocheVisible.showWagon(i))){
    	                            	imagePiocheVisible.takeWagon(i);
    	                                mainDuJoueur.getParent().revalidate();
    	                                mainDuJoueur.getParent().repaint();
    	                                repaint();
    	                            }else{
                                        if (game.getGameFrame().getSound().getclick() ) game.getGameFrame().getSound().playSound("INGAME" , "popUp.wav");
    	                                JOptionPane.showMessageDialog( player.getGame().getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel()
    	                                        ,"Vous ne pouvez pas choisir cette carte ! ","INFORMATION", JOptionPane.INFORMATION_MESSAGE ) ;
    	                            }
    	                            break; // Quitte la boucle si une correspondance est trouvée
    	                        }
    	                    }
    	                }
                	}else {
                        if (player.getNiveau() == 0) { // le message s'affiche si seulement si c'est un vrai joueur
                            if (game.getGameFrame().getSound().getclick() ) game.getGameFrame().getSound().playSound("INGAME" , "popUp.wav");
                            JOptionPane.showMessageDialog(player.getGame().getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel()
                                    , "Vous devez d'abord piocher 1 carte destination au minimum !", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
            	}
            }
             @Override
             public void mouseExited(MouseEvent e) {
                 if (hoveredCardIndex != -1) {
                     hoveredCardIndex = -1;
                     repaint();
                 }
             }

        });
    }
    
    private void setupMouseMotionListener() {
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int previousIndex = hoveredCardIndex;
                hoveredCardIndex = -1;
                
                if (piocheHiddenBounds.contains(e.getPoint())) {
                    hoveredCardIndex = 4;
                }       
                
                for (int i = 0; i < piocheVisibleBounds.length; i++) {
                    if (piocheVisibleBounds[i].contains(e.getPoint())) {
                        hoveredCardIndex = i;
                        break;
                    }
                }
                
                if (hoveredCardIndex != previousIndex) {
                    repaint();
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
            if (4 == hoveredCardIndex) {
                g2d.setColor(new Color(255, 255, 0, 128)); // Jaune semi-transparent
                g2d.fill(piocheHiddenBounds);
            }
        }

        // Dessin des cartes visibles
        for (int i = 0; i < piocheVisibleBounds.length; i++) {
            Rectangle rect = piocheVisibleBounds[i];
            BufferedImage carteVisible = null;
            if(imagePiocheVisible.getTrainCards()[i] != null){
                carteVisible = CardGraphics.getImageFromColor(imagePiocheVisible.getTrainCards()[i]);
            }
            
            if (carteVisible != null) {
                g2d.drawImage(carteVisible, rect.x, rect.y, rect.width, rect.height, null);
                if (i == hoveredCardIndex) {
                    g2d.setColor(new Color(255, 255, 0, 128)); // Jaune semi-transparent
                    g2d.fill(rect);
                }
            }
        }
    }


    /* getters et setters */
    public void setPlayer(Player player) {
        this.player = player;
    }
}

    
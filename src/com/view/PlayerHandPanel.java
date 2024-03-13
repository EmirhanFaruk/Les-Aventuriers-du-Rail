package com.view;

import com.model.Player;
import com.model.config.carte.CarteWagon;
import com.view.graphics.CardGraphics;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PlayerHandPanel extends JPanel {
    private Player player;

    public PlayerHandPanel(Player player) {
        this.player = player;
        this.setPreferredSize(new Dimension(800, 200)); // Adaptez cette taille à votre besoin
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawPlayerHand(g);
    }

    private void drawPlayerHand(Graphics g) {
        int x = 10; // Position de départ pour le dessin des cartes

        // Dessine chaque carte de wagon
        for (CarteWagon.Couleur couleur : player.getTrainCard()) {
            CarteWagon carteWagon = new CarteWagon(couleur); // Crée une carte temporaire pour obtenir l'image
            BufferedImage image = CardGraphics.getImage(carteWagon);
            if (image != null) {
                g.drawImage(image, x, 30, null); // Ajustez la position y comme nécessaire
                x += image.getWidth() + 10; // Espace entre les cartes
            }
        }

        // Même logique pour carte destination
    }
}
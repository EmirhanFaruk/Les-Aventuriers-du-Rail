package com.view;

import com.model.Player;
import com.model.config.carte.CarteWagon;
import com.view.graphics.CardGraphics;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PlayerHandPanel extends JPanel {
    private Player player;

    public PlayerHandPanel(Player currentPlayer) {
        setPlayer(currentPlayer); // Défini le joueur et demander un redessin
        setBackground(Color.orange); // Optionnel : couleur de fond pour la main
    }

    public void setPlayer(Player player) {
        this.player = player;
        revalidate(); // Force la mise en page à se rafraîchir si nécessaire
        repaint();    // Demande le redessin du panel
    }

    protected void paintComponent(Graphics g) {
    	super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g; // Convertir Graphics en Graphics2D
        if(this.player != null)this.drawPlayerHand(g2d);
    }

    private void drawPlayerHand(Graphics2D g) {
        int x = 30, i = 0;
        
        while(i < this.player.getTrainCard().size()) {
        	CarteWagon.Couleur couleur = this.player.getTrainCard().get(i);
        	CarteWagon carteWagon = new CarteWagon(couleur);
        	BufferedImage image = CardGraphics.getImage(carteWagon);
        	
        	if (image != null) {
        	    g.drawImage(image, x, 30, null);
        	    x += image.getWidth() + 10;
        	}
            i++;
        }
    }
}

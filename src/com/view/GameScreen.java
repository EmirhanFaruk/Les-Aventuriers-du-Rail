package com.view;


import com.model.Player;
import com.model.config.Plateau;
import com.view.graphics.* ;

import javax.swing.*;
import java.awt.*;

public class GameScreen extends JPanel {
    GameFrame frame;
    MapScreen mapScreen;
    PlayerHandPanel playerHandPanel; // Ajout d'une référence au panneau de la main du joueur
    private static int tile_width, tile_height;
    private int width, height;
    private Player currentPlayer; // Vous aurez probablement besoin d'une référence au joueur courant

    public GameScreen(GameFrame frame, String map, int width, int height, Player currentPlayer) {
        this.frame = frame;
        this.currentPlayer = currentPlayer; // Initialisez currentPlayer
        setSize(width, height);
        this.height = height;
        this.width = width;
        tile_height = getHeight() / 24;
        tile_width = getWidth() / 24;
        this.mapScreen = new MapScreen(map, width, height, tile_width, tile_height);

        // Initialisation du playerHandPanel
        this.playerHandPanel = new PlayerHandPanel(currentPlayer);
        this.playerHandPanel.setPreferredSize(new Dimension(width, 100)); // Ajustez la hauteur selon vos besoins

        setLayout(new BorderLayout());
        add(mapScreen, BorderLayout.CENTER);
        add(playerHandPanel, BorderLayout.SOUTH); // Ajoute le playerHandPanel en bas
    }

    // Méthode pour mettre à jour le panneau de la main du joueur avec un nouveau joueur
    public void updatePlayerHandPanel(Player newPlayer) {
        this.currentPlayer = newPlayer; // Met à jour le joueur courant
        this.remove(playerHandPanel); // Enlève l'ancien panneau de la main
        this.playerHandPanel = new PlayerHandPanel(newPlayer); // Crée un nouveau panneau de la main avec le nouveau joueur
        this.playerHandPanel.setPreferredSize(new Dimension(width, 100)); // Ajuste de nouveau la dimension
        this.add(playerHandPanel, BorderLayout.SOUTH); // Ajoute le nouveau panneau au GameScreen
        this.validate(); // Valide le conteneur après l'ajout ou la suppression de composants
        this.repaint(); // Rafraîchit l'affichage
    }

    public void make( Plateau plateau ){
        mapScreen.makeMap( plateau );
    }
}

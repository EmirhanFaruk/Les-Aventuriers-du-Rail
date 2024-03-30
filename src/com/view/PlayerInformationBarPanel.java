package com.view;

import com.model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PlayerInformationBarPanel extends JPanel {
    private ArrayList<Player> players;
    private Player playerCourant;

    public PlayerInformationBarPanel(ArrayList<Player> players, Player player, int w, int h) {
        this.players = players;
        this.playerCourant = player;
        setPreferredSize(new Dimension(w, h));
        setLayout(new GridLayout(1, players.size()));

        afficheInformationJoueurCourant();
        afficheInformationOtherPlayer();
    }

    private Color playerColor(String str) {
        switch (str) {
            case "JAUNE":
                return Color.YELLOW;
            case "ROUGE":
                return Color.RED;
            case "VERT":
                return Color.GREEN;
            case "BLEU":
                return Color.BLUE;
            default:
                return null;
        }
    }

    private JLabel createAndConfigureLabel(String text, Color bgColor, Font font) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(bgColor);
        label.setFont(font);
        return label;
    }

    public void afficheInformationJoueurCourant() {
        Color playerColor = playerColor(playerCourant.getPlayerCouleur());
        Font playerNameFont = new Font(Font.SANS_SERIF, Font.BOLD, 18); // Définir une police plus grande pour le nom du joueur courant
        Font otherInfoFont = new Font(Font.SANS_SERIF, Font.PLAIN, 12); // Utiliser une police normale pour les autres informations

        JLabel nameLabel = createAndConfigureLabel(playerCourant.getName(), new Color(playerColor.getRed(), playerColor.getGreen(), playerColor.getBlue(), 100), playerNameFont);
        JLabel scoreLabel = createAndConfigureLabel("Score : " + playerCourant.getScore(), new Color(playerColor.getRed(), playerColor.getGreen(), playerColor.getBlue(), 100), otherInfoFont);
        JLabel wagonLabel = createAndConfigureLabel("Wagon : " + playerCourant.getNbrWagon(), new Color(playerColor.getRed(), playerColor.getGreen(), playerColor.getBlue(), 100), otherInfoFont);
        JLabel gareLabel = createAndConfigureLabel("Gare : " + playerCourant.getNbrGare(), new Color(playerColor.getRed(), playerColor.getGreen(), playerColor.getBlue(), 100), otherInfoFont);

        add(nameLabel);
        add(scoreLabel);
        add(wagonLabel);
        add(gareLabel);

    }

    public void afficheInformationOtherPlayer() {
        for (Player otherPlayer : players) {
            if (!otherPlayer.equals(playerCourant)) {
                Color playerColor = playerColor(otherPlayer.getPlayerCouleur());
                Font otherPlayerFont = new Font(Font.SANS_SERIF, Font.PLAIN, 12); // Utiliser une police normale pour les autres joueurs
                add(createAndConfigureLabel(otherPlayer.getName(), new Color(playerColor.getRed(), playerColor.getGreen(), playerColor.getBlue(), 100), otherPlayerFont));
                add(createAndConfigureLabel("Score : " + otherPlayer.getScore(), new Color(playerColor.getRed(), playerColor.getGreen(), playerColor.getBlue(), 100), otherPlayerFont));
                add(createAndConfigureLabel("Wagon : " + otherPlayer.getNbrWagon(), new Color(playerColor.getRed(), playerColor.getGreen(), playerColor.getBlue(), 100), otherPlayerFont));
                add(createAndConfigureLabel("Gare : " + otherPlayer.getNbrGare(), new Color(playerColor.getRed(), playerColor.getGreen(), playerColor.getBlue(), 100), otherPlayerFont));
            }
        }

    }
}

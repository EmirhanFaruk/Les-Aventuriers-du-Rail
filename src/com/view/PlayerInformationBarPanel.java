package com.view;

import com.model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PlayerInformationBarPanel extends JPanel {
    private Player playerCourant;
    private CardLayout cardLayout;

    public PlayerInformationBarPanel( Player player, int w, int h) {
        this.playerCourant = player;
        setPreferredSize(new Dimension(w, h));

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        afficheInformationJoueurCourant();
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

    private JPanel createPlayerPanel(Player player, Color bgColor, Font font) {
        JPanel panel = new JPanel(new GridLayout(1 , 4 ));
        panel.setBackground(bgColor);

        JLabel nameLabel = new JLabel(player.getName());
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setFont(font);

        JLabel scoreLabel = new JLabel("Score : " + player.getScore());
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel wagonLabel = new JLabel("Wagon : " + player.getNbrWagon());
        wagonLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel gareLabel = new JLabel("Gare : " + player.getNbrGare());
        gareLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(nameLabel);
        panel.add(scoreLabel);
        panel.add(wagonLabel);
        panel.add(gareLabel);

        return panel;
    }

    public void afficheInformationJoueurCourant() {
        Color playerColor = playerColor(playerCourant.getPlayerCouleur());
        assert playerColor != null;
        Color newColor = new Color( playerColor.getRed() , playerColor.getGreen() , playerColor.getBlue() ,100 ) ;
        Font playerNameFont = new Font(Font.SANS_SERIF, Font.BOLD, 18);

        JPanel currentPlayerPanel = createPlayerPanel(playerCourant, newColor , playerNameFont);
        add(currentPlayerPanel, "currentPlayer");
    }

    public void updateCurrentPlayer(Player newPlayerCourant) {
        removeAll();

        Color playerColor = playerColor(newPlayerCourant.getPlayerCouleur());
        assert playerColor != null;
        Color newColor = new Color( playerColor.getRed() , playerColor.getGreen() , playerColor.getBlue() ,  100 ) ;
        Font playerNameFont = new Font(Font.SANS_SERIF, Font.BOLD, 18);

        JPanel currentPlayerPanel = createPlayerPanel(newPlayerCourant, newColor , playerNameFont);
        add(currentPlayerPanel, "currentPlayer");

        cardLayout.show(this, "currentPlayer");
        revalidate();
        repaint();
    }

    public void setPlayerCourant(Player playerCourant ) {
        this.playerCourant = playerCourant ;
        updateCurrentPlayer( playerCourant ) ;
    }

}

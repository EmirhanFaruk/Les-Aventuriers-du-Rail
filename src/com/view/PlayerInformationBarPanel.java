package com.view;

import com.model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PlayerInformationBarPanel extends JPanel {
    private ArrayList<Player> players;
    private Player playerCourant;
    private CardLayout cardLayout;

    public PlayerInformationBarPanel(  ArrayList<Player> players ,  Player player, int w, int h) {
        this.players = players ;
        this.playerCourant = player;
        setPreferredSize(new Dimension(w, h));

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        afficheInfoAll();
    }

    /**
     * Donne la couleur du joueur
     * @param str couleur du joueur
     * @return Color
     */
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

    /**
     * Une focntion qui crée un panel pour mettre les informations nécessaires
     * @param player le joueur
     * @param bgColor sa couleur
     * @param font la taille du texte
     * @return Panel avec toutes les informations
     */
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


    /**
     * Une fonction qui affiche tout les informations des joueurs
     */
    public void afficheInfoAll(){
        JPanel panel = new JPanel( ) ;
        panel.setLayout( new GridLayout( 1 , 4 ) ) ;
        panel.add( afficheInformationJoueurCourant() ) ;
        afficheInformationOtherPlayer( panel );

        add( panel , "AllInformation") ;
    }

    /**
     * Une fonction renvoie un panel du joueur courant
     * @return Jpanel du joueur
     */
    public JPanel afficheInformationJoueurCourant() {
        Color playerColor = playerColor(playerCourant.getPlayerCouleur());
        assert playerColor != null;
        Color newColor = new Color( playerColor.getRed() , playerColor.getGreen() , playerColor.getBlue() ,100 ) ;
        Font playerNameFont = new Font(Font.SANS_SERIF, Font.BOLD, 18);

        return createPlayerPanel(playerCourant, newColor , playerNameFont);
    }

    /**
     * Une fonciton qui ajoute au panel les informations des autres joueurs
     * @param panel le panel à utiliser
     */
    public void afficheInformationOtherPlayer( JPanel panel ) {
        for (Player otherPlayer : players) {
            if (!otherPlayer.equals(playerCourant)) {
                Color playerColor = playerColor(otherPlayer.getPlayerCouleur());
                Font otherPlayerFont = new Font(Font.SANS_SERIF, Font.PLAIN, 12); // Utiliser une police normale pour les autres joueurs
                assert playerColor != null;
                panel.add(createPlayerPanel(otherPlayer , new Color(playerColor.getRed(), playerColor.getGreen(), playerColor.getBlue(), 100), otherPlayerFont));
            }
        }

        revalidate();
        repaint();
    }

    /**
     * Une fonction qui met a jour la barre d'information avec le nouveau joueur courant
     * @param newPlayerCourant le nouveau joueur
     */
    public void updateCurrentPlayer(Player newPlayerCourant) {
        removeAll();

        Color playerColor = playerColor(newPlayerCourant.getPlayerCouleur());
        assert playerColor != null;

        afficheInfoAll();

        cardLayout.show(this, "currentPlayer");
        revalidate();
        repaint();
    }

    /**
     * Une fonction qui change le joueur courant et met à jour
     * @param playerCourant le nouveau joueur courant
     */
    public void setPlayerCourant(Player playerCourant ) {
        this.playerCourant = playerCourant ;
        updateCurrentPlayer( playerCourant ) ;
    }

}

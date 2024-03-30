package com.view;

import com.model.Player;

import javax.swing.*;
import java.awt.*;

public class PlayerInformationBarPanel extends JPanel {
    private Player player ;

    public PlayerInformationBarPanel ( Player player , int w, int h ){
        this.player = player ;
        setPreferredSize(new Dimension( w , h ));
        setLayout(new GridLayout( 1 , 4 ));


        background();
        afficheInformationJoueur();
    }

    private void background (){
        Color playerColor = playerColor(player.getPlayerCouleur());
        assert playerColor != null;
        setBackground(new Color(playerColor.getRed(), playerColor.getGreen(), playerColor.getBlue(), 100));
    }


    private Color playerColor ( String str ) {
        switch ( str ){
            case "JAUNE" : return Color.YELLOW  ;
            case "ROUGE" : return Color.RED ;
            case "VERT" : return Color.GREEN ;
            case "BLEU" : return Color.BLUE ;
            default: return null ;
        }
    }

    /**
     * Une fonction qui alligne les étiquettes
     * @param text nom de l'étiquette
     * @return une étiquette
     */
    private JLabel createCenteredLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    /**
     * Une fonction qui met tout les étiquettes
     */
    public void afficheInformationJoueur (){
        add(createCenteredLabel("Joueur : " + player.getName() ) ) ;
        add(createCenteredLabel("Score : " + player.getScore() ) ) ;
        add(createCenteredLabel( "Wagon : " + player.getNbrWagon() ) ) ;
        add(createCenteredLabel("Gare : " + player.getNbrGare() ) ) ;
    }
}

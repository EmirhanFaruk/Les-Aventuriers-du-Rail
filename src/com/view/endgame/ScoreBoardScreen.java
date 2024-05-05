package com.view.endgame;

import com.model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class ScoreBoardScreen extends JPanel{
    private ArrayList<Player> players ;

    /**
     * Le constructeur de scoreboardScreen
     * @param players une liste de joueur
     */
    public ScoreBoardScreen( ArrayList<Player> players , int width , int height ){
        this.players = players ;
        setLayout(new GridLayout(players.size() + 1, 5 )); // +1 pour l'en-tête
        setPreferredSize( new Dimension( width , height ));
        createEnTete();
        listePlayerTrier();
        afficheScoreBoard();
    }

    /**
     * Une fonction qui fait les en tete
     */
    private void createEnTete() {
        add(createCenteredHeader("NOM"));
        add(createCenteredHeader("POINTS"));
        add(createCenteredHeader("NOMBRES DE RAILS POSER"));
        add(createCenteredHeader("NOMBRE DE GARE RESTANTE")) ;
        add(createCenteredHeader("MISSION COMPLETE"));
    }

    /**
     * Une fonction qui crée une étiquette en fonction du texte
     * @param text le nom de l'étiquette
     * @return une étiquette
     */
    private JLabel createCenteredHeader(String text) {
        JLabel label = new JLabel(text);
        label.setOpaque(true);
        label.setBackground(Color.BLACK);
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    /**
     * Une fonction qui trie la liste des joueurs
     */
    public void listePlayerTrier(){
        players.sort(Comparator.comparingInt(Player::scoreFinal).reversed());
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
    public void afficheScoreBoard (){
        for (Player player : players) {
            add(createCenteredLabel(player.getName()));
            add(createCenteredLabel(String.valueOf(player.scoreFinal()))) ;
            add(createCenteredLabel(String.valueOf( player.initNbragon() - player.getNbrWagon()))) ;
            add(createCenteredLabel(String.valueOf(player.getNbrGare()))) ;
            add(createCenteredLabel(String.valueOf(player.getMissionComplete()))) ;
        }
    }
}

package com.view.endgame;

import com.view.GameScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * La classe EndGameScreen représente l'écran de fin de jeu, affichant les scores des joueurs et offrant des options pour retourner au menu, redémarrer le jeu ou quitter.
 */
public class EndGameScreen extends JPanel {
    private GameScreen gameScreen ;
    private JPanel button_panel;
    private JPanel scoreBoardPanel ;
    public EndGameScreen ( GameScreen gameScreen ,  int width , int height ){
        this.gameScreen = gameScreen ;
        setSize(width , height );
        this.button_panel = makeButton() ;
        this.scoreBoardPanel = new ScoreBoardScreen( gameScreen.getFrame().getMain().getGame().getListPlayer() , width , (int) (height* 0.7)) ;
        add( this.scoreBoardPanel ) ;
        add( this.button_panel , BorderLayout.SOUTH ) ;
    }

    /**
     * Une fonction qui crée les buttons
     * @return tout les buttons que l'in veut
     */
    private JPanel makeButton (){
        JPanel resultat = new JPanel() ;
        resultat.setLayout(new GridLayout( 3 , 1 ));
        JButton buttonMenu = new JButton("MENU") ;
        JButton buttonRestart = new JButton("RESTART") ;
        JButton buttonExit = new JButton("EXIT") ;

        buttonMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameScreen.getFrame().playSoundClick("END" ,"click.wav" );
                gameScreen.getFrame().changeMusicIsMusic("MENU" , "tchu-tchu-song.wav");
                gameScreen.getFrame().quitMainMenu();
            }
        });

        buttonRestart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameScreen.getFrame().playSoundClick("END" ,"click.wav" );
                gameScreen.getFrame().getMain().restart();
            }
        });

        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameScreen.getFrame().playSoundClick("END" ,"click.wav" );
                gameScreen.getFrame().quitGame();
            }
        });

        resultat.add(buttonMenu) ;
        resultat.add(buttonRestart) ;
        resultat.add(buttonExit) ;
        return resultat ;
    }
}

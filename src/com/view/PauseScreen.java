package com.view;

import com.view.endgame.ScoreBoardScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PauseScreen extends JPanel {
    private GameScreen gameScreen ;
    private JPanel titlePanel ;
    private JPanel button_panel;
    private JPanel scoreBoardPanel ;

    /**
     * Constructeur de PauseScreen
     * @param gameScreen l'écran de jeu
     * @param width width
     * @param height height
     */
    public PauseScreen( GameScreen gameScreen , int width , int height ){
        this.gameScreen = gameScreen ;
        setSize(width , height );
        this.titlePanel = makeTitle() ;
        this.button_panel = makeButton() ;
        this.scoreBoardPanel = new ScoreBoardScreen( gameScreen.getFrame().getMain().getGame().getListPlayer() , width , (int) (height* 0.7)) ;
        add( this.titlePanel , BorderLayout.NORTH ) ;
        add( this.scoreBoardPanel ) ;
        add( this.button_panel , BorderLayout.SOUTH ) ;
    }

    /**
     * Une fonction qui affiche l'entête de page
     * @return le panel pour l'entête
     */
    private JPanel makeTitle(){
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel(" PAUSE ");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20 ));

        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(Box.createVerticalGlue());
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalGlue());

        return titlePanel;

    }

    /**
     * Une fonction qui permet de faire les buttons
     * @return un panel pour les buttons
     */
    private JPanel makeButton (){
        JPanel resultat = new JPanel() ;
        resultat.setLayout(new GridLayout( 3 , 1 ));
        JButton buttonMenu = new JButton("MENU") ;
        JButton buttonContinue = new JButton("CONTINUE") ;
        JButton buttonExit = new JButton("EXIT") ;

        buttonMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ( gameScreen.getFrame().getSound().getclick()) gameScreen.getFrame().getSound().playSound("PAUSE" ,"click.wav" );
                gameScreen.getFrame().quitMainMenu();
                if ( gameScreen.getFrame().getSound().getMusic() ) gameScreen.getFrame().getSound().changeMusic("MENU" , "tchu-tchu-song.wav");
            }
        });

        buttonContinue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameScreen.getFrame().getMain().setRunning(true);
                if ( gameScreen.getFrame().getSound().getclick()) gameScreen.getFrame().getSound().playSound("PAUSE" ,"click.wav" );
                GameManagerScreen gm = gameScreen.getFrame().getGameScreen().getGameManagerScreen() ;
                gm.removePause();
                gm.getCardLayout().show( gm , gameScreen.getFrame().getIngame_screen_s() );
                if ( gameScreen.getFrame().getSound().getMusic()) gameScreen.getFrame().getSound().playMusic("INGAME" ,"inGame.wav" );
                //DEBUG : System.out.println("On doit revenir a la page du jeu");
            }
        });

        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ( gameScreen.getFrame().getSound().getclick()) gameScreen.getFrame().getSound().playSound("PAUSE" ,"click.wav" );
                gameScreen.getFrame().quitGame();
            }
        });

        resultat.add(buttonMenu) ;
        resultat.add(buttonContinue) ;
        resultat.add(buttonExit) ;
        return resultat ;
    }
}

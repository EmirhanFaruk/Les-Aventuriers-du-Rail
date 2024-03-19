package com.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EndGameScreen extends JPanel {
    private GameScreen gameScreen ;
    private JPanel button_panel;
    private int width , height ;
    public EndGameScreen ( GameScreen gameScreen ,  int width , int height ){
        this.gameScreen = gameScreen ;
        this.width = width ;
        this.height = height ;
        setSize(width , height );
        this.button_panel = makeButton() ;
        add( this.button_panel , BorderLayout.CENTER);
    }

    private JPanel makeButton (){
        JPanel resultat = new JPanel() ;
        resultat.setLayout(new GridLayout( 3 , 1 ));
        JButton buttonMenu = new JButton("MENU") ;
        JButton buttonRestart = new JButton("RESTART") ;
        JButton buttonExit = new JButton("EXIT") ;

        buttonMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameScreen.getFrame().quitMainMenu();
            }
        });

        buttonRestart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameScreen.getFrame().getMain().restart();
            }
        });

        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameScreen.getFrame().quitGame();
            }
        });

        resultat.add(buttonMenu) ;
        resultat.add(buttonRestart) ;
        resultat.add(buttonExit) ;
        return resultat ;
    }

}

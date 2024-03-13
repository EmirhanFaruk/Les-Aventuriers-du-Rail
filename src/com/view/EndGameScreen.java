package com.view;

import javax.swing.*;

public class EndGameScreen extends JPanel {
    private GameFrame gameFrame ;
    private int width , height ;
    public EndGameScreen ( GameFrame gameFrame ,  int width , int height ){
        this.gameFrame = gameFrame ;
        this.width = width ;
        this.height = height ;
        setSize(width , height );
    }

}

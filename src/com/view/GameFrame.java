package com.view;

import com.controller.Main;
import com.model.Game;
import com.view.mainmenu.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class GameFrame extends JFrame
{
    public static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];


    private boolean in_main_menu = true;

    private boolean in_game = false;

    private double scale;

    // Pour changer le mode
    private JPanel main_panel;
    private final CardLayout cardLayout = new CardLayout();

    private GameScreen gameScreen ;

    private final String main_menu_screen_s = "MAIN MENU", ingame_screen_s = "INGAME";

    private Menu menu;

    private Main main;



    /**
     * Constructeur de GameView, assigner les attributs
     */
    public GameFrame(int width, int height, Main main)
    {
        // Les attributs de JPanel
        this.setTitle("Tchu Tchuuu");
        this.setSize(width, height);
        this.setPreferredSize(new Dimension(width, height));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        this.main = main;
        // On commence par menu
        // On ne peut pas produire game encore car on n'a pas encore choisit le map.
        menu = new Menu(width, height, this);

        main_panel = new JPanel();
        main_panel.setLayout(cardLayout);
        main_panel.add(main_menu_screen_s, menu);
        cardLayout.show(main_panel, ingame_screen_s);

        this.add(main_panel);

        pack();

        this.setVisible(true);

    }


    public void startGame(String map, String[] player_names, String[] player_types)
    {
        main.startGame(map, player_names, player_types);

        gameScreen = null ;
        gameScreen = new GameScreen( this , getWidth() , getHeight()) ;

        main_panel.add(ingame_screen_s , gameScreen ) ;
        setMinimumSize(getSize());
        pack();
        setMinimumSize(null);
        gameScreen.make( main.game.getPlateau());
        cardLayout.show(main_panel, ingame_screen_s);
    }






    @Override
    public void setSize(int width, int height)
    {
        super.setSize(width, height);
        if(main_panel != null)
        {
            main_panel.setSize(width, height);
        }
    }

    public void quitMainMenu()
    {
        cardLayout.show(main_panel, main_menu_screen_s);
        menu.showMenu();
        main.setRunning(false);
    }

    public void quitGame()
    {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    public GraphicsDevice getDevice()
    {
        return device;
    }
}

package com.view;

import com.view.mainmenu.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class GameFrame extends JFrame implements Runnable
{
    public static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];

    private boolean running = false;

    private boolean in_main_menu = true;

    private boolean in_game = false;

    private double scale;

    // Pour changer le mode
    private JPanel main_panel;
    private final CardLayout cardLayout = new CardLayout();

    private final String main_menu_screen_s = "MAIN MENU", ingame_screen_s = "INGAME";

    private Menu menu;

    private Thread game_thread;


    /**
     * Constructeur de GameView, assigner les attributs
     */
    public GameFrame(int width, int height)
    {
        // Les attributs de JPanel
        this.setTitle("Tchu Tchuuu");
        this.setSize(width, height);
        this.setPreferredSize(new Dimension(width, height));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // On commence par menu
        // On ne peut pas produire game encore car on n'a pas encore choisit le map.
        menu = new Menu(width, height, this);

        main_panel = new JPanel();
        main_panel.setLayout(cardLayout);
        main_panel.add(main_menu_screen_s, menu);
        cardLayout.show(main_panel, main_menu_screen_s);

        this.add(main_panel);

        pack();

        this.setVisible(true);

    }


    public void startGame(String map, String difficulty, String mode, String character)
    {
        setMinimumSize(getSize());
        pack();
        setMinimumSize(null);
        cardLayout.show(main_panel, ingame_screen_s);
        running = true;
        startGame_thread();
    }

    private void startGame_thread()
    {
        game_thread = new Thread(this);
        game_thread.start();
    }

    /**
     * Une func qui fait rouler le mainLoop
     */
    @Override
    public void run()
    {
        double start;
        double required_fps = (double) 1000000000/60;
        double end = required_fps;
        while(running)
        {
            start = System.nanoTime();
            if(end >= required_fps)
            {
                //game.update(end/1000000000);
                end = System.nanoTime() - start;
            }
            else
            {
                end += System.nanoTime() - start;
            }
        }
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
        running = false;
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

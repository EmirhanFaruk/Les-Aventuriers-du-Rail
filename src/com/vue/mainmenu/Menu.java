package com.vue.mainmenu;

import com.vue.GameFrame;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JPanel
{
    /**
     * Home button.
     */
    public class HomeButton implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            cardLayout.show(main_panel, home_mode);
        }
    }

    /**
     * Play button.
     */
    public class PlayButton implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            cardLayout.show(main_panel, play_mode);
        }
    }

    /**
     * Settings button.
     */
    public class SettingsButton implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            cardLayout.show(main_panel, settings_mode);
        }
    }

    private int width, height;


    // Les panels principales
    private JPanel button_panel;
    private JPanel main_panel;


    // Les buttons
    private final HomeButton hbl = new HomeButton();
    private final PlayButton pbl = new PlayButton();
    private final SettingsButton sbl = new SettingsButton();



    // Pour changer le mode
    private final CardLayout cardLayout = new CardLayout();

    private final String home_mode = "HOME";
    private final String play_mode = "PLAY";
    private final String settings_mode = "SETTINGS";


    // Le JFrame
    private final GameFrame frame;

    /**
     * Constructeur de Menu.
     * @param width largueur
     * @param height hauteur
     */
    public Menu(int width, int height, GameFrame frame)
    {
        this.width = width;
        this.height = height;
        this.frame = frame;

        make();
    }


    /**
     * Produire le panel.
     */
    private void make()
    {
        // Le reste: affichage
        // En bas: home - play - settings
        this.setLayout(new BorderLayout());

        button_panel = makeButtonPanel();
        main_panel = makeMainPanel();



        this.add(button_panel, BorderLayout.SOUTH);
        this.add(main_panel);
    }

    public void showMenu()
    {
        cardLayout.show(main_panel, home_mode);
    }

    /**
     * Produire le panel avec les buttons.
     * @return JPanel des buttons
     */
    private JPanel makeButtonPanel()
    {
        JPanel res = new JPanel();
        res.setLayout(new GridLayout(1, 3));

        JButton home = new JButton("HOME");
        home.addActionListener(hbl);
        JButton play = new JButton("PLAY");
        play.addActionListener(pbl);
        JButton settings = new JButton("SETTINGS");
        settings.addActionListener(sbl);

        JButton quit = new JButton("QUIT");
        quit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                frame.quitGame();
            }
        });

        JButton[] bl = {home, play, settings, quit};
        for (JButton button : bl)
        {
            button.setBorderPainted(false);
            button.setBackground(Color.BLACK);
            button.setForeground(Color.GRAY);
        }

        res.add(home);
        res.add(play);
        res.add(settings);
        res.add(quit);

        return res;
    }

    /**
     * Produire le main panel qui sera dans le JFrame quand le mode est "menu"
     * @return main panel
     */
    private JPanel makeMainPanel()
    {
        JPanel res  = new JPanel();
        // Pour pouvoir changer le mode
        res.setLayout(cardLayout);

        // Ajout des modes
        res.add(home_mode, makeHome());
        res.add(play_mode, makePlay());
        res.add(settings_mode, makeSettings());

        cardLayout.show(res, home_mode);

        return res;
    }

    /**
     * Produire JPanel pour home: Image de menu.
     * @return JPanel de Home
     */
    private JPanel makeHome() {return new Home(width, height);}

    /**
     * Produire le menu de jeu ou on peut choisir un niveau et sa difficulté, et ou on peut lancer le jeu.
     * @return JPanel de Play
     */
    private JPanel makePlay()
    {
        return new Play(this.frame);
    }

    /**
     * Produire le menu des parametres ou il y a le reglage de largeur et hauteur, et aussi fullscreen
     * @return Jpanel de Settings
     */
    private JPanel makeSettings()
    {
        return new Settings(this);
    }

    public void setAllSize(int width, int height)
    {
        this.width = width;
        this.height = height;
        frame.setSize(width, height);
        this.setSize(width, height);
        removeAll();
        make();
        cardLayout.show(main_panel, settings_mode);
    }



    public GraphicsDevice getDevice()
    {
        return frame.getDevice();
    }

    public GameFrame getFrame()
    {
        return frame;
    }
}

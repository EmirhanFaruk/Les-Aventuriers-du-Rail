package com.view.mainmenu;

import com.view.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * La classe Menu représente le menu principal du jeu, permettant la navigation entre les différentes sections telles que
 * l'accueil, les règles, les paramètres et le lancement du jeu.
 */
public class Menu extends JPanel {

    /**
     * ActionListener pour le bouton Home.
     */
    public class HomeButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e)
        {   
            frame.playSoundClick("MENU" ,"ChangePageInTheMenu.wav");
            cardLayout.show(main_panel, home_mode);
        }
    }

    /**
     * ActionListener pour le bouton Play.
     */
    public class PlayButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            frame.playSoundClick("MENU" ,"ChangePageInTheMenu.wav");
            cardLayout.show(main_panel, play_mode);
        }
    }

    /**
     * ActionListener pour le bouton Settings.
     */
    public class SettingsButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            frame.playSoundClick("MENU" ,"ChangePageInTheMenu.wav");
            cardLayout.show(main_panel, settings_mode);
        }
    }

    /**
     * ActionListener pour le bouton Rules.
     */
    public class RulesButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            cardLayout.show(main_panel, rules_mode);
            frame.playSoundClick("MENU" ,"RulesSelection.wav");
        }
    }

    private int width, height;

    // Les panels principaux
    private JPanel button_panel;
    private JPanel main_panel;

    // Les boutons
    private final HomeButton hbl = new HomeButton();
    private final PlayButton pbl = new PlayButton();
    private final SettingsButton sbl = new SettingsButton();
    private final RulesButtonAction rbl = new RulesButtonAction();

    // Pour changer le mode
    private final CardLayout cardLayout = new CardLayout();

    private final String home_mode = "HOME";
    private final String play_mode = "PLAY";
    private final String settings_mode = "SETTINGS";
    private final String rules_mode = "RULES";

    // Le JFrame
    private final GameFrame frame;

    /**
     * Constructeur de Menu.
     * @param width la largeur
     * @param height la hauteur
     * @param frame l'objet GameFrame
     */
    public Menu(int width, int height, GameFrame frame) {
        this.width = width;
        this.height = height;
        this.frame = frame;

        make();
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                gameFrameResized(evt);
            }
        });
    }

    private void gameFrameResized(ComponentEvent e) {
        main_panel.repaint();
    }

    /**
     * Crée le panel principal du menu.
     */
    private void make() {
        this.setLayout(new BorderLayout());

        button_panel = makeButtonPanel();
        main_panel = makeMainPanel();

        this.add(button_panel, BorderLayout.SOUTH);
        this.add(main_panel);
    }

    /**
     * Affiche le menu principal.
     */
    public void showMenu() {
        cardLayout.show(main_panel, home_mode);
    }

    /**
     * Crée le panel avec les boutons.
     * @return JPanel des boutons
     */
    private JPanel makeButtonPanel() {
        JPanel res = new JPanel();
        res.setLayout(new GridLayout(1, 3));

        JButton home = new JButton("HOME");
        home.addActionListener(hbl);
        JButton rules = new JButton("RULES");
        rules.addActionListener(rbl);
        JButton play = new JButton("PLAY");
        play.addActionListener(pbl);
        JButton settings = new JButton("SETTINGS");
        settings.addActionListener(sbl);

        JButton quit = new JButton("QUIT");
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                frame.playSoundClick("MENU" ,"ChangePageInTheMenu.wav" );
                frame.quitGame();
            }
        });

        JButton[] bl = {home, rules, play, settings, quit};
        for (JButton button : bl) {
            button.setBorderPainted(false);
            button.setBackground(Color.BLACK);
            button.setForeground(Color.GRAY);
        }

        res.add(home);
        res.add(rules);
        res.add(play);
        res.add(settings);
        res.add(quit);

        return res;
    }

    /**
     * Crée le panel principal qui sera affiché dans le JFrame quand le mode est "menu".
     * @return le panel principal
     */
    private JPanel makeMainPanel() {
        JPanel res = new JPanel();
        res.setLayout(cardLayout);

        res.add(home_mode, makeHome());
        res.add(play_mode, makePlay());
        res.add(settings_mode, makeSettings());
        res.add(rules_mode, makeRules());

        cardLayout.show(res, home_mode);

        return res;
    }

    /**
     * Crée le JPanel pour l'accueil.
     * @return JPanel de Home
     */
    private JPanel makeHome() {
        return new Home(width, height);
    }

    /**
     * Crée le menu de jeu où l'on peut choisir un niveau et une difficulté, et où l'on peut lancer le jeu.
     * @return JPanel de Play
     */
    private JPanel makePlay() {
        return new Play(this.frame);
    }

    /**
     * Crée le menu des paramètres où il y a les réglages de largeur et hauteur, et aussi le mode plein écran.
     * @return JPanel de Settings
     */
    private JPanel makeSettings() {
        return new Settings(this);
    }

    /**
     * Crée le menu des règles.
     * @return JPanel de Rules
     */
    private JPanel makeRules() {
        return new Rules(width, height);
    }

    /**
     * Définit la taille de tous les composants.
     * @param width la largeur
     * @param height la hauteur
     */
    public void setAllSize(int width, int height) {
        this.width = width;
        this.height = height;
        frame.setSize(width, height);
        this.setSize(width, height);
        removeAll();
        make();
        cardLayout.show(main_panel, settings_mode);
    }

    /**
     * Obtient l'appareil graphique.
     * @return l'appareil graphique
     */
    public GraphicsDevice getDevice() {
        return frame.getDevice();
    }

    /**
     * Obtient le frame du jeu.
     * @return le GameFrame
     */
    public GameFrame getFrame() {
        return frame;
    }
}

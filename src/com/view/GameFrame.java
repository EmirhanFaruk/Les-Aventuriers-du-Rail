package com.view;

import com.controller.Main;
import com.model.Sound;
import com.view.mainmenu.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

/**
 * La classe GameFrame représente la fenêtre principale de l'application de jeu.
 * Elle utilise un CardLayout pour naviguer entre différents écrans de jeu tels que le menu principal, le jeu en cours, la pause et la fin de jeu.
 */
public class GameFrame extends JFrame {
    public static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];

    private double scale;

    // Pour changer le mode
    private JPanel main_panel;
    private final CardLayout cardLayout = new CardLayout();

    private String currentCard;

    private GameScreen gameScreen;

    private final String main_menu_screen_s = "MAIN MENU", ingame_screen_s = "INGAME", pause_screen_s = "PAUSE", endgame_screen_s = "ENDGAME";

    private Menu menu;

    private Main main;

    private Sound sound;

    /**
     * Constructeur de GameFrame, initialisant les attributs et configurant l'interface utilisateur.
     *
     * @param width  La largeur de la fenêtre
     * @param height La hauteur de la fenêtre
     * @param main   L'objet principal Main de l'application
     */
    public GameFrame(int width, int height, Main main) {
        // Les attributs de JPanel
        this.setTitle("Tchu Tchuuu");
        this.setSize(width, height);
        this.setPreferredSize(new Dimension(width, height));
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.sound = new Sound();
        playMusicIsMusic("MENU" , "tchu-tchu-song.wav" );

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
        setLocationRelativeTo(null);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                try {
                    if (currentCard.equals(ingame_screen_s) || currentCard.equals(pause_screen_s)) {
                        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                            try {
                                main.pause();
                            } catch (Exception ignored) {
                            }
                        }
                    }
                } catch (Exception ignored) {
                }
            }

        });

        setFocusable(true);
        requestFocusInWindow();
        this.setVisible(true);
    }

    /**
     * Démarre une nouvelle partie avec les paramètres spécifiés.
     *
     * @param map          La carte sélectionnée
     * @param mode         Le mode de jeu
     * @param player_names Les noms des joueurs
     * @param player_types Les types des joueurs
     * @param player_Colors Les couleurs des joueurs
     */
    public void startGame(String map, String mode, String[] player_names, String[] player_types, Color[] player_Colors) {
        main.startGame(map, mode, player_names, player_types, player_Colors);
        if (sound.getMusic()) {
            sound.changeMusic("INGAME", "inGame.wav");
        }
        gameScreen = null;

        gameScreen = new GameScreen(this, map, getWidth(), getHeight(), main.game.getListPlayer().get(main.getGame().getRound().getWhoIsPlaying()), main.game);

        main_panel.add(ingame_screen_s, gameScreen);
        setMinimumSize(getSize());
        setMinimumSize(null);
        gameScreen.getGameManagerScreen().make(main.game.getPlateau());
        cardLayout.show(main_panel, ingame_screen_s);
        currentCard = ingame_screen_s;
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        if (main_panel != null) {
            main_panel.setSize(width, height);
        }
    }

    /**
     * Retourne au menu principal.
     */
    public void quitMainMenu() {
        cardLayout.show(main_panel, main_menu_screen_s);
        currentCard = main_menu_screen_s;
        menu.showMenu();
        main.setRunning(false);
    }

    /**
     * Quitte le jeu.
     */
    public void quitGame() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }


    public void playSoundClick(String screen, String sound_name)
    {
        if(sound.getclick())
        {
            playSound(screen, sound_name);
        }
    }

    public void playSound(String screen, String sound_name)
    {
        sound.playSound( screen ,sound_name );
    }


    public void playMusic(String screen, String sound_name)
    {
        sound.playMusic(screen, sound_name);
    }

    public void playMusicIsMusic(String screen, String sound_name)
    {
        if (getSound().getMusic())
        {
            playMusic(screen, sound_name);
        }
    }

    public void changeMusic(String screen, String sound_name)
    {
        sound.changeMusic( screen ,sound_name );
    }

    public void changeMusicIsMusic(String screen, String sound_name)
    {
        if (getSound().getMusic())
        {
            changeMusic(screen, sound_name);
        }
    }


    /* getters et setters */
    public GraphicsDevice getDevice()
    {
        return device;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public MapScreen getMapScreen() {
        if (gameScreen != null) {
            return gameScreen.getMapScreen();
        }

        return null;
    }

    public GameMapPanel getGameMapPanel() {
        if (gameScreen != null) {
            return gameScreen.getGameMapPanel();
        }

        return null;
    }

    public void setCurrentCard(String currentCard) {
        this.currentCard = currentCard;
    }

    public String getIngame_screen_s() {
        return ingame_screen_s;
    }

    public String getEndgame_screen_s() {
        return endgame_screen_s;
    }

    public String getPause_screen_s() {
        return pause_screen_s;
    }

    public Main getMain() {
        return main;
    }

    public Sound getSound() {
        return sound;
    }

    public String getMode() {
        return main.getMode();
    }
}

package com.controller;

import com.model.Game;
import com.view.GameFrame;

import java.awt.*;

/**
 * Classe principale du contrôleur qui gère le cycle de vie du jeu, y compris le lancement, la pause, et le redémarrage.
 */
public class Main implements Runnable {
    public GameFrame gameFrame;
    public Game game;
    private Thread game_thread;
    private String map;
    private String mode;
    private String[] player_names;
    private String[] player_types;
    private Color[] player_colors;
    private boolean running = false;

    /**
     * Lance le jeu en initialisant le GameFrame et le Game.
     */
    public void lance() {
        this.gameFrame = new GameFrame(1280, 720, this);
        this.game = new Game(gameFrame);
    }

    /**
     * Démarre le thread du jeu.
     */
    private void startGame_thread() {
        game_thread = new Thread(this);
        game_thread.start();
    }

    /**
     * Redémarre le jeu avec les mêmes paramètres.
     */
    public void restart() {
        gameFrame.startGame(this.map, this.mode, this.player_names, this.player_types, this.player_colors);
        gameFrame.setCurrentCard(gameFrame.getIngame_screen_s());

        gameFrame.changeMusicIsMusic("INGAME" ,"inGame.wav" );
        // DEBUG : System.err.println("Une nouvelle game");
    }

    /**
     * Démarre une nouvelle partie avec les paramètres spécifiés.
     *
     * @param map           La carte du jeu.
     * @param mode          Le mode de jeu.
     * @param player_names  Les noms des joueurs.
     * @param player_types  Les types des joueurs.
     * @param player_colors Les couleurs des joueurs.
     */
    public void startGame(String map, String mode, String[] player_names, String[] player_types, Color[] player_colors) {
        running = true;
        this.map = map;
        this.mode = mode;

        this.player_names = player_names;
        this.player_types = player_types;
        this.player_colors = player_colors;
        game.makeGame(map,player_names,player_types,player_colors);
        startGame_thread();
    }

    /**
     * Affiche le panneau approprié en fonction de l'état de pause du jeu.
     */
    public void pause() {
        if (running) {
            running = false;
            gameFrame.setCurrentCard(gameFrame.getPause_screen_s());

            game.getGameFrame().changeMusicIsMusic("PAUSE" , "pause.wav");

            gameFrame.getGameScreen().getGameManagerScreen().showPause();
        } else {
            running = true;
            gameFrame.setCurrentCard(gameFrame.getIngame_screen_s());

            game.changeMusicIsMusic("INGAME" , "inGame.wav");

            gameFrame.getGameScreen().getGameManagerScreen().removePause();
        }
    }

    /**
     * Fonction principale qui exécute la boucle de jeu.
     */
    @Override
    public void run() {
        double start;
        double required_fps = (double) 1000000000 / 60;
        double end = required_fps;
        while (running) {
            start = System.nanoTime();
            if (end >= required_fps) {
                game.updateGame(end / 1000000000);
                //GameManagerScreen updateGraphics = this.gameFrame.getGameScreen().getGameManagerScreen();
                //if(updateGraphics != null)updateGraphics.update();
                end = System.nanoTime() - start;
            } else {
                end += System.nanoTime() - start;
            }
        }
    }

    /* Getters et Setters */

    /**
     * Définit l'état de fonctionnement du jeu.
     *
     * @param b true pour démarrer le jeu, false pour le mettre en pause.
     */
    public void setRunning(boolean b) {
        running = b;
    }

    /**
     * Retourne l'objet Game associé à ce contrôleur.
     *
     * @return L'objet Game.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Retourne le mode de jeu actuel.
     *
     * @return Le mode de jeu.
     */
    public String getMode() {
        return mode;
    }

    /**
     * Retourne la carte du jeu actuelle.
     *
     * @return La carte du jeu.
     */
    public String getMap() {
        return map;
    }
}
package com.controller;

import com.model.Game;
import com.view.GameFrame;

public class Main implements Runnable
{
    public GameFrame gameFrame;
    public Game game ;
    private Thread game_thread;

    private String map ;
    private String[] player_names  ;
    private String[] player_types ;
    private boolean running = false;

    public void lance()
    {
        this.gameFrame = new GameFrame(800, 500, this);
        this.game =new Game( gameFrame);
    }

    private void startGame_thread()
    {
        game_thread = new Thread(this);
        game_thread.start();
    }

    public void restart (){
        startGame( this.map , this.player_names , this.player_types );
        System.err.println("Une nouvelle game");
    }
    public void startGame(String map, String[] player_names, String[] player_types)
    {
        running = true;
        this.map = map ;
        this.player_names = player_names ;
        this.player_types = player_types ;
        game.makeGame(map);
        startGame_thread();
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
            //System.out.println("running");
            start = System.nanoTime();
            if(end >= required_fps)
            {
                game.updateGame( );
                end = System.nanoTime() - start;
            }
            else
            {
                end += System.nanoTime() - start;
            }
            //game.dinumueCarte();
        }
    }


    public void setRunning(boolean b)
    {
        running = b;
    }

    public Game getGame() {
        return game;
    }


}

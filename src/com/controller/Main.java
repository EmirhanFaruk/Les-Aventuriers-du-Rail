package com.controller;

import com.model.Game;
import com.view.GameFrame;

public class Main implements Runnable
{
    public GameFrame gameFrame;
    public Game game = new Game();
    private Thread game_thread;
    private boolean running = false;

    public void lance()
    {
        GameFrame gameFrame = new GameFrame(800, 500, this);
    }

    private void startGame_thread()
    {
        game_thread = new Thread(this);
        game_thread.start();
    }

    public void startGame(String map, String[] player_names, String[] player_types)
    {
        running = true;
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
                game.updateGame(end/1000000000);
                end = System.nanoTime() - start;
            }
            else
            {
                end += System.nanoTime() - start;
            }
        }
    }


    public void setRunning(boolean b)
    {
        running = b;
    }
}

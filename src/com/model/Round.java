package com.model;

import com.model.ai.GarePosFinder;
import com.model.bot.NormalBot;
import com.model.bot.StrongBot;
import com.model.bot.WeakBot;
import com.model.config.carte.CarteManager;

import com.view.GameMapPanel;
import com.view.MapScreen;
import com.view.PlayerHandPanel;

public class Round {

    private boolean playing = true; //Le jeu en pose ou pas
    private int whoIsPlaying = 0; //Quel joueur est entrain de jouer
    private boolean endTurn = false; //Si le tour est finis ou non
    private int action = 2; //Le nombre d'action qu'il reste pour piocher une carte wagon
    private WeakBot weakBotPlay= new WeakBot();
    private NormalBot normalBotPlay = new NormalBot();
    private StrongBot strongBotPlay = new StrongBot();


    public int getWhoIsPlaying() {
        return whoIsPlaying;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public void setEndTurn(boolean endTurn) {
        this.endTurn = endTurn;
    }



    private double betweenRoundTimer = 0; // Comme ça on peut voir pour 1 seconde ce qui ce passe quand les bots jouent
    private final double betweenRoundTimerMax = 1; // Valeur max de timer


    public boolean roundFinished(){
        //Savoir si le joueur/ia a fini de jouer ou non
        return this.endTurn;
    }

    public void endRound(Game game) {
        if (whoIsPlaying == 0)
        {
            //GarePosFinder.printForAll(game.getVilles(), 2, true, game.getListPlayer().get(whoIsPlaying));
            //GarePosFinder.printForAll(game.getVilles(), 1, false, game.getListPlayer().get(whoIsPlaying));
            //GarePosFinder.printForAllDiff(game.getVilles(), 1, true, game.getListPlayer().get(whoIsPlaying));
            //GarePosFinder.printForAllDiff(game.getVilles(), 0, false, game.getListPlayer().get(whoIsPlaying));
        }



        //Fonction qui finit le tour du bot
        setEndTurn(true);
        whosNext(game);

        //Variable pour avoir Player
        Player joueur = game.getListPlayer().get(whoIsPlaying);

        //Variable pour avoir acces au gameMapPanel
        GameMapPanel gameMapPanel = game.getGameMapPanel();

        //Variable pour avoir acces au PlayerHandPanel
        PlayerHandPanel playerHandPanel = gameMapPanel.getPlayerHandPanel();

        gameMapPanel.setPlayerCourant(joueur);

        gameMapPanel.getMapScreen().repaintAll(playerHandPanel);


        // DEBUG :System.out.println(whoIsPlaying);
    }
    public void whosNext(Game game){
        //Passer au prochain joueur

        //On reset le round
        this.endTurn = false;
        this.action = 2;

        //On change de joueur
        if(whoIsPlaying == game.getListPlayer().size() -1){
            whoIsPlaying = 0;
        }
        else{
            whoIsPlaying ++;
        }

    }



    public void round(Game game, double deltaTime)
    {




        if (betweenRoundTimer <= 0)
        {
            switch (game.getListPlayer().get(whoIsPlaying).getNiveau()) {

                case(1):
                    weakBotPlay.play(game);

                    break;

                case(2):
                    normalBotPlay.play(game);
                    break;

                case(3):
                    strongBotPlay.play(game);
                    break;

                default:
                    break;
            }

            // Soit le joueur, soit les bots doivent rendre action 0 pour que leur tour finissent

            if (action == 0)
            {
                whosNext(game);
                betweenRoundTimer = betweenRoundTimerMax;
            }
        }
        else
        {
            betweenRoundTimer -= deltaTime;
        }


    }



}

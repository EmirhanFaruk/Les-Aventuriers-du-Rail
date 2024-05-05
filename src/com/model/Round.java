package com.model;

import com.model.ai.GarePosFinder;
import com.model.bot.NormalBot;
import com.model.bot.StrongBot;
import com.model.bot.WeakBot;
import com.model.config.carte.CarteManager;

import com.view.GameMapPanel;
import com.view.MapScreen;
import com.view.PlayerHandPanel;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

public class Round {

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



    private final double betweenRoundTimerMax = 0.5; // Valeur max de timer
    private double betweenRoundTimer = 0; // Comme ça on peut voir pour 1 seconde ce qui ce passe quand les bots jouent


    public boolean roundFinished(){
        //Savoir si le joueur/ia a fini de jouer ou non
        return this.endTurn;
    }

    public void endRound(Game game) {

        //Fonction qui finit le tour du bot
        setEndTurn(true);
        
        //Piocher une carte destination comptera comme une action maintenant
        game.getJoueurCourant().setFirstTurnOver(true);
        
        //Variable pour avoir le prochain Player
        whosNext(game);
        Player joueur = game.getJoueurCourant();

        //Variable pour avoir acces au gameMapPanel
        GameMapPanel gameMapPanel = game.getGameMapPanel();

        //Variable pour avoir acces au PlayerHandPanel
        PlayerHandPanel playerHandPanel = gameMapPanel.getPlayerHandPanel();
               
        //Reroll les cartes destinations (pour un autre joueur)
        game.getCarteManager().rerollDestination();

        //Change de joueur courant
        gameMapPanel.setPlayerCourant(joueur);
        
        //Change toutes les images pour le nouveau joueur
        gameMapPanel.getMapScreen().repaintAll(playerHandPanel);

        //On repaint a chaque fois
        game.getMapScreen().repaint();


        betweenRoundTimer = betweenRoundTimerMax;
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
        Player joueur = game.getListPlayer().get(whoIsPlaying);
        if (betweenRoundTimer <= 0)
        {
            switch (joueur.getNiveau()) {

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
        }
        else
        {
            if (joueur.getNiveau() != 0)
            {
                betweenRoundTimer -= deltaTime;
            }
            else
            {
                betweenRoundTimer = betweenRoundTimerMax;
            }
        }


    }



}

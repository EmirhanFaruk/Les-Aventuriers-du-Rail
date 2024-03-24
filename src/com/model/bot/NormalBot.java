package com.model.bot;

import com.model.Game;
import com.model.Round;
import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteManager;

public class NormalBot implements BotAction{
    @Override
    public void drawCardWagon(Round round,CarteManager carteManager, Game game) {

    }

    @Override
    public boolean takeRail(Game game,Round round) {

        return true;
    }

    @Override
    public boolean takeGare(Game game, int wichStation, Round round) {

        return true;
    }

    @Override
    public CarteDestination[] takeMissionsCard(int max, CarteManager carteManager, Game game){

    return new CarteDestination[5];
    }

    @Override
    public void endRound(Round round,Game game) {
        //Fonction qui finit le tour du bot
        round.setEndTurn(true);
        round.whosNext(game);
    }

    @Override
    public void play(Game game, CarteManager carteManager, Round round) {

    }


}

package com.model.bot;

import com.model.Game;
import com.model.Round;
import com.model.config.carte.CarteManager;
import com.model.config.carte.CarteWagon;

public interface BotAction {

    //Piocher une/des cartes wagons visible
    void drawCardWagon();

    //Prendre des rails
    void takeRail();

    //Poser une gare
    void takeGare();

    //Prendre une/des cartes missions
    void takeMissionsCard();

    //La fonction principale du bot
    void play(Game game, CarteManager carteManager, Round round);


}

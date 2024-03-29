package com.model.bot;

import com.model.Game;
import com.model.Round;
import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteManager;
import com.model.config.carte.CarteWagon;

public interface BotAction {

    //Piocher une/des cartes wagons visible
    abstract void drawCardWagon(Round round,CarteManager carteManager, Game game);

    //Prendre des rails
    abstract boolean takeRail(Game game,Round round);

    //Poser une gare
    abstract boolean takeGare(Game game, int wichStation, Round round);

    //Prendre une/des cartes missions
    abstract CarteDestination[] takeMissionsCard(int max, CarteManager carteManager, Game game);

    //La fonction principale du bot
    abstract void play(Game game, CarteManager carteManager, Round round);


}

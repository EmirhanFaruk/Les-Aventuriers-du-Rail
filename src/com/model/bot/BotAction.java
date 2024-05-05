package com.model.bot;

import com.model.Game;
import com.model.Round;
import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteManager;
import com.model.config.carte.CarteWagon;

public interface BotAction {

    //Piocher une/des cartes wagons visible
    void drawCardWagon(Game game);

    //Prendre des rails
    boolean takeRail(Game game);

    //Poser une gare
    boolean takeGare(Game game, int wichStation);

    //Prendre une/des cartes missions
    CarteDestination[] takeMissionsCard(int max, Game game);

    //La fonction principale du bot
    void play(Game game);


}

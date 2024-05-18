package com.model.bot;

import com.model.Game;
import com.model.Round;
import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteManager;
import com.model.config.carte.CarteWagon;

/**
 * Interface représentant les actions qu'un bot peut effectuer le jeu
 */
public interface BotAction {

    /**
     * Permet au bot de piocher une ou plusieurs cartes wagons visibles.
     *
     * @param game Le jeu actuel dans lequel le bot évolue.
     */
    void drawCardWagon(Game game);

    /**
     * Permet au bot de prendre des rails sur le plateau de jeu.
     *
     * @param game Le jeu actuel dans lequel le bot évolue.
     * @return {@code true} si l'action est réussie, {@code false} sinon.
     */
    boolean takeRail(Game game);

    /**
     * Permet au bot de poser une gare sur le plateau de jeu.
     *
     * @param game Le jeu actuel dans lequel le bot évolue.
     * @param wichStation L'index de la station que le bot souhaite poser(seulement utilisé avec le WeakBot).
     * @return {@code true} si l'action est réussie, {@code false} sinon.
     */
    boolean takeGare(Game game, int wichStation);

    /**
     * Permet au bot de prendre une ou plusieurs cartes missions.
     *
     * @param max Le nombre maximal de points d'une carte destination/l'accumulation des cartes destination que le bot peut prendre.
     * @param game Le jeu actuel dans lequel le bot évolue.
     * @return Un tableau de cartes missions que le bot a prises.
     */
    CarteDestination[] takeMissionsCard(int max, Game game);

    /**
     * La fonction principale du bot pour jouer son tour.
     *
     * @param game Le jeu actuel dans lequel le bot évolue.
     */
    void play(Game game);
}

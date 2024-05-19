package com.model.bot;

import com.model.Game;
import com.model.Player;
import com.model.ai.GarePosFinder;
import com.model.ai.Node;
import com.model.config.Rail;
import com.model.config.Route;
import com.model.config.Ville;
import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteWagon;
import java.util.ArrayList;
import java.util.Random;

/**
 * Le bot "NormalBot" est une implémentation de l'interface BotAction.
 * Il représente un bot de la difficulté normale qui joue de manière semi-aléatoire en utilisant différentes stratégies de jeu.
 */
public class NormalBot implements BotAction {

    private final StrongBot strongBot = new StrongBot();
    private final WeakBot weakBot = new WeakBot();


    /**
     * Permet au bot de piocher une ou plusieurs cartes wagons visibles ou de la pioche.
     *
     * @param game Le jeu actuel.
     */
    @Override
    public void drawCardWagon(Game game) {
        strongBot.drawCardWagon(game);
    }

    /**
     * Permet au bot de prendre des rails (poser des wagons) sur le plateau de jeu.
     *
     * @param game Le jeu actuel.
     * @return {@code true} si le bot a réussi à poser des wagons sur une route, {@code false} sinon.
     */
    @Override
    public boolean takeLoad(Game game) {
        return weakBot.takeLoad(game);
    }

    /**
     * Permet au bot de poser une gare sur le plateau de jeu.
     *
     * @param game        Le jeu actuel.
     * @param whichStation L'index de la gare à poser, qu'il ne sera pas utilisé ici.
     * @return {@code true} si le bot a réussi à poser une gare, {@code false} sinon.
     */
    @Override
    public boolean useGare(Game game, int whichStation) {
        return strongBot.useGare(game,whichStation);

    }

    /**
     * Permet au bot de prendre une ou des cartes missions.
     *
     * @param max  Le nombre maximal de points d'une carte destination/l'accumulation des cartes destination que le bot peut prendre.
     * @param game Le jeu actuel.
     * @return Un tableau de cartes missions prises par le bot.
     */
    @Override
    public CarteDestination[] takeMissionsCard(int max, Game game) {
       return strongBot.takeMissionsCard(max,game);
    }


    /**
     * Permet au bot d'utiliser les cartes nuke de manière aléatoire.
     *
     * @param game Le jeu en cours.
     * @return Un boolean pour dire si l'action a bien était fait.
     */
    @Override
    public boolean useNuke(Game game) {
        return weakBot.useNuke(game);
    }


    /**
     * Procède au premier tour du bot en ajoutant des missions et en choisissant les actions de manière optimale.
     *
     * @param game Le jeu en cours.
     */
    private void firstTurn(Game game) {
        strongBot.firstTurn(game);
    }




    /**
     * La fonction principale du bot pour jouer son tour.
     *
     * @param game Le jeu actuel.
     */
    @Override
    public void play(Game game) {

        //Premier tour du bot
        if (!game.getJoueurCourant().getFirstTurnOver()) {
            firstTurn(game);


        } else {

            Random random = new Random();

            int action;

            //On regarde on est en quel mode, si on est en mode nuke, on a une action en plus
            if(game.getGameFrame().getMain().getMode() == "NORMAL"){
                action = random.nextInt(4);

            }else{
                action = random.nextInt(5);
            }
            switch (action) {
                case 0:
                    // Piocher des cartes wagons
                    drawCardWagon(game);
                    break;
                case 1:
                    // Piocher des cartes missions
                    CarteDestination[] newMissions = takeMissionsCard(8, game);
                    Player currentPlayer = game.getJoueurCourant();
                    for (CarteDestination mission : newMissions) {
                        currentPlayer.getDestinationsList().add(mission);
                    }
                    break;
                case 2:
                    // Poser des wagons
                    if (!takeLoad(game)) {
                        play(game);
                    }
                    break;
                case 3:
                    // Poser une gare
                    if (!useGare(game, 0)) {
                        play(game);
                    }
                    break;

                default:
                    //Utiliser une nuke
            }

            // Fin du tour
            game.getRound().endRound(game);
        }



    }
}

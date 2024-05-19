package com.model.bot;

import com.model.Game;
import com.model.Player;
import com.model.Round;
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

    /**
     * Vérifie s'il manque seulement une carte pour compléter une route spécifique.
     *
     * @param route La route à vérifier.
     * @param game  Le jeu actuel.
     * @return {@code true} s'il ne manque qu'une carte pour compléter la route, {@code false} sinon.
     */
    public boolean missOneCardOnly(Route route, Game game) {
        Player player = game.getJoueurCourant();
        ArrayList<CarteWagon.Couleur> playerTrainList = player.getTrainList();
        int count = 0;

        for (CarteWagon.Couleur couleur : playerTrainList) {
            if (player.compatibleColor(route, couleur)) {
                count++;
            }
        }

        return count + 1 == route.getLongueur();
    }

    /**
     * Vérifie s'il est possible de compléter une route en ne manquant qu'une carte.
     *
     * @param game Le jeu actuel.
     * @return La couleur de la carte manquante si possible, sinon {@code null}.
     */
    public Rail.Content canCompletePathMissingOneCard(Game game) {
        Player player = game.getJoueurCourant();
        ArrayList<CarteDestination> destinations = player.getDestinationsList();

        for (CarteDestination destination : destinations) {
            ArrayList<Ville> villes = Node.findClosestPath(destination.getPremiereVille(), destination.getDeuxiemeVille());
            ArrayList<Route> routes = GarePosFinder.getNeededRoutes(villes, player);

            for (Route route : routes) {
                if (missOneCardOnly(route, game)) {
                    return route.getCouleur();
                }
            }
        }
        return null;
    }

    /**
     * Permet au bot de piocher une ou plusieurs cartes wagons visibles ou de la pioche.
     *
     * @param game Le jeu actuel.
     */
    @Override
    public void drawCardWagon(Game game) {
        Player player = game.getListPlayer().get(game.getRound().getWhoIsPlaying());
        Rail.Content missingColor = canCompletePathMissingOneCard(game);

        if (missingColor != null) {
            // Trouver la position de la carte manquante dans les cartes visibles
            int position = -1;
            CarteWagon.Couleur[] visibleCards = game.getCarteManager().getTrainCards();

            for (int i = 0; i < visibleCards.length; i++) {
                if (visibleCards[i].ordinal() == missingColor.ordinal()) {
                    position = i;
                    break;
                }
            }

            if (position != -1) {
                player.getTrainList().add(game.getCarteManager().takeWagon(position));
                player.getTrainList().add(game.getCarteManager().drawCard());
            } else {
                // Chercher une carte locomotive parmi les cartes visibles
                int positionLoc = -1;

                for (int i = 0; i < visibleCards.length; i++) {
                    if (visibleCards[i] == CarteWagon.Couleur.LOC) {
                        positionLoc = i;
                        break;
                    }
                }

                if (positionLoc != -1) {
                    player.getTrainList().add(game.getCarteManager().takeWagon(positionLoc));
                } else {
                    // Sinon, piocher deux cartes de la pioche
                    player.getTrainList().add(game.getCarteManager().drawCard());
                    player.getTrainList().add(game.getCarteManager().drawCard());
                }
            }
        } else {
            // Si aucune couleur spécifique n'est recherchée, piocher deux cartes aléatoires
            player.getTrainList().add(game.getCarteManager().drawCard());
            player.getTrainList().add(game.getCarteManager().drawCard());
        }
    }

    /**
     * Permet au bot de prendre des rails (poser des wagons) sur le plateau de jeu.
     *
     * @param game Le jeu actuel.
     * @return {@code true} si le bot a réussi à poser des wagons sur une route, {@code false} sinon.
     */
    @Override
    public boolean takeRail(Game game) {
        Player player = game.getJoueurCourant();

        for (Route route : game.getRoutes()) {
            if (player.mettreRoute(route)) {
                // Si le joueur a pu poser des wagons sur la route
                for (Rail rail : route.getRailsRoute()) {
                    rail.setOccuperPar(player);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Permet au bot de poser une gare sur le plateau de jeu.
     *
     * @param game        Le jeu actuel.
     * @param wichStation L'index de la gare à poser, qu'il ne sera pas utilisé ici.
     * @return {@code true} si le bot a réussi à poser une gare, {@code false} sinon.
     */
    @Override
    public boolean takeGare(Game game, int wichStation) {
        Player player = game.getJoueurCourant();
        ArrayList<CarteDestination> carteDestinations = player.getDestinationsList();

        if (player.getNbrGare() > 0) {
            for (CarteDestination cd : carteDestinations) {
                Ville ville1 = cd.getPremiereVille();
                Ville ville2 = cd.getDeuxiemeVille();

                Ville garePos = GarePosFinder.getWantedVilleDiff(ville1, ville2, game.getVilles(), 1, false, player);

                if (garePos != null && !cd.getComplete()) {
                    player.transformerEnGare(garePos, player.getTrainList().get(0));
                    return true;
                }
            }
            return false;
        } else {
            // Si aucune gare n'est disponible, piocher des cartes missions
            CarteDestination[] toAdd = takeMissionsCard(6, game);
            for (CarteDestination cd : toAdd) {
                player.getDestinationsList().add(cd);
            }
            return true;
        }
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
        CarteDestination[] carteDestinations = game.getCarteManager().getDestinationsCards();
        ArrayList<Integer> indicesAChoisir = new ArrayList<>();

        int totalPoints = 0;

        for (int i = 0; i < carteDestinations.length; i++) {
            if (carteDestinations[i].getNombrePoints() + totalPoints <= max) {
                indicesAChoisir.add(i);
                totalPoints += carteDestinations[i].getNombrePoints();
            }
        }

        int[] indices = indicesAChoisir.stream().mapToInt(Integer::intValue).toArray();
        return game.getCarteManager().takeDestination(indices);
    }

    @Override
    public boolean useNuke(Game game) {
        return false;
    }


    /**
     * Procède au premier tour du bot en ajoutant des missions et en choisissant les actions de manière optimale.
     *
     * @param game Le jeu en cours.
     */
    private void firstTurn(Game game) {
        CarteDestination[] carteDestination = takeMissionsCard(6, game);

        for (int z = 0; z < carteDestination.length; z++) {
            game.getJoueurCourant().getDestinationsList().add(carteDestination[z]);
        }

        game.getJoueurCourant().setFirstTurnOver(true);
        optimalCompleteMission(game);
    }

    /**
     * Procède de manière optimale à compléter les missions du bot en prenant des rails, en posant des gares,
     * ou en piochant des cartes wagons.
     *
     * @param game Le jeu en cours.
     */
    private void optimalCompleteMission(Game game) {
        if (takeRail(game)) {
            game.getRound().endRound(game);
        } else {
            if (takeGare(game, 0)) {
                game.getRound().endRound(game);
            } else {
                drawCardWagon(game);
                game.getRound().endRound(game);
            }
        }
    }



    /**
     * La fonction principale du bot pour jouer son tour.
     *
     * @param game Le jeu actuel.
     */
    @Override
    public void play(Game game) {
        Random random = new Random();
        int action = random.nextInt(4);

        if (!game.getJoueurCourant().getFirstTurnOver()) {
            firstTurn(game);
        } else {
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
                    if (!takeRail(game)) {
                        play(game);
                    }
                    break;
                default:
                    // Poser une gare
                    if (!takeGare(game, 0)) {
                        play(game);
                    }
                    break;
            }

            // Fin du tour
            game.getRound().endRound(game);
        }



    }
}

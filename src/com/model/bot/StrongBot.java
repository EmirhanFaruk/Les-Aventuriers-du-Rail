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
/**
 * Il représente un bot de la difficulté normale qui joue de manière semi-aléatoire en utilisant différentes stratégies de jeu.
 */
/**
 * Le bot "StrongBot" est une implémentation de l'interface BotAction.
 * Ce bot est le plus sophistiqué pour prendre des décisions dans le jeu.
 * Il suit une logique calculée et optimisée pour maximiser ses chances de victoire.
 */
public class StrongBot implements BotAction {

    /**
     * Vérifie s'il manque une seule carte pour compléter une route.
     *
     * @param route La route à vérifier.
     * @param game Le jeu en cours.
     * @return true s'il manque une seule carte pour compléter la route, sinon false.
     */
    public boolean missOneCardOnly(Route route, Game game) {
        Player player = game.getJoueurCourant();
        ArrayList<CarteWagon.Couleur> playerTrainList = player.getTrainList();
        int count = 0;

        for (int i = 0; i < playerTrainList.size(); i++) {
            if (player.compatibleColor(route, playerTrainList.get(i))) count++;
        }

        return count + 1 == route.getLongueur();
    }

    /**
     * Détermine la couleur de carte manquante pour compléter un chemin.
     *
     * @param game Le jeu en cours.
     * @return La couleur de la carte manquante pour compléter un chemin, ou null s'il n'en manque aucune.
     */
    public Rail.Content canCompletePathMissingOneCard(Game game) {
        Player player = game.getJoueurCourant();
        ArrayList<CarteDestination> destination = player.getDestinationsList();

        for (int i = 0; i < destination.size(); i++) {
            ArrayList<Ville> villes = Node.findClosestPath(destination.get(i).getPremiereVille(), destination.get(i).getDeuxiemeVille());
            ArrayList<Route> routesPossible = GarePosFinder.getNeededRoutes(villes, game.getJoueurCourant());

            for (int z = 1; z < routesPossible.size(); z++) {
                if (missOneCardOnly(routesPossible.get(z), game)) {
                    return routesPossible.get(z).getCouleur();
                }
            }
        }
        return null;
    }

    /**
     * Permet au bot de piocher des cartes wagons de manière stratégique.
     *
     * @param game Le jeu en cours.
     */
    @Override
    public void drawCardWagon(Game game) {
        Player player = game.getListPlayer().get(game.getRound().getWhoIsPlaying());
        Rail.Content color = canCompletePathMissingOneCard(game);
        if (color != null) {
            int position = -1;

            for (int i = 0; i < game.getCarteManager().getTrainCards().length; i++) {
                if (game.getCarteManager().getTrainCards()[i].ordinal() == color.ordinal()) {
                    position = i;
                }
            }

            if (position != -1) {
                player.getTrainList().add(game.getCarteManager().takeWagon(position));
                player.getTrainList().add(game.getCarteManager().drawCard());
            } else {
                int positionLoc = -1;

                for (int i = 0; i < game.getCarteManager().getTrainCards().length; i++) {
                    if (game.getCarteManager().getTrainCards()[i] == CarteWagon.Couleur.LOC) {
                        positionLoc = i;
                    }
                }

                if (positionLoc != -1) {
                    player.getTrainList().add(game.getCarteManager().takeWagon(positionLoc));
                } else {
                    player.getTrainList().add(game.getCarteManager().drawCard());
                    player.getTrainList().add(game.getCarteManager().drawCard());
                }
            }
        } else {
            player.getTrainList().add(game.getCarteManager().drawCard());
            player.getTrainList().add(game.getCarteManager().drawCard());
        }
    }

    /**
     * Permet au bot de prendre des rails (poser des wagons) pour compléter des routes de manière optimale.
     *
     * @param game Le jeu en cours.
     * @return true si le bot a réussi à prendre une route, sinon false.
     */
    @Override
    public boolean takeRail(Game game) {
        ArrayList<CarteDestination> destination = game.getJoueurCourant().getDestinationsList();

        for (int i = 0; i < destination.size(); i++) {
            ArrayList<Ville> villes = Node.findClosestPath(destination.get(i).getPremiereVille(), destination.get(i).getDeuxiemeVille());
            ArrayList<Route> routesPossible = GarePosFinder.getNeededRoutes(villes, game.getJoueurCourant());

            for (int z = 0; z < routesPossible.size(); z++) {
                if (game.getJoueurCourant().mettreRoute(routesPossible.get(z))) {
                    ArrayList<Rail> listeRail = routesPossible.get(z).getRailsRoute();
                    Player bot = game.getListPlayer().get(game.getRound().getWhoIsPlaying());

                    for (Rail rail : listeRail) {
                        rail.setOccuperPar(bot);
                    }
                    //DEBUG :System.err.println("takeRail True");
                    return true;
                }
            }
        }

        //DEBUG :System.out.println("takeRail False");
        return false;
    }

    /**
     * Permet au bot de poser une gare de manière stratégique.
     *
     * @param game Le jeu en cours.
     * @param wichStation Pas utilisé dans la fonction.
     * @return true si la gare a été posée avec succès, sinon false.
     */
    @Override
    public boolean takeGare(Game game, int wichStation) {
        ArrayList<CarteDestination> carteDestinations = game.getJoueurCourant().getDestinationsList();
        Player joueur = game.getJoueurCourant();

        if (joueur.getNbrGare() > 0) {
            for (CarteDestination cd : carteDestinations) {
                Ville ville1 = cd.getPremiereVille();
                Ville ville2 = cd.getDeuxiemeVille();

                Ville toTransformInGare = GarePosFinder.getWantedVilleDiff(ville1, ville2, game.getVilles(), 1, false, joueur);

                if (toTransformInGare != null && !cd.getComplete()) {
                    joueur.transformerEnGare(toTransformInGare, joueur.getTrainList().get(0));
                    //DEBUG : System.err.println("takeGare true");
                    return true;
                }
            }
            return false;
        } else {
            CarteDestination[] toAdd = takeMissionsCard(6, game);

            for (int i = 0; i < toAdd.length; i++) {
                game.getJoueurCourant().getDestinationsList().add(toAdd[i]);
            }
            return true;
        }
    }

    /**
     * Permet au bot de piocher une ou des cartes missions de manière optimisée.
     *
     * @param max Le nombre maximum de points cumulés que les cartes missions piochées peuvent avoir.
     * @param game Le jeu en cours.
     * @return Un tableau de cartes missions piochées.
     */
    @Override
    public CarteDestination[] takeMissionsCard(int max, Game game) {
        CarteDestination[] carteDestinations = game.getCarteManager().getDestinationsCards();
        ArrayList<Integer> aPiocher = new ArrayList<>();
        int total = 0;

        for (int i = 0; i < carteDestinations.length; i++) {
            if (carteDestinations[i].getNombrePoints() + total <= max) {
                aPiocher.add(i);
                total += carteDestinations[i].getNombrePoints();
            }
        }

        int[] renvoie = new int[aPiocher.size()];
        for (int y = 0; y < renvoie.length; y++) {
            renvoie[y] = aPiocher.get(y);
        }

        return game.getCarteManager().takeDestination(renvoie);
    }

    @Override
    public boolean useNuke(Game game) {
        return false;
    }

    /**
     * Vérifie si toutes les missions du bot sont complétées.
     *
     * @param game Le jeu en cours.
     * @return true si toutes les missions sont complétées, sinon false.
     */
    private boolean allMissionIsCompleted(Game game) {
        for (int i = 0; i < game.getJoueurCourant().getDestinationsList().size(); i++) {
            if (!game.getJoueurCourant().getDestinationsList().get(i).getComplete()) {
                return false;
            }
        }
        return true;
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
     * La méthode principale du StrongBot, qui gère ses actions de manière optimale
     * en fonction de l'état du jeu et de ses objectifs stratégiques.
     *
     * @param game Le jeu en cours.
     */
    @Override
    public void play(Game game) {
        if (!game.getJoueurCourant().getFirstTurnOver()) {
            firstTurn(game);
        } else {
            if (allMissionIsCompleted(game)) {
                CarteDestination[] addCard = takeMissionsCard(6, game);
                for (int j = 0; j < addCard.length; j++) {
                    game.getJoueurCourant().getDestinationsList().add(addCard[j]);
                }
                game.getRound().endRound(game);
            } else {
                optimalCompleteMission(game);
            }
        }
    }
}

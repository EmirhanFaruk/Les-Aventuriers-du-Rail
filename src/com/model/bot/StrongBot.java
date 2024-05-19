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
 * Il représente un bot de la difficulté normale qui joue de manière semi-aléatoire en utilisant différentes stratégies de jeu.
 * Le bot "StrongBot" est une implémentation de l'interface BotAction.
 * Ce bot est le plus sophistiqué pour prendre des décisions dans le jeu.
 * Il suit une logique calculée et optimisée pour maximiser ses chances de victoire.
 */
public class StrongBot implements BotAction {

    /**
     * Vérifie s'il manque une seule carte pour compléter une route.
     *
     * @param route La route a vérifier.
     * @param game Le jeu en cours.
     * @return true s'il manque une seule carte pour compléter la route, sinon false.
     */
    public boolean missOneCardOnly(Route route, Game game) {
        Player player = game.getJoueurCourant();
        ArrayList<CarteWagon.Couleur> playerTrainList = player.getTrainList();
        int count = 0;

        for (CarteWagon.Couleur couleur : playerTrainList) {
            if (player.compatibleColor(route, couleur)) count++;
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

        for (CarteDestination carteDestination : destination) {
            ArrayList<Ville> villes = Node.findClosestPath(carteDestination.getPremiereVille(), carteDestination.getDeuxiemeVille());
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
    public boolean takeLoad(Game game) {
        ArrayList<CarteDestination> destination = game.getJoueurCourant().getDestinationsList();

        for (CarteDestination carteDestination : destination) {
            ArrayList<Ville> villes = Node.findClosestPath(carteDestination.getPremiereVille(), carteDestination.getDeuxiemeVille());
            ArrayList<Route> routesPossible = GarePosFinder.getNeededRoutes(villes, game.getJoueurCourant());

            for (Route route : routesPossible) {
                if (game.getJoueurCourant().mettreRoute(route)) {
                    ArrayList<Rail> listeRail = route.getRailsRoute();
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
     * @param whichStation Pas utilisé dans la fonction.
     * @return true si la gare a été posée avec succès, sinon false.
     */
    @Override
    public boolean useGare(Game game, int whichStation) {
        ArrayList<CarteDestination> carteDestinations = game.getJoueurCourant().getDestinationsList();
        Player joueur = game.getJoueurCourant();

        if (joueur.getNbrGare() > 0) {
            for (CarteDestination cd : carteDestinations) {
                Ville ville1 = cd.getPremiereVille();
                Ville ville2 = cd.getDeuxiemeVille();

                Ville toTransformInGare = GarePosFinder.getWantedVilleDiff(ville1, ville2, game.getVilles(), 1, false, joueur, game.isModeNuke());

                if (toTransformInGare != null && !cd.getComplete()) {
                    joueur.transformerEnGare(toTransformInGare, joueur.getTrainList().get(0));
                    //DEBUG : System.err.println("takeGare true");
                    return true;
                }
            }
            return false;
        } else {
            CarteDestination[] toAdd = takeMissionsCard(6, game);

            for (CarteDestination carteDestination : toAdd) {
                game.getJoueurCourant().getDestinationsList().add(carteDestination);
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


    /**
     * Permet au bot de detruire une gare.
     *
     * @param game Le jeu en cours.
     * @return Un boolean pour dire si l'action a bien était fait.
     */
    private boolean destroyGare(Game game) {

        //On parcours la liste des villes
        for(Ville ville : game.getVilles()){

            //On regarde quelle gare est occupé et n'appartenant pas au bot, puis on rase la gare et on retire la carte nuke au bot
            if(ville.getIsOccuped() != null && ville.getIsOccuped() != game.getJoueurCourant()){

                game.getJoueurCourant().retirerGareAutrePlayer(ville,ville.getIsOccuped());
                return true;

            }
        }
        return false;
    }

    /**
     * Permet au bot de détruire une route.
     *
     * @param game Le jeu en cours.
     * @return Un boolean pour dire si l'action a bien était fait.
     */
    private boolean destroyRoute(Game game) {
        //On parcours la liste des routes
        for(Route route : game.getRoutes()){

            //On regarde quelle gare est occupé et n'appartenant pas au bot, puis on rase la gare et on retire la carte nuke au bot
            if(route.getProprietaire() != null && route.getProprietaire() != game.getJoueurCourant()){

                game.getJoueurCourant().retirerRouteAutreJoueurBot(route,route.getProprietaire());
                return true;

            }

        }
        return false;
    }


    /**
     * Permet de verifier s'il y a bien au moins une gare de prise.
     *
     * @param game Lejeu en cours.
     * @return Renvoie un boolean pour dire s'il y a bien au moins une gare.
     */
    private boolean checkGares(Game game){

        //Variable qui repésente la liste des villes dans la partie
        ArrayList<Ville> villes = game.getVilles();

        //On parcours la liste des villes
        for (Ville ville : villes) {

            //On regarde qu'il ya une gare et qu'elle appartient pas au bot
            if (ville.estUneCaseGare() && ville.getIsOccuped() != game.getJoueurCourant()) {
                return true;
            }

        }

        return false;
    }

    /**
     * Permet de verifier s'il y a bien au moins une route de prise.
     *
     * @param game Lejeu en cours.
     * @return Renvoie un boolean pour dire s'il y a bien au moins une gare.
     */
    private boolean checkRoutes(Game game){

        //Variable qui repésente la liste des villes dans la partie
        ArrayList<Ville> villes = game.getVilles();

        //On parcours la liste des villes
        for (Ville ville : villes) {

            //On regarde qu'il ya une gare et qu'elle appartient pas au bot
            if (ville.estUneCaseGare() && ville.getIsOccuped() != game.getJoueurCourant()) {
                return true;
            }

        }

        return false;
    }

    /**
     * Permet au bot d'utiliser les cartes nuke de manière aléatoire.
     *
     * @param game Le jeu en cours.
     * @return Un boolean pour dire si l'action a bien était fait.
     */
    @Override
       public boolean useNuke(Game game) {

            //On verifie que le bot a bien des cartes nuke
            if(game.getJoueurCourant().checkACarteNuke()){

                //On verifie qu'il y a bien des routes et des gares prises et n'appartenant pas au bot
                if(checkGares(game) && checkRoutes(game)){

                    Random random = new Random();


                    //On voit si le bot va detruire une gare ou une route
                    switch (random.nextInt(2)){

                        //Si c'est 0, alors il détruit une gare
                        case(0):

                            return destroyGare(game);

                        //Sinon il détruit une route
                        default:

                            return destroyRoute(game);


                    }

                }
                //On verifie sinon si il y a au moins une gare
                if(checkGares(game)){

                    return destroyGare(game);

                }

                //On verifie sinon si y il a au moins une route
                if(checkRoutes(game)){

                    return destroyRoute(game);
                }

                return false;


                //sinon il n'a pas fait d'action
            }else{
                return false;

            }

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
    public void firstTurn(Game game) {
        CarteDestination[] carteDestination = takeMissionsCard(6, game);

        for (CarteDestination destination : carteDestination) {
            game.getJoueurCourant().getDestinationsList().add(destination);
        }

        game.getJoueurCourant().setFirstTurnOver(true);
        optimalCompleteMission(game);
    }

    /**
     * Procède de manière optimale à compléter les missions du bot en prenant des rails, en posant des gares,
     * ou en piochant des cartes wagons. Si on est dans le mode nuke alors le bot cherchera à détruire une route.
     *
     * @param game Le jeu en cours.
     */
    private void optimalCompleteMission(Game game) {
        if (takeLoad(game)) {
            game.getRound().endRound(game);
        } else {

            if (useGare(game, 0)) {
                game.getRound().endRound(game);
            } else {

                if(!game.getGameFrame().getMain().getMode().equals("NORMAL") && useNuke(game) ){

                    game.getRound().endRound(game);

                }else{

                    drawCardWagon(game);
                    game.getRound().endRound(game);
                }


            }
        }
    }

    /**
     * La méthode principale du StrongBot, qui gère ses actions de manière optimale
     * en fonction principale du jeu et de ses objectifs stratégiques.
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
                for (CarteDestination carteDestination : addCard) {
                    game.getJoueurCourant().getDestinationsList().add(carteDestination);
                }
                game.getRound().endRound(game);
            } else {
                optimalCompleteMission(game);
            }
        }
    }
}

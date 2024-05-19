package com.model.bot;

import com.model.Game;
import com.model.Player;
import com.model.Round;
import com.model.config.Rail;
import com.model.config.Route;
import com.model.config.Ville;
import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteManager;
import com.model.config.carte.CarteWagon;

import java.util.ArrayList;
import java.util.Random;

/**
 * Le bot "WeakBot" est une implémentation de l'interface BotAction.
 * Il représente un bot de la difficulté facile qui joue de manière aléatoire.
 */
public class WeakBot implements BotAction {

    /**
     * Permet au bot de piocher une ou des cartes wagons de manière aléatoire.
     *
     * @param game Le jeu en cours.
     */
    @Override
    public void drawCardWagon(Game game) {
        Random random = new Random();
        // Variable pour accéder à la manche
        Round round = game.getRound();
        // Variable pour accéder à la gestion des cartes
        CarteManager carteManager = game.getCarteManager();

        // Tant que le bot a encore des actions à effectuer
        while (round.getAction() > 0) {
            int nbr = random.nextInt(2);

            // Choix aléatoire d'actions : pioche ou prend une carte du board
            if (nbr == 0) {
                // Pioche une carte
                game.getJoueurCourant().getTrainList().add(carteManager.drawCard());
                round.setAction(round.getAction() - 1);
            } else {
                // Choix aléatoire d'une carte wagon du board
                int position = random.nextInt(carteManager.getTrainCards().length);

                // Vérifie si le bot a assez d'actions pour choisir la carte
                if (carteManager.possibleTakeWagon(round.getAction(), position)) {
                    CarteWagon.Couleur carte = carteManager.takeWagon(position);

                    // Ajuste les actions selon la carte piochée
                    if (carte == CarteWagon.Couleur.LOC) {
                        round.setAction(round.getAction() - 2);
                    } else {
                        round.setAction(round.getAction() - 1);
                    }

                    // Ajoute la carte à la liste du bot
                    game.getListPlayer().get(round.getWhoIsPlaying()).getTrainList().add(carte);
                }
            }
        }
    }

    /**
     * Permet au bot de poser des rails sur des routes disponibles.
     *
     * @param game Le jeu en cours.
     * @return true si le bot réussit à prendre une route, sinon false.
     */
    @Override
    public boolean takeRail(Game game) {
        // Variable pour accéder à la manche
        Round round = game.getRound();

        boolean toSetDownWagon = false;

        // Parcourt toutes les routes pour vérifier si le bot peut prendre une route
        for (int i = 0; i < game.getRoutes().size(); i++) {
            toSetDownWagon = game.getListPlayer().get(round.getWhoIsPlaying()).mettreRoute(game.getRoutes().get(i));

            // Si le bot prend une route, il l'occupe et termine l'exécution
            if (toSetDownWagon) {
                ArrayList<Rail> listeRail = game.getRoutes().get(i).getRailsRoute();
                int tailleRoute = listeRail.size();
                Player bot = game.getListPlayer().get(round.getWhoIsPlaying());

                for (int j = 0; j < tailleRoute; j++) {
                    listeRail.get(j).setOccuperPar(bot);
                }

                return true;
            }
        }
        return false;
    }

    /**
     * Permet au bot de poser une gare sur une ville non occupée.
     *
     * @param game Le jeu en cours.
     * @param wichStation L'identifiant de la ville où poser la gare.
     * @return true si le bot réussit à poser une gare, sinon false.
     */
    @Override
    public boolean takeGare(Game game, int wichStation) {
        // Variable pour accéder à la manche
        Round round = game.getRound();
        // Accès au joueur courant
        Player player = game.getListPlayer().get(round.getWhoIsPlaying());

        Random random = new Random();

        // Vérifie que la ville n'est pas occupée et que le bot peut poser une gare
        if (game.getVilles().get(wichStation).getIsOccuped() == null && player.getTrainList().size() > player.nombreDeCartePourPoserUneGare() && player.getNbrGare() > 0) {
            int card = random.nextInt(player.getTrainList().size());

            // Transforme la ville en gare
            player.transformerEnGare(game.getVilles().get(wichStation), player.getTrainList().get(card));

            return true;
        } else {
            return false;
        }
    }

    /**
     * Permet au bot de piocher des cartes missions de manière aléatoire.
     *
     * @param max  Pas utilisé dans la fonction pour le weak.
     * @param game Le jeu en cours.
     * @return Un tableau de cartes missions piochées.
     */
    @Override
    public CarteDestination[] takeMissionsCard(int max, Game game) {
        // Variable pour accéder à la gestion des cartes
        CarteManager carteManager = game.getCarteManager();

        Random random = new Random();

        // Choix aléatoire du nombre de cartes à piocher (entre 1 et 3)
        int nombreDeCartePris = random.nextInt(3) + 1;
        int[] indicesCartes = new int[nombreDeCartePris];

        // Génère un tableau d'indices aléatoires pour les cartes à piocher
        for (int y = 0; y < indicesCartes.length; y++) {
            indicesCartes[y] = random.nextInt(carteManager.getDestinationsCards().length);
        }

        // Retourne les cartes destinations piochées
        return carteManager.takeDestination(indicesCartes);
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
     * Méthode principale du WeakBot pour jouer un tour.
     *
     * @param game Le jeu en cours.
     */
    @Override
    public void play(Game game) {

        //Variable pour avoir round

        Round round = game.getRound();

        // Vérifie si c'est le premier tour du bot
        if (!game.getJoueurCourant().getFirstTurnOver()) {
            // Pioche des cartes missions au premier tour
            CarteDestination[] carteDestination = takeMissionsCard(0, game);

            // Ajoute les cartes missions à la liste du bot
            for (int z = 0; z < carteDestination.length; z++) {
                game.getJoueurCourant().getDestinationsList().add(carteDestination[z]);
            }

            // Pioche des cartes wagons
            drawCardWagon(game);

            round.endRound(game);
        } else {


            // Choix aléatoire des actions à effectuer et on verifie que c'est le mode nuke ou non
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
                    // Pioche des cartes wagons
                    drawCardWagon(game);
                    round.endRound(game);
                    break;

                case 1:
                    // Pioche des cartes missions
                    CarteDestination[] carteDestination = takeMissionsCard(0, game);
                    for (int z = 0; z < carteDestination.length; z++) {
                        game.getJoueurCourant().getDestinationsList().add(carteDestination[z]);
                    }
                    round.endRound(game);
                    break;

                case 2:
                    // Pose des wagons
                    if (takeRail(game)) {
                        round.endRound(game);
                    } else {
                        play(game);
                    }
                    break;

                case 3:
                    // Pose une gare
                    int wichStation = random.nextInt(game.getVilles().size());
                    if (takeGare(game, wichStation)) {
                        round.endRound(game);
                    } else {
                        play(game);
                    }
                    break;

                default:
                    //Seulement si il y a le mode nuke, alors on detruit une ville/route
                    if(useNuke(game)){
                        round.endRound(game);
                    }else{
                        play(game);
                    }
                    break;
            }
        }
    }
}

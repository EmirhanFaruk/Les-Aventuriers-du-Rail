package com.model.bot;

import com.model.Game;
import com.model.Player;
import com.model.Round;
import com.model.config.Rail;
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
     * Méthode principale du WeakBot pour jouer un tour.
     *
     * @param game Le jeu en cours.
     */
    @Override
    public void play(Game game) {
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

            game.getRound().endRound(game);
        } else {
            // Choix aléatoire des actions à effectuer
            Random random = new Random();
            int whatToDo = random.nextInt(4);

            switch (whatToDo) {
                case 0:
                    // Pioche des cartes wagons
                    drawCardWagon(game);
                    game.getRound().endRound(game);
                    break;

                case 1:
                    // Pioche des cartes missions
                    CarteDestination[] carteDestination = takeMissionsCard(0, game);
                    for (int z = 0; z < carteDestination.length; z++) {
                        game.getJoueurCourant().getDestinationsList().add(carteDestination[z]);
                    }
                    game.getRound().endRound(game);
                    break;

                case 2:
                    // Pose des wagons
                    if (takeRail(game)) {
                        game.getRound().endRound(game);
                    } else {
                        play(game);
                    }
                    break;

                default:
                    // Pose une gare
                    int wichStation = random.nextInt(game.getVilles().size());
                    if (takeGare(game, wichStation)) {
                        game.getRound().endRound(game);
                    } else {
                        play(game);
                    }
                    break;
            }
        }
    }
}

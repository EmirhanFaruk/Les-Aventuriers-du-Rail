package com.model.bot;

import com.model.Game;
import com.model.Round;
import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteManager;

import java.util.ArrayList;

public class StrongBot implements BotAction {
    @Override
    public void drawCardWagon(Round round,CarteManager carteManager, Game game) {

    }

    @Override
    public boolean takeRail(Game game, Round round) {

        return true;
    }

    @Override
    public boolean takeGare(Game game, int wichStation, Round round) {

        return true;
    }

    @Override
    public CarteDestination[] takeMissionsCard(int max, CarteManager carteManager, Game game) {
        //Fonction qui compare les cartes destinations pour savoir quelles cartes prendre

        //On regarde lequel des cartes mission a la plus petite route,
        ArrayList<CarteDestination> tab = new ArrayList<>();
        int[] tmp = new int[2];
        //Premiere bouble qui va prendre la carte la plus petite
        for (int i = 1; i < carteManager.getDestinationsCards().length; i++) {
            if (carteManager.getDestinationsCards()[i].getNombrePoints() < carteManager.getDestinationsCards()[i - 1].getNombrePoints()) {
                tmp[0] = i;
            }
        }
        //Deuxieme boucle qui ajoute une deuxieme carte mission si la somme < max
        for (int y = 0; y < carteManager.getDestinationsCards().length; y++) {
            if ((carteManager.getDestinationsCards()[y].getNombrePoints() + (carteManager.getDestinationsCards()[tmp[0]].getNombrePoints()) <= max && y != tmp[0])) {

                tmp[1] = y;
            }
        }

        //Troisieme bouble qui regarde si la derniere carte + les cartes deja prisent soit < max
        for (int z = 0; z < carteManager.getDestinationsCards().length; z++) {
            if ((carteManager.getDestinationsCards()[z].getNombrePoints() + (carteManager.getDestinationsCards()[tmp[0]].getNombrePoints()) <= max && z != tmp[0] && z != tmp[1])) {

                tmp[2] = z;
            }
        }

        return carteManager.takeDestination(tmp, game);


    }

    @Override
    public void endRound(Round round,Game game) {
        //Fonction qui finit le tour du bot
        round.setEndTurn(true);
        round.whosNext(game);
    }


    private boolean allMissionIsCompleted(Game game, Round round){
        for (int i = 0; i < game.getListPlayer().get(round.getWhoIsPlaying()).getDestinationsList().size(); i++) {

            if (!game.getListPlayer().get(round.getWhoIsPlaying()).getDestinationsList().get(i).getComplete()) {
                return false;
            }

        }
        return true;
    }


    private boolean canCompletePath(Game game,Round round){
        //TODO contenu du for
        //On regarde si les routes pour completer toute les missions du joueurs ne sont pas bloqués
        for (int l = 0; l < game.getListPlayer().get(round.getWhoIsPlaying()).getDestinationsList().size(); l++) {

            if(game.getRoutes().get(l).getProprietaire() == null) {
                return true;
            }
        }
        return false;

    }






    @Override
    public void play(Game game, CarteManager carteManager, Round round) {

        //Fonction principale du bot fort


        //1- On regarde si il a complété toute ses missions ou pas :
        if (allMissionIsCompleted(game,round)) {
            //2- On prends une a deux nouvelles mission, en fonction de la longueur des routes, au total il ne doit pas dépasser 5 comme longueur des routes total puis les prends

            CarteDestination[] addCard = takeMissionsCard(6, carteManager, game);
            for (int j = 0; j < addCard.length; j++) {
                game.getListPlayer().get(round.getAction()).getDestinationsList().add(addCard[j]);
            }


        } else {

            //3- On regarde si il peut faire finir sa mission avec les routes non prise qu'il lui manque

                if (! canCompletePath(game,round)) {

                            /*
                   -Si non:

                    4- On cherche l'endroit le plus optimale pour poser une gare :

                        5- Si il y a plus de chemin possible :
                         alors on regarde si les autres missions sont complété

                                 Si oui :
                                    Alors prendre une nouvelle carte mission on fait l'étape 2

                                Sinon :
                                    Completer les autres missions
             */
                } else {
                /*
                        -Si on peut poser les wagons :

                            6- On pose les wagons
                 */
                    if (true) {


                    } else {

                        // -Sinon :  7- On pioche :

                        if (true) {
                            //-Si il manque une carte:

                            if (true) {

                                if (true) {
                                    // 8- On prends la couleur manquante sur le tas de carte visible et on tire aléatoirement dans la pioche invisible


                                } else {

                                    //9- Si on peut pas, on prends une carte locomotive sur le tas de carte visible
                                }

                            } else {
                                //10- Sinon on pioche 2 cartes dans la pioche invisible

                            }


                        } else {

                            if (true) {
                                //  11- On choisit toute les couleurs qu'on a besoin de prendre sur le tas de carte visible (en fonction de la route qu'on veut compléter en priorité)

                            } else {
                                //Sinon on pioche 2 cartes aléatoire


                            }

                        }


                    }


                }


            }


        }

    }







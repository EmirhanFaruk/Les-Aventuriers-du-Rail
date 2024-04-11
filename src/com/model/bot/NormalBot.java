package com.model.bot;

import com.model.Game;
import com.model.Round;
import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteManager;

import java.util.ArrayList;
import java.util.Random;

public class NormalBot implements BotAction{
    @Override
    public void drawCardWagon(Game game) {
        //Si il peut completer une route avec 2 carte alors piocher les 2 cartes

        //Si il peut completer une route avec 1 carte alors piocher la carte

        //Sinon piocher 2 cartes non visible

    }

    public boolean takeRail(Game game) {
        //Variable pour avoir round
        Round round = game.getRound();

        //On a toSetDownWagon qui verifie que le joueur a pris ou non une route, si oui alors on arrete la fonction, sinon on rappelle la fonction

        boolean toSetDownWagon = false;

        //On regarde pour toute les routes si il peut prendre la route ou non
        for(int i = 0; i< game.getVilles().size();i++){

            toSetDownWagon = game.getListPlayer().get(round.getWhoIsPlaying()).mettreRoute(game.getRoutes().get(i));

            //si il trouve une route qu'il peut prendre alors il prends la route et arrete la fonction, tout en passant au joueur suivant
            if(toSetDownWagon){

                return true;
            }

        }
        return false;
    }

    @Override
    public boolean takeGare(Game game, int wichStation) {
        //Meme logique que le bot intelligent

        return true;
    }

    @Override
    public CarteDestination[] takeMissionsCard(int max,Game game) {
        //Variable pour avoir carteManager
        CarteManager carteManager = game.getCarteManager();

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
    public void play(Game game) {
        //Fonction principale du bot normal

        Random random = new Random();
        int whatToDo = random.nextInt(4);

        switch (whatToDo) {


            case (0):
                /*        CARTES WAGONS        */


                break;


            case (1):
                /*        CARTES MISSIONS        */

                break;

            case (2):
                /*        POSER DES WAGONS       */

                //On regarde si les rails ont bien était posés
                if (takeRail(game)) {
                    game.getRound().endRound(game);

                } else {
                    play(game);

                }

                break;


            default:
                /*        POSER UNE GARE       */


                break;

        }
    }


}

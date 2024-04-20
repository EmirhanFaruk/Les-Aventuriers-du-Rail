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
        //Fonction qui compare les cartes destinations pour savoir quelles cartes prendre en fonction de la variable max
        // (le maximum de points cumulés dans les cartes missions que le bot prends)

        //Variable pour avoir acces a la liste de carte destination
        CarteDestination[] carteDestinations = game.getCarteManager().getDestinationsCards();

        //On va garder dans cette liste les positions des cartes destinations qu'on ajoute par la suite
        ArrayList<Integer> aPiocher = new ArrayList<>();

        //Cette variable sert savoit si on dépasse max
        int total = 0;

        for(int i = 0; i< carteDestinations.length;i++ ){

            if(carteDestinations[i].getNombrePoints() + total <= max){

                aPiocher.add(i);
                total += carteDestinations[i].getNombrePoints();

            }


        }

        int[] renvoie = new int[aPiocher.size()];

        for(int y = 0; y < renvoie.length;y++){
            renvoie[y] = aPiocher.get(y);
        }


        return game.getCarteManager().takeDestination(renvoie);

    }


    @Override
    public void play(Game game) {
        //Fonction principale du bot normal

        if(true){

        }else{

        }
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

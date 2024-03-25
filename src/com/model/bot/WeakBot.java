package com.model.bot;

import com.model.Game;
import com.model.Round;
import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteManager;
import com.model.config.carte.CarteWagon;

import java.util.Random;

public class WeakBot implements BotAction{

    @Override
    public void drawCardWagon(Round round,CarteManager carteManager, Game game) {
        Random random = new Random();

        //Si l'ia a encore des actions
        while (round.getAction() < 0){
            int nbr = random.nextInt(2);
            //Savoir si elle pioche ou prends une carte du board

            if(random.nextInt() == 0){
                //On enleve 1 action et pioche une carte
                game.getListPlayer().get(round.getWhoIsPlaying()).getTrainList().add(carteManager.drawCard());
                round.setAction(round.getAction()-1);
            }

            else{
                //Sinon elle choisit aléatoirement dans la liste de carte wagon sur le board
                int position = random.nextInt(carteManager.getTrainCards().length);

                //On verifie si elle a assez d'action pour choisir la carte, car carte normale = 1 point et locomotive = 2 points, sinon elle recommence dans le while
                if(carteManager.possibleTakeWagon(round.getAction(), position)){
                    CarteWagon.Couleur carte = carteManager.takeWagon(position);

                    //Si la carte pioché est une locomotive, on eneleve 2 points
                    if(carte == CarteWagon.Couleur.LOC){
                        round.setAction(round.getAction()-2);
                    }else{
                        //Sinon on enleve 1 point
                        round.setAction(round.getAction()-1);
                    }
                    //Puis on l'ajoute dans la liste de carte
                    game.getListPlayer().get(round.getWhoIsPlaying()).getTrainList().add(carte);

                }

            }

        }
    }

    @Override
    public boolean takeRail(Game game,Round round) {
        //On a toSetDownWagon qui verifie que le joueur a pris ou non une route, si oui alors on arrete la fonction, sinon on rappelle la fonction

        boolean toSetDownWagon = false;

        //On regarde pour toute les routes si il peut prendre la route ou non
        for(int i = 0; i< game.getVilles().length;i++){

            toSetDownWagon = game.getListPlayer().get(round.getWhoIsPlaying()).mettreRoute(game.getRoutes().get(i));

            //si il trouve une route qu'il peut prendre alors il prends la route et arrete la fonction, tout en passant au joueur suivant
            if(toSetDownWagon){

                return true;
            }

        }
        return false;
    }

    @Override
    public boolean takeGare(Game game, int wichStation, Round round) {
        //On regarde dans la liste de gare a la position "wichSation" si la gare est deja prise ou non, de plus on regarde si le bot a toujours des gares
        if(game.getVilles()[wichStation].getIsOccuped() == null && game.getListPlayer().get(round.getWhoIsPlaying()).getNbrGare() < 0){

            game.getVilles()[wichStation].setIsOccuped(game.getListPlayer().get(round.getWhoIsPlaying()));

            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public CarteDestination[] takeMissionsCard(int max, CarteManager carteManager, Game game){

        Random random = new Random();

        //On choisit un nombre aleatoire et le joueur prends au hasard soit 1/2/3 cartes qu'on met dans un tableau
        int nombreDeCartePris = random.nextInt(carteManager.getDestinationsCards().length -1);
        int[] tabNombre = new int[nombreDeCartePris];

        //Le bot prends les "nombreDeCartePris"
        for(int y = 0; y< nombreDeCartePris; y++){

            tabNombre[y] = y;

        }

        //Je stock les cartes destinations dans une liste
        return carteManager.takeDestination(tabNombre,game);


    }

    @Override
    public void endRound(Round round,Game game) {
        //Fonction qui finit le tour du bot
        round.setEndTurn(true);
        round.whosNext(game);
    }

    @Override
    public void play(Game game, CarteManager carteManager, Round round) {
        //Fonction principale du bot faible

        System.out.println("faiefhaeipfhapifafbaifanfbaf");

        Random random = new Random();
        int whatToDo = random.nextInt(4);

        switch (whatToDo){


            case(0):
                /*        CARTES WAGONS        */

                drawCardWagon(round,carteManager,game);

                endRound(round,game);

                break;


            case(1):
                /*        CARTES MISSIONS        */

                CarteDestination[] carteDestination = takeMissionsCard(0,carteManager,game);

                //Pour ensuite les ajouter dans la liste des missions du bot
                for(int z = 0; z<carteDestination.length;z++){

                    game.getListPlayer().get(round.getWhoIsPlaying()).getDestinationsList().add(carteDestination[z]);
                }

                endRound(round,game);

                break;



            case(2):
                /*        POSER DES WAGONS       */

                //On regarde si les rails ont bien était posés
                if(takeRail(game,round)){

                    endRound(round,game);

                }
                else{
                    play(game,carteManager,round);

                }

                break;


            default :
                /*        POSER UNE GARE       */

                int wichStation = random.nextInt(game.getVilles().length);

                if(takeGare(game,wichStation,round)){
                  endRound(round,game);

                }
                else{
                    play(game,carteManager,round);
                }


                break;



        }


    }



}


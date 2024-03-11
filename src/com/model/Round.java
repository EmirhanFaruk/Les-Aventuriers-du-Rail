package com.model;

import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteManager;
import com.model.config.carte.CarteWagon;

import java.util.Random;

public class Round {

    private boolean playing = true; //Le jeu en pose ou pas
    private int whoIsPlaying = 0; //Quel joueur est entrain de jouer
    private boolean endTurn = false; //Si le tour est finis ou non
    private int action = 2; //Le nombre d'action qu'il reste pour piocher une carte wagon
    private boolean missionCardTaked = false;

    public boolean roundFinished(){
        //Savoir si le joueur/ia a fini de jouer ou non
        return this.endTurn;
    }


    public void whosNext(Game game){
        //Passer au prochain joueur

        //On reset le round
        this.endTurn = false;
        this.action = 2;
        this.missionCardTaked = false;

        //On change de joueur
        if(whoIsPlaying == game.getListPlayer().size() -1){
            whoIsPlaying = 1;
        }
        else{
            whoIsPlaying ++;
        }

    }


    private void weakBotPlay(Game game, CarteManager carteManager) {
        //L'ia pas tres maline

        Random random = new Random(2);

        /*        CARTES WAGONS        */

        //Si l'ia a encore des actions
        while (action < 0){
            int nbr = random.nextInt(2);
            //Savoir si elle pioche ou prends une carte du board

            if(random.nextInt() == 0){
                //On enleve 1 action et pioche une carte
                game.getListPlayer().get(whoIsPlaying).getTrainList().add(carteManager.drawCard());
                action --;
            }

            else{
                //Sinon elle choisit aléatoirement dans la liste de carte wagon sur le board
                int position = random.nextInt(carteManager.getTrainCards().length);

                //On verifie si elle a assez d'action pour choisir la carte, car carte normale = 1 point et locomotive = 2 points, sinon elle recommence dans le while
                if(carteManager.possibleTakeWagon(this.action, position)){
                    CarteWagon.Couleur carte = carteManager.takeWagon(position,action);

                    //Si la carte pioché est une locomotive, on eneleve 2 points
                    if(carte == CarteWagon.Couleur.LOC){
                        action = action - 2;
                    }else{
                        //Sinon on enleve 1 point
                        action --;
                    }
                    //Puis on l'ajoute dans la liste de carte
                    game.getListPlayer().get(whoIsPlaying).getTrainList().add(carte);

                }

            }

        }

        /*        CARTES MISSIONS        */

        int nombreDecartePris = random.nextInt(carteManager.getDestinationsCards().length -1);
        int[] tabNombre = new int[nombreDecartePris];
        for(int y = 0; y< nombreDecartePris; y++){

            tabNombre[y] = random.nextInt(2);

        }
        CarteDestination[] carteDestination = carteManager.takeDestination(tabNombre,game);

        for(int z = 0; z<carteDestination.length;z++){
            game.getListPlayer().get(whoIsPlaying).getDestinationsList().add(carteDestination[z]);
        }


        /*        POSER DES WAGONS       */



        /*        RESET POUR LE PROCHAIN JOUEUR       */

        endTurn = true;
        whosNext(game);

    }


    private void normalBotPlay(Game game, CarteManager carteManager) {
    }


    private void strongBotPlay(Game game, CarteManager carteManager) {
    }



    public void round(Game game,CarteManager carteManager){


        switch(game.getListPlayer().get(whoIsPlaying).getNiveau()){

            case(1):
                weakBotPlay(game,carteManager);
                break;

            case(2):
                normalBotPlay(game,carteManager);
                break;

            case(3):
                strongBotPlay(game,carteManager);
                break;

            default: break;
        }



    }



}

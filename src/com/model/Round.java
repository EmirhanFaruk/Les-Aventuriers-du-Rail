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

        Random random = new Random();
        int whatToDo = random.nextInt(3);

        switch (whatToDo){


            case(0):
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

                break;


            case(1):
                /*        CARTES MISSIONS        */

                //On choisit un nombre aleatoire et le joueur prends au hasard soit 1/2/3 cartes qu'on met dans un tableau
                int nombreDeCartePris = random.nextInt(carteManager.getDestinationsCards().length -1);
                int[] tabNombre = new int[nombreDeCartePris];

                //Le bot prends les "nombreDeCartePris"
                for(int y = 0; y< nombreDeCartePris; y++){

                    tabNombre[y] = y;

                }

                //Je stock les cartes destinations dans une liste
                CarteDestination[] carteDestination = carteManager.takeDestination(tabNombre,game);

                //Pour ensuite les ajouter dans la liste des missions du bot
                for(int z = 0; z<carteDestination.length;z++){
                    game.getListPlayer().get(whoIsPlaying).getDestinationsList().add(carteDestination[z]);
                }


                break;



            case(2):
            /*        POSER DES WAGONS       */

                //On a toSetDownWagon qui verifie que le joueur a pris ou non une route, si oui alors on arrete la fonction, sinon on rappelle la fonction

                boolean toSetDownWagon = false;

                //On regarde pour toute les routes si il peut prendre la route ou non
                for(int i = 0; i< game.getVilles().length;i++){

                    toSetDownWagon = game.getListPlayer().get(whoIsPlaying).mettreRoute(game.getRoutes().get(i));

                    //si il trouve une route qu'il peut prendre alors il prends la route et arrete la fonction, tout en passant au joueur suivant
                    if(toSetDownWagon){

                        endTurn = true;
                        whosNext(game);

                        return;
                    }

                }
                weakBotPlay(game,carteManager);


                break;


            default :
                /*        POSER UNE GARE       */

                int wichStation = random.nextInt(game.getVilles().length);

                //On regarde dans la liste de gare a la position "wichSation" si la gare est deja prise ou non, de plus on regarde si le bot a toujours des gares
                if(game.getVilles()[wichStation].getIsOccuped() == null && game.getListPlayer().get(whoIsPlaying).getNbrGare() < 0){
                    game.getVilles()[wichStation].setIsOccuped(game.getListPlayer().get(whoIsPlaying));
                }
                else{
                    weakBotPlay(game,carteManager);
                }

                break;



        }




        /*        RESET POUR LE PROCHAIN JOUEUR       */

        endTurn = true;
        whosNext(game);

    }


    private void normalBotPlay(Game game, CarteManager carteManager) {

        /*        CARTES MISSIONS        */

        /*        CARTES WAGONS        */

        /*        POSER DES WAGONS       */

        /*        POSER UNE GARE       */


        /*        RESET POUR LE PROCHAIN JOUEUR       */
        endTurn = true;
        whosNext(game);

    }


    private void strongBotPlay(Game game, CarteManager carteManager) {

        /*       /!\  Principes fondamentaux de ce bot  /!\

                     ----- MISSION PRINCIPALE ------

              · Il doit completer toute ses missions pour ne pas avoir de malus

              · Il doit avoir le plus de gare possible a la fin pour ajouter des points au compteur


                     ----- PROCEDE DE CHAQUE TOUR ------

              1- On regarde si il a complété ou pas ses missions :

                    -Si oui :

                    2- On prends une a deux nouvelles mission, en fonction de la longueur des routes, au total il ne doit pas dépasser 5 comme longueur des routes total
                        puis les prends

                    -Sinon :

                    3- On regarde si il peut faire finir sa route avec les routes non prise :

                    -Si non :

                    4- On cherche l'endroit le plus optimale pour poser une gare :

                        5- Si il y a plus de chemin possible :
                         alors on regarde si les autres missions sont complété

                                 Si oui :
                                    Alors prendre une nouvelle carte mission on fait l'étape 2

                                Sinon :
                                    Completer les autres missions

                    -Si oui :

                        -Si on peut poser les wagons :

                            6- On pose les wagons

                        -Sinon :

                            7- On pioche :

                               -Si il manque une carte:

                                    8- On prends la couleur manquante sur le tas de carte visible et on tire aléatoirement dans la pioche invisible

                                    9- Si on peut pas, on prends une carte locomotive sur le tas de carte visible

                                    10- Sinon on pioche 2 cartes dans la pioche invisible

                               -Sinon :

                                    11- On choisit toute les couleurs qu'on a besoin de prendre sur le tas de carte visible (en fonction de la route qu'on veut compléter en priorité)

                                    Sinon on pioche 2 cartes aléatoire


         */





        /*        CARTES MISSIONS        */

            /*
            Pour l'ia intelligente je pars du principe qu'il ne va prendre des cartes missions seulement si
            il a complété toute les cartes missions qu'il possede sinon il ne fait jamais cette option


            -Plus tard peut etre completer juste ceux court pour ensuite prendre d'autre mission

             */

        /*        CARTES WAGONS        */

            /*
            C'est plus optimal de choisir les cartes pioche visible que de prendre ceux invisible

            Il prendra une carte locomotive si il n'y a pas de couleur qui lui permet de completer sa
            au prochain tour ca lui permet de prendre une route ensuite, puis il



             */


        /*        POSER DES WAGONS       */

            /*
            L'ia posera seulement des wagons qui aident a compléter ses missions

             */

        /*        POSER UNE GARE       */

            /*
            Il posera une gare si le chemin est pris par un autre joueur
            Comme ca ca lui permettra de faciliter de finir ses missions
             */


        /*        RESET POUR LE PROCHAIN JOUEUR       */

        endTurn = true;
        whosNext(game);


    }



    public void round(Game game,CarteManager carteManager){
    //La fonction qui indique qui joue, le round de qui

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

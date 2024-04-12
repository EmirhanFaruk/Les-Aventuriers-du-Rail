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

public class WeakBot implements BotAction{

    @Override
    public void drawCardWagon(Game game) {
        Random random = new Random();
        //Variable pour avoir round
        Round round = game.getRound();
        //Variable pour avoir carteManager
        CarteManager carteManager = game.getCarteManager();

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
    public boolean takeRail(Game game) {
        //On a toSetDownWagon qui verifie que le joueur a pris ou non une route, si oui alors on arrete la fonction, sinon on rappelle la fonction

        //Variable pour avoir round
        Round round = game.getRound();

        boolean toSetDownWagon = false;

        //On regarde pour toute les routes si il peut prendre la route ou non
        for(int i = 0; i< game.getVilles().size(); i++){

            toSetDownWagon = game.getListPlayer().get(round.getWhoIsPlaying()).mettreRoute(game.getRoutes().get(i));

            //si il trouve une route qu'il peut prendre alors il prends la route et arrete la fonction, tout en passant au joueur suivant
            if(toSetDownWagon){
                ArrayList<Rail> listeRail =   game.getRoutes().get(i).getRailsRoute() ;
                int tailleRoute =  game.getRoutes().get(i).getLongueur();
                Player bot = game.getListPlayer().get( round.getWhoIsPlaying());

                for(int j = 0 ; j  < tailleRoute ; j++) {
                    listeRail.get( j ).setOccuperPar( bot );
                }
                game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel().getMapScreen().repaint();
                // DEBUG : System.err.println("Le botW a poser les wagons");

                return true;
            }

        }
        return false;
    }

    @Override
    public boolean takeGare(Game game, int wichStation) {
        //Variable pour avoir round
        Round round = game.getRound();
        //Variable du joueur
        Player player = game.getListPlayer().get(round.getWhoIsPlaying());

        Random random = new Random();
        int card = random.nextInt( player.getTrainCard().size() ) ;
        //On regarde dans la liste de gare a la position "wichSation" si la gare est deja prise ou non, de plus on regarde si le bot a toujours des gares et on verifie qu'il a assez de carte a enlever
        if( game.getVilles().get(wichStation).getIsOccuped() == null ){
            player.transformerEnGare( game.getVilles().get(wichStation) , player.getTrainCard().get( card ) );
            //DEBUG System.err.println( "Le botW " + player.getName() +" a poser une gare, le nom de la ville est " +   game.getVilles().get(wichStation).getNom() );

            game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel().getMapScreen().repaint();

            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public CarteDestination[] takeMissionsCard(int max, Game game){
        //Variable pour avoir carteManager
        CarteManager carteManager = game.getCarteManager();

        Random random = new Random();

        //On choisit un nombre aleatoire et le joueur prends au hasard soit 1/2/3 cartes qu'on met dans un tableau
        int nombreDeCartePris = random.nextInt(carteManager.getDestinationsCards().length -1);
        int[] tabNombre = new int[nombreDeCartePris];

        //Le bot prends les "nombreDeCartePris"
        for(int y = 0; y< nombreDeCartePris; y++){

            tabNombre[y] = y;

        }

        //Je stock les cartes destinations dans une liste
        return carteManager.takeDestination(tabNombre);


    }


    @Override
    public void play(Game game) {
        //Fonction principale du bot faible

        Random random = new Random();
        int whatToDo = random.nextInt(4);

        switch (whatToDo){


            case(0):
                /*        CARTES WAGONS        */

                drawCardWagon(game);

                game.getRound().endRound(game);

                break;


            case(1):
                /*        CARTES MISSIONS        */

                CarteDestination[] carteDestination = takeMissionsCard(0,game);

                //Pour ensuite les ajouter dans la liste des missions du bot
                for(int z = 0; z<carteDestination.length;z++){

                    game.getListPlayer().get(game.getRound().getWhoIsPlaying()).getDestinationsList().add(carteDestination[z]);
                }

                game.getRound().endRound(game);

                break;



            case(2):
                /*        POSER DES WAGONS       */

                //On regarde si les rails ont bien était posés
                if(takeRail(game)){

                    game.getRound().endRound(game);

                }
                else{
                    play(game);

                }

                break;


            default :
                /*        POSER UNE GARE       */

                int wichStation = random.nextInt(game.getVilles().size());

                if(takeGare(game,wichStation)){
                  game.getRound().endRound(game);

                }
                else{
                    play(game);
                }


                break;



        }


    }



}


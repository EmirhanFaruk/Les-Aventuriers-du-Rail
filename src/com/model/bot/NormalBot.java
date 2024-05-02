package com.model.bot;

import com.model.Game;
import com.model.Player;
import com.model.Round;
import com.model.ai.GarePosFinder;
import com.model.ai.Node;
import com.model.config.Rail;
import com.model.config.Route;
import com.model.config.Ville;
import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteManager;
import com.model.config.carte.CarteWagon;

import java.util.ArrayList;
import java.util.Random;

public class NormalBot implements BotAction{
    public boolean missOneCardOnly(Route route, Game game){

        //Variable pour avoir le joueur courent
        Player player= game.getJoueurCourant();
        //Varibale de la liste de carte du bot
        ArrayList<CarteWagon.Couleur> playerTrainList =  player.getTrainList();
        //Nombre de carte de la couleur de la route
        int count = 0;

        for(int i = 0; i < playerTrainList.size(); i++) {
            if(player.compatibleColor(route, playerTrainList.get(i)))count++;
        }

        return count+1 == route.getLongueur();
    }
    public Rail.Content canCompletePathMissingOneCard(Game game) {

        //Fonction qui si il manque une carte pour completer une route dans la liste de carte

        //Variable pour avoir Player du bot
        Player player= game.getJoueurCourant();
        //Variable pour avoir les cartes destination du bot
        ArrayList<CarteDestination> destination = player.getDestinationsList();


        //On regarde toute les routes qu'on doit completer pour finir une mission
        for(int i =0; i<destination.size();i++){

            //Liste de ville qu'on a besoin
            ArrayList<Ville> villes = Node.findClosestPath(destination.get(i).getPremiereVille(),destination.get(i).getDeuxiemeVille());
            //Liste des routes que le bot doit completer pour finir sa missions
            ArrayList<Route> routesPossible = GarePosFinder.getNeededRoutes(villes,game.getJoueurCourant());

            for(int z = 1; z < routesPossible.size();z++){

                // On regarde si il manque juste 1 carte max
                if(missOneCardOnly(routesPossible.get(z),game)){

                    return routesPossible.get(z).getCouleur();
                }
            }



        }
        return null;
    }

    @Override
    public void drawCardWagon(Game game) {
        //Variable pour appeler le joueur (le bot)
        Player player = game.getListPlayer().get(game.getRound().getWhoIsPlaying());

        //Variable qui va déterminer si oui ou non on peut prendre
        Rail.Content color = canCompletePathMissingOneCard(game);
        //-Si il manque une carte:
        if (color != null) {

            int position = -1;

            //On cherche la couleur correspondante
            for(int i = 0; i< game.getCarteManager().getTrainCards().length;i++){
                if(game.getCarteManager().getTrainCards()[i].ordinal() == color.ordinal()){
                    position = i;
                }
            }

            // 8- On prends la couleur manquante sur le tas de carte visible et on tire aléatoirement dans la pioche invisible
            if (position != -1) {

                player.getTrainList().add(game.getCarteManager().takeWagon(position));
                player.getTrainList().add(game.getCarteManager().drawCard());



                //9- Si on peut pas, on prends une carte locomotive sur le tas de carte visible
            } else{
                //On cherche une carte locomotive

                int positionLoc = -1;

                for(int i = 0; i< game.getCarteManager().getTrainCards().length;i++){
                    if(game.getCarteManager().getTrainCards()[i] == CarteWagon.Couleur.LOC){
                        position = i;
                    }
                }

                if(positionLoc != -1){

                    player.getTrainList().add(game.getCarteManager().takeWagon(positionLoc));

                    //10- Sinon on pioche 2 cartes dans la pioche invisible
                } else{

                    player.getTrainList().add(game.getCarteManager().drawCard());
                    player.getTrainList().add(game.getCarteManager().drawCard());

                }



            }


        } else {

            //Sinon on pioche 2 cartes aléatoire
            player.getTrainList().add(game.getCarteManager().drawCard());
            player.getTrainList().add(game.getCarteManager().drawCard());


        }

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

        //Variable pour avoir les cartes destinations du joueur
        ArrayList<CarteDestination> carteDestinations = game.getJoueurCourant().getDestinationsList();

        //Variable joueur
        Player joueur = game.getJoueurCourant();

        //4- On cherche l'endroit le plus optimale pour poser une gare (et si on a toujours des gares):
        if(joueur.getNbrGare() > 0 ){

            for(CarteDestination cd: carteDestinations){

                //Ville 1 de la carte
                Ville ville1 = cd.getPremiereVille();
                //Ville 2 de la gare
                Ville ville2 = cd.getDeuxiemeVille();

                Ville toTransformInGare = GarePosFinder.getWantedVilleDiff(ville1,ville2,game.getVilles(),1,false, joueur);

                //On regarde pour toute les missions
                if(toTransformInGare != null &&  !(cd.getComplete())){

                    joueur.transformerEnGare( toTransformInGare , joueur.getTrainList().get(0) );

                    return true;

                }

            }
            return false;

        }else{
            //5- Si il y a plus de chemin possible :

            // Alors prendre une nouvelle carte mission on fait l'étape 2
            CarteDestination[] toAdd  = takeMissionsCard(6,game);

            for(int i = 0; i< toAdd.length;i++){

                game.getJoueurCourant().getDestinationsList().add(toAdd[i]);

            }
            return true;

        }


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


        Random random = new Random();
        int whatToDo = random.nextInt(4);

        switch (whatToDo) {


            case (0):
                /*        CARTES WAGONS        */

                drawCardWagon(game);
                game.getRound().endRound(game);


                break;


            case (1):
                /*        CARTES MISSIONS        */

                CarteDestination[] toAdd = takeMissionsCard(8,game);

                //Pour ensuite les ajouter dans la liste des missions du bot
                for(int z = 0; z<toAdd.length;z++){

                    game.getListPlayer().get(game.getRound().getWhoIsPlaying()).getDestinationsList().add(toAdd[z]);
                }

                game.getRound().endRound(game);

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

                if(takeGare(game,0)){
                    game.getRound().endRound(game);

                }
                else{
                    play(game);
                }


                break;

        }
    }


}

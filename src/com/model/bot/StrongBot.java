package com.model.bot;

import com.model.Game;
import com.model.Player;
import com.model.ai.GarePosFinder;
import com.model.ai.Node;
import com.model.config.Rail;
import com.model.config.Route;
import com.model.config.Ville;
import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteWagon;

import java.util.ArrayList;

public class StrongBot implements BotAction {


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


    @Override
    public boolean takeRail(Game game) {
        //Fonction qui permet de prendre des routes, et renvoie false si le bot n'a pas assez de carte

        //Variable pour avoir les cartes destination du bot
        ArrayList<CarteDestination>destination =  game.getJoueurCourant().getDestinationsList();

        for(int i =0; i<destination.size();i++){

            //Liste de ville qu'on a besoin
            ArrayList<Ville> villes = Node.findClosestPath(destination.get(i).getPremiereVille(),destination.get(i).getDeuxiemeVille());
            //Liste des routes que le bot doit completer pour finir sa missions
            ArrayList<Route> routesPossible = GarePosFinder.getNeededRoutes(villes,game.getJoueurCourant());


            for(int z = 0; z < routesPossible.size();z++){


                //On regarde si la route est null ou pas, si non alors on prends la route
                if(game.getJoueurCourant().mettreRoute(routesPossible.get(z))){


                    ArrayList<Rail> listeRail = routesPossible.get(z).getRailsRoute() ;
                    Player bot = game.getListPlayer().get( game.getRound().getWhoIsPlaying());

                    for (Rail rail : listeRail) {
                        rail.setOccuperPar(bot);
                    }
                    System.err.println("takeRail True");

                    return true ;

                }
            }



        }

        // DEBUG : System.out.println("takeRail False");

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


                    System.err.println("takeGare true 1");

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
    public CarteDestination[] takeMissionsCard(int max, Game game) {
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




    private boolean allMissionIsCompleted(Game game){
        //Fonction qui regarde si toute les missions sont complétés

        for (int i = 0; i < game.getJoueurCourant().getDestinationsList().size(); i++) {


            if (!game.getJoueurCourant().getDestinationsList().get(i).getComplete()) {
                return false;
            }

        }
        return true;
    }




    private void firstTurn(Game game) {
        //Fonction qui fait le premuier tour des bots fort

        //On rajoute les missions
        CarteDestination[] carteDestination = takeMissionsCard(6,game);


        for(int z = 0; z<carteDestination.length;z++){

            game.getJoueurCourant().getDestinationsList().add(carteDestination[z]);

        }

        game.getJoueurCourant().setFirstTurnOver(true);

        optimalCompleteMission(game);

    }

    private void optimalCompleteMission(Game game) {
        //Fonction procede de facon optimal si on peut ou non poser des rails pour completer les missions, sinon poser une gare ou piocher

        //Si on peut completer une route
        if (takeRail(game) ) {


            game.getRound().endRound(game);

            //Si non cherche l'endroit le plus optimal pour poser une gare
        } else {

            //6- On verifie qu'on peut poser une gare (de facon optimal)
            if ( takeGare(game,0)) {
                game.getRound().endRound(game);


                //Sinon :  7- On pioche :
            } else {

                drawCardWagon(game);

                game.getRound().endRound(game);

            }


        }
    }


    @Override
    public void play(Game game) {
        //Fonction principale du bot fort



        //On regarde si c'est le premier tour
        if(!game.getJoueurCourant().getFirstTurnOver()){

            firstTurn(game);

        }else {

            //1- On regarde si il a complété toute ses missions ou pas :
            if (allMissionIsCompleted(game)) {


                //2- On prends une a deux nouvelles mission, en fonction de la longueur des routes, au total il ne doit pas dépasser 5 comme longueur des routes total puis les prends
                CarteDestination[] addCard = takeMissionsCard(6, game);
                for (int j = 0; j < addCard.length; j++) {


                    game.getJoueurCourant().getDestinationsList().add(addCard[j]);
                }

                game.getRound().endRound(game);

                //3- On regarde si il peut faire finir sa mission avec les routes non prise qu'il lui manque
            } else {

                //Si non:
               optimalCompleteMission(game);

                }




        }


    }







}


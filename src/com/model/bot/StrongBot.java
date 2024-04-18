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

public class StrongBot implements BotAction {
/*
    private void MissWichCard(Route route) {
        int i = 0;
        setNbrWagon(this.nbrWagon - carteAEnlever);
        // Premiere boucle qui enlève juste la couleur color
        while(i < this.trainList.size() && 0 < carteAEnlever) {
            if ( trainList.get(i) == color ){
                this.trainList.remove(i);
                carteAEnlever--;
            } else {
                i++ ;
            }
        }
        // Deuxième boucle qui enlève les cartes de couleur loc pour complèter les carte à enlèver
        // si les carte de la couleur color est insuffissant
        int j = 0 ;
        while ( j < this.trainList.size() && 0 < carteAEnlever ){
            if ( trainList.get(j) == CarteWagon.Couleur.LOC) {
                this.trainList.remove(j);
                carteAEnlever--;
            } else{
                j++ ;
            }
        }
    }

    public CarteWagon.Couleur canCompletePathWithWagonListCard(Route route){

        return CarteWagon.Couleur.LOC;
    }

    public CarteWagon.Couleur canCompletePathMissingOneCard(Game game) {

        //Fonction qui si il manque une carte pour completer une route dans la liste de carte

        //Variable pour avoir round
        Round round = game.getRound();
        //Variable pour avoir Player du bot
        Player player= game.getListPlayer().get(round.getWhoIsPlaying());
        //Variable pour avoir les cartes destination du bot
        ArrayList<CarteDestination> destination = player.getDestinationsList();


        //On cherche la route la meilleure route possible
        for (int i = 0; i < destination.size(); i++) {

            //On stock la liste de ville dans une variable (ce qui forme une route)
            ArrayList<Ville> ville = Node.findClosestPath(destination.get(i).getPremiereVille(), destination.get(i).getDeuxiemeVille());

            //On regarde la
            Route toComplete = takeRailAux(ville);

            if(toComplete.getLongueur() <= player.getTrainCard().size() && toComplete.getProprietaire() == null){



            }



        }
        return null;
    }
    */

    @Override
    public void drawCardWagon(Game game) {
        //Variable pour appeler le joueur (le bot)
        Player player = game.getListPlayer().get(game.getRound().getWhoIsPlaying());

        //Variable qui va déterminer si oui ou non on peut prendre
        CarteWagon.Couleur color = null ;// canCompletePathMissingOneCard(game);
        //-Si il manque une carte:
        if (color != null) {

            int position = -1;

            //On cherche la couleur correspondante
            for(int i = 0; i< game.getCarteManager().getTrainCards().length;i++){
                if(game.getCarteManager().getTrainCards()[i] == color){
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

            for(int z = 1; z < routesPossible.size();z++){

                //On regarde si la route est null ou pas, si non alors on prends la route
                if(game.getJoueurCourant().mettreRoute(routesPossible.get(z))){
                    return true ;

                }
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


        }else{
            //5- Si il y a plus de chemin possible :

            // Alors prendre une nouvelle carte mission on fait l'étape 2
            takeMissionsCard(6,game);


        }

        return true;


    }

    @Override
    public CarteDestination[] takeMissionsCard(int max, Game game) {
        //Fonction qui compare les cartes destinations pour savoir quelles cartes prendre

        //Pour savoir si il y a au moins une carte destination
        boolean added = false;

        //Variable pour appeler cardDestination
        CarteManager carteManager = game.getCarteManager();


        int[] tmp = new int[3];
        //Premiere bouble qui va prendre la carte la plus petite
        for (int i = 1; i < carteManager.getDestinationsCards().length; i++) {
            if (carteManager.getDestinationsCards()[i].getNombrePoints()
                    < carteManager.getDestinationsCards()[i - 1].getNombrePoints()) {
                tmp[0] = i;
                added = true;
            }
        }

        if(!added){
            //On prends la premiere carte si il n'y a pas de carte < 6
            return carteManager.takeDestination( new int [1]);
        }

        //Deuxieme boucle qui ajoute une deuxieme carte mission si la somme < max
        for (int y = 0; y < carteManager.getDestinationsCards().length; y++) {
        if ((carteManager.getDestinationsCards()[y].getNombrePoints() +
                (carteManager.getDestinationsCards()[tmp[0]].getNombrePoints())
                <= max && y != tmp[0])) {

                tmp[1] = y;
            }
        }

        //Troisieme bouble qui regarde si la derniere carte + les cartes deja prisent soit < max
        for (int z = 0; z < carteManager.getDestinationsCards().length; z++) {
            if ((carteManager.getDestinationsCards()[z].getNombrePoints() +
                    (carteManager.getDestinationsCards()[tmp[0]].getNombrePoints())
                    <= max && z != tmp[0] && z != tmp[1])) {

                tmp[2] = z;
            }
        }



        return carteManager.takeDestination(tmp);


    }




    private boolean allMissionIsCompleted(Game game){
        //Fonction qui regarde si toute les missions sont complétés
        for (int i = 0; i < game.getListPlayer().get(game.getRound().getWhoIsPlaying()).getDestinationsList().size(); i++) {

            if (!game.getListPlayer().get(game.getRound().getWhoIsPlaying()).getDestinationsList().get(i).getComplete()) {
                return false;
            }

        }
        return true;
    }


    private boolean canCompletePath(Game game){
        //Fonction qui regarde si on peut completer une route pour une mission

        //Variable qui donne la liste de destination
        ArrayList<CarteDestination> destination =  game.getListPlayer().get(game.getRound().getWhoIsPlaying()).getDestinationsList();

        //On regarde si les routes pour completer toute les missions du joueurs ne sont pas bloqués
        for (int l = 0; l <destination.size(); l++) {

            //Variable qui donne un chemins possible grace a une liste de ville
            ArrayList<Ville> chemin = Node.findClosestPath(destination.get(l).getPremiereVille(),destination.get(l).getDeuxiemeVille());

            //Si il y a une mission ou on peut remplir alors on la fait
            if(game.getRoutes().get(l).getProprietaire() == null && !chemin.isEmpty()) {
                return true;
            }
        }
        //Si il n'y a pas de mission qui peut etre remplis
        return false;

    }






    @Override
    public void play(Game game) {
        //Fonction principale du bot fort

        //1- On regarde si il a complété toute ses missions ou pas :
        if (allMissionIsCompleted(game)) {

            //2- On prends une a deux nouvelles mission, en fonction de la longueur des routes, au total il ne doit pas dépasser 5 comme longueur des routes total puis les prends
            CarteDestination[] addCard = takeMissionsCard(6, game);
            for (int j = 0; j < addCard.length; j++) {
                game.getListPlayer().get(game.getRound().getAction()).getDestinationsList().add(addCard[j]);
            }

            //3- On regarde si il peut faire finir sa mission avec les routes non prise qu'il lui manque
        } else {

                //Si non:
                if (! (canCompletePath(game) ) &&  takeGare(game,0)) {

                    game.getRound().endRound(game);

                    //Si oui
                } else {

                    //6- On pose les wagons
                    if (takeRail(game)) {

                        game.getRound().endRound(game);


                        //Sinon :  7- On pioche :
                    } else {

                        drawCardWagon(game);

                        game.getRound().endRound(game);

                        }


                    }


                }


            }


    }


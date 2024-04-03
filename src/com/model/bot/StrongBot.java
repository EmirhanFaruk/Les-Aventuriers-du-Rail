package com.model.bot;

import com.model.Game;
import com.model.Player;
import com.model.Round;
import com.model.ai.Node;
import com.model.config.Rail;
import com.model.config.Route;
import com.model.config.Ville;
import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteManager;

import java.util.ArrayList;

public class StrongBot implements BotAction {
    @Override
    public void drawCardWagon(Game game) {
        //Variable pour appeler le joueur (le bot)
        Player player = game.getListPlayer().get(game.getRound().getWhoIsPlaying());

        //-Si il manque une carte:
        if (true) {

            // 8- On prends la couleur manquante sur le tas de carte visible et on tire aléatoirement dans la pioche invisible
            if (true) {



                //9- Si on peut pas, on prends une carte locomotive sur le tas de carte visible
            } else if(true){



                //10- Sinon on pioche 2 cartes dans la pioche invisible
            } else{



            }


        } else {

            //Sinon on pioche 2 cartes aléatoire
            player.getTrainList().add(game.getCarteManager().drawCard());
            player.getTrainList().add(game.getCarteManager().drawCard());


        }

    }

    public Route takeRailAux(ArrayList<Ville> villes){
        //Fonction qui retourne la premiere route que le joueur peut completer dans la liste
        for(int i = 1; i< villes.size() ;i++){

            //Variable qui represente les villes
            Ville ville1 = villes.get(i);
            Ville ville2 = villes.get(i-1);

            //On parcours la liste de route de la ville1 pour trouver celle qui relie a la ville2
            for(int y = 0; i< ville1.getRoutes().size();y++){

                //Variable qui represente la route obtenu dans la liste de route de ville1
                Route route = ville1.getRoutes().get(y);

                //On verifie que c'est bien la route demandé
                if(route.links(ville1,ville2)){

                   return route;

                }

            }

        }
        return null;

    }
    @Override
    public boolean takeRail(Game game) {
        //Variable pour avoir round
        Round round = game.getRound();

        //Fonction qui permet de poser prendre des routes, et renvie false si le bot n'a pas assez de carte
        ArrayList<CarteDestination>destination =  game.getListPlayer().get(round.getWhoIsPlaying()).getDestinationsList();
        for(int i =0; i<destination.size();i++){
            ArrayList<Ville> ville = Node.findClosestPath(destination.get(i).getPremiereVille(),destination.get(i).getDeuxiemeVille());

            Route toAdd = takeRailAux(ville);

            //On regarde si la route est null ou pas, si non alors on prends la route
            if(game.getListPlayer().get(round.getWhoIsPlaying()).mettreRoute(toAdd)){
                return true ;

            }


        }

        return false;
    }

    @Override
    public boolean takeGare(Game game, int wichStation) {

        return true;
    }

    @Override
    public CarteDestination[] takeMissionsCard(int max, Game game) {
        //Fonction qui compare les cartes destinations pour savoir quelles cartes prendre

        //Variable pour appeler cardDestination
        CarteManager carteManager = game.getCarteManager();

        //On regarde lequel des cartes mission a la plus petite route,
        ArrayList<CarteDestination> tab = new ArrayList<>();
        int[] tmp = new int[2];
        //Premiere bouble qui va prendre la carte la plus petite
        for (int i = 1; i < carteManager.getDestinationsCards().length; i++) {
            if (carteManager.getDestinationsCards()[i].getNombrePoints()
                    < carteManager.getDestinationsCards()[i - 1].getNombrePoints()) {
                tmp[0] = i;
            }
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

        return carteManager.takeDestination(tmp, game);


    }




    private boolean allMissionIsCompleted(Game game){
        for (int i = 0; i < game.getListPlayer().get(game.getRound().getWhoIsPlaying()).getDestinationsList().size(); i++) {

            if (!game.getListPlayer().get(game.getRound().getWhoIsPlaying()).getDestinationsList().get(i).getComplete()) {
                return false;
            }

        }
        return true;
    }


    private boolean canCompletePath(Game game){
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

        //Variable du joueur
        Player joueur = game.getListPlayer().get(game.getRound().getWhoIsPlaying());

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
                if (! canCompletePath(game)) {

                            /*

                    4- On cherche l'endroit le plus optimale pour poser une gare :

                        5- Si il y a plus de chemin possible :
                         alors on regarde si les autres missions sont complété

                                 Si oui :
                                    Alors prendre une nouvelle carte mission on fait l'étape 2

                                Sinon :
                                    Completer les autres missions
             */

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









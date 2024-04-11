package com.model.ai;

import com.model.Player;
import com.model.config.Rail;
import com.model.config.Route;
import com.model.config.Ville;
import com.model.config.carte.CarteWagon;

import java.util.ArrayList;

public class GarePosFinder
{
    /**
     * Tries a Ville as a gare, to see if it shortens the path by routeToReduce.
     * @param start start ville for the destination
     * @param end end ville for the destination
     * @param wannaBeGare the ville to try as a gare
     * @param routeToReduce wanted route number to be reduced
     * @param player player for the A* algorithm
     * @return if the difference is less or equal than routeToReduce
     */
    private static boolean tryVille(Ville start, Ville end, Ville wannaBeGare, int routeToReduce, Player player)
    {
        ArrayList<Ville> shortestWay = Node.findClosestPath(start, end, player);

        ArrayList<Ville> gareTry = Node.findClosestPath(start, end, player, wannaBeGare);

        int ogLength = shortestWay.size();
        int newLength = gareTry.size();

        if (ogLength > 1)
        {
            return ogLength - newLength <= routeToReduce;
        }

        return false;
    }


    /**
     * Tries all villes to find the possible solutions(put a gare to somewhere).
     * @param start start ville for the destination
     * @param end end ville for the destination
     * @param villesToTry villes to try as a gare
     * @param routeToReduce wanted route number to be reduced
     * @param player player for the A* algorithm
     * @return the list of the possible villes
     */
    private static ArrayList<Ville> tryAllVilles(Ville start, Ville end, ArrayList<Ville> villesToTry, int routeToReduce, Player player)
    {
        ArrayList<Ville> res = new ArrayList<>();

        for (Ville wannaBeGare : villesToTry)
        {
            if (tryVille(start, end, wannaBeGare, routeToReduce, player))
            {
                res.add(wannaBeGare);
            }
        }

        return res;
    }

    /**
     * Gets wanted villes to pose as a gare within the limit of the max route/rail(to put) number difference.
     * @param start start ville for the destination
     * @param end end ville for the destination
     * @param villesToTry villes to try as a gare
     * @param limit max route number difference
     * @param byRail true if counting rails, route if false
     * @param player player
     * @return the wanted villes
     */
    public static ArrayList<Ville> getWantedVillesDiff(Ville start, Ville end, ArrayList<Ville> villesToTry, int limit, boolean byRail, Player player)
    {

    }

    /**
     * Gets wanted villes to pose as a gare within the limit of the max route/rail to put.
     * @param start start ville for the destination
     * @param end end ville for the destination
     * @param villesToTry villes to try as a gare
     * @param limit max route/rail number to put
     * @param byRail true if counting rails, route if false
     * @param player player
     * @return the wanted villes
     */
    public static ArrayList<Ville> getWantedVilles(Ville start, Ville end, ArrayList<Ville> villesToTry, int limit, boolean byRail, Player player)
    {

    }



    /**
     * Gets needed routes for a way(with the least cost possible)
     * @param way da wae
     * @param player the player to see if the route/ville is already owned
     * @return the routes needed to pose
     */
    private static ArrayList<Route> getNeededRoutes(ArrayList<Ville> way, Player player)
    {
        ArrayList<Route> res = new ArrayList<>();

        // Parcourir les villes
        for (int i = 0; i < way.size() - 1; i++)
        {
            Ville current = way.get(i);
            Ville next = way.get(i + 1);
            // Si un de villes est deja occupé par le joueur, on saute ces villes
            if (!(current.getIsOccuped() == player || next.getIsOccuped() == player)) {
                // Sinon on parcour les routes pour trouver une route qui les lie
                Route routeNeeded = null;
                for (Route route : current.getRoutes())
                {
                    // Si cette route les lie
                    if (route.links(current, next))
                    {
                        // Si cette route est occupé par le joueur, on saute sans ajouter
                        if (route.getProprietaire() == player)
                        {
                            routeNeeded = null;
                            break;
                        }
                        else
                        {
                            // Sinon on trouve la meilleur route a ajouter(pour le cost)
                            boolean shouldReplace =
                                    !(routeNeeded.getCouleur() == Rail.Content.JOKER &&
                                    route.getCouleur() != Rail.Content.JOKER)
                                    ||
                                    !(routeNeeded.getCouleur() != Rail.Content.JOKERETOILEE &&
                                    route.getCouleur() == Rail.Content.JOKERETOILEE);
                            if (shouldReplace)
                            {
                                routeNeeded = route;
                            }
                        }
                    }
                }

                // On ajoute si on a besoin d'ajouter une ville
                if (routeNeeded != null)
                {
                    res.add(routeNeeded);
                }
            }
        }

        return res;
    }


    private static int getRailCount(ArrayList<Route> routes)
    {
        int res = 0;

        for (Route route : routes)
        {
            res += route.getLongueur();
        }

        return res;
    }
}

package com.model.ai;

import com.model.Player;
import com.model.config.Rail;
import com.model.config.Route;
import com.model.config.Ville;

import java.util.ArrayList;

public class GarePosFinder
{

    /**
     * Checks if there's a new possible way of the wannabeGare.
     * @param start start ville for the destination
     * @param end end ville for the destination
     * @param wannaBeGare the ville to try as a gare
     * @param player player for the A* algorithm
     * @return the number wanted
     */
    private static boolean trySingleVilleExists(Ville start, Ville end, Ville wannaBeGare, Player player)
    {
        ArrayList<Ville> gareTry = Node.findClosestPath(start, end, player, wannaBeGare);

        return !gareTry.isEmpty();
    }



    /**
     * Gets the length of the new possible way of the wannabeGare.
     * @param start start ville for the destination
     * @param end end ville for the destination
     * @param wannaBeGare the ville to try as a gare
     * @param byRail true if counting rails, route if false
     * @param player player for the A* algorithm
     * @return the number wanted
     */
    private static int trySingleVille(Ville start, Ville end, Ville wannaBeGare, boolean byRail, Player player)
    {
        ArrayList<Ville> gareTry = Node.findClosestPath(start, end, player, wannaBeGare);



        ArrayList<Route> neededRoutes = getNeededRoutes(gareTry, player);


        int newLength = neededRoutes.size();
        if (byRail)
        {
            newLength = getRailCount(neededRoutes);
        }

        return newLength;
    }


    /**
     * Tries a Ville as a gare, to see if it shortens the path by routeToReduce.
     * @param start start ville for the destination
     * @param end end ville for the destination
     * @param wannaBeGare the ville to try as a gare
     * @param routeToReduce wanted route number to be reduced
     * @param byRail true if counting rails, route if false
     * @param player player for the A* algorithm
     * @return if the difference is less or equal than routeToReduce
     */
    private static boolean tryVilleDiff(Ville start, Ville end, Ville wannaBeGare, int routeToReduce, boolean byRail, Player player, int ogLength, boolean aWayExists)
    {
        int newLength = trySingleVille(start, end, wannaBeGare, byRail, player);

        boolean gareWayExists = trySingleVilleExists(start, end, wannaBeGare, player);

        // Check if a way exists
        if (aWayExists && gareWayExists)
        {
            return ogLength - newLength >= routeToReduce;
        }

        // If not, if a way can be made then true
        return !aWayExists && gareWayExists;
    }


    /**
     * Tries all villes to find the possible solutions(put a gare to somewhere), using tryVilleDiff.
     * @param start start ville for the destination
     * @param end end ville for the destination
     * @param villesToTry villes to try as a gare
     * @param routeToReduce wanted route number to be reduced
     * @param byRail true if counting rails, route if false
     * @param player player for the A* algorithm
     * @return the list of the possible villes
     */
    private static ArrayList<Ville> tryAllVillesDiff(Ville start, Ville end, ArrayList<Ville> villesToTry, int routeToReduce, boolean byRail, Player player, boolean modeNuke)
    {
        ArrayList<Ville> res = new ArrayList<>();

        ArrayList<Ville> shortestWay = Node.findClosestPath(start, end, player);

        boolean aWayExists = !shortestWay.isEmpty();


        ArrayList<Route> neededRoutes = getNeededRoutes(shortestWay, player);
        int ogLength = neededRoutes.size();

        if (byRail)
        {
            ogLength = getRailCount(neededRoutes);
        }

        for (Ville wannaBeGare : villesToTry)
        {
            if (wannaBeGare.getIsOccuped() == null || modeNuke)
            {
                if (tryVilleDiff(start, end, wannaBeGare, routeToReduce, byRail, player, ogLength, aWayExists))
                {
                    res.add(wannaBeGare);
                }
            }
        }

        return res;
    }



    /**
     * Tries all villes to find the possible solutions(put a gare to somewhere), using trySingleVille.
     * @param start start ville for the destination
     * @param end end ville for the destination
     * @param villesToTry villes to try as a gare
     * @param limit max number of routes/rails wanted to use
     * @param byRail true if counting rails, route if false
     * @param player player for the A* algorithm
     * @return the list of the possible villes
     */
    private static ArrayList<Ville> tryAllVilles(Ville start, Ville end, ArrayList<Ville> villesToTry, int limit, boolean byRail, Player player)
    {
        ArrayList<Ville> res = new ArrayList<>();

        for (Ville wannaBeGare : villesToTry)
        {
            if (trySingleVille(start, end, wannaBeGare,  byRail, player) <= limit)
            {
                res.add(wannaBeGare);
            }
        }

        return res;
    }


    /**
     * Gets the lowest cost ville. Used in getWantedVille and getWantedVilleDiff.
     * @param start start ville for the destination
     * @param end end ville for the destination
     * @param allVilles villes to find the lowest cost ville
     * @param byRail true if counting rails, route if false
     * @param player player
     * @return the lowest cost ville
     */
    private static Ville getMinVille(Ville start, Ville end, ArrayList<Ville> allVilles, boolean byRail, Player player)
    {
        Ville min = null;
        if (!allVilles.isEmpty())
        {
            min = allVilles.get(0);
            int minLength = trySingleVille(start, end, min, byRail, player);

            for (Ville wannaBeGare : allVilles)
            {
                int newLength = trySingleVille(start, end, wannaBeGare, byRail, player);
                if (newLength < minLength)
                {
                    min = wannaBeGare;
                    minLength = trySingleVille(start, end, min, byRail, player);
                }
            }

        }

        return min;
    }

    /**
     * Gets wanted ville to pose as a gare within the limit of the min route/rail(to put) number difference.
     * @param start start ville for the destination
     * @param end end ville for the destination
     * @param villesToTry villes to try as a gare
     * @param limit min route number difference
     * @param byRail true if counting rails, route if false
     * @param player player
     * @return the wanted ville
     */
    public static Ville getWantedVilleDiff(Ville start, Ville end, ArrayList<Ville> villesToTry, int limit, boolean byRail, Player player, boolean modeNuke)
    {
        ArrayList<Ville> allVilles = tryAllVillesDiff(start, end, villesToTry, limit, byRail, player, modeNuke);

        return getMinVille(start, end, allVilles, byRail, player);
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
    public static Ville getWantedVille(Ville start, Ville end, ArrayList<Ville> villesToTry, int limit, boolean byRail, Player player)
    {
        ArrayList<Ville> allVilles = tryAllVilles(start, end, villesToTry, limit, byRail, player);

        return getMinVille(start, end, allVilles, byRail, player);
    }



    /**
     * Gets needed routes for a way(with the least cost possible)
     * @param way da wae
     * @param player the player to see if the route/ville is already owned
     * @return the routes needed to pose
     */
    public static ArrayList<Route> getNeededRoutes(ArrayList<Ville> way, Player player)
    {
        ArrayList<Route> res = new ArrayList<>();

        // Parcourir les villes
        for (int i = 0; i < way.size() - 1; i++)
        {
            Ville current = way.get(i);
            Ville next = way.get(i + 1);
            // Si un de villes est deja occupé par le joueur, on saute ces villes
            if (!(current.getIsOccuped() == player || next.getIsOccuped() == player))
            {
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
                            break;
                        }
                        else
                        {
                            if (route.getProprietaire() == null)
                            {
                                // Sinon on trouve la meilleur route a ajouter(pour le cost)
                                boolean shouldReplace = true;
                                if (routeNeeded != null)
                                {
                                    shouldReplace =
                                            !(routeNeeded.getCouleur() == Rail.Content.JOKER &&
                                                    route.getCouleur() != Rail.Content.JOKER)
                                                    ||
                                                    !(routeNeeded.getCouleur() != Rail.Content.JOKERETOILEE &&
                                                            route.getCouleur() == Rail.Content.JOKERETOILEE);
                                }

                                if (shouldReplace)
                                {
                                    routeNeeded = route;
                                }
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


    /**
     * Compter les rails depuis la liste des routes.
     * @param routes la liste des routes
     * @return le nombre des rails
     */
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

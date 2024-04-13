package com.model.ai;

import com.model.Player;
import com.model.config.Route;
import com.model.config.Ville;

import java.util.ArrayList;

public class LongestFinder
{
    /**
     * Trouver le chemin le plus long entre deux villes sur les routes pris par le joueur.
     * @param v1 Ville 1
     * @param v2 Ville 2
     * @param player Joueur
     * @return liste des routes qui fait le plus long chemin
     */
    public static ArrayList<Route> findLongestWay(Ville v1, Ville v2, Player player)
    {
        ArrayList<Route> res = new ArrayList<>();
        ArrayList<Route> currentWay = new ArrayList<>();
        ArrayList<Ville> visited = new ArrayList<>();

        //System.out.println("Start of finding longest between " + v1.getNom() + " - " + v2.getNom());
        deepFirstSearch(v1, v2, visited, currentWay, res, player);


        return res;
    }


    /**
     * Algorithme de deepFirstSearch (on peut meme dire backtracking) qui trouve le plus long chemin par rapport aux points des routes.
     * @param currentVille Ville de debut/Ville courant
     * @param end Ville de destination
     * @param visited Villes visitées
     * @param currentWay Chemin courant
     * @param longestWay Le plus long chemin
     * @param player Le joueur qui a obtenu les routes
     */
    private static void deepFirstSearch(Ville currentVille, Ville end, ArrayList<Ville> visited, ArrayList<Route> currentWay, ArrayList<Route> longestWay, Player player)
    {
        //printWay(currentWay, currentVille, end);

        addDistinct(currentVille, visited);

        if (sameVille(currentVille, end))
        {
            if (calculatePoints(currentWay) > calculatePoints(longestWay))
            {
                longestWay.clear();
                longestWay.addAll(currentWay);
                //System.out.println("Put the most recent longest way: ");
                //printWay(longestWay, currentVille, end);
            }
        }
        else
        {
            for (Route route : currentVille.getRoutes())
            {
                boolean condition = samePlayer(route.getProprietaire(), player) && !getExists(route, currentVille, visited);
                if (condition)
                {
                    currentWay.add(route);
                    deepFirstSearch(findOtherVille(route, currentVille), end, visited, currentWay, longestWay, player);
                    currentWay.remove(currentWay.size() - 1);
                }
            }
        }

        visited.remove(currentVille);
    }

    /**
     * Comparer les noms des deux villes.
     * @param v1 Ville 1
     * @param v2 Ville 2
     * @return le resultat
     */
    private static boolean sameVille(Ville v1, Ville v2)
    {
        return v1.getNom().equals(v2.getNom());
    }

    /**
     * Comparer les noms des joueurs.
     * @param p1 Joueur 1
     * @param p2 Joueur 2
     * @return le resultat
     */
    private static boolean samePlayer(Player p1, Player p2)
    {
        if (p1 != null && p2 != null)
        {
            return p1.getName().equals(p2.getName());
        }
        return false;
    }

    /**
     * Verifier si la ville existe dans la liste villes en comparant leur nom.
     * @param ville Ville a comparer
     * @param villes liste des villes
     * @return le resultat
     */
    private static boolean exists(Ville ville, ArrayList<Ville> villes)
    {
        for (Ville v : villes)
        {
            if (sameVille(ville, v))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Appeler exists avec findOtherVille pour ajouter l'option de route.
     * @param route Route qui a deux villes a comparer
     * @param ville Ville a comparer
     * @param villes La liste des villes a chercher dedans
     * @return le resultat
     */
    private static boolean getExists(Route route, Ville ville, ArrayList<Ville> villes)
    {
        return exists(findOtherVille(route, ville), villes);
    }

    /**
     * Comparer ville avec les villes de route. Renvoie celui qui est different que ville.
     * @param route Route pour les villes
     * @param ville Ville a comparer
     * @return le resultat
     */
    private static Ville findOtherVille(Route route, Ville ville)
    {
        if (sameVille(route.getVille1(), ville))
        {
            return route.getVille2();
        }
        else
        {
            return route.getVille1();
        }
    }

    /**
     * Ajouter si la ville existe pas dans la liste.
     * @param ville Ville a ajouter
     * @param villes La liste des villes
     */
    private static void addDistinct(Ville ville, ArrayList<Ville> villes)
    {
        if (!exists(ville, villes))
        {
            villes.add(ville);
        }
    }

    /**
     * Calculer la distance an ajoutant les points des routes.
     * @param routes La liste des routes
     * @return Le point total des routes
     */
    private static int calculatePoints(ArrayList<Route> routes)
    {
        int res = 0;

        for (Route route : routes)
        {
            res += route.getNombrePoint();
        }

        return res;
    }



    public static void printWay(ArrayList<Route> routes, Ville v1, Ville v2)
    {
        System.out.println("Printing the way from " + v1.getNom() + " to " + v2.getNom());

        Ville ville1 = v1;
        Ville ville2 = null;

        for (Route route : routes)
        {
            ville2 = findOtherVille(route, ville1);
            ville1 = findOtherVille(route, ville2);
            System.out.println("Route between " + ville1.getNom() + " - " + ville2.getNom());
        }

        System.out.println("Way done.");
    }

    public static void doForAll(ArrayList<Ville> villes, Player player)
    {
        for (Ville ville1 : villes)
        {
            for (Ville ville2 : villes)
            {
                if (!sameVille(ville1, ville2))
                {
                    ArrayList<Route> longestWay = findLongestWay(ville1, ville2, player);

                    printWay(longestWay, ville1, ville2);
                }
            }
        }
    }


    public static void printCatPart(ArrayList<Ville> villes, Player player)
    {
        for (Ville ville1 : villes)
        {
            for (Ville ville2 : villes)
            {
                if (!sameVille(ville1, ville2))
                {
                    ArrayList<Route> longestWay = findLongestWay(ville1, ville2, player);

                    printWay(longestWay, ville1, ville2);
                }
            }
        }
    }
}

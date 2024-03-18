package com.model;

import com.model.config.Route;
import com.model.config.Ville;

import java.util.ArrayList;

public class PathFinding
{
    /**
     * Returns an ArrayList of strings that makes the shortest path between 2 villes
     * @return the villes to get to in order to get the shortest path
     */
    public static ArrayList<String> findPath(ArrayList<Route> routes, Ville ville1, Ville ville2)
    {
        ArrayList<String> res = new ArrayList<>();


        return res;
    }





    /**
     * Checks if 2 routes has the same destinations and departs
     * @param r1 Route 1
     * @param r2 Route 2
     * @return true if they are same
     */
    private static boolean sameRoute(Route r1, Route r2)
    {
        boolean res = r1.getVille1().getNom().equals(r2.getVille1().getNom()) && r1.getVille2().getNom().equals(r2.getVille2().getNom());
        res = res || (r1.getVille1().getNom().equals(r2.getVille2().getNom()) && r1.getVille2().getNom().equals(r2.getVille1().getNom()));
        return res;
    }
}

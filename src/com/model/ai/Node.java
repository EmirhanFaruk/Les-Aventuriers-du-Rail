package com.model.ai;

import com.model.Player;
import com.model.config.Route;
import com.model.config.Ville;

import java.util.ArrayList;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Node
{
    private Node parent;

    private final Ville ville;

    private ArrayList<Node> neighbors;

    private double f = Double.MAX_VALUE; // total of move and toGo
    private double g; // the cost already used

    private double h; // the cost to go


    /**
     * Constructor of Node
     * @param v ville of the node
     * @param p Parent node
     */
    private Node(Ville v, Node p)
    {
        ville = v;
        parent = p;
    }

    /**
     * Calculates distance of 2 villes.
     * @param v1 Ville 1
     * @param v2 Ville 2
     * @return double
     */
    private double calculateDistance(Ville v1, Ville v2)
    {
        double x = pow(v1.getX() - v2.getX(), 2);
        double y = pow(v1.getY() - v2.getY(), 2);
        return sqrt(x + y);
    }

    /**
     * Calculates total cost.
     * @param end the target ville
     */
    private void calculateTotal(Ville end)
    {
        // F(Total cost function), total of move and toGo
        toGo(end);
        f = g + h;
    }

    /**
     * Calculates the cost to go.
     * @param end the target ville
     */
    private void toGo(Ville end)
    {
        // Heuristic function, the cost to go
        h = calculateDistance(this.ville, end);
    }


    /**
     * Determines whether the villes of 2 nodes are the same.
     * @param a node 1
     * @param b node 2
     * @return result
     */
    private static boolean same(Node a, Node b)
    {
        // Verifie si les deux noeuds ont la meme coordonnées
        return a.ville.getX() == b.ville.getX() && a.ville.getY() == b.ville.getY();
    }

    /**
     * Gets possible neighbor nodes.
     * @param player if a route is already owned by player, its cost is 0
     * @param owned true if checking for only owned routes, false if also routes with no owners
     */
    private void getNeighbors(Player player, boolean owned)
    {
        neighbors = new ArrayList<>();

        for (Route route : ville.getRoutes())
        {
            boolean available = route.getProprietaire() == player;
            if (!owned)
            {
                available = available || route.getProprietaire() == null;
            }
            if(available)
            {
                Ville[] villes = new Ville[]{route.getVille1(), route.getVille2()};
                for (Ville routeVille : villes)
                {
                    if (routeVille != ville)
                    {
                        Node node = new Node(routeVille, this);
                        node.g = this.g + route.getLongueur();
                        // If owned by player, cost stays the same
                        if (player != null)
                        {
                            if (routeVille.getIsOccuped() == player)
                            {
                                node.g = this.g;
                            }
                        }

                        if (!Node.same(this, node))
                        {
                            neighbors.add(node);
                        }
                    }
                }
            }
        }
    }

    /**
     * Finds lowest cost node of the list.
     * @param list the said list
     * @param lowest true to find the lowest cost, false to find the highest
     * @return the lowest cost node
     */
    private static Node findLowestCost(ArrayList<Node> list, boolean lowest)
    {
        // Self-explanatory name
        // We are sure that the list is not empty
        Node res = list.get(0);
        for (Node n : list)
        {
            if (lowest)
            {
                if(res.g > n.g)
                {
                    res = n;
                }
            }
            else
            {
                if(res.g < n.g)
                {
                    res = n;
                }
            }

        }
        return res;
    }

    /**
     * Adds the given node to the list but if a node with same ville exist, it swaps itself with it.
     * @param list the said list
     * @param to_add the said node
     */
    private static void addListDistinctive(ArrayList<Node> list, Node to_add)
    {
        // Adds element to the list but swaps if coordinates are the same
        Node n = sameIn(list, to_add);
        if(n != null)
        {
            if (n.f > to_add.f)
            {
                list.remove(n);
                list.add(to_add);
            }
            return;
        }
        list.add(to_add);
    }

    /**
     * Determines if a node in the list and the given node has same villes.
     * @param list the said list
     * @param element the said node
     * @return node if exists, null otherwise.
     */
    private static Node sameIn(ArrayList<Node> list, Node element)
    {
        // Returns it if a node with same coordinates exist. If not, returns null.
        for (Node n : list)
        {
            if(Node.same(n, element))
            {
                return n;
            }
        }
        return null;
    }

    /**
     * Determines if the given node is "same" with any of the nodes in the given list.
     * @param list the said list
     * @param element the said node
     * @return true if same, false otherwise
     */
    private static boolean exists(ArrayList<Node> list, Node element)
    {
        for (Node n : list)
        {
            if(Node.same(n, element))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Puts almost every other functions together to make the A* algorithm.
     * @param s the start ville
     * @param e the target ville
     * @param player the player for the routes
     * @param shortest true to find the shortest path, false to find the longest owned
     * @return the node containing a path from end to start with its one way chained parents
     */
    private static Node aStar(Ville s, Ville e, Player player, boolean shortest)
    {
        Node start = new Node(s, null);
        Node end = new Node(e, null);
        ArrayList<Node> closedList = new ArrayList<>();
        ArrayList<Node> openList = new ArrayList<>();

        // Initializing for the loop
        start.calculateTotal(e);
        openList.add(start);


        while(!openList.isEmpty())
        {
            // Get lowest cost node and use that node to proceed
            Node current = Node.findLowestCost(openList, shortest);

            // Remove it from the open list because now it's closed
            openList.remove(current);
            // End algo if arrived to the end
            if(Node.same(current, end))
            {
                return current;
            }

            // Get neightbors, update total cost and add to the closed list
            current.getNeighbors(player, !shortest);
            current.calculateTotal(e);
            addListDistinctive(closedList, current);

            // For each neighbor, we will try to add the possible ones to the open list
            // so we can proceed with these
            for(Node neighbor : current.neighbors)
            {
                // Calculate total cost
                neighbor.calculateTotal(e);

                // See if this neighbor is in closedList
                boolean flag = false;
                for(Node c : closedList)
                {
                    if(Node.same(neighbor, c))
                    {
                        flag = true;
                    }
                }

                // If not in closedList, we can check if we can add it
                if(!flag)
                {
                    neighbor.getNeighbors(player, !shortest);
                    // If already in open and new path shorter,
                    // update the one in the list and do not add it again
                    Node checker = Node.sameIn(openList, neighbor);
                    if((checker != null && neighbor.f < checker.f))
                    {
                        checker.f = neighbor.f;
                        checker.parent = current;
                    }
                    // If it doesn't exist in the list,
                    // Add it to the list
                    if(!exists(openList, neighbor))
                    {
                        openList.add(neighbor);
                    }
                }
            }
        }

        // If the open list has no more elements,
        // it means it's impossible to reach the target node
        return null;
    }


    /**
     * Returns an ArrayList of strings that makes the shortest path between 2 villes
     * @param ville1 1st ville
     * @param ville2 2nd ville
     * @return the villes to get to in order to get the shortest path
     */
    private static ArrayList<Ville> findClosestPath(Ville ville1, Ville ville2)
    {
        ArrayList<Ville> res = new ArrayList<>();

        Node resNode = aStar(ville1, ville2, null, true);

        while(resNode != null)
        {
            res.add(0, resNode.ville);
            resNode = resNode.parent;
        }


        return res;
    }

    /**
     * Returns an ArrayList of strings that makes the shortest path between 2 villes,
     * by also including the player owned routes.
     * @param ville1 1st ville
     * @param ville2 2nd ville
     * @param player the player to find the owned routes. Put null if player isn't needed
     * @return the villes to get to in order to get the shortest path
     */
    public static ArrayList<Ville> findClosestPath(Ville ville1, Ville ville2, Player player)
    {
        if (player == null)
        {
            return findClosestPath(ville1, ville2);
        }

        ArrayList<Ville> res = new ArrayList<>();

        Node resNode = aStar(ville1, ville2, player, true);

        while(resNode != null)
        {
            res.add(0, resNode.ville);
            resNode = resNode.parent;
        }


        return res;
    }


    /**
     * Returns an ArrayList of strings that makes the longest path between 2 villes using owned routes
     * @return the villes to get to in order to get the longest owned path
     */
    public static ArrayList<Ville> findLongestPath(Ville ville1, Ville ville2, Player player)
    {
        ArrayList<Ville> res = new ArrayList<>();

        Node resNode = aStar(ville1, ville2, player, false);

        while(resNode != null)
        {
            res.add(0, resNode.ville);
            resNode = resNode.parent;
        }

        return res;
    }


    public static void printWay(ArrayList<Ville> villes)
    {
        if(!villes.isEmpty())
        {
            System.out.println("\n\n===========================================================\n\n");
            System.out.println("The shortest way from " + villes.get(0).getNom() + " to " + villes.get(villes.size() - 1).getNom() + ":");
            for (int i = 0; i < villes.size(); i++)
            {
                System.out.print(villes.get(i).getNom());
                if(i + 1 != villes.size())
                {
                    for (Route route : villes.get(i).getRoutes())
                    {
                        if(route.getVille1() == villes.get(i + 1) || route.getVille2() == villes.get(i + 1))
                        {
                            System.out.print(" to " + villes.get(i + 1).getNom() + " using " + route.getLongueur() + " " + route.getCouleur() + " rail(s).");
                            break;
                        }
                    }
                }
                System.out.println();
            }
        }
    }

}

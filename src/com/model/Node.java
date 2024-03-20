package com.model;

import com.model.config.Case;
import com.model.config.Plateau;
import com.model.config.Route;
import com.model.config.Ville;

import java.util.ArrayList;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Node
{
    private Node parent;

    private Ville ville;

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
     * Calculates distance of 2 points.
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
     * @param end the target coordinate
     */
    private void calculateTotal(Ville end)
    {
        // F(Total cost function), total of move and toGo
        toGo(end);
        f = g + h;
    }

    /**
     * Calculates the cost to go.
     * @param end the target coordinate
     */
    private void toGo(Ville end)
    {
        // Heuristic function, the cost to go
        h = calculateDistance(this.ville, end);
    }


    /**
     * Determines whether the coordinates of 2 nodes are the same.
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
     */
    private void getNeighbors()
    {
        neighbors = new ArrayList<>();

        for (Route route : ville.getRoutes())
        {
            Ville[] villes = new Ville[]{route.getVille1(), route.getVille2()};
            for (Ville routeVille : villes)
            {
                if(routeVille != ville)
                {
                    Node node = new Node(routeVille, this);
                    node.g = this.g + route.getLongueur();
                    if(!Node.same(this, node))
                    {
                        neighbors.add(node);
                    }
                }
            }
        }
    }

    /**
     * Finds lowest cost node of the list.
     * @param list the said list
     * @return the lowest cost node
     */
    private static Node findLowestCost(ArrayList<Node> list)
    {
        // Self-explanatory name
        // We are sure that the list is not empty
        Node res = list.get(0);
        for (Node n : list)
        {
            if(res.g > n.g)
            {
                res = n;
            }
        }
        return res;
    }

    /**
     * Adds the given node to the list but if a node with same coordinates exist, it swaps itself with it.
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
     * Determines if a node in the list and the given node has same coordinates.
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
     * @return the node containing a path from end to start with its one way chained parents
     */
    private static Node aStar(Ville s, Ville e)
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
            Node current = Node.findLowestCost(openList);
            openList.remove(current);
            if(Node.same(current, end))
            {
                return current;
            }

            current.getNeighbors();
            current.calculateTotal(e);
            addListDistinctive(closedList, current);

            for(Node neighbor : current.neighbors)
            {
                neighbor.calculateTotal(e);
                boolean flag = false;
                for(Node c : closedList)
                {
                    if(Node.same(neighbor, c))
                    {
                        flag = true;
                    }
                }

                if(!flag)
                {
                    neighbor.getNeighbors();
                    // if in open and new path shorter
                    Node checker = Node.sameIn(openList, neighbor);
                    if((checker != null && neighbor.f < checker.f))
                    {
                        checker.f = neighbor.f;
                        checker.parent = current;
                    }
                    if(!exists(openList, neighbor))
                    {
                        //System.out.println("Second if");
                        if(checker != null && neighbor.f < checker.f)
                        {
                            checker.f = neighbor.f;
                            checker.parent = current;
                        }
                        neighbor.calculateTotal(e);
                        openList.add(neighbor);
                    }
                }
            }
        }

        return null;
    }


    /**
     * Returns an ArrayList of strings that makes the shortest path between 2 villes
     * @return the villes to get to in order to get the shortest path
     */
    public static ArrayList<String> findClosestPath(ArrayList<Route> routes, Ville ville1, Ville ville2)
    {
        ArrayList<String> res = new ArrayList<>();

        return res;
    }


    /**
     * Returns an ArrayList of strings that makes the longest path between 2 villes using owned routes
     * @return
     */
    private static ArrayList<String> findLongestPath()
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

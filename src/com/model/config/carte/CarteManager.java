package com.model.config.carte;

import com.model.Game;
import com.model.ai.Node;
import com.model.config.Route;
import com.model.config.Ville;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static com.model.config.carte.CarteWagon.Couleur.*;

/**
 * La classe CarteManager gère les cartes de wagons et de destinations dans le jeu.
 */
public class CarteManager {

    // Le tableau des cartes Wagon du jeu
    private CarteWagon.Couleur[] trainCards = new CarteWagon.Couleur[3];

    // Le tableau des cartes Destination du jeu
    private CarteDestination[] destinationsCards = new CarteDestination[3];

    // La pile de cartes Destination
    public ArrayList<CarteDestination> PileCarteDestination = new ArrayList<>();

    // Vérification du mode de Jeu
    private boolean nuke;

    /**
     * Constructeur pour la classe CarteManager.
     *
     * @param mode Le mode du jeu (peut être "NUKE" pour activer le mode spécial).
     */
    public CarteManager(String mode) {
        // Change la variable `nuke` en true si le mode choisi est "NUKE".
        if (mode.equals("NUKE")) {
            nuke = true;
        }
        // Initialisation des cartes de wagons.
        for (int i = 0; i < trainCards.length; i++) {
            trainCards[i] = drawCard();
        }
        verifAllDifferent();
    }

    /**
     * Initialise la pile de cartes de destination en fonction du jeu donné.
     *
     * @param game L'objet jeu (Game) à partir duquel les routes et villes sont obtenues.
     */
    public void initPileCarteDestination(Game game) {
        ArrayList<Route> gameRoutes = game.getRoutes();
        for (Route route : gameRoutes) {
            // Les cartes destinations à courte distance (une route).
            PileCarteDestination.add(new CarteDestination(route));
        }
        // Crée 21 cartes de destinations supplémentaires.
        for (int i = 0; i < 21; i++) {
            Random random = new Random();
            int villesSize = game.getVilles().size();
            Ville v1 = game.getVilles().get(random.nextInt(villesSize));
            Ville v2 = game.getVilles().get(random.nextInt(villesSize));

            while (v2.getNom().equals(v1.getNom())) {
                v2 = game.getVilles().get(random.nextInt(villesSize));
            }

            while (carteDestExistante(v1, v2)) {
                v1 = game.getVilles().get(random.nextInt(villesSize));
                v2 = game.getVilles().get(random.nextInt(villesSize));
                while (v2.getNom().equals(v1.getNom())) {
                    v2 = game.getVilles().get(random.nextInt(villesSize));
                }
            }
            PileCarteDestination.add(new CarteDestination(v1, v2, nombrePointDistance(v1, v2)));
        }

        Collections.shuffle(PileCarteDestination);
        for (int i = 0; i < destinationsCards.length; i++) {
            destinationsCards[i] = PileCarteDestination.remove(0);
        }
    }

    /**
     * Vérifie si une carte de destination existe déjà pour les villes spécifiées.
     *
     * @param v1 La première ville.
     * @param v2 La deuxième ville.
     * @return true si une carte de destination existe déjà, sinon false.
     */
    private boolean carteDestExistante(Ville v1, Ville v2) {
        String nomV1 = v1.getNom();
        String nomV2 = v2.getNom();
        for (CarteDestination carte : PileCarteDestination) {
            if ((carte.getPremiereVille().getNom().equals(nomV1) && carte.getDeuxiemeVille().getNom().equals(nomV2)) ||
                    (carte.getPremiereVille().getNom().equals(nomV2) && carte.getDeuxiemeVille().getNom().equals(nomV1))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vérifie si une action est possible pour prendre une carte de wagon.
     *
     * @param action   L'action réalisée par le joueur (0 ou 2).
     * @param position La position de la carte de wagon.
     * @return true si l'action est possible, sinon false.
     */
    public boolean possibleTakeWagon(int action, int position) {
        if (trainCards[position] == LOC) {
            return action == 2;
        } else {
            return true;
        }
    }

    /**
     * Permet de prendre une carte de wagon.
     *
     * @param position La position de la carte de wagon.
     * @return La couleur de la carte de wagon prise.
     */
    public CarteWagon.Couleur takeWagon(int position) {
        // Renvoie la couleur de la carte de wagon à la position donnée.
        CarteWagon.Couleur renvoie = trainCards[position];
        trainCards[position] = drawCard();
        verifAllDifferent();
        return renvoie;
    }

    /**
     * Vérifie que les trois cartes de wagons visibles sont différentes.
     */
    private void verifAllDifferent() {
        while (trainCards[0] == trainCards[1] && trainCards[0] == trainCards[2] &&
                trainCards[0] == LOC) {
            trainCards[0] = drawCard();
            trainCards[1] = drawCard();
            trainCards[2] = drawCard();
        }
    }

    /**
     * Montre la couleur de la carte de wagon à une position donnée.
     *
     * @param position La position de la carte de wagon.
     * @return La couleur de la carte de wagon à la position donnée.
     */
    public CarteWagon.Couleur showWagon(int position) {
        return trainCards[position];
    }

    /**
     * Vérifie si le joueur a déjà pris une carte de destination.
     *
     * @return true si le joueur a déjà pris une carte de destination, sinon false.
     */
    public boolean alreadyPickedACard() {
        for (CarteDestination destinationsCard : this.destinationsCards) {
            if (destinationsCard == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Permet de prendre des cartes de destination.
     *
     * @param position Les positions des cartes de destination à prendre.
     * @return Un tableau des cartes de destination prises.
     */
    public CarteDestination[] takeDestination(int[] position) {
        // Fonction qui permet de prendre des cartes de destination à des positions données.
        CarteDestination[] renvoie = new CarteDestination[position.length];
        for (int i = 0; i < position.length; i++) {
            renvoie[i] = piocheCD(position[i]);
        }
        rerollDestination();
        return renvoie;
    }

    /**
     * Renvoyer une carte destination et remplir sa place avec une autre cd depuis la pioche.
     * @param index index de carte
     * @return carte donné par index
     */
    private CarteDestination piocheCD(int index)
    {
        CarteDestination res = destinationsCards[index];
        destinationsCards[index] = getDestination();
        return res;
    }

    /**
     * Remet de nouvelles cartes de destination après en avoir pris.
     */
    public void rerollDestination() {
        for (int i = 0; i < destinationsCards.length; i++) {
            if (destinationsCards[i] != null) {
                PileCarteDestination.add(PileCarteDestination.size(), destinationsCards[i]);
            }
            destinationsCards[i] = getDestination();
        }
    }

    /**
     * Tire une carte de wagon aléatoirement.
     *
     * @return La couleur de la carte de wagon tirée.
     */
    public CarteWagon.Couleur drawCard() {
        Random random = new Random();
        int pioche = random.nextInt(120);
        if (pioche >= 0 && pioche <= 11) {
            return BLEU;
        } else if (pioche >= 12 && pioche <= 23) {
            return VIOLET;
        } else if (pioche >= 24 && pioche <= 35) {
            return MARRON;
        } else if (pioche >= 36 && pioche <= 47) {
            return NOIRE;
        } else if (pioche >= 48 && pioche <= 59) {
            return VERT;
        } else if (pioche >= 60 && pioche <= 71) {
            return JAUNE;
        } else if (pioche >= 72 && pioche <= 83) {
            return BLANC;
        } else if (pioche >= 84 && pioche <= 95) {
            return ROUGE;
        } else if ((pioche >= 96 && pioche <= 107) && nuke) {
            return NUKE;
        } else {
            return LOC;
        }
    }

    /**
     * Permet d'obtenir une carte de destination de la pile.
     *
     * @return La carte de destination obtenue de la pile.
     */
    public CarteDestination getDestination() {
        return PileCarteDestination.remove(0);
    }

    /**
     * Calcule la longueur d'un chemin donné.
     *
     * @param chemin Le chemin à calculer.
     * @return La longueur totale du chemin.
     */
    private int cheminLongueur(ArrayList<Ville> chemin) {
        int res = 0;
        while (chemin.size() > 1) {
            Ville current = chemin.get(0);
            Ville next = chemin.get(1);
            res += findLink(current, next).getLongueur();
            chemin.remove(0);
        }
        return res;
    }

    /**
     * Recherche le lien entre deux villes.
     *
     * @param v1 La première ville.
     * @param v2 La deuxième ville.
     * @return La route entre les deux villes.
     */
    private Route findLink(Ville v1, Ville v2) {
        ArrayList<Route> routes = v1.getRoutes();
        for (Route route : routes) {
            if (links(v1, v2, route)) {
                return route;
            }
        }
        return routes.get(0);
    }

    /**
     * Vérifie si une route relie deux villes.
     *
     * @param v1    La première ville.
     * @param v2    La deuxième ville.
     * @param route La route à vérifier.
     * @return true si la route relie les deux villes, sinon false.
     */
    private boolean links(Ville v1, Ville v2, Route route) {
        return (route.getVille1() == v1 && route.getVille2() == v2) ||
                (route.getVille1() == v2 && route.getVille2() == v1);
    }

    /**
     * Calcule la distance en points entre deux villes.
     *
     * @param v1 La première ville.
     * @param v2 La deuxième ville.
     * @return La distance en points entre les deux villes.
     */
    public int nombrePointDistance(Ville v1, Ville v2) {
        ArrayList<Ville> chemin = Node.findClosestPath(v1, v2, null);
        int longueur = cheminLongueur(chemin);
        return longueur;
    }

    /**
     * Vérifie si toutes les cartes de wagons sont vides.
     *
     * @return true si toutes les cartes de wagons sont vides, sinon false.
     */
    public boolean trainCardisEmpty() {
        for (CarteWagon.Couleur c : trainCards) {
            if (c != null) {
                return false;
            }
        }
        return true;
    }

    /* Getteurs et setteurs */

    /**
     * Obtient les cartes de destinations visibles.
     *
     * @return Un tableau des cartes de destinations visibles.
     */
    public CarteDestination[] getDestinationsCards() {
        return destinationsCards;
    }

    /**
     * Obtient les cartes de wagons visibles.
     *
     * @return Un tableau des cartes de wagons visibles.
     */
    public CarteWagon.Couleur[] getTrainCards() {
        return trainCards;
    }
}

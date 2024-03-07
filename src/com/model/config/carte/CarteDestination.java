package com.model.config.carte;

import com.model.config.Plateau;
import com.model.config.Route;
import com.model.config.Ville;

import javax.swing.*;

public class CarteDestination extends JPanel {

    private Ville premiereVille;
    private Ville deuxiemeVille;
    private int nombrePoints;


    public Ville getPremiereVille() {
        return premiereVille;
    }
    public Ville getDeuxiemeVille() {
        return deuxiemeVille;
    }
    public int getNombrePoints() {
        return nombrePoints;
    }


    //Pour les testes on va utiliser ce constructeur

    public CarteDestination(Route route){
        this.premiereVille = route.getVille1();
        this.deuxiemeVille = route.getVille2();
        this.nombrePoints = route.getNombrePoint();
    }








}

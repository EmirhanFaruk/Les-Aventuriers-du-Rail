package com.model;
import com.model.config.Rail;
import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteWagon;

import java.util.ArrayList;

public class Player {

    private int score;
    private ArrayList<CarteDestination> destinationsList= new ArrayList<>();
    private ArrayList<CarteWagon.Couleur> trainList = new ArrayList<>();

    private CarteWagon carteDestination= new CarteWagon();


    public ArrayList<CarteDestination> getDestinationsList() {
        return destinationsList;
    }

    public ArrayList<CarteWagon.Couleur> getTrainCard() {
        return trainList;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setDestinationsList(ArrayList<CarteDestination> destinationsList) {
        this.destinationsList = destinationsList;
    }

    public void setTrainCard(ArrayList<CarteWagon.Couleur> trainList) {
        this.trainList = trainList;
    }



    private void takeRail(Rail rail){
       //*
        // TODO idea : use the class Rail to ask if we can or not to place train,
        //  verify if we have enough trainCard,
        //  if we can take the tain and verify the color of the train card


    }



}

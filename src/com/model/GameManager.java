package com.model;

import com.model.Wagon;
import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteWagon;

public class GameManager {

    private CarteWagon.Couleur[] boardTrainCard = new CarteWagon.Couleur[5]; //Il y a juste 5 carte sur le board

    private CarteDestination[] boardDestinationCard = new CarteDestination[3];

    private CarteWagon carteWagon = new CarteWagon();
    private CarteDestination carteDestination = new CarteDestination();

    private void initBoard(){
        for(int i = 0; i< boardTrainCard.length; i++){
            boardTrainCard[i] = carteWagon.getPioche();
        }

        for(int y = 0; y < boardDestinationCard.length;y++){
            boardDestinationCard[y] = carteDestination.getDestination();
        }

    }


    private void initCard(){




    }


}

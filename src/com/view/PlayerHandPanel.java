package com.view;

import com.model.Player;
import com.model.config.Plateau;
import com.model.config.carte.CarteWagon;
import com.model.controller.GameController;
import com.view.graphics.CardGraphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PlayerHandPanel extends JPanel {
    private Player player;
    private DrawPlayerHand drawPlayerHand ;
    private JScrollPane scrollPane;
    private int imageWidth , imageHeight ;
    private Plateau plateau ;
    private MapScreen mapScreen ;
    private int width , height ;

    public PlayerHandPanel(Player currentPlayer , GameController gameController, Plateau plateau , int width , int height , MapScreen mapScreen ) {
        setPlayer(currentPlayer);
        setBackground(Color.orange);
        setPreferredSize(new Dimension( width, height ));

        drawPlayerHand = new DrawPlayerHand( player , height  , gameController) ;
        this.plateau = plateau ;
        this.mapScreen = mapScreen ;
        this.width = width ;
        this.height = height ;

        scrollPane = new JScrollPane(this.drawPlayerHand);
        scrollPane.setPreferredSize(new Dimension( width , height ));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.getHorizontalScrollBar().setBackground(Color.ORANGE);

        add(scrollPane, BorderLayout.NORTH);
    }

    public void setPlayer(Player player) {
        this.player = player;
        if (this.drawPlayerHand != null) {
            this.drawPlayerHand.setPlayer(player);
            scrollPane.revalidate();
        }
    }

    public void setMapScreen(MapScreen mapScreen) {
        this.mapScreen = mapScreen;
    }


    public void repaintHand(){
        this.drawPlayerHand.repaint();
        scrollPane.revalidate(); // Forcer la mise en page à se rafraîchir
        scrollPane.repaint();
        this.repaint();
    }

    public DrawPlayerHand getDrawPlayerHand() {
        return this.drawPlayerHand;
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public class DrawPlayerHand extends JPanel {
        Player player ;
        int height ;
        int width ;
        int hFixe ;
        private ArrayList< CarteWagon > listCardWagon ;
        GameController gameController ;

        DrawPlayerHand ( Player player  , int height , GameController gameController) {
            this.player = player;
            this.height = height;
            this.width = 0;
            this.hFixe = 30 ;
            this.gameController = gameController ;
            this.listCardWagon = new ArrayList<>() ;
            setBackground(Color.orange);
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    gameController.couleurCarteAChoisir( e ,  player , PlayerHandPanel.this , mapScreen );
                    repaint();
                }
            });
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            if (this.player != null) {
                this.drawPlayerHand(g2d);
                setPreferredSize(new Dimension(width, height));
                getParent().revalidate(); // Appel à revalidate() sur le parent (JScrollPane)
            }
            repaint();
        }

        private void drawPlayerHand(Graphics2D g) {
            int x = 30, i = 0;

            while (i < this.player.getTrainCard().size()) {
                CarteWagon.Couleur couleur = this.player.getTrainCard().get(i);
                CarteWagon carteWagon = new CarteWagon( couleur , x , hFixe );
                listCardWagon.add( carteWagon ) ;
                BufferedImage image = CardGraphics.getImage(carteWagon);

                if (image != null) {
                    g.drawImage(image, x , hFixe , null);
                    x += image.getWidth() + 10;
                    width = x;
                    imageWidth = image.getWidth() ;
                    imageHeight = image.getHeight() ;
                }
                i++;
            }
        }

        /**
         * Une fonction qui renvoie la couleur de la carte clicker par le joueur
         * @param x width
         * @param y height
         * @return la carte clicker
         */
        public CarteWagon CardClicked (int x , int y ){
            for ( CarteWagon c : listCardWagon ){
                int widthEndCard = c.getWidthInPanel() + imageWidth ;
                int heightEndCard = c.getHeightInPanel() + imageHeight ;
                if ( x > c.getWidthInPanel() && x < widthEndCard && y > c.getHeightInPanel() && y < heightEndCard ){
                    return c ;
                }
            }
           return null ;
        }

        public void setPlayer(Player player) {
            this.player = player;
            repaint();
        }

    }

}

package com.view;

import com.model.Player;
import com.model.config.carte.CarteWagon;
import com.view.graphics.CardGraphics;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PlayerHandPanel extends JPanel {
    private Player player;
    private DrawPlayerHand drawPlayerHand ;
    private JScrollPane scrollPane;

    public PlayerHandPanel( Player currentPlayer , int width , int height ) {
        setPlayer(currentPlayer);
        setBackground(Color.orange);
        setPreferredSize(new Dimension( width, height ));

        drawPlayerHand = new DrawPlayerHand( player , height ) ;

        scrollPane = new JScrollPane(drawPlayerHand);
        scrollPane.setPreferredSize(new Dimension( width , height ));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.getHorizontalScrollBar().setBackground(Color.ORANGE);

        add(scrollPane, BorderLayout.NORTH);
    }

    public void setPlayer(Player player) {
        this.player = player;
        if (drawPlayerHand != null) {
            drawPlayerHand.setPlayer(player);
            scrollPane.revalidate();
        }

    }

    class DrawPlayerHand extends JPanel {
        Player player ;
        int height ;
        int width ;
        DrawPlayerHand ( Player player  , int height ){
            this.player = player ;
            this.height = height ;
            this.width = 0;
            setBackground(Color.orange);
        }
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            if (this.player != null) {
                this.drawPlayerHand(g2d);
                setPreferredSize(new Dimension(width, height));
                scrollPane.revalidate();
            }
        }

        private void drawPlayerHand(Graphics2D g) {
            int x = 30, i = 0;

            while (i < this.player.getTrainCard().size()) {
                CarteWagon.Couleur couleur = this.player.getTrainCard().get(i);
                CarteWagon carteWagon = new CarteWagon(couleur);
                BufferedImage image = CardGraphics.getImage(carteWagon);

                if (image != null) {
                    g.drawImage(image, x, 30, null);
                    x += image.getWidth() + 10;
                    width = x;
                }
                i++;
            }
        }

        public void setPlayer(Player player) {
            this.player = player;
            repaint();
        }

    }

}

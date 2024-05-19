package com.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Classe représentant un panneau d'animation pour une explosion de type "Nuke".
 */
public class NukeAnimationPanel extends JPanel {
    private int frameNumber = 0;
    private Timer timer;

    /**
     * Constructeur de NukeAnimationPanel.
     * Initialise le panneau d'animation et démarre le timer pour l'animation.
     */
    public NukeAnimationPanel() {
        this.setPreferredSize(new Dimension(800, 800));
        timer = new Timer(50, e -> {
            frameNumber++;
            repaint();
            if (frameNumber > 60) { // Arrêter l'animation après 60 frames
                timer.stop();
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                topFrame.dispose();
            }
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawNukeAnimation((Graphics2D) g, frameNumber);
    }

    /**
     * Dessine l'animation de l'explosion de type "Nuke".
     *
     * @param g2d         L'objet Graphics2D utilisé pour dessiner.
     * @param frameNumber Le numéro de la frame actuelle de l'animation.
     */
    private void drawNukeAnimation(Graphics2D g2d, int frameNumber) {
        int width = getWidth();
        int height = getHeight();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fond de ciel bleu et sol brun
        g2d.setColor(new Color(135, 206, 235)); // Ciel bleu
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(new Color(210, 180, 140)); // Sol brun
        g2d.fillRect(0, height / 2, width, height / 2);

        // Dessin de l'effet d'explosion
        float alpha = Math.max(0, 1.0f - (float) frameNumber / 60);
        Color explosionColor = new Color(255, 140, 0, (int) (alpha * 255));
        g2d.setColor(explosionColor);

        int maxRadius = Math.min(width, height) / 2;
        int radius = (int) (frameNumber * (maxRadius / 30.0));

        for (int i = 0; i < 3; i++) {
            int currentRadius = radius - (i * 20);
            if (currentRadius > 0) {
                g2d.setStroke(new BasicStroke(5 - i));
                g2d.draw(new Ellipse2D.Double(width / 2 - currentRadius, height / 2 - currentRadius,
                        2 * currentRadius, 2 * currentRadius));
            }
        }

        // Dessin des rayons lumineux
        g2d.setColor(new Color(255, 215, 0, (int) (alpha * 128)));
        for (int i = 0; i < 360; i += 15) {
            double angle = Math.toRadians(i);
            int x1 = (int) (width / 2 + radius * Math.cos(angle));
            int y1 = (int) (height / 2 + radius * Math.sin(angle));
            int x2 = (int) (width / 2 + (radius + 30) * Math.cos(angle));
            int y2 = (int) (height / 2 + (radius + 30) * Math.sin(angle));
            g2d.drawLine(x1, y1, x2, y2);
        }
    }
}

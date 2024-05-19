package com.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import com.model.Game;
import com.model.Player;
import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteManager;
import com.model.controller.GameController;
import com.view.graphics.CardGraphics;

/**
 * Le panneau représentant la pioche des cartes destination pour le joueur.
 */
public class CarteDestinationPanel extends JPanel {
    private GameController gameController;
    private Player player;
    private boolean[] isCardSelected;
    private int hoveredCardIndex = -1;
    private Rectangle[] piocheVisibleBounds;
    private CarteManager carteDestination;
    private PlayerHandPanel mainDuJoueur;
    private Timer hoverTimer; // Timer pour gérer l'affichage des descriptions
    private Game game;

    /**
     * Constructeur de CarteDestinationPanel.
     *
     * @param width La largeur du panneau.
     * @param height La hauteur du panneau.
     * @param playerHandPanel Le panneau de la main du joueur.
     * @param game Le jeu en cours.
     */
    public CarteDestinationPanel(int width, int height, PlayerHandPanel playerHandPanel, Game game) {
        setBackground(Color.orange);
        setPreferredSize(new Dimension((int) (width * 0.10), height));
        this.game = game;
        this.mainDuJoueur = playerHandPanel;
        this.player = playerHandPanel.getPlayer();
        this.gameController = new GameController();
        this.carteDestination = game.getCarteManager();
        this.isCardSelected = new boolean[3];
        this.piocheVisibleBounds = new Rectangle[3];
        setupMouseAdapter();
        setupMouseMotionListener();
        initHoverTimer();

        // Initialisation des rectangles pour les cartes
        initRectangles(getWidth(), getHeight());  // Assurez-vous que ceci est appelé avant de définir le bouton

        // Création du bouton
        JButton btnPiocherCartes = new JButton("Passer son tour");

        // Après l'ajout du bouton au panneau
        btnPiocherCartes.addActionListener(e -> {
            activerPioche();
            if (game.getGameFrame().getSound().getclick()) game.getGameFrame().getSound().playSound("INGAME", "click.wav");
            // Rendre le focus au panel du game frame après avoir cliqué sur le bouton
            game.getGameFrame().requestFocusInWindow();
        });

        // Taille et position du bouton
        if (piocheVisibleBounds.length > 0) {
            Rectangle firstCardRect = piocheVisibleBounds[0];
            btnPiocherCartes.setBounds(firstCardRect.x, firstCardRect.y - firstCardRect.height - 10, firstCardRect.width, firstCardRect.height);
        }

        // Ajout du bouton au panneau
        this.add(btnPiocherCartes);
    }

    /**
     * Active la pioche de carte pour le joueur et passe au tour suivant si applicable.
     */
    private void activerPioche() {
        if (player.getFirstTurnOver() && carteDestination.alreadyPickedACard()) {
            this.game.getRound().endRound(game);
        }
    }

    /**
     * Installe l'écouteur de souris pour gérer les clics sur les cartes.
     */
    private void setupMouseAdapter() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (int i = 0; i < piocheVisibleBounds.length; i++) {
                    if (piocheVisibleBounds[i].contains(e.getPoint())) {
                        boolean success = gameController.piocherCarteDestination(player, carteDestination, i);
                        if (success) {
                            isCardSelected[i] = true;
                            SwingUtilities.invokeLater(() -> {
                                repaint();  // Assurez-vous que l'interface utilisateur est mise à jour immédiatement
                                mainDuJoueur.getParent().revalidate();
                                mainDuJoueur.getParent().repaint();
                            });
                        }
                        break;  // Quitte la boucle si une correspondance est trouvée
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (hoveredCardIndex != -1) {
                    hoveredCardIndex = -1;
                    repaint();
                }
            }
        });
    }

    /**
     * Initialise le Timer pour gérer l'affichage des descriptions des cartes.
     */
    private void initHoverTimer() {
        hoverTimer = new Timer(2000, e -> showCardDescription());
        hoverTimer.setRepeats(false);
    }

    /**
     * Affiche la description de la carte destination survolée.
     */
    private void showCardDescription() {
        try {
            CarteDestination cD = carteDestination.getDestinationsCards()[this.hoveredCardIndex];
            if (hoveredCardIndex >= 0 && cD != null) { // Vérifie l'index et voit si la carte est nulle
                this.gameController.descriptionCardDestination(cD, game);
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Installe l'écouteur de souris pour gérer les mouvements de la souris.
     */
    private void setupMouseMotionListener() {
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int previousIndex = hoveredCardIndex;
                hoveredCardIndex = -1;
                for (int i = 0; i < piocheVisibleBounds.length; i++) {
                    if (piocheVisibleBounds[i].contains(e.getPoint())) {
                        hoveredCardIndex = i;
                        break;
                    }
                }
                if (hoveredCardIndex != previousIndex) {
                    if (hoveredCardIndex >= 0) {
                        hoverTimer.restart(); // Redémarre le timer chaque fois que la souris entre sur une nouvelle carte
                    } else {
                        hoverTimer.stop(); // Arrête le timer si la souris sort de toutes les cartes
                    }
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        initRectangles(getWidth(), getHeight());

        for (int i = 0; i < piocheVisibleBounds.length; i++) {
            Rectangle rect = piocheVisibleBounds[i];
            BufferedImage carteVisible = CardGraphics.getCardObjectif();

            // Si la carte est déjà prise elle devient grisée (plus prenable)
            if (carteVisible != null) {
                if (isCardSelected[i]) {
                    carteVisible = desaturateImage(carteVisible);
                }
                g2d.drawImage(carteVisible, rect.x, rect.y, rect.width, rect.height, null);
                if (i == hoveredCardIndex) {
                    g2d.setColor(new Color(255, 255, 0, 128)); // Jaune semi-transparent
                    g2d.fill(rect);
                }
            }
        }
    }

    /**
     * Initialise les rectangles représentant les zones des cartes visibles.
     *
     * @param width La largeur du panneau.
     * @param height La hauteur du panneau.
     */
    private void initRectangles(int width, int height) {
        int rectWidth = width;
        int rectHeight = height / 5;
        int startX = (getWidth() - rectWidth) / 2;
        int startY = 20;

        for (int i = 0; i < piocheVisibleBounds.length; i++) {
            startY += rectHeight + 10;
            piocheVisibleBounds[i] = new Rectangle(startX + 2, startY, rectWidth - 4, rectHeight);
        }
    }

    /**
     * Rend une image en niveau de gris après sélection de la carte.
     *
     * @param image L'image originale.
     * @return L'image désaturée.
     */
    public static BufferedImage desaturateImage(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color color = new Color(image.getRGB(x, y));
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                int avg = (r + g + b) / 3; // Calcul de la moyenne des composantes RGB
                int gray = (avg << 16) | (avg << 8) | avg; // Conversion en niveau de gris
                result.setRGB(x, y, (color.getAlpha() << 24) | gray); // Appliquer le niveau de gris
            }
        }

        return result;
    }

    /* getters et setters */

    /**
     * Remet tout à false pour réinitialiser l'état des cartes (évite que les cartes soient grises).
     */
    public void setAllDefault() {
        for (int i = 0; i < this.isCardSelected.length; i++) this.isCardSelected[i] = false;
    }

    /**
     * Définit le joueur associé à ce panneau.
     *
     * @param player Le joueur à associer.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }
}

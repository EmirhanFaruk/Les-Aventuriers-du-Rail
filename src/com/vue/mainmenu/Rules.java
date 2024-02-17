package com.vue.mainmenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Rules extends JPanel
{
    /**
     * Materiel button action.
     */
    public class MaterielButtonAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            cardLayout.show(textPanel, materiel_mode);
        }
    }

    /**
     * Deroulement button action.
     */
    public class DeroulementButtonAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            cardLayout.show(textPanel, deroulement_mode);
        }
    }

    /**
     * Fin Jeu button action.
     */
    public class FinJeuButtonAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            cardLayout.show(textPanel, finjeu_mode);
        }
    }


    private JPanel textPanel;
    private CardLayout cardLayout;

    private final String
            materiel_mode = "MATERIEL",
            deroulement_mode = "DEROULEMENT",
            finjeu_mode = "FIN JEU";

    int width, height;


    public Rules(int width, int height)
    {
        this.width = width;
        this.height = height;
        cardLayout = new CardLayout();
        makeRules();
    }

    @Override
    public void setSize(int width, int height)
    {
        makeRules();
    }


    /**
     * Makes button panel, a panel containing buttons obviously.
     * @return the panel that contains buttons
     */
    private JPanel makeButtonPanel()
    {
        JPanel res = new JPanel();
        res.setBackground(Color.BLACK);

        JButton materiel_button = new JButton("MATERIEL");
        JButton deroulement_button = new JButton("DEROULEMENT");
        JButton finjeu_button = new JButton("FIN JEU");

        materiel_button.addActionListener(new MaterielButtonAction());
        deroulement_button.addActionListener(new DeroulementButtonAction());
        finjeu_button.addActionListener(new FinJeuButtonAction());


        JButton[] bl = {materiel_button, deroulement_button, finjeu_button};
        for (JButton button : bl)
        {
            button.setBorderPainted(false);
            button.setBackground(Color.BLACK);
            button.setForeground(Color.GRAY);
            res.add(button);
        }

        return res;
    }


    /**
     * Makes the JPanel with materiel text in it.
     * @return the said panel
     */
    private JPanel makeMaterielPanel()
    {
        JPanel res = new JPanel();
        res.setLayout(new BorderLayout());
        res.setBackground(Color.BLACK);

        String text = "**Matériel :**\n" +
                "- Plateau de jeu : \n" +
                "Une carte géographique (par exemple, les États-Unis, l'Europe, etc.).\n" +
                "- Cartes Destination : \n" +
                "Indiquent les villes que les joueurs doivent relier pour gagner des points.\n" +
                "- Cartes Wagons : \n" +
                "Représentent différentes couleurs et sont utilisées pour construire des voies ferrées.\n" +
                "- Wagons en plastique de différentes couleurs : \n" +
                "Représentent les voies ferrées construites.\n" +
                "- Marqueurs de score : \n" +
                "pour suivre les points des joueurs.";

        JLabel textLabel = new JLabel(text, SwingConstants.CENTER);
        textLabel.setBackground(Color.BLACK);
        textLabel.setForeground(Color.GRAY);

        res.add(textLabel);

        return res;
    }


    /**
     * Makes the JPanel with deroulement text in it.
     * @return the said panel
     */
    private JPanel makeDeroulementPanel()
    {
        JPanel res = new JPanel();
        res.setBackground(Color.BLACK);

        String text = "**Déroulement du jeu :**\n" +
                "1. Chaque joueur reçoit un nombre de cartes Destination.\n" +
                "2. Des cartes Wagons sont placées face visible sur le plateau.\n" +
                "3. Chaque joueur reçoit des cartes Wagons en main.\n" +
                "4. Les joueurs peuvent :\n" +
                "   - Piocher des cartes Wagons de la réserve.\n" +
                "   - Construire des voies ferrées en jouant des cartes Wagons de la même couleur.\n" +
                "5. Les joueurs peuvent également :\n" +
                "   - Piocher des nouvelles cartes Destination (garder au moins une).\n" +
                "   - Construire les voies ferrées nécessaires pour réaliser leurs cartes Destination.\n" +
                "6. Le tour passe au joueur suivant.";

        JLabel textLabel = new JLabel(text, SwingConstants.CENTER);
        textLabel.setBackground(Color.BLACK);
        textLabel.setForeground(Color.GRAY);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textLabel.setVerticalAlignment(SwingConstants.CENTER);

        res.add(textLabel, BorderLayout.CENTER);

        return res;
    }


    /**
     * Makes the JPanel with fin jeu text in it.
     * @return the said panel
     */
    private JPanel makeFinJeuPanel()
    {
        JPanel res = new JPanel();
        res.setBackground(Color.BLACK);

        String text = "<html><body>" +
                "<h1>Fin du jeu :</h1>\n" +
                "<>La partie prend fin lorsque :\n" +
                "- Un joueur n'a plus que deux ou moins de wagons.\n" +
                "- Un joueur a terminé au moins 6 billets de destination.\n" +
                "- Plus aucun emplacement de voie ferrée ne peut être construit.\n" +
                "Ensuite, les joueurs révèlent leurs cartes Destination et marquent des points en fonction de celles qu'ils ont réalisées et perdent des points pour celles qu'ils n'ont pas réussi à compléter.\n" +
                "\n" +
                "Le joueur avec le plus de points à la fin de la partie remporte le jeu."
                 + "</body></html>";




        res.add(makeTextArea(text));

        return res;
    }


    private JLabel makeTextArea(String text)
    {
        JLabel textArea = new JLabel(text);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.GRAY);


        textArea.setPreferredSize(new Dimension(width / 2, height / 2));


        return textArea;
    }


    /**
     * Makes a JPanel that gathers all of the text panels(materiel, deroulement, fin jeu)
     * @return the said panel
     */
    private JPanel makeTextPanel()
    {
        JPanel res = new JPanel();
        res.setLayout(cardLayout);

        res.add(materiel_mode, makeMaterielPanel());
        res.add(deroulement_mode, makeDeroulementPanel());
        res.add(finjeu_mode, makeFinJeuPanel());

        cardLayout.show(res, materiel_mode);

        return res;
    }

    private void makeRules()
    {
        this.setLayout(new BorderLayout());

        textPanel = makeTextPanel();

        this.add(textPanel, BorderLayout.CENTER);
        this.add(makeButtonPanel(), BorderLayout.SOUTH);

    }
}

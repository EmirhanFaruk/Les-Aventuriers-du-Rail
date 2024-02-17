package com.view.mainmenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

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

    private final String slash = findSlash();

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
     * Finds the slash type of the system
     * @return slash in String type
     */
    private String findSlash()
    {
        String p = System.getProperty("user.dir");
        for (int i = 0; i < p.length(); i++)
        {
            switch (p.charAt(i))
            {
                case '/':
                    return "/";
                case '\\':
                    return "\\";
            }
        }
        return "/";
    }



    /**
     * Reads html file
     * @param filename the html file name
     * @return the content of it
     */
    private String loadHTMLContent(String filename)
    {
        BufferedReader reader;
        String res = "";

        try
        {
            reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();

            while (line != null)
            {
                res += line;
                // read next line
                line = reader.readLine();
            }
            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
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

        String htmlContent = loadHTMLContent("ressources" + slash + "Rules" + slash + "Materiel.html");

        res.add(makeTextPlace(htmlContent));

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

        String htmlContent = loadHTMLContent("ressources" + slash + "Rules" + slash + "Deroulement.html");

        res.add(makeTextPlace(htmlContent));

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

        String htmlContent = loadHTMLContent("ressources" + slash + "Rules" + slash + "Finjeu.html");

        res.add(makeTextPlace(htmlContent));

        return res;
    }


    private JEditorPane makeTextPlace(String text)
    {
        JEditorPane editorPane = new JEditorPane("text/html", text);
        editorPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        editorPane.setAlignmentY(Component.CENTER_ALIGNMENT);
        editorPane.setBorder(null);
        editorPane.setEditable(false);

        editorPane.setPreferredSize(new Dimension((width / 8) * 7, (height / 8) * 7));
        editorPane.setSize(new Dimension((width / 8) * 7, (height / 8) * 7));

        return editorPane;
    }


    /**
     * Makes a JPanel that gathers all the text panels(materiel, deroulement, fin jeu)
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

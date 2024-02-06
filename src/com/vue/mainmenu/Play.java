package com.vue.mainmenu;

import com.gui.GameView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.beancontext.BeanContextServiceAvailableEvent;
import java.io.File;
import java.util.Objects;

public class Play extends JPanel
{
    private JLabel level_name_tag;
    private JLabel difficulty_tag;
    private JLabel mode_tag;
    private JLabel character_tag;

    private GameView frame;

    public Play(GameView frame)
    {
        this.frame = frame;
        makePlay();
    }

    @Override
    public void setSize(int width, int height)
    {
        makePlay();
    }

    private void makePlay()
    {
        setLayout(new BorderLayout());

        add(makeLevelPanel());
        add(makeConfigPanel(), BorderLayout.EAST);

    }

    /*
     * START OF LEVEL LIST FUNCTIONS
     */

    private String findSlash(String p)
    {
        for(int i = 0; i < p.length(); i++)
        {
            switch (p.charAt(i))
            {
                case '/' : return "/";
                case '\\' : return "\\";
            }
        }
        return "/";
    }

    private int getMapCount()
    {
        String path = System.getProperty("user.dir");
        String s = findSlash(path);

        File directory = new File(path + s + "src" + s + "resources" + s + "maps");
        int map_count = 0;
        if(directory.list() != null)
        {
            map_count = directory.list().length;
        }
        return map_count;
    }

    private String[] getMapNames()
    {
        String path = System.getProperty("user.dir");
        String s = findSlash(path);

        File directory = new File(path + s + "src" + s + "resources" + s + "maps");
        if(directory.list() != null)
        {
            String[] res = directory.list();
            for(int i = 0; i < res.length; i++)
            {
                res[i] = res[i].substring(0, res[i].length()-4);
            }
            return res;
        }
        return new String[]{};
    }

    private JButton makeSingleLevelPanel(String level_name)
    {
        JButton res = new JButton(level_name);
        res.setBackground(Color.BLACK);
        res.setForeground(Color.GRAY);
        res.addActionListener(
                new ActionListener()
            {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                level_name_tag.setText(level_name);
            }
        });
        return res;
    }

    private JPanel makeLevelPanel()
    {
        JPanel res = new JPanel();
        int map_count = getMapCount();
        if(map_count > 0)
        {
            res.setLayout(new GridLayout(map_count, 1));
            for (String level_name : getMapNames())
            {
                res.add(makeSingleLevelPanel(level_name));
            }
        }
        return res;
    }


    /*
     * END OF LEVEL LIST FUNCTIONS
     */



    /*
     * START OF DIFFICULTY PANEL FUNCTIONS
     */


    private JPanel makeDifficultyOptions()
    {
        JPanel diffs = new JPanel();
        diffs.setBackground(Color.BLACK);
        diffs.setForeground(Color.GRAY);
        diffs.setLayout(new GridLayout(3, 1));

        String[] diff_list = {"EASY", "NORMAL", "HARD"};
        for(String diff : diff_list)
        {
            JButton button = new JButton(diff);
            button.setBackground(Color.BLACK);
            button.setForeground(Color.GRAY);
            button.addActionListener(
                    new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                            difficulty_tag.setText(diff);
                        }
                    });
            diffs.add(button);
        }

        return diffs;
    }

    private JPanel makeModeOptions()
    {
        JPanel opts = new JPanel();
        opts.setBackground(Color.BLACK);
        opts.setForeground(Color.GRAY);
        opts.setLayout(new GridLayout(1, 2));

        String[] mode_list = {"NORMAL", "MARATHON"};
        for(String mode : mode_list)
        {
            JButton button = new JButton(mode);
            button.setBackground(Color.BLACK);
            button.setForeground(Color.GRAY);
            button.addActionListener(
                    new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                            mode_tag.setText(mode);
                        }
                    });
            opts.add(button);
        }

        return opts;
    }


    private JPanel makeCharacterOptions()
    {
        JPanel opts = new JPanel();
        opts.setBackground(Color.BLACK);
        opts.setForeground(Color.GRAY);
        opts.setLayout(new GridLayout(2, 2));

        String[] character_list = {"VILLAGEOIS", "ARCHER", "SOLDAT", "COMMANDANT"};
        for(String ch : character_list)
        {
            JButton button = new JButton(ch);
            button.setBackground(Color.BLACK);
            button.setForeground(Color.GRAY);
            button.addActionListener(
                    new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                            character_tag.setText(ch);
                        }
                    });
            opts.add(button);
        }

        return opts;
    }


    private JPanel makeDifficultyPanel()
    {
        JPanel res = new JPanel();
        res.setBackground(Color.BLACK);
        res.setForeground(Color.GRAY);

        // diffs, mode, character, texts
        res.setLayout(new GridLayout(4, 1));

        res.add(makeDifficultyOptions());

        res.add(makeCharacterOptions());

        res.add(makeModeOptions());



        // Tried to use a function but it didnt work, so gotta do it like this

        JPanel texts_capsule = new JPanel();
        texts_capsule.setBackground(Color.BLACK);
        texts_capsule.setForeground(Color.GRAY);
        texts_capsule.setLayout(new GridLayout(4, 1));

        JPanel lnt_capsule = new JPanel(); // level name tag capsule
        lnt_capsule.setBackground(Color.BLACK);
        lnt_capsule.setForeground(Color.GRAY);

        level_name_tag = new JLabel("Map1");
        level_name_tag.setHorizontalTextPosition(SwingConstants.CENTER);
        level_name_tag.setVerticalTextPosition(SwingConstants.CENTER);
        level_name_tag.setBackground(Color.BLACK);
        level_name_tag.setForeground(Color.GRAY);

        lnt_capsule.add(level_name_tag);
        texts_capsule.add(lnt_capsule);


        JPanel dt_capsule = new JPanel(); // difficulty tag capsule
        dt_capsule.setBackground(Color.BLACK);
        dt_capsule.setForeground(Color.GRAY);

        difficulty_tag = new JLabel("EASY");
        difficulty_tag.setHorizontalTextPosition(SwingConstants.CENTER);
        difficulty_tag.setVerticalTextPosition(SwingConstants.CENTER);
        difficulty_tag.setBackground(Color.BLACK);
        difficulty_tag.setForeground(Color.GRAY);

        dt_capsule.add(difficulty_tag);
        texts_capsule.add(dt_capsule);


        JPanel mt_capsule = new JPanel(); // mode tag capsule
        mt_capsule.setBackground(Color.BLACK);
        mt_capsule.setForeground(Color.GRAY);

        mode_tag = new JLabel("NORMAL");
        mode_tag.setHorizontalTextPosition(SwingConstants.CENTER);
        mode_tag.setVerticalTextPosition(SwingConstants.CENTER);
        mode_tag.setBackground(Color.BLACK);
        mode_tag.setForeground(Color.GRAY);

        mt_capsule.add(mode_tag);
        texts_capsule.add(mt_capsule);

        JPanel ct_capsule = new JPanel(); // mode tag capsule
        ct_capsule.setBackground(Color.BLACK);
        ct_capsule.setForeground(Color.GRAY);

        character_tag = new JLabel("VILLAGEOIS");
        character_tag.setHorizontalTextPosition(SwingConstants.CENTER);
        character_tag.setVerticalTextPosition(SwingConstants.CENTER);
        character_tag.setBackground(Color.BLACK);
        character_tag.setForeground(Color.GRAY);

        ct_capsule.add(character_tag);
        texts_capsule.add(ct_capsule);


        res.add(texts_capsule);

        return res;
    }



    private JPanel makePlayButton()
    {
        JPanel res = new JPanel();
        res.setBackground(Color.BLACK);
        res.setForeground(Color.GRAY);

        JButton play_button = new JButton("GO!");
        play_button.setBackground(Color.BLACK);
        play_button.setForeground(Color.GRAY);

        play_button.addActionListener(
                new ActionListener()
            {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                frame.startGame(level_name_tag.getText(), difficulty_tag.getText(), mode_tag.getText(), character_tag.getText());
            }
        });

        res.add(play_button);

        return res;
    }

    private JPanel makeConfigPanel()
    {
        JPanel res = new JPanel();
        res.setLayout(new BorderLayout());
        res.add(makeDifficultyPanel(), BorderLayout.CENTER);

        res.add(makePlayButton(), BorderLayout.SOUTH);

        return res;
    }

}

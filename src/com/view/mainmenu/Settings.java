package com.view.mainmenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Settings extends JPanel
{
    private final Menu main;

    // Resolution
    private JComboBox<String> res_box;
    private final int[][] resolutions = {{800, 500}, {800, 600}, {1000, 800}, {1280, 720}, {1920, 1080}};


    // Fullscreen
    private JCheckBox fs_cb;
    private JCheckBox music_cb;
    private JCheckBox click_cb;


    public Settings(Menu main)
    {
        this.main = main;
        makeSettings();
    }

    private void makeSettings()
    {
        this.setLayout(new GridLayout(3, 1));
        // Maybe add only rows and put everything seperate each row?

        // Resolution setting
        add(makeResolutionSetting());
        // Fullscreen setting
        add(makeFullscreenSetting());
        // Sound management
        add(makeSoundSetting());

    }

    private JPanel makeBlackBox()
    {
        JPanel blackbox = new JPanel();
        blackbox.setBackground(Color.BLACK);
        return blackbox;
    }

    private JPanel makeResolutionSetting()
    {
        JPanel resolution_panel = new JPanel();
        resolution_panel.setLayout(new GridLayout(1, 3));
        this.res_box = makeResBox();
        JButton choose_res = makeResChooseButton();

        resolution_panel.add(res_box);
        resolution_panel.add(makeBlackBox());
        resolution_panel.add(choose_res);

        return resolution_panel;
    }

    private JComboBox<String> makeResBox()
    {
        // Making the ComboBox to choose the resolution
        JComboBox<String> res = new JComboBox<>();
        for(int[] couple : resolutions)
        {
            res.addItem(couple[0] + " x " + couple[1]);
        }
        res.setBackground(Color.BLACK);
        res.setForeground(Color.GRAY);
        res.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(main.getFrame().getSound().getclick())
                    main.getFrame().getSound().playSound(4);
            }
        });
        return res;
    }
    

    private JButton makeResChooseButton()
    {
        JButton res = new JButton("Choisir cette resolution");
        res.addActionListener(
                new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(main.getFrame().getSound().getclick())
                    main.getFrame().getSound().playSound(4);
                int index = res_box.getSelectedIndex();
                int[] res = resolutions[index];
                main.setAllSize(res[0], res[1]);
                main.getFrame().setLocationRelativeTo(null);
            }
        });

        res.setBackground(Color.BLACK);
        res.setForeground(Color.GRAY);


        return res;
    }



    private JPanel makeFullscreenSetting()
    {
        JPanel fullscreen_panel = new JPanel();
        fullscreen_panel.setLayout(new GridLayout(1, 3));

        fs_cb = makeFSCheckBox();
        fs_cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(main.getFrame().getSound().getclick())
                    main.getFrame().getSound().playSound(4);
            }
        });
        fullscreen_panel.add(fs_cb);
        fullscreen_panel.add(makeBlackBox());
        fullscreen_panel.add(makeFSButton());

        return fullscreen_panel;
    }

    private JPanel makeSoundSetting()
    {
        JPanel sound_panel = new JPanel();
        sound_panel.setLayout(new GridLayout(1, 3));

        JPanel sound_buttons = makeBlackBox();
        sound_buttons.setLayout(new GridLayout(2, 1));

        music_cb = makeMusicCheckBox();
        music_cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(main.getFrame().getSound().getclick())
                    main.getFrame().getSound().playSound(4);
            }
        });
        click_cb = makeClickSoundCheckBox();
        click_cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(main.getFrame().getSound().getclick())
                    main.getFrame().getSound().playSound(4);
            }
        });

        sound_buttons.add(music_cb);
        sound_buttons.add(click_cb);
        sound_panel.add(sound_buttons);
        sound_panel.add(makeBlackBox());
        sound_panel.add(makeSButton());

        return sound_panel;
    }

    private JCheckBox makeFSCheckBox()
    {
        JCheckBox res = new JCheckBox("Fullscreen");
        res.setBackground(Color.BLACK);
        res.setForeground(Color.GRAY);
        return res;
    }

    private JButton makeFSButton()
    {
        JButton res = new JButton("Choisir cet option de fullscreen");
        res.addActionListener(
                new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        if(main.getFrame().getSound().getclick())
                            main.getFrame().getSound().playSound(4);
                        if(fs_cb.isSelected())
                        {
                            main.getDevice().setFullScreenWindow(main.getFrame());
                            main.setAllSize(main.getFrame().getWidth(), main.getFrame().getHeight());
                            fs_cb.setSelected(true);
                        }
                        else
                        {
                            // Si pas de fullscreen il retourne a setting de resolution.
                            main.getDevice().setFullScreenWindow(null);
                            int[] res = resolutions[res_box.getSelectedIndex()];
                            main.setAllSize(res[0], res[1]);
                        }
                        if(music_cb.isSelected())
                        {
                            main.getFrame().getSound().setMusic(true);
                        }else
                        {
                            main.getFrame().getSound().stop();
                        }

                    }
                });

        res.setBackground(Color.BLACK);
        res.setForeground(Color.GRAY);


        return res;
    }
    private JButton makeSButton()
    {
        JButton res = new JButton("Choisir ses options de son");
        res.addActionListener(
                new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        if(music_cb.isSelected()){
                            main.getFrame().getSound().setMusic(true);
                        }else{
                            main.getFrame().getSound().setMusic(false);
                        }
                        if(click_cb.isSelected()){
                            main.getFrame().getSound().setClick(true);
                        }else{
                            main.getFrame().getSound().setClick(false);
                        }

                        if(main.getFrame().getSound().getclick())
                            main.getFrame().getSound().playSound(4);
                    }
                });

        res.setBackground(Color.BLACK);
        res.setForeground(Color.GRAY);

        return res;
    }

    private JCheckBox makeMusicCheckBox()
    {
        JCheckBox res = new JCheckBox("Music");
        res.setBackground(Color.BLACK);
        res.setForeground(Color.GRAY);
        return res;
    }

    private JCheckBox makeClickSoundCheckBox()
    {
        JCheckBox res = new JCheckBox("Click Sound");
        res.setBackground(Color.BLACK);
        res.setForeground(Color.GRAY);
        return res;
    }

}

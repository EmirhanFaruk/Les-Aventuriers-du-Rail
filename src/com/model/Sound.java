package com.model;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
    static Clip c;
    ArrayList <URL> soundURL = new ArrayList<>();
    static String path = System.getProperty("user.dir");
    static String s = findSlash(path);
    private boolean music = false;
    private boolean click = false;
    static float volume = 0.5f;
    private static final String[] sounds = { "tchu-tchu-song.wav" , "mettreRoute.wav" ,"carte-dest.wav" , "carte-wagon.wav" ,"click.wav" , "end.wav" } ;


    public Sound(){
        initsoundURL();
    }

    private void initsoundURL() {
        for (String sound : sounds) {
            File file = new File(prepath() + sound);
            try {
                soundURL.add(file.toURI().toURL());
            } catch (Exception ignored) {
                //DEBUG : System.out.println("Le son " + sound + " est mal initialiser" );
            }
        }
    }

    private static String findSlash(String p) {
        for (int i = 0; i < p.length(); i++) {
            switch (p.charAt(i)) {
                case '/':
                    return "/";
                case '\\':
                    return "\\";
            }
        }
        return "/";
    }

    private static String prepath() {
        return s+path+s+"ressources" + s + "Sounds" + s;
    }

    public void setFile(int i){
        try {
            AudioInputStream aud = AudioSystem.getAudioInputStream(soundURL.get(i));
            c = AudioSystem.getClip();
            c.open(aud);
        } catch (Exception ignored ) { }
    }

    public void play(){
        c.start();
    }

    public void stop(){
        c.stop();
    }

    public void playMusic(){
        setFile(0);
        setVolume(volume);
        c.start();
        c.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void playSound(int i){
        setFile(i);
        setVolume(volume);
        c.start();
    }

    public void setMusic(boolean music) {
        this.music = music;
    }

    public boolean getMusic() {
        return music;
    }

    public void setClick(boolean click) {
        this.click = click;
    }

    public boolean getclick() {
        return click;
    }

    public void setVolume(float volume) {
        if (volume < 0f || volume > 1f)
            return;
        Sound.volume = volume;
        if(c == null)
            return;
        FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);        
        gainControl.setValue(20f * (float) Math.log10(volume));
    }

    public int getVolume() {
    return (int)volume;
    }
}

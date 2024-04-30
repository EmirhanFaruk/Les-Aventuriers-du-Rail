package com.model;

import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
    static Clip c;
    URL soundURL[] = new URL[6];
    static String path = System.getProperty("user.dir");
    static String s = findSlash(path);


    public Sound(){
        initsoundURL();
    }

    private void initsoundURL() {
        java.io.File af = new java.io.File(prepath()+"tchu-tchu-song.wav");
        try {
            soundURL[0]=af.toURI().toURL();
        } catch (Exception e) {
            e.printStackTrace();
        }

        af = new java.io.File(prepath()+"mettreRoute.wav");
        try {
            soundURL[1]=af.toURI().toURL();
        } catch (Exception e) {
            e.printStackTrace();
        }

        af = new java.io.File(prepath()+"carte-dest.wav");
        try {
            soundURL[2]=af.toURI().toURL();
        } catch (Exception e) {
            e.printStackTrace();
        }

        af = new java.io.File(prepath()+"carte-wagon.wav");
        try {
            soundURL[3]=af.toURI().toURL();
        } catch (Exception e) {
            e.printStackTrace();
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
            AudioInputStream aud = AudioSystem.getAudioInputStream(soundURL[i]);
            c = AudioSystem.getClip();
            c.open(aud);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play(){
        c.start();
    }

    public void stop(){
        c.stop();
    }

    public void playMusic(){
        setFile(0);
        c.start();
        c.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void playSound(int i){
        setFile(i);
        c.start();
    }



}

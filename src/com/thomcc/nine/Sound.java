package com.thomcc.nine;
import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {
  public static Sound getHealth = new Sound("/healthpack.wav");
  public static Sound hurt = new Sound("/hurt.wav");
  public Sound(String filename) {
    try {
      _ac = Applet.newAudioClip(Sound.class.getResource(filename));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  private AudioClip _ac;
  public void play() {
    try {
      new Thread() {
        public void run() {
          _ac.play();
        }
      }.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

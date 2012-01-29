package com.thomcc.nine;
import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {
  public final static Sound getItem = new Sound("/healthpack.wav");
  public final static Sound hurt = new Sound("/hurt.wav");
  public final static Sound shoot = new Sound("/shoot.wav");
  public final static Sound enemyDeath = new Sound("/enemydie.wav");
  public final static Sound notice = new Sound("/notice.wav");
  public final static Sound playerDeath = new Sound("/playerdie.wav");
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

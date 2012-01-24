package com.thomcc.nine;
import java.awt.*;
import java.awt.image.*;


import javax.swing.*;

import com.thomcc.nine.render.Renderer;

public class Nine extends Canvas implements Runnable {
  private static final long serialVersionUID = 988669676528664608L;
  public static final String GAME_NAME = "9";
  public static final int WIDTH = 360;
  public static final int HEIGHT = 240;
  public static final int SCALE = 2;
  public static final int TICKS_PER_SECOND = 60;
  private boolean _running;
  private Renderer _renderer;
  private Input _input;
  private Game _game;
  public Nine() {
    _running = false;
    _renderer = new Renderer(WIDTH, HEIGHT);
    _input = new Input(this);
    _game = new Game(_input);
  }
  public void start() {
    createBufferStrategy(3);
    requestFocus();
    _running = true;
    new Thread(this).start();
  }
  public void stop() { 
    _running = false;
  }
  public void run() {
    long lastPrint = System.currentTimeMillis();
    long lastLoop = System.nanoTime();
    double skipTicks = 1e9 / TICKS_PER_SECOND; 
    int ticks = 0;
    int frames = 0;
    double needed = 0;

    while (_running) {
      long now = System.nanoTime();
      needed += (now - lastLoop) / skipTicks;
      lastLoop = now;
      boolean render = true;
      while (needed > 0) {
        ++ticks;
        tick();
        --needed;
        render = true;
      }
      if (render) {
        ++frames;
        render();
      }
      try {
        Thread.sleep(2);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (System.currentTimeMillis() - lastPrint > 1000) {
        lastPrint += 1000;
        System.out.format("%d ticks, %d fps\n", ticks, frames);
        ticks = 0;
        frames = 0;
      }
    }
  }
  private void render() {
    BufferStrategy bs = getBufferStrategy();
    Graphics g = bs.getDrawGraphics();
    g.clearRect(0, 0, getWidth(), getHeight());
    
    _renderer.render(_game);
    
    g.drawImage(_renderer.image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
    g.dispose();
    bs.show();
  }
  private void tick() {
    _game.tick();
  }
  public static void main(String[] args) {
    Nine game = new Nine();
    game.setMinimumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
    game.setMaximumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
    game.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
    game.setIgnoreRepaint(true);
    JFrame frame = new JFrame(GAME_NAME);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(game);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setVisible(true);
    frame.setIgnoreRepaint(true);
    game.start();
  }
}

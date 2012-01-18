package com.thomcc.nine;
import java.awt.*;
import java.awt.image.*;
import java.util.Random;

import javax.swing.*;

public class Nine extends Canvas implements Runnable {
  public static final String GAME_NAME = "9";
  public static final int WIDTH = 360;
  public static final int HEIGHT = 240;
  public static final int SCALE = 2;
  public static final int TICKS_PER_SECOND = 60;
  private BufferedImage _img;
  private boolean _running;
  public Nine() {
    _running = false;
    _img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
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
  @Override
  public void run() {
    long lastPrint = System.currentTimeMillis();
    long lastLoop = System.nanoTime();
    int maxFrameSkip = 10;
    double skipTicks = 1e9 / TICKS_PER_SECOND; 
    int ticks = 0;
    int frames = 0;
    double needed = 0;
    init();
    while (_running) {
      long now = System.nanoTime();
      needed += (now - lastLoop) / skipTicks;
      lastLoop = now;
      while (needed > 0) {
        ++ticks;
        tick();
        --needed;
      }
      ++frames;
      render();
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
    
    double s = renderTestState/10.0; 
    double ix = Math.cos(s);
    double iy = Math.sin(s);
    int x = (int)((ix + 1.0) / 2.0 * (WIDTH - 64));
    int y = (int)((iy + 1.0) / 2.0 * (HEIGHT - 64));
    Graphics ig = _img.createGraphics();
    ig.clearRect(0, 0, WIDTH,HEIGHT);
    ig.drawImage(img, x, y, null);
    ig.dispose();
    
    g.drawImage(_img, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
    g.dispose();
    bs.show();
  }
  private void init() {
    img = new BufferedImage(64,64, BufferedImage.TYPE_INT_RGB);
    Random random = new Random();
    int[] pix = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
    for (int i = 0; i < 64 * 64; ++i) {
      int r = random.nextInt() & 255;
      int g = random.nextInt() & 255;
      int b = random.nextInt() & 255;      
      pix[i] = r << 16 | g << 8 | b;
    }
    
  }
  private BufferedImage img;
  private long renderTestState = 0;
  private void tick() {
    ++renderTestState;
  }
  public static void main(String[] args) {
    Nine game = new Nine();
    game.setMinimumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
    game.setMaximumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
    game.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
    JFrame frame = new JFrame(GAME_NAME);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(game);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setVisible(true);
    game.start();
  }
}

package com.thomcc.nine;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Renderer {
  
  private static final int SKIN    = 0xffFF9993;
  private static final int CLOTHES = 0xff888888;
  private static final int BLANK   = 0x00ffffff;
  
  private static final int DIRS = 16;
  private static final int SIZE = 12;
  private static final int WIDTH = DIRS * SIZE;
  private static final int HEIGHT = SIZE;
  
  private BufferedImage[] _sprites;
  public BufferedImage image;
  private int _width, _height;
  
  public Renderer(int w, int h) {
    _width = w;
    _height = h;
    image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    _sprites = generateDude();
  }
  
  public void render(Game game) {
    Player p = game.getPlayer();
    int d = p.getDirection();
    Graphics g = image.getGraphics();
    g.clearRect(0, 0, _width, _height);
    g.drawImage(_sprites[d], p.x-6, p.y-6, null);
    g.dispose();
  }
  
  private boolean insideHead(int x, int y) {
    return x > 4 && x < 8 && y > 4 && y < 8;
  }
  private boolean insideBody(int x, int y) {
    return x > 1 && x < 11 && y > 5 && y < 8;
  }
  private int getColor(int x, int y) {
    if (insideHead(x, y)) {
      return SKIN;
    } else if (insideBody(x, y)) {
      return CLOTHES;
    } else {
      return BLANK; 
    }
  }
  public BufferedImage[] generateDude() {
    BufferedImage[] imgs = new BufferedImage[DIRS];
    for (int d = 0; d < DIRS; d++) {
      imgs[d] = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
      int[] pixels = ((DataBufferInt) imgs[d].getRaster().getDataBuffer()).getData();
      double dir = d * Math.PI * 2.0 / DIRS;

      double dCos = Math.cos(dir);
      double dSin = Math.sin(dir);

      for (int j = 0; j < SIZE; j++) {
        for (int i = 0; i < SIZE; i++) {
          int xPix = (int) (dCos * (i - SIZE/2) + dSin * (j - SIZE/2) + SIZE/2 + 0.5);
          int yPix = (int) (dCos * (j - SIZE/2) - dSin * (i - SIZE/2) + SIZE/2 + 0.5);
          pixels[i + j * SIZE] = getColor(xPix, yPix);
        }
      }
    }
    return imgs;
  }
  private BufferedImage getCombined(BufferedImage[] imgs) {
    BufferedImage img = new BufferedImage(SIZE*DIRS, SIZE, BufferedImage.TYPE_INT_RGB);
    Graphics g = img.getGraphics();
    for (int d = 0; d < DIRS; ++d) {
      g.drawImage(imgs[d], d*SIZE, 0, null);
    }
    g.dispose();
    return img;
  }
  public static void main(String[] args) {
    int scale = 4;
    Renderer r = new Renderer(WIDTH, HEIGHT);
    BufferedImage[] imgs = r.generateDude();
    BufferedImage img = r.getCombined(imgs);
    JOptionPane.showMessageDialog(
        null,
        null,
        "generateDude",
        JOptionPane.INFORMATION_MESSAGE,
        new ImageIcon(img.getScaledInstance(WIDTH * scale, HEIGHT * scale,
            Image.SCALE_AREA_AVERAGING)));

  }
}

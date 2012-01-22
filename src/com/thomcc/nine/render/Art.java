package com.thomcc.nine.render;


import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.thomcc.nine.Player;

public class Art {
  public static final int WINGS   = 0xff4e4240;//0xff000bd4;
  public static final int COCKPIT = 0xffff6249;//0xff0023ff;
  private static final int BLANK   = 0x00ffffff;
  private static final int DIRS    = 16;
  
  private static boolean insideCockpit(int x, int y) {
    return x > 4 && x < 8 && y > 4 && y < 7;
  }
  private static boolean insideWings(int x, int y) {
    return x > 1 && x < 11 && y > 5 && y < 8;
  }
  private static int getColor(int x, int y) {
    if (insideCockpit(x, y)) {
      return COCKPIT;
    } else if (insideWings(x, y)) {
      return WINGS;
    } else { 
      return BLANK; 
    }
  }
  
  public static BufferedImage[] generateDude() {
    BufferedImage[] imgs = new BufferedImage[DIRS];
    for (int d = 0; d < DIRS; d++) {
      imgs[d] = new BufferedImage(Player.SIZE, Player.SIZE, BufferedImage.TYPE_INT_ARGB);
      int[] pixels = ((DataBufferInt) imgs[d].getRaster().getDataBuffer()).getData();
      double dir = d * Math.PI * 2.0 / DIRS;
      double cd = Math.cos(dir);
      double sd = Math.sin(dir);
      for (int j = 0; j < Player.SIZE; j++) {
        for (int i = 0; i < Player.SIZE; i++) {
          int xPix = (int) (cd * (i - Player.SIZE/2) + sd * (j - Player.SIZE/2) + Player.SIZE/2 + 0.5);
          int yPix = (int) (cd * (j - Player.SIZE/2) - sd * (i - Player.SIZE/2) + Player.SIZE/2 + 0.5);
          pixels[i + j * Player.SIZE] = getColor(xPix, yPix);
        }
      }
    }
    return imgs;
  }
}

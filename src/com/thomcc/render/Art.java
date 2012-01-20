package com.thomcc.render;


import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Art {
  private static final int SKIN    = 0xffFF9993;
  private static final int CLOTHES = 0xff888888;
  private static final int BLANK   = 0x00ffffff;
  private static final int DIRS    = 16;
  public static final int SIZE     = 12;
  
  private static boolean insideHead(int x, int y) {
    return x > 4 && x < 8 && y > 4 && y < 8;
  }
  private static boolean insideBody(int x, int y) {
    return x > 1 && x < 11 && y > 5 && y < 8;
  }
  private static int getColor(int x, int y) {
    if (insideHead(x, y)) {
      return SKIN;
    } else if (insideBody(x, y)) {
      return CLOTHES;
    } else {
      return BLANK; 
    }
  }
  
  public static BufferedImage[] generateDude() {
    BufferedImage[] imgs = new BufferedImage[DIRS];
    for (int d = 0; d < DIRS; d++) {
      imgs[d] = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
      int[] pixels = ((DataBufferInt) imgs[d].getRaster().getDataBuffer()).getData();
      double dir = d * Math.PI * 2.0 / DIRS;
      double cd = Math.cos(dir);
      double sd = Math.sin(dir);
      for (int j = 0; j < SIZE; j++) {
        for (int i = 0; i < SIZE; i++) {
          int xPix = (int) (cd * (i - SIZE/2) + sd * (j - SIZE/2) + SIZE/2 + 0.5);
          int yPix = (int) (cd * (j - SIZE/2) - sd * (i - SIZE/2) + SIZE/2 + 0.5);
          pixels[i + j * SIZE] = getColor(xPix, yPix);
        }
      }
    }
    return imgs;
  }
}

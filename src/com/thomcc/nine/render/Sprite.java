package com.thomcc.nine.render;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Sprite {
  public final BufferedImage[] images;
  public final int dirs;
  public final int[][] template;
  public final int[] colors;
  public final int size;
  public Sprite(int[][] img, int dirs, int[] colors) {
    this.dirs = dirs;
    this.colors = colors;
    template = img;
    size = img.length;
    
    for (int i = 0; i < size; ++i)
      if (img[i].length != size)
        throw new IllegalArgumentException("template image must be square!");
    
    images = new BufferedImage[dirs];
    initializeImages();
    
  }
  private int getColor(int x, int y) {
    if (x < 0 || y < 0 || x >= size || y >= size) {
      return 0;
    }
    int c = template[y][x];
    if (c == 0) {
      return 0;
    } else {
      return colors[c-1];
    }
  }
  private void initializeImages() {
    for (int d = 0; d < dirs; ++d) {
      images[d] = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
      int[] pix = ((DataBufferInt) images[d].getRaster().getDataBuffer()).getData();
      double dir = d * Math.PI * 2.0 / dirs;
      double cd = Math.cos(dir);
      double sd = Math.sin(dir);
      for (int j = 0; j < size; ++j) {
        for (int i = 0; i < size; ++i) {
          int xPix = (int) (cd * (i - size/2) + sd * (j-size/2) + size/2 + 0.5);
          int yPix = (int) (cd * (j - size/2) - sd * (i-size/2) + size/2 + 0.5);
          pix[i+j*size] = getColor(xPix, yPix);
        }
      }
    }
  }
  public Sprite recolor(int[] colors) {
    return new Sprite(template, dirs, colors);
  }
  public BufferedImage get() { return get(0); }
  public BufferedImage get(int d) { return images[d]; }
}

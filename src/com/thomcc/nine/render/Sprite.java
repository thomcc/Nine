package com.thomcc.nine.render;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Sprite {
  public final BufferedImage[] images;
  public final int dirs;
  public final int[][] template;
  public final int[] colors;
  public final int width, height;
  public Sprite(int[][] img, int dirs, int[] colors) {
    this.dirs = dirs;
    this.colors = colors;
    template = img;
    height = img.length;
    width = img[0].length;
    images = new BufferedImage[dirs];
    initializeImages();
  }
  private int getColor(int x, int y) {
    if (x < 0 || y < 0 || x >= width || y >= height) {
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
      images[d] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      int[] pix = ((DataBufferInt) images[d].getRaster().getDataBuffer()).getData();
      double dir = d * Math.PI * 2.0 / dirs;
      double cd = Math.cos(dir);
      double sd = Math.sin(dir);
      for (int j = 0; j < height; ++j) {
        for (int i = 0; i < width; ++i) {
          int xPix = (int) (cd * (i - width/2) + sd * (j-height/2) + width/2 + 0.5);
          int yPix = (int) (cd * (j - height/2) - sd * (i-width/2) + height/2 + 0.5);
          pix[i+j*width] = getColor(xPix, yPix);
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

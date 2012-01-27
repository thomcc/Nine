package com.thomcc.nine.render;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Sprite {
  public final BufferedImage[][] images;
  public final int dirs;
  public final int[][][] templates;
  public final int[] colors;
  public final int height;
  public final int width;
  public final int num;
  public Sprite(int[][][] imgs, int dirs, int[] colors) {
    this.dirs = dirs;
    this.colors = colors;
    
    templates = imgs;
    
    height = imgs[0].length;
    width = imgs[0][0].length;
    num = imgs.length;
    images = new BufferedImage[num][dirs];
    initializeImages();
    
  }
  private int getColor(int t, int x, int y) {
    if (x < 0 || y < 0 || x >= width || y >= height) {
      return 0;
    }
    int c = templates[t][y][x];
    return colors[c];
    
  }
  private void initializeImages() {
    for (int t = 0; t < num; ++t) {
      for (int d = 0; d < dirs; ++d) {
        images[t][d] = new BufferedImage(height, height, BufferedImage.TYPE_INT_ARGB);
        int[] pix = ((DataBufferInt) images[t][d].getRaster().getDataBuffer()).getData();
        double dir = d * Math.PI * 2.0 / dirs;
        double cd = Math.cos(dir);
        double sd = Math.sin(dir);
        for (int j = 0; j < height; ++j) {
          for (int i = 0; i < width; ++i) {
            int xPix = (int) (cd * (i - width/2) + sd * (j-height/2) + width/2 + 0.5);
            int yPix = (int) (cd * (j - height/2) - sd * (i-width/2) + height/2 + 0.5);
            pix[i+j*height] = getColor(t, xPix, yPix);
            
          }
        }
      }
    }
  }
  public BufferedImage get() { return get(0, 0); }
  public BufferedImage get(int d) { return (get(0, d)); }
  public BufferedImage get(int t, int d) { return images[t][d]; }
}

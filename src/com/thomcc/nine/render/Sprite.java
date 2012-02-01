package com.thomcc.nine.render;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Sprite {
  public final BufferedImage[][] images;
  public final int dirs;
  public final int[][][] templates;
  public final int[] colors;
  public final int templ_height;
  public final int templ_width;
  public final int img_width;
  public final int img_height;
  public final int num;
  public Sprite(int[][][] imgs, int dirs, int[] colors) {
    this.dirs = dirs;
    this.colors = colors;
    
    templates = imgs;
    templ_height = imgs[0].length;
    templ_width = imgs[0][0].length;
    img_width = (int)(Math.sqrt(2)*templ_width);
    img_height = (int)(Math.sqrt(2)*templ_height);
    num = imgs.length;
    images = new BufferedImage[num][dirs];
    initializeImages();
    
  }
  private int getColor(int t, int x, int y) {
    if (x < 0 || y < 0 || x >= templ_width || y >= templ_height) {
      return 0;
    }
    int c = templates[t][y][x];
    return colors[c];
    
  }
  private void initializeImages() {
    for (int t = 0; t < num; ++t) {
      for (int d = 0; d < dirs; ++d) {
        images[t][d] = new BufferedImage(img_height, img_height, BufferedImage.TYPE_INT_ARGB);
        int[] pix = ((DataBufferInt) images[t][d].getRaster().getDataBuffer()).getData();
        double dir = d * Math.PI * 2.0 / dirs;
        double cd = Math.cos(dir);
        double sd = Math.sin(dir);
        for (int j = 0; j < img_height; ++j) {
          for (int i = 0; i < img_width; ++i) {
            int xPix = (int)Math.floor(cd * (i - img_width/2) + sd * (j-img_height/2) + templ_width/2 + 0.5);
            int yPix = (int)Math.floor(cd * (j - img_height/2) - sd * (i-img_width/2) + templ_height/2 + 0.5);
            pix[i+j*img_width] = getColor(t, xPix, yPix);
            
          }
        }
      }
    }
  }
  public BufferedImage get() { return get(0, 0); }
  public BufferedImage get(int d) { return (get(0, d)); }
  public BufferedImage get(int t, int d) { return images[t][d]; }
}

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Sprites {
  
  private static final int SKIN = 0xFF9993;
  private static final int CLOTHES = 0x888888;
  private static final int HAIR = 0x8B4513;
  private static final int BLANK = 0xffffff;
  //private static final int SIGNAL = 0xff00ff;
  
  private static final int DIRS = 16;
  private static final int SIZE = 12;
  private static final int WIDTH = DIRS * SIZE;
  private static final int HEIGHT = SIZE;
  
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
  private static BufferedImage[] generateDude() {
    BufferedImage[] imgs = new BufferedImage[DIRS];
    //BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    //int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
    for (int d = 0; d < DIRS; d++) {
      imgs[d] = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);
      int[] pixels = ((DataBufferInt) imgs[d].getRaster().getDataBuffer()).getData();
      double dir = d * Math.PI * 2.0 / DIRS;

      double dCos = Math.cos(dir);
      double dSin = Math.sin(dir);
      
      ///int xoff = SIZE * d;
      for (int j = 0; j < SIZE; j++) {
        for (int i = 0; i < SIZE; i++) {
          int xPix = (int) (dCos * (i - 6) + dSin * (j - 6) + 6.5);
          int yPix = (int) (dCos * (j - 6) - dSin * (i - 6) + 6.5);
          pixels[i + j * SIZE] = getColor(xPix, yPix);
        }
      }
    }
    return imgs;
  }
  private static BufferedImage getCombined(BufferedImage[] imgs) {
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
    BufferedImage[] imgs = generateDude();
    BufferedImage img = getCombined(imgs);
    JOptionPane.showMessageDialog(
        null,
        null,
        "generateDude",
        JOptionPane.INFORMATION_MESSAGE,
        new ImageIcon(img.getScaledInstance(WIDTH * scale, HEIGHT * scale,
            Image.SCALE_AREA_AVERAGING)));

  }
}

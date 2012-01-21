package com.thomcc.nine.level;

import java.util.Random;

import com.thomcc.nine.Player;
import com.thomcc.nine.level.gen.VoronoiLevelGen;

public class ShipLevel implements Level{
  public int[][] map;
  public final int width, height;
  public ShipLevel() { this(700, 700, 100); }
  public ShipLevel(int width, int height, int points) {
    this.width = width; this.height = height;
    long now = System.nanoTime();
    map = new VoronoiLevelGen(points).generate(width, height);
    long later = System.nanoTime();
    long t = later-now;
    long millis = t/1000000;
    System.out.format("Voronoi calculated in %.1f seconds. (%s nanoseconds, %s milliseconds)\n", (double)t/1e9, t, millis);
    System.out.format("\tWidth: %s, Height: %s, Points: %s\n", width, height, points);
  }
  public boolean inBounds(int x, int y) {
    return x >= 0 && y >= 0 && x < width && y < height;
  }
  public boolean blocks(int x, int y) {
    if (!inBounds(x, y)) return true;
    return map[y][x] != 0;
  }
  
  /*
  public boolean isOpen(int x, int y, int rx, int ry, double d) {
    double cd = Math.cos(d);
    double sd = Math.sin(d);
    int size = 12;
    for (int i = 0; i < size; ++i) {
      for (int j = 0; j < size; ++j) {
        int xPix = (int) (cd*(i-size/2) + sd*(j-size/2) + x+0.5);
        int yPix = (int) (cd*(j-size/2) - sd*(i-size/2) + y+0.5);
        
      }
    }
    return false;
  }
  */
  
  public void findPlayerLocation(Player p) {
    Random r = new Random();
    int x = -1;
    int y = -1;
    while (true) {
      x = r.nextInt(width);
      y = r.nextInt(height);
      if (map[y  ][x] == 0 && map[y][x-2] == 0 && map[y][x+2] == 0 &&
          map[y-2][x] == 0 && map[y+2][x] == 0) break; 
    }
    p.setPosition(x, y);
  }
  
  public void addPlayer(Player p) {
    p.setLevel(this);
    findPlayerLocation(p);
  }
  public int getWidth() { return width; }
  public int getHeight() { return height; }
}

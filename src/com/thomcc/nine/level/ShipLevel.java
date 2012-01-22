package com.thomcc.nine.level;

import java.util.Random;

import com.thomcc.nine.Player;
import com.thomcc.nine.level.gen.VoronoiLevelGen;
import com.thomcc.nine.render.Renderer;

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
  public boolean blocks(int x, int y) {
    while (x < 0) x += width;
    while (y < 0) y += height;
    
    return map[y % width][x % height] != 0;
  }
  public int get(int x, int y) {
    while (x < 0) x += width;
    while (y < 0) y += height;
    return map[y][x];
  }
  public void findPlayerLocation(Player p) {
    Random r = new Random();
    int x = -1; int y = -1;
    while (true) {
      x = r.nextInt(width);
      y = r.nextInt(height);
      if (get(x, y) == 0 && 
          get(x-p.rx, y-p.ry) == 0 && get(x-p.rx, y+p.ry) == 0 && 
          get(x+p.rx, y-p.ry) == 0 && get(x+p.rx, y+p.ry) == 0) break;
    }
    p.setPosition(x, y);
  }
  
  public void addPlayer(Player p) {
    p.setLevel(this);
    findPlayerLocation(p);
  }
  public int getWidth() { return width; }
  public int getHeight() { return height; }
  public void render(Renderer r) {
    r.renderShipLevel(this);
  }
}

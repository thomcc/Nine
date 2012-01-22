package com.thomcc.nine.level;

import java.util.Random;

import com.thomcc.nine.Player;
import com.thomcc.nine.level.gen.VoronoiLevelGen;
import com.thomcc.nine.render.Renderer;

public class ShipLevel implements Level{
  public int[][] map;
  public final int width, height;
  private Player _player;
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
    _player = p;
    findPlayerLocation(p);
  }
  public int[][] getMinimap(int w, int h) {
    int xs = width/w;
    int ys = height/h;
    //int xl = width - xs*w;
    //int yl = width - ys*h;
    int[][] mini = new int[h][w];
    for (int y = 0; y < h; ++y) {
      for (int x = 0; x < w; ++x) {
        int xx = x * xs;
        int yy = y * ys;
        mini[y][x] = map[yy][xx]; 
      }
    }
    
    int px = _player.getX();
    int py = _player.getY();
    
    while (px < width) px += width;
    while (py < height) py += height;
    
    px %= width;
    py %= height;
    
    px /= xs;
    py /= ys;
    
    
    int[][] pspot = {
        { 2, 2, 2 },
        { 0, 3, 0 },
        { 0, 0, 0 }
    };
    double pdir = _player.dir;
    double cd = Math.cos(pdir);
    double sd = Math.sin(pdir);
    for (int j = 0; j < 3; ++j) {
      for (int i = 0; i < 3; ++i) {
        int yPix = (int) (cd * (i - 1) + sd * (j - 1) + 1.5);
        int xPix = (int) (cd * (j - 1) - sd * (i - 1) + 1.5);
        int posy = py+j;
        int posx = px+i;
        if (mini[posy%70][posx%70] == 0) mini[posy%70][posx%70] = pspot[yPix][xPix];
      }
    }
//    mini[py][px] = 2;
    
    return mini;
  }
  public int getWidth() { return width; }
  public int getHeight() { return height; }
  public void render(Renderer r) {
    r.renderShipLevel(this);
  }
}

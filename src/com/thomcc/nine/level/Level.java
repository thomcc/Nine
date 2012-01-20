package com.thomcc.nine.level;

import java.util.Random;

import com.thomcc.nine.Player;

public class Level {
  public int[][] map;
  public final int width;
  public final int height;
  public final int scale = 16;
  public Level() { this(50, 50); }
  public Level(int w, int h) {
    map = new Maze(w, h).getGrid();
    width = map.length;
    height = map[0].length;
  }
  public boolean inBounds(int x, int y) {
    return x >= 0 && y >= 0 && x < width && y < height;
  }
  public boolean blocks(int tx, int ty) {
    if (!inBounds(tx, ty)) return true;
    return map[ty][tx] != 0;
  }
  public void findPlayerLocation(Player p) {
    Random r = new Random();
    int x = -1;
    int y = -1;
    while (true) {
      x = r.nextInt(width);
      y = r.nextInt(height);
      if (map[y][x] == 0) break; 
    }
    p.setPosition(x*16+8, y*16+8);
  }
  public void addPlayer(Player p) {
    p.setLevel(this);
    findPlayerLocation(p);
  }
}

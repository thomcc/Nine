package com.thomcc.nine.level;

import java.util.Random;

public class Maze {
  public static final int SOUTH = 1;
  public static final int EAST = 2;
  public static final int WEST = 4;
  public static final int NORTH = 8;
  private static final int MAX_ROOM_AREA = 100;
  public static int opposite(int dir) {
    switch(dir) {
    case SOUTH: return NORTH;
    case EAST: return WEST;
    case WEST: return EAST;
    case NORTH: return SOUTH;
    default: throw new IllegalArgumentException(); // never will happen?
    }
  }
  public static final int HORIZONTAL = 1;
  public static final int VERTICAL = 2;
  
  private Random _rand;
  //private Stack<Region> _stack;
  private int _width, _height;
  private int[][] _grid;
  public Maze(int w, int h) {
    _rand = new Random();
    _width = w;
    _height = h;
    _grid = new int[h][w];
    divide(0, 0, w, h, 0);
  }
  public int[][] getGrid() {
    return mappify(_grid);
  }
  private int findOrientation(int width, int height) {
    if (width < height) {
      return HORIZONTAL;
    } else if (height < width) {
      return VERTICAL;
    } else if (_rand.nextBoolean()) {
      return HORIZONTAL;
    } else {
      return VERTICAL;
    }
  }
  private boolean needToStop(int width, int height, int count) {
    if (width*height > MAX_ROOM_AREA) return false;
    else if (width < 2 || height < 2) return true;
    else {
      double g = Math.abs(_rand.nextGaussian());
      double cmaxguess = (Math.log(Math.max(width, height))/Math.log(2))+count;
      double howMuchLeft = (cmaxguess-count) / cmaxguess;
      return howMuchLeft > g;
    }
    
  }
  private void divide(int x, int y, int width, int height, int count) {
    if(needToStop(width, height, count)) return;
    
    boolean horizontal = findOrientation(width, height) == HORIZONTAL;
    // wall start
    int wx = x + (horizontal || (width <= 2) ?  0 : _rand.nextInt(width-2));
    int wy = y + (horizontal && (height > 2) ? _rand.nextInt(height-2) : 0);
    
    int px = wx + (horizontal ? _rand.nextInt(width) : 0);
    int py = wy + (horizontal ? 0 : _rand.nextInt(height));
    // direction
    int dx = horizontal ? 1 : 0;
    int dy = horizontal ? 0 : 1; 
    int length = horizontal ? width : height;
    // perpendicular
    int dir = horizontal ?  SOUTH : EAST;
    
    for (int i = 0; i < length; ++i) {
      if (wx >= _width || wy >= _height)
        System.out.format("wx: %d wy: %d\n", wx, wy);
      else if (wx != px || wy != py) _grid[wy][wx] |= dir;
      wx += dx;
      wy += dy;
    }
    int nx = x;
    int ny = y;
    int w = horizontal ? width : wx-x+1;
    int h = horizontal ? wy-y+1 : height;

    divide(nx, ny, w, h, count+1);
    
    nx = horizontal ? x : wx+1;
    ny = horizontal ? wy+1 : y;
    w = horizontal ? width : x+width-wx-1; 
    h = horizontal ? y+height-wy-1 : height;      
    
    divide(nx, ny, w, h, count+1);
  }
  
  @SuppressWarnings("unused")
  private static void displayMaze(int[][] grid) {
    System.out.print(" ");
    for (int i = 0; i < grid[0].length * 2 - 1; ++i) 
      System.out.print("_");
    System.out.println();
    
    for (int y = 0; y < grid.length; ++y) {
      System.out.print("|");
      
      int[] row = grid[y];

      for (int x = 0; x < row.length; ++x) {
        boolean bottom = y+1 >= grid.length;
        boolean south = ((grid[y][x] & SOUTH) != 0) || bottom;
        boolean south2 = x+1 < grid[y].length && (grid[y][x+1] & SOUTH) != 0 || bottom;
        boolean east = (grid[y][x] & EAST) != 0 || (x+1 >= grid[y].length);
        
        System.out.print(south ? "_" : " ");
        System.out.print(east ? "|" : ((south && south2) ? "_" : " "));
      }
      System.out.println();
    }
  }
  @SuppressWarnings("unused")
  private static void displayMaze2(int[][] grid) {
    
    for (int i = 0; i < grid[0].length * 2+1; ++i) 
      System.out.print("#");
    System.out.println();
    
    for (int y = 0; y < grid.length; ++y) {
      
      
      int[] row = grid[y];
      for (int r = 0; r < 2; ++r) {
      System.out.print("#");
      for (int x = 0; x < row.length; ++x) {
        boolean bottom = y+1 >= grid.length;
        boolean south = ((grid[y][x] & SOUTH) != 0) || bottom;
        boolean south2 = x+1 < grid[y].length && (grid[y][x+1] & SOUTH) != 0 || bottom;
        boolean east = (grid[y][x] & EAST) != 0 || (x+1 >= grid[y].length);
        
        System.out.print(south ? (r == 0 ? " " : "#") : " ");
        System.out.print(east ? "#" : ((south && south2) ? (r == 0 ? " " :"#") : " "));
      }
      System.out.println();
      }
    }
  }
  public static int[][] mappify(int[][] grid) {
    int w = grid[0].length;
    int h = grid.length;
    int xx = 0;
    int yy = 0;
    int[][] map = new int[h*2+1][w*2+1];
    
    for (int i = 0; i < map[0].length; ++i)
      map[0][i] = 1;

    ++yy;
    for (int y = 0; y < grid.length; ++y) {
      int[] row = grid[y];
      for (int r = 0; r < 2; ++r) {
        map[yy][xx++] = 1;
        for (int x = 0; x < row.length; ++x) {
          boolean bottom = y+1 >= grid.length;
          boolean south = ((grid[y][x] & SOUTH) != 0) || bottom;
          boolean east = (grid[y][x] & EAST) != 0 || (x+1 >= grid[y].length);

          map[yy][xx++] = (south ? (r == 0 ? 0 : 1) : 0);
          map[yy][xx++] = (east ? 1 : (south ? (r == 0 ? 0 : 1) : 0));
        }
        xx = 0;
        ++yy;
      }
    }
    return map;
  }/*
  public static void main(String[] args) {
//    while (true) {

      int width = 128;
      int height = 128;

      Maze mg = new Maze(width, height);
      int[][] map = mg.getGrid();

      BufferedImage image = new BufferedImage(map[0].length, map.length,
          BufferedImage.TYPE_INT_RGB);
      int[] pix = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
      for (int i = 0; i < pix.length; ++i) {
        int x = i % map.length;
        int y = i / map.length;
        if (map[y][x] == 0) {
          pix[i] = 0xffffff;
        } else if (map[y][x] == 1) {
          pix[i] = 0;
        } else {
          pix[i] = 0xff0000;
        }
      }
      JOptionPane.showMessageDialog(
          null,
          null,
          "maze",
          JOptionPane.YES_NO_OPTION,
          new ImageIcon(image.getScaledInstance(image.getWidth() * 3,
              image.getHeight() * 3, Image.SCALE_AREA_AVERAGING)));

    }*/
 // }

}
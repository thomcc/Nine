package com.thomcc.nine.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

public class VoronoiLevelGen {
  public int points;
  
  public VoronoiLevelGen(int points) { this.points = points; }
  
  public int[][] generateAndCheck(int width, int height) {
    int[][] l = generate(width, height);
    new Checker(l).checkAndFix();
    return l;
    //return generate(templ_width, templ_height);
  }
  private class Point {
    int x, y, rsz; // region size
    Point(int x, int y) {
      this.x = x; this.y = y;
      this.rsz = 0;
    }
  }
  private class Checker {
    int[][] level;
    int[][] seen;
    int w, h;
    List<Point> regions;
    Comparator<Point> rComp = new Comparator<Point>() {
      public int compare(Point a, Point b) {
        return (int)Math.signum(a.rsz - b.rsz); }};
    
    Checker(int[][] level) {
      this.level = level;
      this.w = level[0].length;
      this.h = level.length;
      this.seen = new int[h][w];
      this.regions = new ArrayList<Point>();
    }
    
    public void checkAndFix() {
      check();
      if (regions.size() > 1) {
        fix();
      }
    }
    
    public void check() {
      for (int j = 0; j < h; ++j) {
        for (int i = 0; i < w; ++i) {
          if (level[j][i] == 0 && seen[j][i] == 0) {
            Point p = new Point(i, j);
            regions.add(p);
            flood(p, regions.size(), seen, level, true);
          }
        }
      }
    }
    
    public boolean inBounds(int x, int y) {
      return x >= 0 && y >= 0 && x < w && y < h;
    }
    
    public void fix() {
      Collections.sort(regions, rComp);
      Point max = regions.get(0);
      for (Point r : regions) if (max.rsz < r.rsz) max = r;
      for (Point r : regions) if (r != max) fix(r); 
    }
    
    public void fix(Point r) { 
      flood(r, 1, level, level, false);
      //regions.remove(r);
    }
    
    public void flood(Point start, int value, int[][] set, int[][] check, boolean checking) {
      Stack<Point> s = new Stack<Point>();
      s.push(start);
      Point cur;
      int regionSize = 0;
      while (!s.empty()) {
        cur = s.pop();
        int y = cur.y;
        int x = cur.x;
        if (inBounds(x, y) &&
            set[y][x] == 0 &&
            check[y][x] == 0) {
          set[y][x] = value;
          ++regionSize;
          s.push(new Point(x, y-1));
          s.push(new Point(x-1, y));
          s.push(new Point(x, y+1));
          s.push(new Point(x+1, y));
        }
      }
      if (checking) {
        System.out.format("found region %s of size %s\n", value, regionSize);
        start.rsz = regionSize;
      } else {
        System.out.format("filled region of size %s\n", regionSize);
      }
    }
    
    
  }
  
  public int[][] generate(int width, int height) {
    
    VoronoiNoise v = new VoronoiNoise(width, height, points);
    
    double[][] noise = v.calculate(VoronoiNoise.DistanceMetric.DistanceChebychev);
    
    int[][] level = new int[height][width];
    
    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        double cell = noise[y][x];
        if (x < 20 || y < 20 || x >= width-20 || y >= height-20) level[y][x] = -1;
        else if (cell < 0.20) level[y][x] = 0;
        else if (cell < 0.50) level[y][x] = 1;
        else level[y][x] = 2;
      }
    }
    
    return level;
  }
  public void testChecking() {
    int[][] l = new int[][] {
        { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
        { 1, 0, 0, 1, 0, 0, 0, 0, 0, 1 },
        { 1, 0, 0, 1, 1, 1, 1, 0, 0, 1 },
        { 1, 0, 0, 0, 0, 0, 1, 1, 1, 1 },
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
        { 1, 0, 0, 0, 0, 1, 1, 1, 1, 1 },
        { 1, 0, 0, 0, 0, 1, 0, 0, 0, 1 },
        { 1, 0, 0, 0, 0, 1, 0, 0, 0, 1 },
        { 1, 0, 0, 0, 0, 1, 0, 0, 0, 1 },        
        { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
    };
    System.out.println("Before:");
    for (int j = 0; j < l.length; ++j) {
      System.out.println();
      for (int i = 0; i < l[j].length; ++i) {
        switch (l[j][i]) {
        case 0: System.out.print(" "); break;
        case 1: System.out.print("#"); break;
        }
      }
    }
    new Checker(l).checkAndFix();
    System.out.println("After:");
    for (int j = 0; j < l.length; ++j) {
      System.out.println();
      for (int i = 0; i < l[j].length; ++i) {
        switch (l[j][i]) {
        case 0: System.out.print(" "); break;
        case 1: System.out.print("#"); break;
        }
      }
    }
  }
  //public static void main(String[] args) {
  //  new VoronoiLevelGen(8).testChecking();
  //}
}
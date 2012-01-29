package com.thomcc.nine.level.gen;

public class VoronoiLevelGen {
  public int points;
  
  public VoronoiLevelGen(int points) { this.points = points; }
  
  public int[][] generate(int width, int height) {
    
    VoronoiNoise v = new VoronoiNoise(width, height, points);
    
    double[][] noise = v.calculate(VoronoiNoise.DISTANCE_CHEBYCHEV);
    
    int[][] level = new int[height][width];
    
    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        double cell = noise[y][x];
        if (x < 20 || y < 20 || x >= width-20 || y >= height-20) level[y][x] = -1;
        else if (cell < 0.20) level[y][x] = 0;
        else if (cell < 0.50) level[y][x] = 1;
        else level[y][x] = 2;
//        level[y][x] = noise[y][x] > 0.20 ? 1 : 0;
      }
    }
    
    return level;
  }
  
}
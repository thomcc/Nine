package com.thomcc.nine.level.gen;

public class VoronoiLevelGen implements LevelGen {
  public int points;
  public VoronoiLevelGen(int points) {
    this.points = points;
  }
  @Override
  public int[][] generate(int width, int height) {
    VoronoiNoise v = new VoronoiNoise(width, height, points);
    double[][] noise = v.calculate(VoronoiNoise.DISTANCE_MANHATTAN);
    int level[][] = new int[height][width];
    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        double c = noise[y][x];
        level[y][x] = c > 0.1 ? 0 : 1;
      }
    }
    return level;
  }

}

package com.thomcc.nine.render;


public class Art {
  public static final int PLAYERCOLOR = 0xffff6249;//0xff0023ff;
  private static final int _ = 0;
  private static final int A = 1;
  private static final int B = 2;
  private static final int[] playerColors = { 0, 0xffff6249, 0xff4e4240 };
  private static final int[][] playerTemplate = new int[][] {
    { _, _, _, _, _, _, _, _, _, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _ },
    { _, B, _, _, _, A, A, A, _, _, _, B },
    { _, B, B, B, B, A, A, A, B, B, B, B },
    { _, _, B, B, B, B, B, B, B, B, B, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _ }
  };
  private static final int[] enemyColors = { 0, 0xff262626, 0xff649f42 };
  private static final int[][] enemyTemplate = new int[][] {
    { _, _, _, _, _, _, _, _, _, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _ },
    { _, _, _, _, A, _, _, _, A, _, _, _ },
    { _, _, _, A, A, _, _, _, A, A, _, _ },
    { _, _, _, A, _, _, A, _, _, A, _, _ },
    { _, _, _, A, _, B, B, B, _, A, _, _ },
    { _, _, _, A, A, B, B, B, A, A, _, _ },
    { _, _, _, A, A, B, B, B, A, A, _, _ },
    { _, _, _, _, A, A, A, A, A, _, _, _ },
    { _, _, _, _, _, A, A, A, _, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _ }
  };
  private static final int[] bulletColors = { 0, 0xff282b35, 0xff82333b };
  private static final int[][] bulletTemplate = new int[][] {
    { _, A, A, _ },
    { A, B, B, A },
    { A, B, B, A },
    { _, A, A, _ }
  };
  private static final int[][] nineTemplate = new int[][] {
    { _, _, _, _, _, _, _, 1, 1, 1, 1, _, _, _, _, _, _ },
    { _, _, _, _, _, _, 1, _, _, _, _, 1, _, _, _, _, _ },
    { _, _, _, _, _, 1, _, _, _, _, _, _, 1, _, _, _, _ },
    { _, _, _, _, _, 1, _, _, _, _, _, _, 1, _, _, _, _ },
    { _, _, _, _, 1, _, _, _, _, _, _, _, _, 1, _, _, _ },
    { _, _, _, _, 1, _, _, _, _, _, _, _, _, 1, _, _, _ },
    { _, _, _, _, 1, _, _, _, _, _, _, _, _, 1, _, _, _ },
    { _, _, _, _, _, 1, _, _, _, _, _, _, _, 1, _, _, _ },
    { _, _, _, _, _, _, 1, 1, 1, 1, 1, 1, 1, 1, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _, _, 1, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _, _, 1, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _, _, 1, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _, 1, _, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _, 1, _, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, 1, _, _, _, _, _ },
    { _, _, _, _, _, _, _, _, _, 1, 1, _, _, _, _, _, _ },
    { _, _, _, _, _, 1, 1, 1, 1, _, _, _, _, _, _, _, _ }
  };
  
  public final Sprite[] sprites;
  public final Sprite nine;
  public Art() {
    sprites = new Sprite[] {
        new Sprite(playerTemplate, 16, playerColors),
        new Sprite(bulletTemplate, 1, bulletColors),
        new Sprite(enemyTemplate, 16, enemyColors)
    };
    nine = new Sprite(nineTemplate, 1, new int[] {0, 0xffdddddd});
    
  }
  
}

package com.thomcc.nine.render;


public class Art {
  public static final int PLAYERCOLOR = 0xffff6249;//0xff0023ff;
  public static final int ENEMYCOLOR = 0xff649f42;
  private static final int _ = 0;
  private static final int A = 1;
  private static final int B = 2;
  private static final int C = 3;
  private static final int D = 4;
//  private static final int E = 5;
  private static final int[] playerColors = { 0, 0xffff6249, 0xff4e4240 };
  private static final int[] healthPackColors = { 0, 0xffa3ce9c, 0xff19a204, 0xff3cc327, 0xff0f6a01, 0xff7b9b76 }; 
  private static final int[][][] healthPackTemplate = new int[][][] {
    { { C, C, C, C, C, C, C },
      { C, A, B, B, B, A, D },   
      { C, A, B, B, B, A, D },   
      { C, A, A, A, A, A, D },
      { C, A, B, B, B, A, D },
      { C, A, B, B, B, A, D },
      { D, D, D, D, D, D, D } }
  };
  private static final int[][][] oneUpTemplate = new int[][][] {
    { { C, C, C, C, C, C, C },
      { C, B, B, B, A, A, D },   
      { C, B, A, B, B, A, D },   
      { C, A, A, A, B, A, D },
      { C, B, A, B, B, A, D },
      { C, B, B, B, B, A, D },
      { D, D, D, D, D, D, D } }
  };
  
  private static final int[] b3colors = new int[] {
      0, 0xff82333b, 0xffddbbbb, 0xffd22345, 0xff832735 
  };
  private static final int[][][] b3template = new int[][][] {
    { { C, C, C, C, C, C, C },
      { C, A, B, B, A, A, D },
      { C, A, A, A, B, A, D },
      { C, A, B, B, A, A, D },
      { C, A, A, A, B, A, D },
      { C, A, B, B, A, A, D },
      { D, D, D, D, D, D, D } }
  };
  
  private static final int[][][] playerTemplate = new int[][][] {{
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
  }};
  private static final int[] enemyColors = { 0, 0xff262626, 0xff649f42 };
  private static final int[][][] enemyTemplate = new int[][][] {{
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
  }};
  private static final int[] bulletColors = { 0, 0xff282b35, 0xff82333b };
  private static final int[][][] bulletTemplate = new int[][][] {{
    { _, A, A, _ },
    { A, B, B, A },
    { A, B, B, A },
    { _, A, A, _ }
  }};
  private static final int[][][] nineTemplate = new int[][][] {{
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
  }};

  public final Sprite[] sprites;
  public final Sprite nine;
  public Art() {
    sprites = new Sprite[] {
        new Sprite(playerTemplate, 16, playerColors),
        new Sprite(bulletTemplate, 1, bulletColors),
        new Sprite(enemyTemplate, 16, enemyColors),
        new Sprite(healthPackTemplate, 1, healthPackColors),
        new Sprite(b3template, 1, b3colors),
        new Sprite(oneUpTemplate, 1, healthPackColors)
    };
    nine = new Sprite(nineTemplate, 1, new int[] {0, 0xffff6249});
    
  }
  
}

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
  private static final int[] playerColors = { 0, 0xffff6249, 0xff4e4240, 0xff888888 };
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
  private static final int[] superColors = { 0, 0xffff6249, 0xff000000, 0xff888888, 0xff4e4240 };
  private static final int[][][] superTemplate = new int[][][] {
    { { C, C, C, C, C, C, D },
      { C, B, A, A, A, B, D },   
      { C, A, B, B, B, B, D },   
      { C, B, A, A, A, B, D },
      { C, B, B, B, B, A, D },
      { C, B, A, A, A, B, D },
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
  }, { // superpowered
    { _, _, _, _, _, _, _, _, _, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _ },
    { _, _, _, _, C, C, A, C, C, _, _, _ },
    { _, C, _, _, C, A, A, A, C, _, _, C },
    { _, A, A, B, B, A, A, A, B, B, A, A },
    { _, _, C, C, C, B, B, B, C, C, C, _ },
    { _, _, _, _, A, A, A, A, A, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _ }
  }};
  private static final int[] strongEnemyColors = { 0, 0xff262626, 0xffeeeeee, 0xfffa0021 };
  private static final int[][][] strongEnemyTemplate = new int[][][] {{
    { _, _, _, _, _, _, _, _, _, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _ },
    { _, _, _, A, _, _, _, _, _, A, _, _ },
    { _, _, A, A, _, _, _, _, _, A, A, _ },
    { _, A, _, A, _, B, B, B, _, A, _, A },
    { _, A, _, A, A, B, B, B, A, A, _, A },
    { _, A, _, _, A, B, B, B, A, _, _, A },
    { _, A, A, A, A, A, A, A, A, A, A, A },
    { _, A, _, _, _, _, _, _, _, _, _, A },
    { _, _, _, _, _, _, _, _, _, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _ }
  },{
    { _, _, _, _, _, _, _, _, _, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _ },
    { _, _, _, _, _, _, _, _, _, _, _, _ },
    { _, _, _, A, _, _, _, _, _, A, _, _ },
    { _, _, A, A, _, _, _, _, _, A, A, _ },
    { _, A, _, A, _, C, C, C, _, A, _, A },
    { _, A, _, A, A, C, C, C, A, A, _, A },
    { _, A, _, _, A, C, C, C, A, _, _, A },
    { _, A, A, A, A, A, A, A, A, A, A, A },
    { _, A, _, _, _, _, _, _, _, _, _, A },
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
  public static final int PLAYER_INDEX = 0;
  public static final int BULLET_INDEX = 1;
  public static final int ENEMY_INDEX = 2;
  public static final int HEALTHPACK_INDEX = 3;
  public static final int GUN3_INDEX = 4;
  public static final int ONEUP_INDEX = 5;
  public static final int SENEMY_INDEX = 6;
  public static final int SUPER_INDEX = 7;
  public final Sprite[] sprites;
  public final Sprite nine;
  public Art() {
    sprites = new Sprite[] {
        new Sprite(playerTemplate, 16, playerColors),
        new Sprite(bulletTemplate, 1, bulletColors),
        new Sprite(enemyTemplate, 16, enemyColors),
        new Sprite(healthPackTemplate, 1, healthPackColors),
        new Sprite(b3template, 1, b3colors),
        new Sprite(oneUpTemplate, 1, healthPackColors),
        new Sprite(strongEnemyTemplate, 16, strongEnemyColors),
        new Sprite(superTemplate, 1, superColors)
    };
    nine = new Sprite(nineTemplate, 1, new int[] {0, 0xffff6249});
    
  }
  
}

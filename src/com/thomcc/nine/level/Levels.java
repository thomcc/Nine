package com.thomcc.nine.level;

import java.util.Random;

import com.thomcc.nine.entity.StrongEnemy;


//import com.thomcc.nine.entity.Enemy;
//import com.thomcc.nine.render.Renderer;

public class Levels {
  private static final Random _random = new Random();
  
  public static Level getLevel(int num) {
    if (num % 5 == 0) return makeToughLevel(num);
    return makeGenericLevel(num);
  }
  private static Level makeGenericLevel(int num) {
    int n = num*5+15;
    int sz = 575 + num*25;
    double pointmod = (_random.nextDouble()+2*_random.nextDouble()+4*_random.nextDouble())*4.0/7.0;
    Level l = new Level(sz, sz, (int)(Math.sqrt(sz)/pointmod));
    //l.addEnemies(n);
    l.seedEnemies(n, num*3);
    l.num = num;
    l.description = "Kill all " + n + " enemies.";
    return l;
  }
  private static Level makeToughLevel(int num) {
    int n = num / 5;
    int sz = 900+10*num;
    int points = 30+5*n;
    Level l = new Level(sz, sz, points);
    for (int i = 0; i < 15+5*n; ++i) {
      l.findLocationAndAdd(new StrongEnemy());
    }
    l.num=num;
    l.description = "Tough Level: kill all "+15+5*n+ " strong enemies";
    return l;
  }
}

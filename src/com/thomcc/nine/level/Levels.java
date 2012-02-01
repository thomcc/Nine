package com.thomcc.nine.level;

//import java.util.Random;

//import com.thomcc.nine.entity.Enemy;
//import com.thomcc.nine.render.Renderer;

public class Levels {
  //private static final Random _random = new Random();
  
  public static Level getLevel(int num) {
    //switch (num) {
    
    //case 1: return makeLevel1();
    
    //case 2: return makeLevel2();
    
    //default: return makeGenericLevel(num); 
    //}
    return makeGenericLevel(num);
  }
  private static Level makeGenericLevel(int num) {
    int n = num*5+15;
    int sz = 575 + num*25;
    Level l = new Level(sz, sz, sz/10);
    l.addEnemies(n);
    l.num = num;
    l.description = "Kill all " + n + " enemies.";
    return l;
  }
  
}

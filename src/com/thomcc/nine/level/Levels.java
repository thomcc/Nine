package com.thomcc.nine.level;

//import java.util.Random;

import com.thomcc.nine.entity.Enemy;
import com.thomcc.nine.render.Renderer;

public class Levels {
  //private static final Random _random = new Random();
  
  public static Level getLevel(int num) {
    switch (num) {
    
    case 1: return makeLevel1();
    
    case 2: return makeLevel2();
    
    default: return makeGenericLevel(num); 
    }
  }
  private static Level makeLevel1() {
    Level l = new Level();
    int n = 20;
    l.addEnemies(n);
    l.num = 1;
    l.description = "Easy. Kill "+n+" enemies to advance to the next round";
    return l;
  }
  
  private static Level makeLevel2() {
    Level l = new Level();
    int n = 20;
    l.addEnemies(n-1);
    l.findLocationAndAdd(new Enemy(){public void render(Renderer r) {}});
    l.num = 2;
    l.description = "A bit harder, kill "+n+" enemies to advance to the next round, but one of them will only show up on your minimap.";
    return l;
  }
  private static Level makeGenericLevel(int num) {
    int n = num*5+20;
    int sz = 600 + num*25;
    Level l = new Level(sz, sz, sz/10);
    l.addEnemies(n);
    l.num = num;
    l.description = "Sorry, haven't made any more custom levels yet, but you can keep playing!  Just normal enemies in these.";
    return l;
  }
  
}

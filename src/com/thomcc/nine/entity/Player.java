package com.thomcc.nine.entity;


import com.thomcc.nine.render.Renderer;


public class Player extends Entity {
  
  public static final int SIZE = 12;

  public Player() {
    _friction = 0.9;
    _maxSpeed = 4.0;
  }
  
  public void tick(boolean u, boolean d, boolean l, boolean r, boolean click) {
    if (d) _py += 1.0;
    if (r) _px += 1.0;
    if (l) _px -= 1.0;
    if (u) _py -= 1.0;
    
    updatePosition();
    reduceSpeed();
  }
  
  public void render(Renderer r) {
    int d = (((int) (dir / (Math.PI * 2) * 16 + 20.5)) & 15);
    r.renderPlayer((int)x, (int)y, d);
  }

}

package com.thomcc.nine.entity;


import com.thomcc.nine.*;
import com.thomcc.nine.level.Level;
import com.thomcc.nine.render.Renderer;

public class Player extends Entity {
  
  public static final int SIZE = 12;
  public Input _i;
  public int health;
  private int fireCount;
  public Player(Input i) {
    _i = i;
    health = 20;
    _friction = 0.9;
    _maxSpeed = 4.0;
    fireCount = 0;
  }
  public void setLevel(Level l) {
    super.setLevel(l);
    l.findPlayerLocation(this);
  }
  public void tick() {
    if (_i.down) _py += 1.0;
    if (_i.right) _px += 1.0;
    if (_i.left) _px -= 1.0;
    if (_i.up) _py -= 1.0;
    if (_i.mouseDown) fire();
    super.tick();
  }
  public void fire() {
    //System.out.println("firing!");
    if (fireCount-- > 0) return;
    _level.add(new Bullet(this, dir, 10));
    fireCount += 3;
  }
  public void hurt(Entity cause, int damage, double dir) {
    health -= damage;
    double cd = Math.cos(dir);
    double sd = Math.sin(dir);
    _px += cd*damage;
    _py += sd*damage;
  }
  
  public void render(Renderer r) {
    int d = (((int) (dir / (Math.PI * 2) * 16 + 20.5)) & 15);
    r.renderPlayer((int)x, (int)y, d);
  }

}

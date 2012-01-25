package com.thomcc.nine.entity;

import com.thomcc.nine.render.Renderer;

public class Enemy extends Entity {
  private int _health;
  public Enemy() {
    _health = 1;
    _collisionFriction = 0.3;
    _canSlide = true;
    _friction = 0.98;
    _maxSpeed = 4.0;
    rx = ry = 3;
    _moveInterval = 40 + random.nextInt(40);
  }
  private int _moveInterval; 
  public void tick(long ticks) {
    if (ticks % _moveInterval == 0) {
      int dx = 0, dy = 0;
      switch(random.nextInt(3)) {
      case 0: 
        ++dx;
        break;
      case 1:
        --dx;
        break;
      }
      switch(random.nextInt(3)) {
      case 0:
        ++dy;
        break;
      case 1:
        --dy;
        break;
      }
      dir = Math.atan2(dy, dx);
      _px += dx*4;
      _py += dy*4;
    }
    super.tick(ticks);
  }
  public void hurt(Entity cause, int damage, double dir) {
    if (--_health <= 0) { 
      remove();
      return; 
    }
    double cd = Math.cos(dir);
    double sd = Math.sin(dir);
    _px += cd*damage;
    _py += sd*damage;
  }
  protected void touched(Entity e) {
    if (e instanceof Player) {
      e.hurt(this, _health, dir);
    }
  }
  public boolean appearsOnMinimap() { return true; }
  public int getColor() { return 0xff649f42; }
  public void render(Renderer r) {
    int d = (((int) (dir / (Math.PI * 2) * 16 + 20.5)) & 15);
    //r.render(2, getBoundedX(), getBoundedY(), d);
    r.render(2, (int)x, (int)y, d);
  }
}

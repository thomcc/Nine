package com.thomcc.nine.entity;

import com.thomcc.nine.render.Renderer;

public class Enemy extends Mobile {
  public Enemy() {
    health = 1;
    _collisionFriction = 0.3;
    _friction = 0.98;
    _maxSpeed = 4.0;
    rx = ry = 3;
    _moveInterval = 40 + random.nextInt(40);
  }
  private int _moveInterval; 
  protected void behave(long ticks) {
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
  }
  public void die() {
    System.out.format("x:%s y:%s\n", getX(), getY());
    _level.add(new HealthPackItem(getX(), getY()));
    super.die();
  }
  public void tick(long ticks) {
    behave(ticks);
    super.tick(ticks);
  }
  protected void touched(Entity e) {
    if (e instanceof Player) {
      e.hurt(this, 1, dir);
    }
  }
  public boolean appearsOnMinimap() { return true; }
  public int getColor() { return 0xff649f42; }
  public void render(Renderer r) {
    r.render(2, (int)x, (int)y, getDirection());
  }
}

package com.thomcc.nine.entity;

import java.util.ArrayList;

import com.thomcc.nine.render.Renderer;

public class Bullet extends Entity {
  public double xa, ya;
  public double xx, yy;
  private Entity owner;
  private int _time;
  private int _life;
  private int _collisions;
  private final int _maxCollisions;
  public Bullet(Entity owner, double dir, double speed) {
    rx = ry = 1;
    xx = this.x = owner.x;
    yy = this.y = owner.y;
    this.dir = dir;
    _px = Math.cos(dir)*speed;
    _py = Math.sin(dir)*speed;
    this.owner = owner;
    _time = 0;
    _life = 200+random.nextInt(100);
    _maxSpeed = 1000;
    _friction = 1;
    _collisions = 0;
    _maxCollisions = 10;
  }
  
  protected void collision(boolean ycol, int d) {
    if (_collisions++ > _maxCollisions) { remove(); return; }
    if (ycol) { // FIXME this should NOT MAKE THE BULLETS SLIDE ALONG WALLS
      switch (d) {
      case +1: _px += Math.abs(_py/2); break;
      case -1: _px -= Math.abs(_py/2);  break;
      case  0: _py *= -1; break;
      }
    } else {
      switch (d) {
      case +1: _py += Math.abs(_px/2); break;
      case -1: _py -= Math.abs(_px/2); break;
      case  0: _px *= -1; break;
      }
    }
    
    
  }
  public void tick(long ticks) {
    if (++_time >= _life) { remove(); return; }
    updatePosition();
    int x0 = getX();//getBoundedX();
    int y0 = getY();//getBoundedY();
    ArrayList<Entity> toHit = _level.getEntities(x0, y0, x0, y0);
    for (Entity e : toHit) {
      if (!(e instanceof Bullet) ) {//&& e != owner) {
        e.hurt(owner, 2, dir+Math.PI);
        remove();
        return;
      }
    }
  }
  public void render(Renderer r) {
    r.renderBullet((int)x, (int)y);
  }
  
  
}

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
    _life = 60;
    _maxSpeed = 1000;
    _friction = 1;
    _collisions = 0;
    _maxCollisions = 2;
  }
  
  protected void collision() {
    if (_collisions++ > _maxCollisions) remove();
  }
  public void tick() {
    ++_time;
    if (_time >= _life) {
      remove();
      return;
    }
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

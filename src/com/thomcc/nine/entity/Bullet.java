package com.thomcc.nine.entity;

import java.util.List;
import com.thomcc.nine.render.Renderer;

public class Bullet extends Entity {
  public double xa, ya;
  public double xx, yy;
  private Entity owner;
  private int _time;
  private int _life;
  private int _collisions, _maxCollisions;
  private boolean _collided = false;
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
    _collisionFriction = 1;
    _collisions = 0;
    _maxCollisions = 20;
  }
  protected void collision(boolean ycol, double dx, double dy) {
    _collided = true;
    if (++_collisions >= _maxCollisions) { remove(); return; }
    super.collision(ycol, dx, dy);
  }
  public void tick(long ticks) {
    if (++_time >= _life) { remove(); return; }
    _collided = false;
    int x0 = getX();
    int y0 = getY();
    updatePosition();
    int x1 = getX();
    int y1 = getY();
    if (x0 == x1 && y0 == y1 && !_collided) { remove(); return; }
    List<Entity> toHit = _level.getEntities(x1-rx, y1-ry, x1+rx, y1+ry);
    //System.out.println(toHit);
    for (Entity e : toHit) {
      if (!(e instanceof Bullet)) {
        e.hurt(owner, 2, dir+Math.PI);
        remove();
        return;
      }
    }
  }
  public void render(Renderer r) {
    r.render(1, (int)x, (int)y, 0);
  }
  
  
}

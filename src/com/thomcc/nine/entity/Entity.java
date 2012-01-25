package com.thomcc.nine.entity;

import java.util.List;
import java.util.Random;

import com.thomcc.nine.level.Level;
import com.thomcc.nine.render.Renderer;

public class Entity {
  
  protected final Random random = new Random();
  protected Level _level;
  public double x = 0, y = 0;
  public int rx = 1, ry = 1;
  protected int _startX = -1, _startY = -1;
  protected double _px = 0, _py = 0;
  protected double _eyeX = -1, _eyeY = -1;
  public boolean removed = false;
  public double dir = 0;
  protected boolean _canSlide = false;
  protected double _maxSpeed;
  protected double _friction;
  protected double _collisionFriction;
  public int getDirection() {
    return (((int) (dir / (Math.PI * 2) * 16 + 20.5)) & 15);
  }
  public void render(Renderer r) {}
  public void remove() {
    removed = true;
  }
  public void tick(long ticks) {
    updatePosition();
    reduceSpeed();
  }
  
  public void lookAt(int px, int py) {
    _eyeX = px;
    _eyeY = py;
    dir = Math.atan2(py - y, px - x);
  }
  
  public void reduceSpeed() {
    
    _px *= _friction;
    _py *= _friction;
    
    if (Math.abs(_px) < 0.001) _px = 0;
    if (Math.abs(_py) < 0.001) _py = 0;
    
    if (Math.abs(_px) > _maxSpeed) _px = _maxSpeed * Math.signum(_px);
    if (Math.abs(_py) > _maxSpeed) _py = _maxSpeed * Math.signum(_py);
    
    double mag = Math.hypot(_px, _py);
    
    if (mag > _maxSpeed) {
      double angle = Math.atan2(_py, _px);
      double ca = Math.cos(angle);
      double sa = Math.sin(angle);
      _px = _maxSpeed * ca;
      _py = _maxSpeed * sa;
    }
  
  }
  public void hurt(Entity cause, int damage, double dir) {
  }
  protected void collision() {}
  protected void collision(boolean ycol, double dx, double dy) {
    if (!_canSlide) { bounce(dx, dy); return; }
    if (ycol) {
      switch ((int)dx) {
      case +1:
        _px += Math.abs(_py*_collisionFriction);
        _py *= _collisionFriction;
        break;
      case -1:
        _px -= Math.abs(_py*_collisionFriction);
        _py *= _collisionFriction;
        break;
      case 0:
        _py *= _collisionFriction;
        break;
      }
    } else {
      switch ((int)dy) {
      case +1:
        _py += Math.abs(_px*_collisionFriction);
        _px *= _collisionFriction;
        break;
      case -1:
        _py -= Math.abs(_px*_collisionFriction);
        _px *= _collisionFriction;
        break;
      case 0:
        _px *= _collisionFriction;
        break;
      }
    }
  }
  protected void bounce(double dx, double dy) {
    if (dx == 0) {
      _py *= -_collisionFriction;
      return;
    } else if (dy == 0) {
      _px *= -_collisionFriction;
      return;
    }
    double mag = Math.hypot(dx, dy);
    dx /= mag;
    dy /= mag;
    double nx = -dy;
    double ny = dx;
    double pmag = Math.hypot(_px, _py);
    double npx = _px/pmag;
    double npy = _py/pmag;
    pmag *= _collisionFriction;
    double fx = nx * 2 + npx;
    double fy = ny * 2 + npy;
    double fmag = Math.hypot(fy, fx);
    _px = fx*pmag/fmag;
    _py = fy*pmag/fmag;
  }
  public void updatePosition() {
    if (_px == 0 && _py == 0) return;
    //int lastx = getBoundedX(), lasty = getBoundedY();
    if (_py != 0) {
      
      int dy = (_py < 0) ? -1 : 1;

      int t = (int)Math.abs(_py);
      
      for (int iy = 0; iy < t; ++iy) {
        if (canMove(0, dy)) {
          y += dy;
        } else if (canMove(1, dy)) {
          collision(true, 1, dy); 
          break;
        } else if (canMove(-1, dy)) {
          collision(true, -1, dy); 
          break;
        } else {
          collision(true, 0, dy); 
          break; 
        }
      }
    }
    
    if (_px != 0) {
      int dx = (_px < 0) ? -1 : 1;
      int t = (int)Math.abs(_px);
      for (int ix = 0; ix < t; ix++) {
        if (canMove(dx,0)) {
          x += dx;
        } else if (canMove(dx, 1)) {
          collision(false, dx, 1); 
          break;
        } else if (canMove(dx, -1)) {
          collision(false, dx, -1); 
          break;
        } else { 
          collision(false, dx, 0); 
          break; 
        }
      }
    }
    int xx = getBoundedX();
    int yy = getBoundedY();
    //List<Entity> lastinside = _level.getEntities(lastx-rx, lasty-ry, lastx+rx, lasty+ry);
    List<Entity> nowinside = _level.getEntities(xx-rx,yy-ry,xx+rx,yy+ry);
    for (Entity e : nowinside)
      if (e == this) continue;
      else e.touched(this);
    
    
  }
  protected void touched(Entity e) {
    
  }
  public boolean blocks(Entity e) {
    return false;
  }
  
  
  private boolean canMove(int dx, int dy) {
    int xx = (int)x;
    int yy = (int)y;
    int lt = (xx-rx);
    int tt = (yy-ry);
    int rt = (xx+rx);
    int bt = (yy+ry);
    int nlt = (xx+dx-rx);
    int ntt = (yy+dy-ry);
    int nrt = (xx+dx+rx);
    int nbt = (yy+dy+ry);
    for (int x0 = nlt; x0 <= nrt; ++x0) {
      for (int y0 = ntt; y0 <= nbt; ++y0) {
        if (x0 >= lt && x0 <= rt && y0 >= tt && y0 <= bt) 
          continue;
        else if (_level.blocks(x0, y0)) 
          return false;
      }
    }
    return true;
  }
  
  public int getX() { 
    return (int)x;
  }
  
  public int getY() { 
    return (int)y; 
  }
  public int getBoundedX() {
    int xx = (int) x;
    if (_level != null) {
      while (xx < 0) xx += _level.getWidth();
      xx %= _level.getWidth();
    }
    return xx;
  }
  public boolean intersects(int x0, int y0, int x1, int y1) {
    return !(x + rx < x0 || y + ry < y0 || x - rx > x1 || y - ry > y1);
  }
  public int getBoundedY() {
    int yy = (int) y;
    if (_level != null) {
      while (yy < 0) yy += _level.getHeight();
      yy %= _level.getHeight();
    }
    return yy;
  }
  public void setLevel(Level l) { 
    _level = l;
    
  }

  public void setPosition(int x, int y) {
    if (_startX < 0 && _startY < 0) {
      _startX = x;
      this.x = x;
      _startY = y;
      this.y = y;
    } else {
      this.x = x;
      this.y = y;
    }
  }
}

package com.thomcc.nine;



import com.thomcc.nine.level.Level;

public class Player {
  public double dir;
  public int x, y;
  private Level _level;
  private double _dist;
  private int _startX, _startY;
  private int _px, _py; // momentum
  private static final int MOMENTUM_MAX = 6;
  public Player() {
    _startX = _startY = -1;
    x = y = -1;
    _px = 0;
    _py = 0;
    dir = 0;
  }
  public void lookAt(int px, int py) {
    dir = Math.atan2(py - y, px - x);
  }
  public int getDirection() {
    return (((int) (dir / (Math.PI * 2) * 16 + 20.5)) & 15);
  }
  public void tick(boolean u, boolean d, boolean l, boolean r) {
    //int dx = 0, dy = 0;
    
    if (d) _py += 2;
    if (r) _px += 2;
    if (l) _px -= 2;
    if (u) _py -= 2;
    if (i % 60 == 0) {
      System.out.format("_px: %s, _py: %s\n", _px, _py);
    }
    updatePosition();
    if (_px < 0) ++_px;
    else if (_px > 0) --_px;
    else if (_px > MOMENTUM_MAX) _px = MOMENTUM_MAX;
    if (_py < 0) ++_py;
    else if (_py > 0) --_py;
    else if (_py > MOMENTUM_MAX) _py = MOMENTUM_MAX;
    if(i++ % 60 == 0) {
      
    }
    updateDistance();
  }
  private int i = 0;
  public void updatePosition() {
    if (_px == 0 && _py == 0) return;
    if (_py != 0) {
      int dy = (_py < 0) ? -1 : 1;
      int t = Math.abs(_py);
      for (int iy = 0; iy < t; ++iy) {
        if (canMove(0, dy)) {
          y += dy;
        }
        else {
          _py = 0;
          break;
        }
      }      
    }
    if (_px != 0) {
      int dx = (_px < 0) ? -1 : 1;
      int t = Math.abs(_px);
      for (int ix = 0; ix < t; ix++) {
        if (canMove(dx,0)) {
          //System.out.println("moving x");
          x += dx;
        } else {
          _px = 0;
          break;
        }
      }
    }
    
  }
  /*
  private void move(int dx, int dy) {
    if (dx == 0 && dy == 0) return;
    // separate to allow sliding along walls
    if (dx != 0 && canMove(dx, 0)) x += dx; 
    if (dy != 0 && canMove(0, dy)) y += dy;
    updateDistance();
  }*/
  private void updateDistance() {
    double deltax = (this.x / 16.0) - (_startX / 16.0);
    double deltay = (this.y / 16.0) - (_startY / 16.0);
    _dist = Math.sqrt(deltax * deltax + deltay * deltay);
  }
  
  private boolean canMove(int dx, int dy) {
    int rx = 1;
    int ry = 1;
    
    int lt = (x-rx)>>4;
    int tt = (y-ry)>>4;
    int rt = (x+rx)>>4;
    int bt = (y+ry)>>4;
    int nlt = (x+dx-rx)>>4;
    int ntt = (y+dy-ry)>>4;
    int nrt = (x+dx+rx)>>4;
    int nbt = (y+dy+ry)>>4;
    
    for (int x0 = nlt; x0 <= nrt; ++x0) {
      for (int y0 = ntt; y0 <= nbt; ++y0) {
        if (x0 >= lt && x0 <= rt && y0 >= tt && y0 <= bt)
          continue;
        if (_level.blocks(x0, y0)) {
          return false;
        } 
      }
    }

    return true;
  }
  public void setLevel(Level l) { 
    _level = l; 
  }
  public void setPosition(int x, int y) {
    if (_startX < 0 && _startY < 0) {
      _startX = this.x = x;
      _startY = this.y = y;
      _dist = 0;
    } else {
      this.x = x;
      this.y = y;
      updateDistance();
    }
  }
  public double getDistance() { return _dist; }
}

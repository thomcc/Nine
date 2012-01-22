package com.thomcc.nine;


//NOTE TO SELF
//TODO
// RIDGED MULTIFRACTAL
import com.thomcc.nine.level.*;
import com.thomcc.nine.render.Renderer;


public class Player {
  
  public final int rx = 1;
  public final int ry = 1;
  public static final int SIZE = 12;
  
  private Level _level;
  private int _startX, _startY;
  
  private double _px, _py; // momentum
  private static final int MOMENTUM_MAX = 6;
  
  public double x, y;
  public double dir;
  
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
    
    if (d) _py += 1.3;
    if (r) _px += 1.3;
    if (l) _px -= 1.3;
    if (u) _py -= 1.3;
    
    if (i++ % 60 == 0) {
      System.out.format("_px: %s, _py: %s\n", _px, _py);
    }
    
    updatePosition();
    
    _px *= 0.98;
    _py *= 0.98;
    
    if (Math.abs(_px) < 0.001) _px = 0;
    if (Math.abs(_py) < 0.001) _py = 0;
    if (Math.abs(_px) > MOMENTUM_MAX) _px = MOMENTUM_MAX * Math.signum(_px);
    if (Math.abs(_py) > MOMENTUM_MAX) _py = MOMENTUM_MAX * Math.signum(_py);
    
  }
  private int i = 0;
  public void updatePosition() {
    if (_px == 0 && _py == 0) return;
    if (_py != 0) {
      int dy = (_py < 0) ? -1 : 1;
      int t = (int)Math.abs(_py);
      for (int iy = 0; iy < t; ++iy) {
        if (canMove(0, dy)) y += dy;
        else { _py *= -0.3; break; }
      }
    }
    if (_px != 0) {
      int dx = (_px < 0) ? -1 : 1;
      int t = (int)Math.abs(_px);
      for (int ix = 0; ix < t; ix++) {
        if (canMove(dx,0)) x += dx;
        else { _px *= -0.3; break; }
      }
    }
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
  public void render(Renderer r) {
    int d = (((int) (dir / (Math.PI * 2) * 16 + 20.5)) & 15);
    
    r.renderPlayer((int)x, (int)y, d);
  }
  public int getX() { return (int)x; }
  public int getY() { return (int)y; }
  public void setLevel(Level l) { _level = l; }
}

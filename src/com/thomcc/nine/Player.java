package com.thomcc.nine;



import com.thomcc.nine.level.Level;

public class Player {
  public double dir;
  public int x, y;
  private Level _level;
  private double _dist;
  private int _startX, _startY;
  public Player() {
    _startX = _startY = x = y = -1;
    dir = 0;
  }
  public void lookAt(int px, int py) {
    dir = Math.atan2(py - y, px - x);
  }
  public int getDirection() {
    return (((int) (dir / (Math.PI * 2) * 16 + 20.5)) & 15);
  }
  public void tick(boolean u, boolean d, boolean l, boolean r) {
    int dx = 0, dy = 0;
    
    if (d) ++dy;
    if (r) ++dx;
    if (l) --dx;
    if (u) --dy;
    
    move(dx, dy);
    
  }
  
  private void move(int dx, int dy) {
    if (dx == 0 && dy == 0) return;
    // separate to allow sliding along walls
    if (dx != 0 && canMove(dx, 0)) x += dx; 
    if (dy != 0 && canMove(0, dy)) y += dy;
    updateDistance();
  }
  private void updateDistance() {
    double deltax = (this.x / 16.0) - (_startX / 16.0);
    double deltay = (this.y / 16.0) - (_startY / 16.0);
    _dist = Math.sqrt(deltax * deltax + deltay * deltay);
  }
  
  private boolean canMove(int dx, int dy) {
    int rx = 2;
    int ry = 2;
    
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

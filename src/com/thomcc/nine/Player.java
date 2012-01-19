package com.thomcc.nine;

import java.awt.image.BufferedImage;

public class Player {
  public double dir;
  public int x, y;
  private InputHandler _input;
  public Player(InputHandler i) {
    _input = i;
    x = y = 20;
    dir = 0;
  }
  public void lookAt(int px, int py) {
    dir = Math.atan2(py - y, px - x);
  }
  public int getDirection() {
    return (((int) (dir / (Math.PI * 2) * 16 + 20.5)) & 15);
  }
  public void tick() {
    
    if (_input.down) ++y;
    if (_input.right) ++x;
    if (_input.left) --x;
    if (_input.up) --y;
    
    if (_input.mouseX != -1 && _input.mouseY != -1) {
      lookAt(_input.mouseX, _input.mouseY);
    }
  }
}

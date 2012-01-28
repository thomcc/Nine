package com.thomcc.nine.entity;

import com.thomcc.nine.render.Renderer;

public class HealthPackItem extends Item {

  public HealthPackItem(int x, int y) {
    super(x, y);
//    _totalFrames = 1;
    _animDelay = 10;
    
  }

  protected void apply(Mobile m) {
    if (m instanceof Player) {
      ((Player)m).heal(2+random.nextInt(3));
      remove();
    }
  }
  public void render(Renderer r) {
    if (_flickering) {
      if (_flicker) r.render(3, (int)x, (int)y, 0, frame);
    } else r.render(3, (int)x, (int)y, 0, frame);
  }
}

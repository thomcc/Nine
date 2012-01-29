package com.thomcc.nine.entity;

import com.thomcc.nine.render.Renderer;

public class ThreeGunItem extends Item {

  public ThreeGunItem(int x, int y) {
    super(x, y);
    _animDelay = 10;
    
  }

  protected void apply(Mobile m) {
    super.apply(m);
    if (m instanceof Player) {
      Player p = (Player)m;
      p.setGun(new ThreeGun(p));
      remove();
    }
  }
  public void render(Renderer r) {
    if (_flickering) {
      if (_flicker) r.render(4, (int)x, (int)y, 0, frame);
    } else r.render(4, (int)x, (int)y, 0, frame);
  }
}

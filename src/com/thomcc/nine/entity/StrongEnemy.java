package com.thomcc.nine.entity;

import com.thomcc.nine.render.Art;
import com.thomcc.nine.render.Renderer;

public class StrongEnemy extends Enemy {
  private Gun _gun;
  
  
  
  public StrongEnemy() {
    super();
    _spriteIndex = Art.SENEMY_INDEX;
    health = 5;
    vision = 200;
    _gun = new Gun(this);
    _gun.setAmmoRegenRate(1);
  }

  protected void attackPoint(double x, double y, long ticks) {
    dir = Math.atan2(y-this.y, x-this.x);
    if (ticks % 5 == 0) _gun.fire();
    
  }
  
  public void render(Renderer r) {
    r.render(_spriteIndex, (int)x, (int)y, getDirection(), (health > 2) ? 0 : 1); 
  }
  public int getColor() { return (health > 2) ? 0xffeeeeee : 0xfffa0021; }
  
  
}

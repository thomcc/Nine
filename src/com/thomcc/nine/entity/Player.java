package com.thomcc.nine.entity;


import com.thomcc.nine.*;
import com.thomcc.nine.level.Level;
import com.thomcc.nine.render.Renderer;

public class Player extends Mobile {
  public Input _i;
  private int _maxHealth;

  private Gun _gun;
  
  public Player(Input i, Game g) {
    _i = i;
    _maxHealth = 20;
    health = _maxHealth;
    _collisionFriction = 0.3;
    _friction = 0.9;
    _maxSpeed = 4.0;
    _gun = new Gun(this);
  }
  
  public void setLevel(Level l) {
    super.setLevel(l);
    l.findPlayerLocation(this);
  }
  
  private void updateStats(long ticks) {}
  public void tick(long ticks) {
    updateStats(ticks);    
    
    if (_i.down) _py += 1.0;
    if (_i.right) _px += 1.0;
    if (_i.left) _px -= 1.0;
    if (_i.up) _py -= 1.0;
    
    _gun.tick(firing(), ticks);
    
    super.tick(ticks);
  }
  public void fire() {
  }
  public void heal(int n) { 
    if (health < _maxHealth) {
      health += n;
      if (health > _maxHealth) {
        health = _maxHealth;
      }
    }
  }
  public void die() {
    Sound.playerDeath.play();
    super.die();
  }
  public void hurt(Entity cause, int damage, double dir) {
    Sound.hurt.play();
    super.hurt(cause, damage, dir);
  }
  
  public void render(Renderer r) { r.render(0, (int)x, (int)y, getDirection()); }
  public int getFireCount() { return _gun.getAmmo(); }
  private boolean firing() { return _i.fire || _i.mouseDown; }
  public boolean appearsOnMinimap() { return true; }
  public void setGun(Gun g) { _gun = g; }
  public int getColor() { return 0xffff6249; }
  protected void touched(Entity e) { if (e instanceof Item) ((Item) e).apply(this); }
}

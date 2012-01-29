package com.thomcc.nine.entity;


import com.thomcc.nine.*;
import com.thomcc.nine.level.ILevel;
import com.thomcc.nine.render.Renderer;

public class Player extends Mobile {
  public Input _i;
  private int _maxHealth;

  private Gun _gun;
  public int lives;
  public int deadcounter = 0;
  public int invulncounter = 0;
  public Player(Input i, Game g) {
    _i = i;
    lives = 3;
    _maxHealth = 20;
    health = _maxHealth;
    _collisionFriction = 0.3;
    _friction = 0.9;
    _maxSpeed = 4.0;
    _gun = new Gun(this);
  }
  public void setLevel(ILevel l) {
    super.setLevel(l);
    l.findPlayerLocation(this);
  }
  
  private void updateStats(long ticks) {}
  public void tick(long ticks) {
    if (deadcounter == 0) {
      updateStats(ticks);    
    
      if (_i.down) _py += 1.0;
      if (_i.right) _px += 1.0;
      if (_i.left) _px -= 1.0;
      if (_i.up) _py -= 1.0;
    
      _gun.tick(firing(), ticks);
      if (invulncounter > 0) --invulncounter;  
      super.tick(ticks);
    } else {
      if (--deadcounter == 0) {
        invulncounter = 60;
        health = _maxHealth;
      }
    }
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
    deadcounter = 20;
    --lives;
    if (lives == 0) super.die();
  }
  public void hurt(Entity cause, int damage, double dir) {
    if (invulncounter == 0) {
      Sound.hurt.play();
      super.hurt(cause, damage, dir);
    }
  }
  
  public void render(Renderer r) { 
    if (invulncounter == 0 || invulncounter % 5 == 0)
      r.render(0, (int)x, (int)y, getDirection()); 
  }
  public int getFireCount() { return _gun.getAmmo(); }
  private boolean firing() { return _i.fire || _i.mouseDown; }
  public boolean appearsOnMinimap() { return true; }
  public void setGun(Gun g) { _gun = g; }
  public int getColor() { return 0xffff6249; }
  protected void touched(Entity e) { if (e instanceof Item) ((Item) e).apply(this); }
}

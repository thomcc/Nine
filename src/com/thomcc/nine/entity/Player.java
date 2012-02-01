package com.thomcc.nine.entity;


import com.thomcc.nine.*;
import com.thomcc.nine.entity.item.Item;
import com.thomcc.nine.level.Level;
import com.thomcc.nine.render.Renderer;

public class Player extends Mobile {
  public Input _i;
  private int _maxHealth;
  public int shotsFired = 0;
  private Gun _gun;
  
  public int lives;
  // after the player dies there's a brief delay before they 
  // respawn.  deadcounter represents this number
  private int deadcounter = 0;
  // once they respawn they can't be damaged for invulncounter more ticks
  private int invulncounter = 0;
  
  public Player(Input i, Game g) {
    _i = i;
    lives = 3;
    _maxHealth = 20;
    health = _maxHealth;
    _collisionFriction = 0.3;
    _friction = 0.9;
    size = 12;
    _maxSpeed = 4.0;
    _gun = new Gun(this);
  }
  
  public void setLevel(Level l) {
    super.setLevel(l);
    l.findAndSetLocation(this);
  }
  
  private void updateStats(long ticks) {}
  public void tick(long ticks) {
    if (deadcounter == 0) {
      updateStats(ticks);    
      // update our momentum/speed/whatever i'm calling _px and _py today
      // to be in accordance with the user's demands
      if (_i.down) _py += 1.0;
      if (_i.right) _px += 1.0;
      if (_i.left) _px -= 1.0;
      if (_i.up) _py -= 1.0;
      // also shoot maybe.
      _gun.tick(firing(), ticks);
      //decrement the invulnerability counter
      if (invulncounter > 0) --invulncounter;
      // and then let the mobile or entity motion code take care of actually
      // moving and whatnot
      super.tick(ticks);
    } else {
      if (--deadcounter == 0) {
        // i'm invincibleeee!
        invulncounter = 60;
        // after respawning set health to max, and lose any upgrades
        health = _maxHealth;
        _gun = new Gun(this);
      }
    }
  }
  
  public void didShoot() {
    _level.play(Sound.shoot);
    ++shotsFired;
  }
  // duh.
  public void heal(int n) { 
    if (health < _maxHealth) {
      health += n;
      if (health > _maxHealth) {
        health = _maxHealth;
      }
    }
  }
  
  // make the explosion sound, and decrement lives (and setup the 
  // die/respawn/invulnerable stuff).  if we're out of lives then _really_ die.
  public void die() {
    _level.play(Sound.playerDeath);
    deadcounter = 20;
    _level.playerDied();
    --lives;
    if (lives == 0) super.die();
  }
  
  // take damage, play the sound (unless we're invulnerable)
  public void hurt(Entity cause, int damage, double dir) {
    if (invulncounter == 0) {
      _level.play(Sound.hurt);
      super.hurt(cause, damage, dir);
    }
  }
  
  public void render(Renderer r) {
    // if they're not blinking, or they are and its one of the times we
    // want to draw them, ... do that.
    if (invulncounter == 0 || invulncounter % 3 == 0)
      r.render(0, (int)x, (int)y, getDirection()); 
  }
  public int getFireCount() { return _gun.getAmmo(); }
  private boolean firing() { return _i.fire || _i.mouseDown; }
  public boolean appearsOnMinimap() { return true; }
  public void setGun(Gun g) { _gun = g; }
  public int getColor() { return 0xffff6249; }
  public Gun getGun() { return _gun; }
  protected void touched(Entity e) { if (e instanceof Item) ((Item) e).apply(this); }
}

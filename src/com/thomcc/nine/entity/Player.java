package com.thomcc.nine.entity;


import com.thomcc.nine.*;
import com.thomcc.nine.level.Level;
import com.thomcc.nine.render.Renderer;

public class Player extends Mobile {
  public Input _i;
  private int _maxHealth;
  private int _ammoCount, _maxFireCount, _reAmmoRate;
  private int _fireRate;
  private boolean _canFire = true;
  private boolean _fireNotClicked = true;
  public Player(Input i, Game g) {
    _i = i;
    _maxHealth = 50;
    health = _maxHealth;
    _collisionFriction = 0.3;
    _friction = 0.9;
    _maxSpeed = 4.0;
    _maxFireCount = 20;
    _reAmmoRate = 20;
    _ammoCount = _maxFireCount;
    _fireRate = 10;
  }
  
  public void setLevel(Level l) {
    super.setLevel(l);
    l.findPlayerLocation(this);
  }
  
  private void updateStats(long ticks) { // if this gets above 5 i should write a "Stat" class
    if (ticks % _reAmmoRate == 0 && _ammoCount < _maxFireCount && !firing()) { ++_ammoCount; }
    if (ticks % _fireRate == 0 || _fireNotClicked) { _canFire = true; }
  }
  public void tick(long ticks) {
    _canFire = false;
    updateStats(ticks);    
    
    if (_i.down) _py += 1.0;
    if (_i.right) _px += 1.0;
    if (_i.left) _px -= 1.0;
    if (_i.up) _py -= 1.0;
    if (firing() && _canFire) fire();
    if (!firing()) {
      _fireNotClicked = true;
    } else {
      _fireNotClicked = false;
    }
    
    //if (_i.mod_shift) hurt(this, 5, 0); 
    
    super.tick(ticks);
  }
  public void fire() {
    if (_ammoCount == 0) return;
    Sound.shoot.play();
    _level.add(new Bullet(this, dir, 6));
    _ammoCount--;
  }
  public void heal(int n) { 
    if (health < _maxHealth) {
      health += n;
      if (health > _maxHealth) {
        health = _maxHealth;
      }
      Sound.getHealth.play();
    }
  }
  public void hurt(Entity cause, int damage, double dir) {
    Sound.hurt.play();
    super.hurt(cause, damage, dir);
  }
  public void render(Renderer r) { r.render(0, (int)x, (int)y, getDirection()); }
  public int getFireCount() { return _ammoCount; }
  private boolean firing() { return _i.fire || _i.mouseDown; }
  public boolean appearsOnMinimap() { return true; }
  
  public int getColor() { return 0xffff6249; }
  protected void touched(Entity e) {
    if (e instanceof Item) ((Item) e).apply(this);
  }
}

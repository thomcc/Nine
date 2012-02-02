package com.thomcc.nine.entity;

import com.thomcc.nine.render.Art;

public class Gun {
  
  protected Mobile _owner;
  public boolean canFire = false;
  protected int _fireRate;
  protected boolean _canRegenAmmo;
  protected int _maxAmmo;
  protected int _ammo;
  protected int _ammoRegenRate;
  protected int _bulletSpeed;
  protected boolean _fireNotClicked = true;
  protected int _bulletSpriteIndex;
  public Gun(Mobile m) {
    _owner = m;
    _fireRate = 15;
    _canRegenAmmo = true;
    _maxAmmo = 10;
    _ammo = _maxAmmo;
    _ammoRegenRate = 30;
    _bulletSpeed = 6;
    _bulletSpriteIndex = Art.BULLET_INDEX;
  }
  
  public void tick(boolean firing, long ticks) {
    
    canFire = fireIsPossible(ticks);
    if (firing) {
      tryFire();
      _fireNotClicked = false;
    }
    else _fireNotClicked = true;
    if (_canRegenAmmo && 
        _ammo < _maxAmmo && 
        (ticks % _ammoRegenRate) == 0) ++_ammo;
  }
  
  public boolean fireIsPossible(long ticks) {
    return _fireNotClicked || (ticks % _fireRate) == 0;
  }
  
  public boolean tryFire() {
    if (_ammo == 0) return false;
    if (canFire) {
      --_ammo;
      fire();
      _owner.didShoot();
      
      return true;
    } else return false;
  }
  
  public void fire() {
    Bullet b = new Bullet(_owner, _owner.dir, _bulletSpeed);
    b.setSpriteIndex(_bulletSpriteIndex);
    _owner._level.add(new Bullet(_owner, _owner.dir, _bulletSpeed));
  }
  
  public void setAmmoRegenRate(int rr) { 
    if (rr <= 0) {
      _canRegenAmmo = false;
      _ammoRegenRate = rr;
    } else {
      _canRegenAmmo = true;
      _ammoRegenRate = rr;
    }
  }
  public void setBulletSpriteIndex(int si) { _bulletSpriteIndex = si; }
  public void replenishAmmo() { _ammo = _maxAmmo; }
  public void setBulletSpeed(int s) { _bulletSpeed = s; }
  public void setFireRate(int r) { _fireRate = r; }
  public String getAmmoString() { return "Ammo: "+_ammo; }
  public void setMaxAmmo(int max) { _maxAmmo = max; } 
  public int getAmmo() { return _ammo; }
}

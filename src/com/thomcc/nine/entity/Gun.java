package com.thomcc.nine.entity;

import com.thomcc.nine.Sound;

public class Gun {
  
  protected Player _player;
  public boolean canFire = false;
  protected int _fireRate;
  protected boolean _canRegenAmmo;
  protected int _maxAmmo;
  protected int _ammo;
  protected int _ammoRegenRate;
  protected int _bulletSpeed;
  protected boolean _fireNotClicked = true;
  public Gun(Player p) {
    _player = p;
    _fireRate = 10;
    _canRegenAmmo = true;
    _maxAmmo = 10;
    _ammo = _maxAmmo;
    _ammoRegenRate = 30;
    _bulletSpeed = 6;
  }
  
  public void tick(boolean firing, long ticks) {
    
    canFire = fireIsPossible(ticks);
    if (firing) {
      tryFire();
      _fireNotClicked = false;
    }
    else _fireNotClicked = true;
    if (_canRegenAmmo && _ammo < _maxAmmo && (ticks % _ammoRegenRate) == 0) ++_ammo;
  }
  
  public boolean fireIsPossible(long ticks) {
    return _fireNotClicked || (ticks % _fireRate) == 0;
  }
  
  public boolean tryFire() {
    if (_ammo == 0) return false;
    if (canFire) {
      Sound.shoot.play();
      fire();
      --_ammo;
      return true;
    } else return false;
  }
  
  public void fire() {
    _player._level.add(new Bullet(_player, _player.dir, _bulletSpeed));
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
  public void setBulletSpeed(int s) { _bulletSpeed = s; }
  public void setFireRate(int r) { _fireRate = r; }
  public String getAmmoString() { return "Ammo: "+_ammo; }
  public void setMaxAmmo(int max) { _maxAmmo = max; } 
  public int getAmmo() { return _ammo; }
}

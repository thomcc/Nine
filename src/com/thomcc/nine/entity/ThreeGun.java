package com.thomcc.nine.entity;

public class ThreeGun extends Gun {
  private int _uses;
  private boolean infinite = false;
  public ThreeGun(Player p) { 
    super(p);
    _uses = 15;
  }
  public void infiniteUses() {
    infinite = true;
  }
  public void fire() {
    double dir = _owner.dir;
    double dir2 = dir+Math.PI/6;
    double dir3 = dir-Math.PI/6;
    _owner._level.add(new Bullet(_owner, dir, _bulletSpeed));
    _owner._level.add(new Bullet(_owner, dir2, _bulletSpeed));
    _owner._level.add(new Bullet(_owner, dir3, _bulletSpeed));
    if (!infinite && --_uses == 0) _owner.setGun(new Gun(_owner));
  }
  
  
  public void addUses(int u) { _uses += u; }
  public String getAmmoString() {
    String s = super.getAmmoString();
    if (!infinite) return s + ", special uses: "+_uses;
    else return s;
  }
}

package com.thomcc.nine.entity;

public class ThreeGun extends Gun {
  private int _uses;
  public ThreeGun(Player p) { 
    super(p);
    _uses = 15;
  }
  
  public void fire() {
    double dir = _player.dir;
    double dir2 = dir+Math.PI/6;
    double dir3 = dir-Math.PI/6;
    _player._level.add(new Bullet(_player, dir, _bulletSpeed));
    _player._level.add(new Bullet(_player, dir2, _bulletSpeed));
    _player._level.add(new Bullet(_player, dir3, _bulletSpeed));
    if (--_uses == 0) _player.setGun(new Gun(_player));
  }
  
  
  public void addUses(int u) { _uses += u; }
  public String getAmmoString() {
    String s = super.getAmmoString();
    return s + ", special uses: "+_uses;
  }
}

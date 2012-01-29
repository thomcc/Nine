package com.thomcc.nine.entity;

public class ThreeGun extends Gun {
  public ThreeGun(Player p) { super(p); }
  public void fire() {
    double dir = _player.dir;
    double dir2 = dir+Math.PI/6;
    double dir3 = dir-Math.PI/6;
    _player._level.add(new Bullet(_player, dir, _bulletSpeed));
    _player._level.add(new Bullet(_player, dir2, _bulletSpeed));
    _player._level.add(new Bullet(_player, dir3, _bulletSpeed));
  }
}

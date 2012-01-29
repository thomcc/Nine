package com.thomcc.nine.entity;

public class ThreeGunItem extends Item {
  public ThreeGunItem(int x, int y) { super(x, y); spriteIndex = 4; }
  protected void onPlayerContact(Player p) { p.setGun(new ThreeGun(p)); }
}

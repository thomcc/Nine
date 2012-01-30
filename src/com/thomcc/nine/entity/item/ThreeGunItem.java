package com.thomcc.nine.entity.item;

import com.thomcc.nine.entity.Player;
import com.thomcc.nine.entity.ThreeGun;

public class ThreeGunItem extends Item {
  public ThreeGunItem(int x, int y) { super(x, y); spriteIndex = 4; }
  protected void onPlayerContact(Player p) { p.setGun(new ThreeGun(p)); }
}

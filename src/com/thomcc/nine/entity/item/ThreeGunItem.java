package com.thomcc.nine.entity.item;

import com.thomcc.nine.entity.Gun;
import com.thomcc.nine.entity.Player;
import com.thomcc.nine.entity.ThreeGun;
import com.thomcc.nine.render.Art;

public class ThreeGunItem extends Item {
  public ThreeGunItem(int x, int y) { super(x, y); spriteIndex = Art.GUN3_INDEX; }
  protected void onPlayerContact(Player p) {
    Gun g = p.getGun();
    if (g instanceof ThreeGun) ((ThreeGun)g).addUses(15);
    else p.setGun(new ThreeGun(p)); 
  }
}

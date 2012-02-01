package com.thomcc.nine.entity.item;

import com.thomcc.nine.entity.Player;
import com.thomcc.nine.render.Art;

public class HealthPackItem extends Item {

  public HealthPackItem(int x, int y) {
    super(x, y);
    _animDelay = 10;
    spriteIndex = Art.HEALTHPACK_INDEX;
  }

  protected void onPlayerContact(Player p) { p.heal(2+random.nextInt(3)); }
}

package com.thomcc.nine.entity.item;

import com.thomcc.nine.entity.Player;
import com.thomcc.nine.render.Art;

public class SuperItem extends Item {
  public SuperItem(int x, int y) {
    super(x, y);
    _animDelay = 10;
    spriteIndex = Art.SUPER_INDEX;
  }

  protected void onPlayerContact(Player p) { p.superStart(); }

}

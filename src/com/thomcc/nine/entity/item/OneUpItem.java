package com.thomcc.nine.entity.item;

import com.thomcc.nine.entity.Player;

public class OneUpItem extends Item {
  public OneUpItem(int x, int y) { super(x, y); spriteIndex = 5; }
  protected void onPlayerContact(Player p) { ++p.lives; }
}

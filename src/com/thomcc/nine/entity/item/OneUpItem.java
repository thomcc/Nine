package com.thomcc.nine.entity.item;

import com.thomcc.nine.entity.Player;
import com.thomcc.nine.render.Art;

public class OneUpItem extends Item {
  public OneUpItem(int x, int y) { super(x, y); spriteIndex = Art.ONEUP_INDEX; }
  protected void onPlayerContact(Player p) { ++p.lives; }
}

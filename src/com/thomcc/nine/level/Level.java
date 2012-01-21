package com.thomcc.nine.level;

import com.thomcc.nine.Player;

public interface Level {
  public boolean inBounds(int x, int y);
  public boolean blocks(int tx, int ty);
  public void findPlayerLocation(Player p);
  public void addPlayer(Player p);
  public int getWidth();
  public int getHeight();


}

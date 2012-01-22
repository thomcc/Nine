package com.thomcc.nine.level;

import com.thomcc.nine.Player;
import com.thomcc.nine.render.Renderer;

public interface Level {
  public boolean blocks(int tx, int ty);
  public void findPlayerLocation(Player p);
  public void addPlayer(Player p);
  public int getWidth();
  public int getHeight();
  public void render(Renderer r);
  public int[][] getMinimap(int w, int h);
}

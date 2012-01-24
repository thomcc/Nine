package com.thomcc.nine.level;

import java.util.ArrayList;

import com.thomcc.nine.entity.Entity;
import com.thomcc.nine.entity.Player;
import com.thomcc.nine.render.Renderer;

public interface Level {
  public boolean blocks(int tx, int ty);
  public void add(Entity e);
  public void remove(Entity e);
  public int getWidth();
  public int getHeight();
  public void render(Renderer r);
  public ArrayList<Entity> getEntities(int x0, int y0, int x1, int y1);
  public void findPlayerLocation(Player p);
  public int[][] getMinimap(int w, int h);
  public void tick(long ticks);
  public Player getPlayer();
}

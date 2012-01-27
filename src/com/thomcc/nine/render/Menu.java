package com.thomcc.nine.render;

import com.thomcc.nine.Game;
import com.thomcc.nine.Input;

public class Menu {
  public Input i;
  public Game g;
  
  protected int selected = 0;
//  private int _lastMX = -1, _lastMY = -1;
  protected String title = "";
  protected String[] options;// = { "Start", "How to play" };
  protected boolean _hit = false;
  public void init(Game game, Input input) {
    i = input;
    g = game;
  }
  
  public void tick() {
    if (i.up && !_hit) --selected;
    if (i.down && !_hit) ++selected;
    int len = options.length;
    
    _hit = i.up || i.down;
    
    if (selected < 0) selected += len;
    if (selected >= len) selected -= len;
    
    if (i.select) {
      onSelect(selected);
    }
  }
  protected void onSelect(int which) {}
  
  public void render(Renderer r) {
    r.clear();
    renderTitle(r);
    renderMenuItems(r);
  }
  protected void renderTitle(Renderer r) {
    int w = r.getViewportWidth();
    int sw = title.length()*Renderer.CHAR_WIDTH;
    r.renderString(title, (w-sw)/2, 50);
  }
  
  protected void renderMenuItems(Renderer r) {
    for (int i = 0; i < options.length; ++i) {
      String s = options[i];
      String d;
      if (selected == i) {
        d = "-> "+s;
      } else {
        d = "   " + s;
      }
      r.renderString(d, 90, 90+i*24);
    }
  }
  
}

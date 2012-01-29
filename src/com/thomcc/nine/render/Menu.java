package com.thomcc.nine.render;

import com.thomcc.nine.Game;
import com.thomcc.nine.Input;

public class Menu {
  public Input input;
  public Game g;
  protected String title = "";
  private boolean wait = false;
  protected int menuItemActive = 0xffffff;
  protected int menuItemInactive = 0x888888;
  protected MenuItem[] items = new MenuItem[] {
      new MenuItem("nothing", 90, 90)
  };
  protected boolean _hit = false;
  public void init(Game game, Input input) {
    this.input = input;
    if (input.mouseDown) wait = true;
    else wait = false;
    g = game;
  }
  
  public void tick() {
    if (input.mouseDown && !wait) {
      int chosen = -1;
      for (int item = 0; item < items.length && chosen == -1; ++item)
        if (items[item].contains(input.mouseX, input.mouseY))
          chosen = item;
      
      if (chosen >= 0) onSelect(chosen); 
    } else if (!input.mouseDown && wait) {
      wait = false;
    }
  }
  protected void onSelect(int which) {}
  protected void renderFrame(Renderer r) {
    r.clear();
  }
  public void render(Renderer r) {
    r.centerAround(this);
    renderFrame(r);
    renderTitle(r);
    renderContent(r);
    renderMenuItems(r);
  }
  protected void renderContent(Renderer r) {}

  protected void renderTitle(Renderer r) {
    int w = r.getViewportWidth();
    int sw = title.length()*Renderer.CHAR_WIDTH;
    r.renderString(title, (w-sw)/2, 50);
  }
  protected void renderMenuItems(Renderer r) {
    for (int i = 0; i < items.length; ++i) {
      items[i].render(r, items[i].contains(input.mouseX, input.mouseY) ? menuItemActive : menuItemInactive);
    }
  }
}

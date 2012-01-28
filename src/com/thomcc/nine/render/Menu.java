package com.thomcc.nine.render;

import com.thomcc.nine.Game;
import com.thomcc.nine.Input;

public class Menu {
  public Input input;
  public Game g;
  protected String title = "";
  protected MenuItem[] items = new MenuItem[] {
      new MenuItem("nothing", 90, 90)
  };
  protected boolean _hit = false;
  public void init(Game game, Input input) {
    this.input = input;
    g = game;
  }
  
  public void tick() {
    if (input.mouseDown) {
      int chosen = -1;
      for (int item = 0; item < items.length && chosen == -1; ++item)
        if (items[item].contains(input.mouseX, input.mouseY))
          chosen = item;
      
      if (chosen >= 0) onSelect(chosen); 
    }
  }
  protected void onSelect(int which) {}
  protected void renderFrame(Renderer r) {
    r.clear();
  }
  public void render(Renderer r) {
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
      items[i].render(r, items[i].contains(input.mouseX, input.mouseY) ? 0xffffff : 0x888888);
    }
  }
}

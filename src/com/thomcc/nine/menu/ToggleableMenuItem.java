package com.thomcc.nine.menu;

import com.thomcc.nine.render.Renderer;

public class ToggleableMenuItem extends MenuItem {
  public boolean state = false;
  public ToggleableMenuItem(String title, int x, int y) {
    super(title, x, y);
    width += 4*Renderer.CHAR_WIDTH;
  }
  
  public void click() {
    this.state = !this.state;
  }
  
  public void render(Renderer r, int color) { 
    r.renderString("["+(state ? "x" : " ")+"] "+title, x, y, color); 
  }
}

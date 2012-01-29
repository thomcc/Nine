package com.thomcc.nine.render;

public class MenuItem {
  public final String title;
  public int width, height;
  public final int x, y;
  public MenuItem(String title, int x, int y) {
    this.title = title;
    this.x = x;
    this.y = y;
    this.height = Renderer.CHAR_HEIGHT;
    this.width = Renderer.CHAR_WIDTH*title.length();
  }
  public boolean contains(int mx, int my) {
    return mx >= x && mx < width+x && my >= y && my < height+y;
  }  
  
  public void render(Renderer r, int color) {
    r.renderString(title, x, y, color);
  }
  public void click() {}
}

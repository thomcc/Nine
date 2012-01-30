package com.thomcc.nine.menu;

import com.thomcc.nine.Nine;
import com.thomcc.nine.render.Renderer;

public class TitleMenu extends Menu {
  public TitleMenu() {
    items = new MenuItem[] { 
        new MenuItem("New Game", 90, 90),
        new MenuItem("How to play", 90, 90+24),
        new MenuItem("Credits", 90, 90+48)
    };
  }
  protected void renderTitle(Renderer r) {
    r.render9((Nine.WIDTH-17)/2, 15);
  }
  protected void onSelect(int which) {
    switch (which) {
    case 0: g.setMenu(null); g.start(); break;
    case 1: g.setMenu(new HelpMenu()); break;
    case 2: g.setMenu(new EgoMenu()); break;
    }
  }
  
  
}

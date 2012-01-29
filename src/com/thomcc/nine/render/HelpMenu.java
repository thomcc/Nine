package com.thomcc.nine.render;

import com.thomcc.nine.Nine;

public class HelpMenu extends Menu {
  private int offset = 30;
  public HelpMenu() {
    items = new MenuItem[] {
        new MenuItem("<- Go Back", Nine.WIDTH-120, Nine.HEIGHT-24)
    };
  }
  private int ticks;
  private int rotation = 0;
  public void tick() {
    if (++ticks % 40 == 0) rotation = (1+rotation) % 16;
    super.tick();
  }
  protected void renderContent(Renderer r) {
    String[] strs = new String[] {
        "9 is a game which takes place in space.",
        "Controls are simple: ",
        " - WASD/Arrow keys/Numpad for motion",
        " - Aim with the mouse",
        " - Shoot by clicking (or pressing the space bar)",
        " ",
        "You fly around in your space ship:",
        "",
        "And shoot enemies which look like this:",
        "",
        "Try to pick up items: ", "",
        "And beware, your own bullets can hurt you!"
    };
    
    int x = offset;
    int y = offset;
    boolean renderedPlayer = false;
    boolean renderedEnemy = false;
    for (int i = 0; i < strs.length; ++i) {
      String s = strs[i];
      if (s == "") {
        if (!renderedPlayer) {
          r.render(0, x + Nine.WIDTH/2-80, y+12, rotation);
          y += 24;
          renderedPlayer = true;
        } else if (!renderedEnemy) {
          r.render(2, x+Nine.WIDTH/2-80, y+12, 15-rotation);
          renderedEnemy = true;
          y += 24;
        } else {
          int tempx = x+strs[i-1].length()*Renderer.CHAR_WIDTH;
          int tempy = y-12+6;
          r.render(3, tempx, tempy, 0);
          tempx += 7;
          r.render(4, tempx, tempy, 0);
        }

      } else {
        r.renderString(s, x, y);
        y += 12;
      }
    }
  }
  
  protected void onSelect(int which) {
    switch(which) {
    case 0: g.setMenu(new TitleMenu()); break;
    }
  }
  
}

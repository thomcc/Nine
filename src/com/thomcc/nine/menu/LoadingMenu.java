package com.thomcc.nine.menu;

import com.thomcc.nine.Nine;
import com.thomcc.nine.render.Renderer;

public class LoadingMenu extends Menu {
  public void render(Renderer r) {
    r.renderString("Loading...", (Nine.WIDTH-60)/2, (Nine.HEIGHT-12)/2);
  }
}

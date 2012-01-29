package com.thomcc.nine.render;

import com.thomcc.nine.Nine;

public class LoadingMenu extends Menu {
  public void render(Renderer r) {
    r.renderString("Loading...", (Nine.WIDTH-60)/2, (Nine.HEIGHT-12)/2);
  }
}

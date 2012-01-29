package com.thomcc.nine.render;

import java.awt.Color;

import com.thomcc.nine.Nine;

public class PauseMenu extends Menu {
  public static final Color bgColor = new Color(0x88000000, true);
  private int x;
  public PauseMenu() {
    title = "Paused!";
    x = (Nine.WIDTH - (13*Renderer.CHAR_WIDTH+40))/2;
    items = new MenuItem[] {
        new MenuItem("Back to game", x+20, 90),
        new MenuItem("Settings", x+20, 90+24),
        new MenuItem("Quit to title", x+20, 90+48)
    };
  }
  protected void renderFrame(Renderer r) {
    r.fill(bgColor);
    r.drawMenuBox(x, 70, 13*Renderer.CHAR_WIDTH+40,48+50);
  }
  protected void renderContent(Renderer r) {}
  
  protected void renderTitle(Renderer r) {
    int w = r.getViewportWidth();
    int sw = title.length()*Renderer.CHAR_WIDTH;
    r.renderString(title, (w-sw)/2, 50);
  }
  
  protected void onSelect(int which) {
    switch(which) {
    case 0: g.unPause(); break;
    case 1: g.setMenu(new SettingsMenu()); break;
    case 2: g.lose(); g.setMenu(new TitleMenu()); break;
    }
  }
}

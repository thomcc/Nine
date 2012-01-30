package com.thomcc.nine.menu;

import java.awt.Color;

import com.thomcc.nine.Nine;
import com.thomcc.nine.render.Renderer;

public class PauseMenu extends Menu {
  public static final Color bgColor = new Color(0x88000000, true);
  protected int width, height;
  protected int titley;
  protected int padding;
  public PauseMenu() {
    title = "Paused!";
    
    padding = 20;
    
    width = 13*Renderer.CHAR_WIDTH;
    height = 4*Renderer.CHAR_HEIGHT;
    titley = 50;
    int xx = getTextX();//(Nine.WIDTH-width)/2 + padding;
    int yy = getTextY();//((Nine.HEIGHT-height)+titley)/2+padding;
    items = new MenuItem[] {
        new MenuItem("Back to game", xx, yy),
        new MenuItem("Settings", xx, yy+2*Renderer.CHAR_HEIGHT),
        new MenuItem("Quit to title", xx, yy+4*Renderer.CHAR_HEIGHT)
    };
  }
  protected void renderFrame(Renderer r) {
    r.fill(bgColor);
    int h = getFrameH();
    int w = getFrameW();
    int xx = (Nine.WIDTH-w)/2;
    int yy = ((Nine.HEIGHT-h)+titley)/2;
    r.drawMenuBox(xx, yy, w, h);
  }
  protected int getFrameH() { return height+5*padding/2; }
  protected int getFrameW() { return width+padding*2; }
  protected int getTextX() {
    return (Nine.WIDTH-getFrameW())/2+padding;
  }
  protected int getTextY() {
    return ((Nine.HEIGHT-getFrameH())+titley)/2+padding;
  }
  protected void renderContent(Renderer r) {}
  
  protected void renderTitle(Renderer r) {
    int w = r.getViewportWidth();
    int sw = title.length()*Renderer.CHAR_WIDTH;
    r.renderString(title, (w-sw)/2, titley);
  }
  
  protected void onSelect(int which) {
    switch(which) {
    case 0: g.unPause(); break;
    case 1: g.setMenu(new SettingsMenu()); break;
    case 2: g.lose(); g.setMenu(new TitleMenu()); break;
    }
  }
}

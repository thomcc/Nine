package com.thomcc.nine.menu;

import com.thomcc.nine.Nine;
import com.thomcc.nine.render.Renderer;

public class GameOverMenu extends Menu {
  public GameOverMenu() {
    title = "Game Over!";
    items = new MenuItem[] { 
        new MenuItem("New Game", 90, 90),
        new MenuItem("Back to main menu.", 90, 90+24)
    };  
  }
  protected void renderContent(Renderer r) {
    String s = "Final Score: "+g.score;
    int w = s.length()*Renderer.CHAR_WIDTH;
    r.renderString(s, (Nine.WIDTH-(w+40))/2+20, 75);
  }
  protected void onSelect(int which) {
    switch(which) {
    case 0: g.setMenu(null); g.start(); break;
    case 1: g.setMenu(new TitleMenu()); break;
    }
  }

}

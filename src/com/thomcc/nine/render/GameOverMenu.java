package com.thomcc.nine.render;

public class GameOverMenu extends Menu {
  public GameOverMenu() {
    title = "You have failed.";
    items = new MenuItem[] { 
        new MenuItem("retry", 90, 90),
        new MenuItem("give up", 90, 90+24)
    };
  }
  protected void onSelect(int which) {
    switch(which) {
    case 0: g.setMenu(null); g.start(); break;
    case 1: System.exit(0);
    }
  }
}

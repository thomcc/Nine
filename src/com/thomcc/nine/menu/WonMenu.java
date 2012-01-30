package com.thomcc.nine.menu;

public class WonMenu extends Menu {
  public WonMenu() {
    title = "You have succeeded!";
    items = new MenuItem[] { 
        new MenuItem("New Game", 90, 90),
        new MenuItem("Back to main menu.", 90, 90+24)
    };  
  }
  protected void onSelect(int which) {
    switch(which) {
    case 0: g.setMenu(null); g.start(); break;
    case 1: g.setMenu(new TitleMenu()); break;
    }
  }
}

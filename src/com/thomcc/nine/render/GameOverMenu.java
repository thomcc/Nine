package com.thomcc.nine.render;

public class GameOverMenu extends Menu {
  public GameOverMenu() {
    title = "You have failed.";
    options = new String[] { "retry", "give up" };
  }
  protected void onSelect(int which) {
    switch(which) {
    case 0: g.setMenu(null); g.start(); break;
    case 1: System.exit(0);
    }
  }
}

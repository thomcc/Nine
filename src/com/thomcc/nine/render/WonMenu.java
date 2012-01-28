package com.thomcc.nine.render;

public class WonMenu extends Menu {
  public WonMenu() {
    title = "You have succeeded!";
    options = new String[] { "again!", "quit!" };
  }
  protected void onSelect(int which) {
    switch(which) {
    case 0: g.setMenu(null); g.start(); break;
    case 1: System.exit(0);
    }
  }
}

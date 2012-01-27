package com.thomcc.nine.render;

public class TitleMenu extends Menu {
  public TitleMenu() {
    options = new String[] {"New Game", "How to play"};
  }
  
  protected void onSelect(int which) {
    switch (which) {
    case 0: g.setMenu(null); g.start(); break;
    }
  }
  
  
}

package com.thomcc.nine.render;

import com.thomcc.nine.Game;
import com.thomcc.nine.Input;

public class Menu {
  public Input i;
  public Game g;
  
  private int _selected = 0;
//  private int _lastMX = -1, _lastMY = -1;
  
  private static final String[] options = { "Start", "How to play" };
  
  public void init(Game game, Input input) {
    i = input;
    g = game;
  }
  
  public void tick() {
    if (i.up) --_selected;
    if (i.down) ++_selected;
    int len = options.length;
    if (_selected < 0) _selected += len;
    if (_selected >= len) _selected -= len;
    
  }
  public void render(Renderer r) {
    
  }
  
}

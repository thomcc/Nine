package com.thomcc.nine.menu;

import java.util.ArrayList;

import com.thomcc.nine.Game;
import com.thomcc.nine.Input;
import com.thomcc.nine.Sound;
import com.thomcc.nine.render.Renderer;

public class Menu {
  public Input input;
  public Game g;
  protected String title = "";
  private boolean wait = false;
  protected int menuItemActive = 0xffffff;
  protected int menuItemInactive = 0x888888;
  protected MenuItem[] items = new MenuItem[0];
  protected boolean _hit = false;
  public void init(Game game, Input input) {
    this.input = input;
    if (input.mouseDown) wait = true;
    else wait = false;
    g = game;
  }
  protected boolean mouseDownLast = false;
  public void tick() {

    if (input.mouseDown && !wait) {
      int chosen = -1;
      for (int item = 0; item < items.length && chosen == -1; ++item)
        if (items[item].contains(input.mouseX, input.mouseY))
          chosen = item;
      clicked(chosen);
    } else if (!input.mouseDown && wait) {
      wait = false;
    }
    mouseDownLast = input.mouseDown;
  }
  protected void clicked(int chosen) {
    if (chosen >= 0) {
      items[chosen].click();
      if (items[chosen].disabled) {
        
        g.playSound(Sound.no);
      }
      else onSelect(chosen); 
    }
  }
  protected void onSelect(int which) {}
  protected void renderFrame(Renderer r) {
    r.clear();
  }
  public void render(Renderer r) {
    r.centerAround(this);
    renderFrame(r);
    renderTitle(r);
    renderContent(r);
    renderMenuItems(r);
  }
  protected void renderContent(Renderer r) {}

  
  protected String[] splitup(String text, int cols) {
    ArrayList<StringBuilder> lines = new ArrayList<StringBuilder>();
    String[] words = text.split("[\\s]+");
    StringBuilder line = new StringBuilder();
    
    for (int pos = 0; pos < words.length; ++pos) {

      line.append(line.length() == 0 ? "" : " ");
      line.append(words[pos]);
      if (pos+1 == words.length) continue; 
      if (line.length()+words[pos+1].length()+1 > cols){
        lines.add(line);
        line = new StringBuilder();
      }
    }
    if (line.length() > 0) lines.add(line);
    
    String[] ss = new String[lines.size()];
    
    for (int i = 0; i < lines.size(); ++i)
      ss[i] = lines.get(i).toString();
    
    return ss;
  }
  protected void renderTitle(Renderer r) {
    int w = r.getViewportWidth();
    int sw = title.length()*Renderer.CHAR_WIDTH;
    r.renderString(title, (w-sw)/2, 50);
  }
  protected void renderMenuItems(Renderer r) {
    for (int i = 0; i < items.length; ++i) {
      int col;
      if(items[i].disabled) {
        if (items[i].contains(input.mouseX, input.mouseY)) {
          col = 0x880000;
        } else {
          col = 0xff4444 ;
        }
      } else {
        if (items[i].contains(input.mouseX, input.mouseY)) {
          col = menuItemActive;
        } else {
          col = menuItemInactive;
        }
      }
      items[i].render(r, col);
    }
  }
}

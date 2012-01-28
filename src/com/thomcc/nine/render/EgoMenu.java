package com.thomcc.nine.render;


public class EgoMenu extends HelpMenu {
  
  protected void renderContent(Renderer r) {
    String[] strs = {
        "Special thanks to everybody who I forced to",
        "play incomplete, buggy, half-broken versions of",
        "this game whenever you came online.  You were a",
        "tremendous help.", " ", " ",
        " ",
        "Art, sounds, code, and font by Thom Chiovoloni. ",
        "Everything is (c) 2012 Thom Chiovoloni, all rights",
        "reserved."
    };
    
    int x = 30;
    int y = 30;
    for (String s : strs) {
      r.renderString(s, x, y);
      y += 12;
    }
  }
  
}

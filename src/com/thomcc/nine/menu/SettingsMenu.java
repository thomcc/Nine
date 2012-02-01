package com.thomcc.nine.menu;

import java.awt.Color;

import com.thomcc.nine.Game;
import com.thomcc.nine.Input;
import com.thomcc.nine.Settings;
import com.thomcc.nine.render.Renderer;

public class SettingsMenu extends PauseMenu {
  public static final Color bgColor = new Color(0x88000000, true);
  private Settings s;
  public SettingsMenu() {
    title = "Settings!";
    padding = 20;
    
    width = 16*Renderer.CHAR_WIDTH;
    height = 4*Renderer.CHAR_HEIGHT;
    titley = 50;
    int xx = getTextX();//(Nine.WIDTH-templ_width)/2 + padding;
    int yy = getTextY();//((Nine.HEIGHT-templ_height)+titley)/2+padding;
    items = new MenuItem[] {
        new ToggleableMenuItem("Show Minimap", xx, yy),
        new ToggleableMenuItem("Play Sounds", xx, yy+2*Renderer.CHAR_HEIGHT),
        new MenuItem("Okay", xx, yy+4*Renderer.CHAR_HEIGHT)
    };
  }
  public void clicked(int chosen) {
    if (!mouseDownLast) {
      if (chosen >= 0) {
        items[chosen].click();
        onSelect(chosen); 
      }
    }
  }
  public void init(Game game, Input input) {
    super.init(game, input);
    this.s = game.settings;
    ((ToggleableMenuItem)items[0]).state = s.getShowMinimap();
    ((ToggleableMenuItem)items[1]).state = s.getPlaySounds();
  }
  protected void onSelect(int which) {
    switch(which) {
    case 0: s.toggleShowMinimap(); break;
    case 1: s.togglePlaySounds(); break;
    case 2: g.setMenu(new PauseMenu()); break;
    }
  }
}

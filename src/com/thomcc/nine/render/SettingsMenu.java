package com.thomcc.nine.render;

import java.awt.Color;

import com.thomcc.nine.Game;
import com.thomcc.nine.Input;
import com.thomcc.nine.Nine;
import com.thomcc.nine.Settings;

public class SettingsMenu extends PauseMenu {
  public static final Color bgColor = new Color(0x88000000, true);
  private int x;
  private Settings s;
  public SettingsMenu() {
    title = "Settings!";
    x = (Nine.WIDTH - (13*Renderer.CHAR_WIDTH+40))/2;
    items = new MenuItem[] {
        new ToggleableMenuItem("Show Minimap", x+20, 90),
        new ToggleableMenuItem("Play Sounds", x+20, 90+24),
        new MenuItem("Okay", x+20, 90+48)
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

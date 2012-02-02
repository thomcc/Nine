package com.thomcc.nine.menu;

import com.thomcc.nine.Game;
import com.thomcc.nine.Input;
import com.thomcc.nine.Sound;
import com.thomcc.nine.entity.Player;
import com.thomcc.nine.render.Renderer;

public class WonLevelMenu extends PauseMenu {
  public WonLevelMenu() {
    title = "Level Complete! Purchase Upgrades";
    this.width = 40*Renderer.CHAR_WIDTH;
    this.height = 8*Renderer.CHAR_HEIGHT;
    int x = getTextX();
    int y = getTextY();
    int ch = Renderer.CHAR_HEIGHT;
    this.items = new MenuItem[] {
        new MenuItem("More ammo!", x, y),
        new MenuItem("Ammo returns faster!", x, y+ch),
        new MenuItem("Shoot faster!", x, y+ch*2),
        new MenuItem("More health!", x, y+ch*3),
        new MenuItem("Faster ship!", x, y+ch*4),
        new MenuItem("Faster bullets!", x, y+ch*5),
        new MenuItem("More lives!", x, y+ch*6),
        new MenuItem("I fight for glory alone! (points)", x, y+ch*7)
    };
    
  }
  public void init(Game game, Input input) {
    super.init(game, input);
    game.playSound(Sound.winLevel);
    Player p = game.getPlayer();
    p.superStop();
    if (!p.canUpgradeMA()) items[0].disable();
    if (!p.canUpgradeARR()) items[1].disable();
    if (!p.canUpgradeFR()) items[2].disable();
    if (!p.canUpgradeH()) items[3].disable();
    if (!p.canUpgradeS()) items[4].disable();
    if (!p.canUpgradeBS()) items[5].disable();
  }
  protected void onSelect(int which) {
    Player p = g.getPlayer();
    switch(which) {
    case 0: 
      p.upgradeMA();
      break;
    case 1:
      p.upgradeARR();
      break;
    case 2:
      p.upgradeFR();
      break;
    case 3:
      p.upgradeH();
      break;
    case 4:
      p.upgradeS();
      break;
    case 5:
      p.upgradeBS();
      break;
    case 6: 
      p.lives += 3;
      break;
    case 7:
      p.score += 15000;
      break;
    default:
      return;
    }
    p.replenish();
    g.playSound(Sound.scoreUp);
    g.unPause();
    g.start(g.levelNumber+1);
  }
}

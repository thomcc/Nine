package com.thomcc.nine;

import com.thomcc.nine.entity.Player;
import com.thomcc.nine.level.*;


public class Game {
  private Player _player;
  private Level _level;
  private Input _ih;
  public int offX, offY;
  public static boolean fancyGraphics = true;
  private long _ticks;
  
  public Game(Input ih) {
    offX = 0;
    offY = 0;
    _ih = ih;
    _level = new ShipLevel();
    _player = new Player(_ih);
    _level.add(_player);
  }
  
  public void setOffset(int x, int y) { 
    offX = x; 
    offY = y;
  }
  public void tick() {
    ++_ticks;
    //_player.tick();
    _level.tick();
    _player.lookAt(_ih.mouseX+offX, _ih.mouseY+offY);
  }
  
  public Player getPlayer() { 
    return _player; 
  }
  
  public long getTicks() { 
    return _ticks; 
  }
  
  public void setPlayer(Player p) { 
    _player = p; 
  }
  
  public Level getLevel() { 
    return _level; 
  }
  
}

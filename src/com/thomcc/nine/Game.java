package com.thomcc.nine;

import com.thomcc.nine.entity.Player;
import com.thomcc.nine.level.*;


public class Game {
  private Player _player;
  private Level _level;
  private InputHandler _ih;
  public int offX, offY;
  public static boolean fancyGraphics = true;
  private long _ticks;
  
  public Game(InputHandler ih) {
    offX = 0;
    offY = 0;
    _ih = ih;
    _level = new ShipLevel();
    _player = new Player();
    _level.addPlayer(_player);
  }
  
  public void setOffset(int x, int y) { 
    offX = x; 
    offY = y; 
  }
  
  public void tick() {
    ++_ticks;
    _player.tick(_ih.up, _ih.down, _ih.left, _ih.right, _ih.mouseDown);
    int mx = _ih.mouseX;
    int my = _ih.mouseY;
    mx += offX;
    my += offY;
    _player.lookAt(mx, my);
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

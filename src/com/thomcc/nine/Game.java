package com.thomcc.nine;

import com.thomcc.nine.level.*;


public class Game {
  private Player _player;
  private Level _level;
  private InputHandler _ih;
  public int offX, offY;
  public int controlMode;
  public static final int CONTROL_MODE_KEYBOARD = 1;
  public static final int CONTROL_MODE_MOUSE = 2;
  private long _ticks;
  public Game(InputHandler ih) {
    offX = 0;
    offY = 0;
    _ih = ih;
    _level = new ShipLevel();
    controlMode = CONTROL_MODE_KEYBOARD;
    _player = new Player();
    _player.setControlMode(controlMode);
    _level.addPlayer(_player);
  }
  
  public void setOffset(int x, int y) { offX = x; offY = y; }
  public void tick() {
    ++_ticks;
    _player.tick(_ih.up, _ih.down, _ih.left, _ih.right);
    int mx = _ih.mouseX;
    int my = _ih.mouseY;
    mx += offX;
    my += offY;
    _player.lookAt(mx, my);
  }
  public void setControlMode(int cm) {
    controlMode = cm;
    _player.setControlMode(cm);
  }
  public Player getPlayer() { return _player; }
  public long getTicks() { return _ticks; }
  public void setPlayer(Player p) { _player = p; }
  public Level getLevel() { return _level; }
}

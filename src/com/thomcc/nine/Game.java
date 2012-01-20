package com.thomcc.nine;

import com.thomcc.nine.level.Level;

public class Game {
  private Player _player;
  private Level _level;
  private InputHandler _ih;
  public int offX, offY;
  public Game(InputHandler ih) {
    offX = 0;
    offY = 0;
    
    _ih = ih;
    _level = new Level();
   
    _player = new Player();
    _level.addPlayer(_player);
  }
  public void setOffset(int x, int y) {
    offX = x;
    offY = y;
  }
  
  public void tick() {
    _player.tick(_ih.up, _ih.down, _ih.left, _ih.right);
    int mx = _ih.mouseX;
    int my = _ih.mouseY;
    mx += offX;
    my += offY;
    _player.lookAt(mx, my);
  }
  public Player getPlayer() { return _player; }
  public void setPlayer(Player p) { _player = p; }
  public Level getLevel() { return _level; }
}

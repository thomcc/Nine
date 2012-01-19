package com.thomcc.nine;

public class Game {
  private Player _player;
  public Game(InputHandler ih) {
    _player = new Player(ih);
  }
  
  public Player getPlayer() { return _player; }
  public void setPlayer(Player p) { _player = p; }
  public void tick() {
    _player.tick();
  }
  
  
  
}

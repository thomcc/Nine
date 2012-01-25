package com.thomcc.nine;

import com.thomcc.nine.entity.Player;
import com.thomcc.nine.level.*;
import com.thomcc.nine.render.Menu;
import com.thomcc.nine.render.Renderer;


public class Game {
  private Player _player;
  private Level _level;
  private Input _ih;
  public int offX, offY;
  private long _ticks;
  private Menu _menu;
  public Game(Input ih) {
    offX = 0;
    offY = 0;
    _ih = ih;
    _level = new ShipLevel();
    _player = new Player(_ih, this);
    _level.add(_player);
  }
  public void setMenu(Menu m) {
    _menu = m;
    if (m != null) m.init(this, _ih);
  }
  public void tick() {
    _level.tick(_ticks++);
    _player.lookAt(_ih.mouseX+offX, _ih.mouseY+offY);
    if (_menu != null) _menu.tick();
  }
  public void render(Renderer r) {
    r.centerAroundPlayer(this);
    _level.render(r);
    renderGui(r);
  }
  private void renderGui(Renderer r) {
    r.renderMinimap(_level);
    r.renderString("Ammo: "+_player.getFireCount(), 6, 6);
    r.renderString("Health: "+_player.health, 6, 6+Renderer.CHAR_HEIGHT);
    r.renderString("Enemies: " + _level.enemiesRemaining(), 6, 6+Renderer.CHAR_HEIGHT*2);
    if (_menu != null) _menu.render(r);
  }
  public void setOffset(int x, int y) { offX = x; offY = y; }
  public Player getPlayer() { return _player; }
  public long getTicks() { return _ticks; }
  public void setPlayer(Player p) { _player = p; }
  public Level getLevel() { return _level; }
}

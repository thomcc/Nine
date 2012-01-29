package com.thomcc.nine;

import java.util.List;

import com.thomcc.nine.entity.Player;
import com.thomcc.nine.level.*;
import com.thomcc.nine.render.LoadingMenu;
import com.thomcc.nine.render.Menu;
import com.thomcc.nine.render.PauseMenu;
import com.thomcc.nine.render.Renderer;
import com.thomcc.nine.render.TitleMenu;
import com.thomcc.nine.render.WonMenu;


public class Game {
  private Player _player;
  private ILevel _level;
  private Input _ih;
  public int offX, offY;
  private long _ticks;
  private Menu _menu;
  public boolean lost = true;
  public boolean won = false;
  public boolean loading = false;
  public boolean hasDisplayedLoading = false;
  public boolean ticking = false;
  public boolean paused = false;
  public Settings settings;
  public Game(Input ih) {
    settings = new Settings();
    offX = 0;
    offY = 0;
    _ih = ih;
    setMenu(new TitleMenu());
  }
  public void pause() {
    paused = true;

    setMenu(new PauseMenu());
  }
  public void unPause() {
    paused = false;

    setMenu(null);
  }
  public void start() {
    lost = false;
    won = false;
    ticking = false;
    loading = true;
    hasDisplayedLoading = false;
    setMenu(new LoadingMenu());
  }
  
  public void loadGame() {
    _level = new LevelImpl();
    _player = new Player(_ih, this);
    _level.add(_player);
    loading = false;
    ticking = true;
    setMenu(null);
  }
  
  public void setMenu(Menu m) {
    _menu = m;
    if (m != null) m.init(this, _ih);
  }
  public void lose() {
    ticking = false;
    lost = true;
    setMenu(new TitleMenu());
  }
  public void win() {
    won = true;
    ticking = false;
    setMenu(new WonMenu());
    
  }
  
  public void tick() {
    if (_ih.pause) pause();
    if (ticking && !paused) {
      _level.tick(_ticks++);
      if (_player.removed){
        lose();
      } else if (_level.won()) {
        win();
      } else {
        _player.lookAt(_ih.mouseX+offX, _ih.mouseY+offY);
      }
      playAll(_level.getSounds());
    }
    if (_menu != null) _menu.tick();
    if (loading && hasDisplayedLoading) loadGame();
    
  }
  public void playAll(List<Sound> sounds) {
    if (settings.getPlaySounds()) 
      for (Sound s : sounds) 
        s.play();
    sounds.clear();
  }
  public boolean hasMenu() {
    return _menu != null;
  }
  public void render(Renderer r) {
    if (!lost && !won && !loading) {
      r.centerAround(this);
      _level.render(r);
      renderGui(r);
    }
    if (_menu != null) _menu.render(r);
    if (loading && !hasDisplayedLoading) hasDisplayedLoading = true;
  }
  
  private void renderGui(Renderer r) {
    if (settings.getShowMinimap()) r.renderMinimap(_level);
    r.renderString("Ammo: "+_player.getFireCount(), 6, 6);
    r.renderString("Health: "+_player.health+" x "+_player.lives, 6, 6+Renderer.CHAR_HEIGHT);
    r.renderString("Enemies: " + _level.enemiesRemaining(), 6, 6+Renderer.CHAR_HEIGHT*2);
    
  }
  
  public void setOffset(int x, int y) { offX = x; offY = y; }
  public Player getPlayer() { return _player; }
  public long getTicks() { return _ticks; }
  public void setPlayer(Player p) { _player = p; }
  public ILevel getLevel() { return _level; }
}

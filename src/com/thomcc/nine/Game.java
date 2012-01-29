package com.thomcc.nine;

import java.util.List;
import java.util.Random;

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
  // offset for drawing and for mouse input.  
  public int offX, offY;
  // ticks.  duh.
  private long _ticks;
  // currently active menu
  private Menu _menu;
  private Random _random;
  // settings!  yay, not a singleton like i was going to do
  public Settings settings;
  // should we render
  public boolean shouldRender = false;
  // next tick, should we load
  public boolean loading = false;
  // okay we gotta load but, have we let the user know yet that this... might take a while
  public boolean hasDisplayedLoading = false;
  // tick the game?
  public boolean ticking = false;
  // are we paused?
  public boolean paused = false;
  
  public Game(Input ih) {
    settings = new Settings();
    offX = 0;
    offY = 0;
    _ih = ih;
    _random = new Random();
    setMenu(new TitleMenu());
  }
  // basically, display the loading menu and set a variable so that next time we tick
  // (after the loading menu actually renders) we'll load the game
  public void start() {
    ticking = false;
    loading = true;
    hasDisplayedLoading = false;
    setMenu(new LoadingMenu());
  }
  // load the level, add some enemies, make a new player, and set some shit to indicate that
  // this all happened.
  public void loadGame() {
    _level = new LevelImpl();
    _level.addEnemies(_random.nextInt(10)+15);
    _player = new Player(_ih, this);
    _level.add(_player);
    loading = false;
    ticking = true;
    shouldRender = true;
    setMenu(null);
  }
  // TICK!
  public void tick() {
    if (_ih.pause) pause();
    if (ticking && !paused) {
      _level.tick(_ticks++);
      
      if (_player.removed) lose();
      else if (_level.won()) win();
      else _player.lookAt(_ih.mouseX+offX, _ih.mouseY+offY);
      
      // level might have some sounds queued (listed?) up.  play them.
      playAll(_level.getSounds());
    }
    if (_menu != null) _menu.tick();
    // more loading cruft
    if (loading && hasDisplayedLoading) loadGame();
  }
  
  public void render(Renderer r) {
    //  don't if we shouldn't!!
    if (shouldRender) {
      r.centerAround(this);
      _level.render(r);
      renderGui(r);
    }
    
    if (_menu != null) _menu.render(r);
    // full circle with the loading cruft now.
    if (loading && !hasDisplayedLoading) hasDisplayedLoading = true;
  }
  
  // TODO make the methods i wrote to do this actually do them.
  private void renderGui(Renderer r) {
    if (settings.getShowMinimap()) r.renderMinimap(_level);
    r.renderString("Ammo: "+_player.getFireCount(), 6, 6);
    r.renderString("Health: "+_player.health+" x "+_player.lives, 6, 6+Renderer.CHAR_HEIGHT);
    r.renderString("Enemies: " + _level.enemiesRemaining(), 6, 6+Renderer.CHAR_HEIGHT*2);
  }
  
  // make sounds happen if they gotta.
  public void playAll(List<Sound> sounds) {
    if (settings.getPlaySounds()) for (Sound s : sounds) s.play();
    sounds.clear();
  }
  
  // never can have too many one liners, can you?
  
  // set the menu.  if it's not null, give it a pointer to this and the input handler
  public void setMenu(Menu m) { _menu = m; if (m != null) m.init(this, _ih); }
  // lose the game, basically just stop ticking and rendering and return to the tile
  public void lose() { shouldRender = ticking = false; setMenu(new TitleMenu()); }
  // win the game.  congratulate the player i guess for now.
  public void win() { shouldRender = ticking = false; setMenu(new WonMenu()); }
  // pause and unpause.  Pretty straightforward.
  public void pause() { paused = true; setMenu(new PauseMenu()); }
  public void unPause() { paused = false; setMenu(null); }
  // this is used to determine whether or not Nine should call pause when focus is lost (e.g. will it matter?)
  public boolean hasMenu() { return _menu != null; }
  // set the offset (render calls this based on player position)
  public void setOffset(int x, int y) { offX = x; offY = y; }
  // duh.
  public Player getPlayer() { return _player; }
  public long getTicks() { return _ticks; }
  public void setPlayer(Player p) { _player = p; }
  public ILevel getLevel() { return _level; }
}

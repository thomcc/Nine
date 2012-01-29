package com.thomcc.nine.entity;

import com.thomcc.nine.Sound;
import com.thomcc.nine.render.Renderer;

public class Item extends Entity {

  protected int _time;
  protected int _life;
  protected boolean _flicker = false;
  protected boolean _flickering = false;
  protected int frame = 0;
  protected int _totalFrames;
  protected int _animDelay;
  protected int spriteIndex;
  public Item(int x, int y) {
    this.x = x;
    this.y = y;
    spriteIndex = -1;
    _animDelay = 2;
    _canMove = false;
    rx = ry = 3;
    _time = 0;
    _animDelay = 10;
    _totalFrames = 1;
    _life = 400+random.nextInt(100);
  }
  
  
  public void tick(long ticks) {
    super.tick(ticks);
    if (--_life <= 0) { remove(); return; }
    if (_life - _time < 100) _flickering = true;
    // this animation code isn't used.  I might as well ditch it because
    // i doubt I'll ever get around to it...
    if (ticks % _animDelay == 0) frame = (frame+1) % _totalFrames;
    if (_flickering) _flicker = !_flicker; //ticks % _animDelay == 0;
  }
  
  public void render(Renderer r) {
    if (spriteIndex > 0) {
      if (_flickering) {
        if (_flicker) r.render(spriteIndex, (int)x, (int)y, 0, frame);
      } else r.render(spriteIndex, (int)x, (int)y, 0, frame);
    }
  }
  protected void touched(Entity e) { if (e instanceof Mobile) apply((Mobile)e);}
  protected void apply(Mobile m) {
    if (m instanceof Player) {
      _level.play(Sound.getItem);
      onPlayerContact((Player)m);
      remove();
    } else {
      onMobileContact(m);
    }
  }
  // :/  yeah yeah yeah this is ugly.
  protected void onPlayerContact(Player p) {}
  protected void onMobileContact(Mobile m) {}
}

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
  public Item(int x, int y) {
    this.x = x;
    this.y = y;
    _animDelay = 2;
    _canMove = false;
    rx = ry = 3;
    _time = 0;
    _totalFrames = 1;
    _life = 400+random.nextInt(100);
  }
  
  
  public void tick(long ticks) {
    super.tick(ticks);
    if (--_life <= 0) { remove(); return; }
    if (_life - _time < 100) _flickering = true;
    if (ticks % _animDelay == 0) frame = (frame+1) % _totalFrames;
    if (_flickering) _flicker = !_flicker; //ticks % _animDelay == 0;
  }
  
  public void render(Renderer r) {
  }
  protected void touched(Entity e) { if (e instanceof Mobile) apply((Mobile)e);}
  protected void apply(Mobile m) {
    Sound.getItem.play();
  }
  
}

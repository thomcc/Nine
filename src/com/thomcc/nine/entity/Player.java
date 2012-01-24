package com.thomcc.nine.entity;


import com.thomcc.nine.*;
import com.thomcc.nine.level.Level;
import com.thomcc.nine.render.Renderer;

public class Player extends Entity {
  
  public static final int SIZE = 12;
  public Input _i;
  public int health;
  private int _maxHealth, _regenHealthRate;
  private int _fireCount, _maxFireCount, _reAmmoRate;
  //private Game _game;
  public Player(Input i, Game g) {
    _i = i;
    _maxHealth = 50;
    health = _maxHealth;
    _regenHealthRate = 40;
    
    _friction = 0.9;
    _maxSpeed = 4.0;
    _maxFireCount = 20;
    _reAmmoRate = 10;
    _fireCount = _maxFireCount;
    //_game = g;
  }
  public void setLevel(Level l) {
    super.setLevel(l);
    l.findPlayerLocation(this);
  }
  private void updateStats(long ticks) { // if this gets above 5 i should write a "Stat" class
    if (ticks % _reAmmoRate == 0 && _fireCount < _maxFireCount) { ++_fireCount; }
    if (ticks % _regenHealthRate == 0 && health < _maxHealth) { ++health; } 
  }
  public void tick(long ticks) {
    updateStats(ticks);    
    
    if (_i.down) _py += 1.0;
    if (_i.right) _px += 1.0;
    if (_i.left) _px -= 1.0;
    if (_i.up) _py -= 1.0;
    if (_i.mouseDown) fire();
    
    
    super.tick(ticks);
  }
  public void fire() {
    //System.out.println("firing!");
    if (_fireCount == 0) return;
    _level.add(new Bullet(this, dir, 6));
    _fireCount--;
  }
  public void hurt(Entity cause, int damage, double dir) {
    health -= damage;
    double cd = Math.cos(dir);
    double sd = Math.sin(dir);
    _px += cd*damage;
    _py += sd*damage;
  }
  
  public void render(Renderer r) {
    int d = (((int) (dir / (Math.PI * 2) * 16 + 20.5)) & 15);
    r.renderPlayer((int)x, (int)y, d);
  }
  public int getFireCount() {
    return _fireCount;
  }

}

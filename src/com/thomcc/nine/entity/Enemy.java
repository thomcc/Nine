package com.thomcc.nine.entity;

import com.thomcc.nine.Sound;
import com.thomcc.nine.entity.item.HealthPackItem;
import com.thomcc.nine.entity.item.OneUpItem;
import com.thomcc.nine.entity.item.ThreeGunItem;
import com.thomcc.nine.render.Renderer;

public class Enemy extends Mobile {
  private enum State {
    Wandering, Staring
  }
  public double vision = 50;
  private State _state = State.Wandering;
  private double player_stare_x = -1;
  private double player_stare_y = -1;
  private int player_stare_bc = -1;
  private int _attack = -1;
  private double _age = 0;
  public Enemy() {
    health = 1;
    _collisionFriction = 0.3;
    _friction = 0.98;
    _maxSpeed = 5.0;
    rx = ry = 3;
    size = 12;
    _moveInterval = 40 + random.nextInt(40);
  }
  private int _moveInterval;

  public void tick(long ticks) {
    ++_age;
    behave(ticks); // BEHAVE.
    super.tick(ticks);
  }
  
  
  
  protected void behave(long ticks) {
    // enemies wander until they see you, at which point they stare intensely.
    // if you move or shoot they lunge towards you.  They sorta suck at hitting 
    // you but can do lots of damage if they succeed.
    switch(_state) {
    case Wandering:
      wander(ticks);
      break;
    case Staring:
      keepStaring(ticks);
      break;
    }
  }
  
  // start staring at the player.
  private void stareAt(Player p) {
    _level.play(Sound.notice);
    player_stare_x = p.x;
    player_stare_y = p.y;
    player_stare_bc = p.getFireCount();
    dir = Math.atan2(p.y-y, p.x-x);
    _px = 0;
    _py = 0;
    setState(State.Staring);
  }
  
  // player moved, gotta kill them now.
  private void attack() {
    Player p = _level.getPlayer();
    if (_attack == -1) {
      _px = (player_stare_x-x)/2;
      _py = (player_stare_y-y)/2;
      // they stay interested for about 10 ticks after which they wander off.
      _attack = 10;
    } else --_attack;
    if (!canSee(p) || _attack == 0) {
      setState(State.Wandering);
    }
  }
  
  private void keepStaring(long ticks) {
    Player p = _level.getPlayer();
    if (player_stare_bc < p.getFireCount()) player_stare_bc = p.getFireCount();
    
    if (player_stare_bc != p.getFireCount() || p.x != player_stare_x || p.y != player_stare_y) {
      attack();
    }
    if (!canSee(p)) {
      setState(State.Wandering);
    }
  }
  
  protected boolean canSee(Entity e) {
    Player p = _level.getPlayer();
    if (Math.hypot(x-p.x, y-p.y) > vision) return false; 
    // if the player is > vision pixels away, or they're outside the fov
    // or if theres a wall in their way, we can't see them.
    // otherwise we can.
    double pedir = Math.atan2(p.y-y, p.x-x); 
    if (Math.abs(dir-pedir)<Math.PI/6) {
      if (_level.wallBetween((int)x, (int)y, (int)p.x, (int)p.y)) {
        //System.out.println("A WALL BLOCKS MY VISION");
        return false;
      } else return true;
    } else return false;
  }
  protected void wander(long ticks) {
    Player p = _level.getPlayer();
    if (canSee(p)) {
      stareAt(p);
      // keep behaving?
      behave(ticks);
      return;
    }
    if (ticks % _moveInterval == 0) {
      int dx = 0, dy = 0;
      switch(random.nextInt(3)) {
      case 0: 
        ++dx;
        break;
      case 1:
        --dx;
        break;
      }
      switch(random.nextInt(3)) {
      case 0:
        ++dy;
        break;
      case 1:
        --dy;
        break;
      }
      dir = Math.atan2(dy, dx);
      _px += dx*4;
      _py += dy*4;
    }
    
  }
  public void die() {
    _level.play(Sound.enemyDeath);
    // drop a healthpack ever 1/3 times
    if (random.nextInt(3) == 0) 
      _level.add(new HealthPackItem(getX(), getY()));
    // drop a 3gun every 1/10 times unless we dropped a health pack
    else if (random.nextInt(10) == 0) 
      _level.add(new ThreeGunItem(getX(), getY()));
    // drop a +1life every 1/15 times unless we dropped something already
    else if (random.nextInt(15) == 0) {
      _level.add(new OneUpItem(getX(), getY()));
    }
    
    super.die();
  }
  public int getScoreValue() { return (int)(50.0*(1.0+_age/360.0)); }
  protected void touched(Entity e) { if (e instanceof Player) e.hurt(this, 1, dir); }
  public boolean appearsOnMinimap() { return true; }
  public int getColor() { return 0xff649f42; }
  public void render(Renderer r) { r.render(2, (int)x, (int)y, getDirection()); }
  private void setState(State state) { _state = state; }
}

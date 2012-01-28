package com.thomcc.nine.entity;

import com.thomcc.nine.Sound;
import com.thomcc.nine.render.Renderer;

public class Enemy extends Mobile {
  private enum State {
    Wandering, Staring, Attacking
  }
  public double vision = 50;
  private State _state = State.Wandering;
  private double player_stare_x = -1;
  private double player_stare_y = -1;
  private int player_stare_bc = -1;
  private int _attack = -1;
  public Enemy() {
    health = 1;
    _collisionFriction = 0.3;
    _friction = 0.98;
    _maxSpeed = 5.0;
    rx = ry = 3;
    _moveInterval = 40 + random.nextInt(40);
  }
  private int _moveInterval;
  protected void behave(long ticks) {
    switch(_state) {
    case Wandering:
      wander(ticks);
      break;
    case Staring:
      keepStaring(ticks);
      break;
    }
  }
  private void stareAt(Player p) {
    player_stare_x = p.x;
    player_stare_y = p.y;
    player_stare_bc = p.getFireCount();
    dir = Math.atan2(p.y-y, p.x-x);
    _px = 0;
    _py = 0;
    setState(State.Staring);
  }
  private void attack() {
    Player p = _level.getPlayer();
    if (_attack == -1) {
      _px = (player_stare_x-x)/2;
      _py = (player_stare_y-y)/2;
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
    /*
    if (canSee(p)) {
      
      System.out.println("I SEE YOU");
      double d = Math.hypot(p.x-x, p.y-y);
      _px = 0;//(p.x - x)/d;
      _py = 0;//(p.y - y)/d;
      dir = Math.atan2(p.y-y,p.x-x);
    } else {
      setState(State.Wandering);
    }*/
  }
  private void setState(State state) {
    _state = state;
  }
  protected boolean canSee(Entity e) {
    Player p = _level.getPlayer();
    if (Math.hypot(x-p.x, y-p.y) > vision) { return false; }
    double pedir = Math.atan2(p.y-y, p.x-x); 
    return (Math.abs(dir-pedir)<Math.PI/8);
  }
  protected void wander(long ticks) {
    Player p = _level.getPlayer();
    if (canSee(p)) {
      stareAt(p);
      behave(ticks);
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
    Sound.enemyDeath.play();
    
    if (random.nextInt(5) == 0) 
      _level.add(new HealthPackItem(getX(), getY()));
    
    super.die();
  }
  public void tick(long ticks) {
    behave(ticks);
    super.tick(ticks);
  }
  protected void touched(Entity e) {
    if (e instanceof Player) {
      e.hurt(this, 1, dir);
    }
  }
  public boolean appearsOnMinimap() { return true; }
  public int getColor() { return 0xff649f42; }
  public void render(Renderer r) {
    r.render(2, (int)x, (int)y, getDirection());
  }
}

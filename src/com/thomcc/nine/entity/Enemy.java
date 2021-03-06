package com.thomcc.nine.entity;

import com.thomcc.nine.Sound;
import com.thomcc.nine.entity.item.HealthPackItem;
import com.thomcc.nine.entity.item.Item;
import com.thomcc.nine.entity.item.OneUpItem;
import com.thomcc.nine.entity.item.SuperItem;
import com.thomcc.nine.entity.item.ThreeGunItem;
import com.thomcc.nine.render.Art;
import com.thomcc.nine.render.Renderer;

public class Enemy extends Mobile {
  
  protected enum State { Wandering, Staring, Attacking }
  
  public double vision = 50;
  
  private State _state = State.Wandering;
  
  private double player_stare_x = -1;
  private double player_stare_y = -1;
  private int player_stare_bc = -1;
  private int _attack = -1;
  private double _age = 0;
  protected double baseScore;
  
  public Enemy() {
    health = 1;
    _collisionFriction = 0.3;
    _friction = 0.98;
    _maxSpeed = 5.0;
    rx = ry = 3;
    size = 12;
    baseScore = 50.0;
    _spriteIndex = Art.ENEMY_INDEX;
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
    case Wandering: wander(ticks); break;
    case Staring: stare(ticks); break;
    case Attacking: attack(ticks); break;
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
  protected void attack(long ticks) {
    Player p = _level.getPlayer();
    if (_attack == -1) {
      // they stay interested for about 10 ticks after which they wander off.
      _attack = 30;
    }
    if (_attack >= 0) {
      --_attack;
      if (canSee(p)) attackPoint(p.x, p.y, ticks);
    }
    postAttack(p);
  }
  
  protected void attackPoint(double x, double y, long ticks) {
    dir = Math.atan2(y-this.y, x-this.x);
    _px = (x-this.x)/2;
    _py = (y-this.y)/2;
  }



  protected void postAttack(Player p) {
    if (_attack == 0) {
      setState(State.Wandering);
    } else if (!canSee(p)) {
      dir += 0.1;
    }
  }
  
  
  private void stare(long ticks) {
    Player p = _level.getPlayer();
    if (player_stare_bc < p.getFireCount()) player_stare_bc = p.getFireCount();
    
    if (player_stare_bc != p.getFireCount() || p.x != player_stare_x || p.y != player_stare_y) {
      setState(State.Attacking);
      return;
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
    Item toDrop = chooseItemToDrop();
    if (toDrop != null) _level.add(toDrop);
    
    super.die();
  }
  protected Item chooseItemToDrop() {
    if (random.nextInt(3) == 0) 
      return new HealthPackItem(getX(), getY());
    // drop a 3gun every 1/10 times unless we dropped a health pack
    else if (random.nextInt(10) == 0) 
      return new ThreeGunItem(getX(), getY());
    // drop a +1life every 1/15 times unless we dropped something already
    else if (random.nextInt(15) == 0) {
      return new OneUpItem(getX(), getY());
    } else if (random.nextInt(30) == 0) {
      return new SuperItem(getX(), getY());
    }
    else return null;
  }
  public int getScoreValue() { return (int)(baseScore*(1.0+_age/360.0)); }
  protected void touched(Entity e) { if (e instanceof Player) e.hurt(this, 1, dir); }
  public boolean appearsOnMinimap() { return true; }
  public int getColor() { return 0xff649f42; }
  public void render(Renderer r) { r.render(_spriteIndex, (int)x, (int)y, getDirection()); }
  private void setState(State state) { _state = state; }
}

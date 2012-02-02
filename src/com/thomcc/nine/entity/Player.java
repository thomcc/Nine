package com.thomcc.nine.entity;


import com.thomcc.nine.*;
import com.thomcc.nine.entity.item.Item;
import com.thomcc.nine.level.Level;
import com.thomcc.nine.render.Art;
import com.thomcc.nine.render.Renderer;

public class Player extends Mobile {
  public Input _i;
  private int _maxHealth;
  public int shotsFired = 0;
  private Gun _gun;
  public int score = 0;
  private static final int nextLifeStep = 2500;
  private static final int superCounterStart = 300;
  private int _nextLife = nextLifeStep;
  public int lives;
  // after the player dies there's a brief delay before they 
  // respawn.  deadcounter represents this number
  private int deadcounter = 0;
  // once they respawn they can't be damaged for invulncounter more ticks
  private int invulncounter = 0;
  private int bulletLife = 300;
  private int bulletBounces = 10;
  private int maxAmmo = 15;
  private int fireRate = 10;
  private int ammoRegenRate = 30;
  private int bulletSpeed = 6;
  
  private boolean isSuper = false;
  private int superCounter = 0;
  
  public Player(Input i, Game g) {
    _i = i;
    lives = 3;
    _maxHealth = 25;
    health = _maxHealth;
    _collisionFriction = 0.3;
    _friction = 0.9;
    size = 12;
    _spriteIndex = Art.PLAYER_INDEX;
    _maxSpeed = 4.0;
    setGun(new Gun(this));
  }
  
  public void setLevel(Level l) {
    super.setLevel(l);
    l.findAndSetLocation(this);
  }
  public void superStart() {
    if (!isSuper) {
      isSuper = true;
      superCounter = superCounterStart;
      ThreeGun tg = new ThreeGun(this);
      tg.infiniteUses();
      setGun(tg);
      _level.play(Sound.superGet);
    }
  }
  public void superStop() {
    if (isSuper) {
      isSuper = false;
      superCounter = 0;
      setGun(new Gun(this));
      _level.play(Sound.superLose);
      replenish();
    }    
  }
  private void updateStats(long ticks) {}
  public void tick(long ticks) {
    if (score > _nextLife) {
      ++lives;
      _nextLife += nextLifeStep;
    }
    
    if (isSuper) --superCounter;
    if (superCounter <= 0) superStop();
    
    if (deadcounter == 0) {
      updateStats(ticks);    
      // update our momentum/speed/whatever i'm calling _px and _py today
      // to be in accordance with the user's demands
      if (_i.down) _py += 1.0;
      if (_i.right) _px += 1.0;
      if (_i.left) _px -= 1.0;
      if (_i.up) _py -= 1.0;
      // also shoot maybe.
      if (isSuper && firing()) {
        _gun.fire();
        didShoot();
      }
      else _gun.tick(firing(), ticks);
      //decrement the invulnerability counter
      if (invulncounter > 0) --invulncounter;
      // and then let the mobile or entity motion code take care of actually
      // moving and whatnot
      super.tick(ticks);
    } else {
      if (--deadcounter == 0) {
        respawn();
      }
    }
  }
  public void respawn() {
    // i'm invincibleeee!
    invulncounter = 60;
    // after respawning set health to max, and lose any upgrades
    health = _maxHealth;
    setGun(new Gun(this));
    replenish();
  }
  public void didShoot() {
    _level.play(Sound.shoot);
    ++shotsFired;
  }

  public void heal(int n) { 
    if (health < _maxHealth) {
      health += n;
      if (health > _maxHealth) {
        health = _maxHealth;
      }
    }
  }
  
  // make the explosion sound, and decrement lives (and setup the 
  // die/respawn/invulnerable stuff).  if we're out of lives then _really_ die.
  public void die() {
    _level.play(Sound.playerDeath);
    deadcounter = 20;
    _level.playerDied();
    --lives;
    if (lives == 0) super.die();
  }
  
  // take damage, play the sound (unless we're invulnerable)
  public void hurt(Entity cause, int damage, double dir) {
    if (deadcounter == 0 && invulncounter == 0 && !isSuper) {
      _level.play(Sound.hurt);
      super.hurt(cause, damage, dir);
    }
  }
  
  public void render(Renderer r) {
    // if they're not blinking, or they are and its one of the times we
    // want to draw them, ... do that.
    if (deadcounter == 0 && (invulncounter == 0 || invulncounter % 3 == 0))
      r.render(_spriteIndex, (int)x, (int)y, getDirection(), isSuper ? 1 : 0); 
  }
  
  public void setGun(Gun g) { 
    _gun = g;
    g.setMaxAmmo(maxAmmo);
    g.setAmmoRegenRate(ammoRegenRate);
    g.setBulletSpeed(bulletSpeed);
    g.setFireRate(fireRate);
    g.setBulletBounces(bulletBounces);
    g.setBulletLife(bulletLife);
    g.replenishAmmo();
  }
  
  public boolean appearsOnMinimap() { return true; }
  public int getFireCount() { return _gun.getAmmo(); }
  private boolean firing() { return _i.fire || _i.mouseDown; }
  
  public boolean canUpgradeS() { return _maxSpeed < 6; }
  public boolean canUpgradeH() { return true; }
  public boolean canUpgradeMA() { return true; }
  public boolean canUpgradeFR() { return fireRate >= 5; }
  public boolean canUpgradeARR() { return ammoRegenRate >= 10; }
  public boolean canUpgradeBS() { return true; }
  public boolean canUpgradeBL() { return true; }
  public void upgradeS() { _maxSpeed += 0.3; }
  public void upgradeH() { _maxHealth += 5; }
  public void upgradeMA() { setMaxAmmo(maxAmmo+5); }
  public void upgradeFR() { setFireRate(fireRate-3); }
  public void upgradeARR() { setAmmoRegenRate(ammoRegenRate-5); }
  public void upgradeBS() { setBulletSpeed(bulletSpeed+1); }
  public void upgradeBL() { bulletLife += 50; bulletBounces += 3; } 
  public void setMaxAmmo(int a) { maxAmmo = a; _gun.setMaxAmmo(a); }
  public void setAmmoRegenRate(int arr) { ammoRegenRate = arr; _gun.setAmmoRegenRate(arr); }
  public void setFireRate(int fr) { fireRate = fr; _gun.setFireRate(fr); }
  public void setBulletSpeed(int bs) { bulletSpeed = bs; _gun.setBulletSpeed(bs); }
 

  public int getColor() { return 0xffff6249; }
  public Gun getGun() { return _gun; }
  protected void touched(Entity e) { if (e instanceof Item) ((Item) e).apply(this); }

  public void replenish() {
    health = _maxHealth;
    _gun.replenishAmmo();
  }
}

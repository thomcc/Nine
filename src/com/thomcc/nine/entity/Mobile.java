package com.thomcc.nine.entity;

public class Mobile extends Entity {
  public int health;
  protected double _eyeX = -1, _eyeY = -1;
  public Mobile() {
    _collisionFriction = 0.3;
    _friction = 0.90;
    _canMove = true;
    health = 1;
  }
  public void die() { remove(); }
  public void hurt(Entity cause, int damage, double dir) {
    health -= damage;
    if (health <= 0) {
      die(); return; 
    }
    double cd = Math.cos(dir);
    double sd = Math.sin(dir);
    _px += cd*damage;
    _py += sd*damage;
  }
  protected void collision(boolean ycol, double dx, double dy) {
    if (ycol) {
      switch ((int)dx) {
      case +1:
        _px += Math.abs(_py*_collisionFriction);
        _py *= _collisionFriction;
        break;
      case -1:
        _px -= Math.abs(_py*_collisionFriction);
        _py *= _collisionFriction;
        break;
      case 0:
        _py *= _collisionFriction;
        break;
      }
    } else {
      switch ((int)dy) {
      case +1:
        _py += Math.abs(_px*_collisionFriction);
        _px *= _collisionFriction;
        break;
      case -1:
        _py -= Math.abs(_px*_collisionFriction);
        _px *= _collisionFriction;
        break;
      case 0:
        _px *= _collisionFriction;
        break;
      }
    }
  }
  
  public void lookAt(int px, int py) {
    _eyeX = px;
    _eyeY = py;
    dir = Math.atan2(py - y, px - x);
  }
  
}

package com.thomcc.nine.level;

import java.util.ArrayList;

import java.util.Random;

import com.thomcc.nine.entity.Enemy;
import com.thomcc.nine.entity.Entity;
import com.thomcc.nine.entity.Player;
import com.thomcc.nine.level.gen.VoronoiLevelGen;
import com.thomcc.nine.render.Renderer;

public class ShipLevel implements Level{
  public int[][] map;
  public final int width, height;
  private Player _player;
  private ArrayList<Entity> _entities;
  private int[][] _cachedmm;
  private int _cmmw = -1, _cmmh = -1;
  private ArrayList<Entity>[] _entLookup;
  private int _enemiesRemaining;
  private Random _random;
  public boolean won = false;
  public ShipLevel() { this(600, 600, 50); }
  

  @SuppressWarnings("unchecked")
  public ShipLevel(int width, int height, int points) {
    this.width = width; this.height = height;
    _random = new Random();
    long now = System.nanoTime();
    map = new VoronoiLevelGen(points).generate(width, height);
    long later = System.nanoTime();
    long t = later-now;
    long millis = t/1000000;
    
    System.out.format("Voronoi calculated in %.1f seconds. (%s nanoseconds, %s milliseconds)\n", (double)t/1e9, t, millis);
    System.out.format("\tWidth: %s, Height: %s, Points: %s\n", width, height, points);
    _entities = new ArrayList<Entity>();
    _enemiesRemaining = 0;
    _entLookup = new ArrayList[width * height / 64];
    for (int i = 0; i < _entLookup.length; ++i) {
      _entLookup[i] = new ArrayList<Entity>();
    }
    for (int i = 0; i < 15+_random.nextInt(10); ++i) {
      findLocationAndAdd(new Enemy());
      ++_enemiesRemaining;
    }
  }

  public boolean blocks(int x, int y) {
    if (!inBounds(x, y)) return true;
    else return map[y][x] != 0;
  }
  
  public void findPlayerLocation(Player p) {
    Random r = new Random();
    int x = -1; int y = -1;
    while (true) {
      x = r.nextInt(width);
      y = r.nextInt(height);
      if (get(x, y) == 0 && 
          get(x-p.rx, y-p.ry) == 0 && get(x-p.rx, y+p.ry) == 0 && 
          get(x+p.rx, y-p.ry) == 0 && get(x+p.rx, y+p.ry) == 0) 
        break;
    }
    p.setPosition(x, y);
  }
  
  
  
  private void updateCachedMinimap(int w, int h) {
    if (w != _cmmw || h != _cmmh) {
      _cachedmm = new int[h][w];
      _cmmw = w;
      _cmmh = h;
    }
    
    int xs = width/w;
    int ys = height/h;
    
    for (int y = 0; y < h; ++y)
      for (int x = 0; x < w; ++x)
        _cachedmm[y][x] = map[(int)((y+0.5)*ys)][(int)((x+0.5)*xs)];
    
  }
  
  public int[][] getMinimap(int w, int h) {
    if (_cachedmm == null || w != _cmmw || h != _cmmh) {
      updateCachedMinimap(w, h);
    }
    return _cachedmm;
  }
  
  
  public Player getPlayer() { return _player; }
  public void findLocationAndAdd(Entity e) {
    Random r = new Random();
    int x = -1; int y = -1;
    while (true) {
      x = r.nextInt(width);
      y = r.nextInt(height);
      if (get(x, y) == 0 && 
          get(x-e.rx, y-e.ry) == 0 && get(x-e.rx, y+e.ry) == 0 && 
          get(x+e.rx, y-e.ry) == 0 && get(x+e.rx, y+e.ry) == 0) 
        break;
    }
    e.setPosition(x, y);
    add(e);
  }
  
  public void add(Entity e) {
    if (e instanceof Player) {
      _player = (Player) e;
    }
    e.removed = false;
    _entities.add(e);
    
    e.setLevel(this);
    insertEntity(e.getX() >> 4, e.getY() >> 4, e);
  }
  


  public void remove(Entity e) {
    e.removed = true;
    _entities.remove(e);
  }
  public ArrayList<Entity> getEntities() { return _entities; }
  public ArrayList<Entity> getEntities(int x0, int y0, int x1, int y1) {
    ArrayList<Entity> res = new ArrayList<Entity>();
    int xt0 = (x0 >> 4) - 1;
    int yt0 = (y0 >> 4) - 1;
    int xt1 = (x1 >> 4) + 1;
    int yt1 = (y1 >> 4) + 1;
    for (int y = yt0; y <= yt1; ++y) 
      for (int x = xt0; x <= xt1; ++x) 
        for (Entity e : _entLookup[x+y*(width >> 4)]) 
          if (e.intersects(x0, y0, x1, y1)) 
            res.add(e);
    return res;
  }
  public void tick(long ticks) {
    for (int i = 0; i < _entities.size(); ++i) {
      Entity e = _entities.get(i);
      int tx = e.getX() >> 4;
      int ty = e.getY() >> 4;
      e.tick(ticks);
      int ntx = e.getX() >> 4;
      int nty = e.getY() >> 4;
      
      if (e.removed) {
        _entities.remove(i--);
//        _entities.remove(e);
        removeEntity(ntx, nty, e);
        removeEntity(tx, ty, e);//?
        if (e instanceof Enemy) {
          if (--_enemiesRemaining <= 0) {
            won = true;
          }
        }
      } else if (tx != ntx || ty != nty) {
        removeEntity(tx, ty, e);
        insertEntity(ntx, nty, e);
      }
    }
  }
  public void render(Renderer r) {
    r.render(this);
    for (Entity e : _entities) e.render(r);
  }
  public boolean wallBetween(int x0, int y0, int x1, int y1) {
    if (!inBounds(x0, y0) || !inBounds(x1, y1)) return true;
    int dx = Math.abs(x1-x0);
    int dy = Math.abs(y1-y0);
    int sx = x0 < x1 ? 1 : -1;
    int sy = y0 < y1 ? 1 : -1;
    int err = dx-dy;
    while (x0 != x1 && y0 != y1) {
      if (blocks(x0, y0)) return true;
      int e2 = 2*err;
      if (e2 > -dy) {
        err -= dy;
        x0 += sx;
      }
      if (e2 < dx) {
        err += dx;
        y0 += sy;
      }
    }
    return false;
  }
  public boolean won() { return won; }
  public int getWidth() { return width; }
  public int getHeight() { return height; }
  public boolean inBounds(int x, int y) { return x >= 0 && y >= 0 && x < width && y < height; }
  public int get(int x, int y) { return map[y][x]; }
  private void insertEntity(int i, int j, Entity e) { _entLookup[i+j*(width>>4)].add(e); }
  private void removeEntity(int i, int j, Entity e) { _entLookup[i+j*(width>>4)].remove(e); }
  public int enemiesRemaining() { return _enemiesRemaining; }
}

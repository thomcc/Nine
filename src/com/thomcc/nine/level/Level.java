package com.thomcc.nine.level;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;

import com.thomcc.nine.Sound;
import com.thomcc.nine.entity.Enemy;
import com.thomcc.nine.entity.Entity;
import com.thomcc.nine.entity.Player;
import com.thomcc.nine.level.gen.VoronoiLevelGen;
import com.thomcc.nine.render.LevelDescriptionMenu;
import com.thomcc.nine.render.Renderer;

public class Level {
  public int[][] map;
  public final int width, height;
  private Player _player;
  
  protected Random _random;
  
  private ArrayList<Entity> _entities;  
  private ArrayList<Entity>[] _entLookup;
  private int _activeEnemies;
  
  public int num;
  
  public int _enemiesKilled;
  
  public String description;
  
  public int score;
  // bounds for the cached minimap
  private int[][] _cachedmm;
  
  private int _cmmw = -1, _cmmh = -1;
  
  private List<Sound> toPlay;
  private long time = 0;
  public boolean won = false;
  public int scoreScroll = 0;
  
  public Level() { this(600, 600, 50); }
  
  // yeah and i hate you you too java. 
  @SuppressWarnings("unchecked")
  public Level(int width, int height, int points) {
    this.width = width; this.height = height;
    _random = new Random();

    // initialize our lookup table, which will be used mostly for collision detection
    _entLookup = new ArrayList[width * height / 64];
    for (int i = 0; i < _entLookup.length; ++i) 
      _entLookup[i] = new ArrayList<Entity>();

    toPlay = new LinkedList<Sound>();
    _entities = new ArrayList<Entity>();
    _activeEnemies = 0;
    
    generateLevel(points);
  }
  
  // create and add num enemies
  public void addEnemies(int num) {
    for (int i = 0; i < num; ++i) {
      findLocationAndAdd(new Enemy());
    }
  }
  
  public void generateLevel(int points) {
    System.out.println("## Generating level:");
    long now = System.nanoTime();
    map = new VoronoiLevelGen(points).generateAndCheck(width, height);
    long later = System.nanoTime();
    long t = later-now;
    long millis = t/1000000;
    System.out.format("Voronoi calculated in %.1f seconds. (%s nanoseconds, %s milliseconds)\n", (double)t/1e9, t, millis);
    System.out.format("\tWidth: %s, Height: %s, Points: %s\n", width, height, points);
    System.out.println("## Level generated.");
  }
  // is this a place something can go?
  public boolean blocks(int x, int y) {
    if (!inBounds(x, y)) return true;
    else return map[y][x] != 0;
  }
  
  public int[][] getMinimap(int w, int h) {
    // we want to be sure to cache the minimap, but it sometimes needs updating.
    if (_cachedmm == null || w != _cmmw || h != _cmmh) updateCachedMinimap(w, h);    
    return _cachedmm;
  }
  private void updateCachedMinimap(int w, int h) {
    if (w != _cmmw || h != _cmmh) {
      _cachedmm = new int[h][w];
      _cmmw = w;
      _cmmh = h;
    }
    int xStep = width/w;
    int yStep = height/h;
    for (int y = 0; y < h; ++y) {
      for (int x = 0; x < w; ++x) {
        int yy = (int)((y+0.5)*yStep);
        int xx = (int)((x+0.5)*xStep);
        int val = inBounds(xx, yy) ? map[yy][xx] : 1; 
        _cachedmm[y][x] = val;
      }
    }
  }
  
  // find an open spot and set the entity's location to that spot.
  // am I taking into account the difference between their x location and where
  // collision/drawing occurs?
  public void findAndSetLocation(Entity e) {
    int x = -1; int y = -1;
    int erx = e.rx;
    int ery = e.ry;
    while (true) {
      x = _random.nextInt(width);
      y = _random.nextInt(height);
      // let's not embed them in a wall or anything.
      if (get(x, y) == 0 && 
          get(x-erx, y-ery) == 0 && 
          get(x-erx, y+ery) == 0 && 
          get(x+erx, y-ery) == 0 && 
          get(x+erx, y+ery) == 0) {
        break;
      }
    }
    e.setPosition(x, y);
  }
  
  
  // do everything necessary to add an entity to this level
  public void add(Entity e) {
    if (e instanceof Player) {
      // then set our player.
      _player = (Player) e;
    }
    // make sure they don't think they're removed, and add them to the list
    e.removed = false;
    _entities.add(e);
    // set their level, put them in the lookup table
    e.setLevel(this);
    insertEntity(e.getX() >> 4, e.getY() >> 4, e);
  }
  
  // get all entities within x0, y0, x1, y1.
  public ArrayList<Entity> getEntities(int x0, int y0, int x1, int y1) {
    ArrayList<Entity> res = new ArrayList<Entity>();
    // scale to within the lookuptable (as with tick), increasing the bounds to handle
    // cases where i'm just asking for a single point, and when it's on bounds' edge
    int xt0 = (x0 >> 4) - 1;
    int yt0 = (y0 >> 4) - 1;
    int xt1 = (x1 >> 4) + 1;
    int yt1 = (y1 >> 4) + 1;
    // iterate over the rectangle, check if there's an intersection, and if there is cool, add it to the list    
    for (int y = yt0; y <= yt1; ++y) 
      for (int x = xt0; x <= xt1; ++x) 
        for (Entity e : _entLookup[x+y*(width >> 4)])  
          if (e.intersects(x0, y0, x1, y1))  
            res.add(e);
    
    return res;
  }
  
  public void tick(long ticks) {
    ++time;
    for (int i = 0; i < _entities.size(); ++i) {
      Entity e = _entities.get(i);
      // find where it is and divide by 16 to scale into the range of the lookup table
      int lookup_x = e.getX() >> 4;
      int lookup_y = e.getY() >> 4;
      
      e.tick(ticks);
      
      // and then divide the new location by 16 to scale into the range of the lookup table
      int new_lookup_x = e.getX() >> 4;
      int new_lookup_y = e.getY() >> 4;
      
      if (e.removed) {
        // remove the enemy, and decrement the index so we don't skip anybody.
        _entities.remove(i--);
        //removeEntity(new_lookup_x, new_lookup_y, e); // i really don't _think_ i need this, but... i might.
        removeEntity(lookup_x, lookup_y, e); 
        
        if (e instanceof Enemy) {
          // kill kill kill
          ++_enemiesKilled;
          score += ((Enemy) e).getScoreValue();
          scoreScroll  += 15;
          if (--_activeEnemies <= 0) {
            // sweet, level is won and now the game will take care of the rest
            won = true;
          }
        }
      } else if (lookup_x != new_lookup_x || lookup_y != new_lookup_y) {
        // they've moved so update the lookup table
        removeEntity(lookup_x, lookup_y, e);
        insertEntity(new_lookup_x, new_lookup_y, e);
      }
    }
  }
  
  
  
  // use bresenham's line algorithm to check if theres a blocking point between two other points
  // might need to refactor this out of here if i decide to implement something like a line indicating
  // where you're aiming.
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
  
  //tell the renderer to draw us and tell the entities to draw themselves
  // (they just tell the renderer to draw their sprite)
  public void render(Renderer r) {
    r.render(this);
    for (Entity e : _entities) e.render(r);
  }
  //a holdover from when this did both of these things.  but hey, a little abstraction never hurts, right?
  public void findLocationAndAdd(Entity e) { 
    findAndSetLocation(e); 
    add(e); 
    if (e instanceof Enemy) ++_activeEnemies;
  }
  public String getScoreString() {
    int s = score;
    if (scoreScroll == 0) return "Score: "+s;
    else if (scoreScroll > 0) return "Score: " + (s - scoreScroll--);
    else return "Score: " + (s - scoreScroll++);
  }
  public void playerDied() {
    if (score - 60 < 0) {
      scoreScroll = -score;
      score = 0;
    } else {
      scoreScroll = -60;
      score -= 60;
    }
  }
  // yay i love one liners. 
  
  
  // set the player then do the rest of the adding
  public void add(Player p) { _player = p; add((Entity)p); }
  // add a sound to the list so that the game can play or not play it later
  public void play(Sound s) { toPlay.add(s); }
  // return list of sounds for the game
  public List<Sound> getSounds() { return toPlay; }
  // are there any enemies left or (potentially) has some other winning condition been satisfied
  public boolean won() { return won; }
  // get _all_ entities.
  public ArrayList<Entity> getEntities() { return _entities; }
  // will we get an error if we do map[y][x]
  public boolean inBounds(int x, int y) { return x >= 0 && y >= 0 && x < width && y < height; }
  // insert or remove entity into/from the lookup table at i,j
  private void insertEntity(int i, int j, Entity e) { _entLookup[i+j*(width>>4)].add(e); }
  private void removeEntity(int i, int j, Entity e) { _entLookup[i+j*(width>>4)].remove(e); }
  // remove entity alltogether from level.
  public void remove(Entity e) { e.removed = true; _entities.remove(e); }
  // how many are left?
  public int enemiesRemaining() { return _activeEnemies; }
  // obvious
  public int getWidth() { return width; }
  public int getHeight() { return height; }
  public int get(int x, int y) { return map[y][x]; }
  public Player getPlayer() { return _player; }
  public long getTime() { return time; }
  public LevelDescriptionMenu getDescriptionMenu() { return new LevelDescriptionMenu(num, description); }

}

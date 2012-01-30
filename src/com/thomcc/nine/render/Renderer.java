package com.thomcc.nine.render;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.thomcc.nine.Game;
import com.thomcc.nine.entity.Entity;
import com.thomcc.nine.entity.Player;
import com.thomcc.nine.level.*;
import com.thomcc.nine.level.gen.VoronoiNoise;

public class Renderer {
  public static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789()[]<>/"+
                                     "abcdefghijklmnopqrstuvwxyz.,?!:;_`'\"@#$%^&\\"+
                                     " *+-=~{}|";
  public static final int CHAR_WIDTH = 6;
  public static final int CHAR_HEIGHT = 12;
  public static final int CHARS_PER_ROW = 43;

  private static final int FLOOR = 0x616786;
  private static final int DFLOOR = 0x575d79;
  private static final int WALL_OUTER = 0x222222;
  private static final int WALL_INNER = 0x2D81C3;

  public static final Color MENU_BOX_COLOR = new Color(0x24, 0x4e, 0x67);
  //public static final int MENU_TEXT_COLOR = 0xacc2cf;
  public static final Color MENU_OUTLINE_COLOR = new Color(0x22, 0x31, 0x3a);
  
  private int[] _pix;
  
  public BufferedImage image;
  
  private int _width, _height;
  private int _offX = 0, _offY = 0;
  private Graphics _g;
  private int _patW, _patH;
  private boolean[][] _floorPattern;
  public Color textColor = new Color(255, 255, 230);
  private BufferedImage _fontImg;
  private int[] _fontPix;
  private final Art _art;
  public Renderer(int w, int h) {
    _width = w; _height = h;
    
    image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    _pix = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    _g = image.getGraphics();
    
    try { _fontImg = ImageIO.read(Renderer.class.getResourceAsStream("/font.png")); } 
    catch (IOException e) { throw new RuntimeException(e); }
    _fontPix = _fontImg.getRGB(0, 0, _fontImg.getWidth(), _fontImg.getHeight(), null, 0, _fontImg.getWidth());
    
    _art = new Art();
    
    _patW = _patH = 300;
    _floorPattern = new boolean[_patH][_patW];
    generateFloorPattern();
  }
  
  private void generateFloorPattern() {
    double[][] floorPat = new VoronoiNoise(_patW, _patH, 60).calculate(VoronoiNoise.DISTANCE_NORMAL);
    for (int y = 0; y < _patH; ++y) 
      for (int x = 0; x < _patW; ++x) 
        if (floorPat[y][x] < 0.05) _floorPattern[y][x] = true;
        else _floorPattern[y][x] = false;
  }
  
  public void renderMinimap(Level _level) {
    
    int mmW = _level.width/10; int mmH = _level.height/10;
    
    int i = 10, j = 10;
    
    while (mmW > 80) mmW = _level.width / --i;
    while (mmH > 80) mmH = _level.height / --j;
    
    int mmXoff = _width-10-mmW;
    
    int mmYoff = 10;
    
    int[][] m = _level.getMinimap(mmW, mmH);
    BufferedImage mmImg = new BufferedImage(mmW, mmH, BufferedImage.TYPE_INT_RGB);
    int[] pix = ((DataBufferInt)mmImg.getRaster().getDataBuffer()).getData();
    for (int y = 0; y < mmH; ++y)
      for (int x = 0; x < mmW; ++x) 
        switch (m[y][x]) {
        case 0: pix[x+y*mmW] = FLOOR; break;
        case -1: case 1: pix[x+y*mmW] = WALL_OUTER; break;
        case 2: pix[x+y*mmW] = WALL_INNER; break;
        }
    for (Entity e : _level.getEntities()) {
      if (!e.appearsOnMinimap()) continue;
      int col = e.getColor();
      int x = e.getX();
      int y = e.getY();
      x /= _level.getHeight()/mmH;
      y /= _level.getWidth()/mmW;
      if (x+y*mmW < pix.length) pix[x+y*mmW] = col;
    }
    _g.setColor(Color.BLACK);
    _g.drawRect(mmXoff-1, mmYoff-1, mmW+1, mmH+1);
    _g.drawImage(mmImg, mmXoff, mmYoff, null);
  }
  
  public void render(Level l) {
    int[][] map = l.map;
    int lw = l.width;
    int lh = l.height;
    for (int y = 0; y < _height; ++y) {
      int yp = _offY + y;   // yp is the coordinate on the levels map
      int yy = _offY/3 + y; // yy is the coordinate on the background map 
      while (yp < 0) yp += lh;
      yp %= lh;
      yy %= _patH;
      int[] cellrow = map[yp]; 
      for (int x = 0; x < _width; ++x) {
        int xp = _offX + x;
        int xx = _offX/3 + x;
        while (xp < 0) xp += lw;
        xp %= lw;
        int cell = cellrow[xp];
        int col = FLOOR;
        if (cell == 2) col = WALL_INNER;
        else if (cell == 1) col = WALL_OUTER;
        else if (cell == -1) col = WALL_OUTER;
        else {
          xx %= _patW;
          if (_floorPattern[yy][xx]) col = DFLOOR;
          else col = FLOOR;
        }
        _pix[x+y*_width] = col;
      }
    }
  }
  
  public void render(Sprite s, int x, int y, int dir, int template) {
    y -= _offY+s.height/2;
    x -= _offX+s.width/2;
    _g.drawImage(s.get(template, dir), x, y, null);
  }
  
  private void renderFontChar(int px, int py, int xf, int yf, int col) {
    int offset = xf * CHAR_WIDTH + yf * CHAR_HEIGHT * _fontImg.getWidth();
    for (int y = 0; y < CHAR_HEIGHT; ++y) {
      if (y + py < 0 || y + py >= _height) continue;
      for (int x = 0; x < CHAR_WIDTH; ++x) {
        if (x + px < 0 || x + px >= _width || _fontPix[x+y*_fontImg.getWidth()+offset] > 0) continue;
        _pix[(x + px) + (y + py)*_width] = col;
      }
    }
  }
  
  public void renderString(String str, int x, int y, int color) { 
    for (int i = 0; i < str.length(); ++i) {
      int ix = CHARS.indexOf(str.charAt(i));
      if (ix >= 0) renderFontChar(x+i*CHAR_WIDTH, y, ix % CHARS_PER_ROW, ix / CHARS_PER_ROW, color);
    }
  }
  
  public void centerAround(Game g) {
    Player p = g.getPlayer();
    if (p == null) {
      setOffset(0, 0);
      g.setOffset(0, 0);
      return;
    }
    int xo = p.getX()-_width/2;
    int yo = p.getY()-_height/2;
    Level l = g.getLevel();
    int xmin = 0;
    int xmax = l.getWidth()-_width;
    int ymin = 0;
    int ymax = l.getHeight()-_height;
    if (xo < xmin) xo = xmin;
    else if (xo > xmax) xo = xmax;
    if (yo < ymin) yo = ymin;
    else if (yo > ymax) yo = ymax;
    setOffset(xo, yo);
    g.setOffset(xo, yo);
  }
  
  public void drawMenuBox(int x0, int y0, int w, int h) {
    _g.setColor(MENU_BOX_COLOR);
    _g.fillRect(x0, y0, w, h);
    _g.setColor(MENU_OUTLINE_COLOR);
    _g.drawRect(x0, y0, w, h);
  }
  
  public void centerAround(Menu m) { setOffset(0, 0); }
  public void clear() { _g.clearRect(0, 0, _width, _height); }
  public int getViewportWidth() { return _width; }
  public int getViewportHeight() { return _height; }
  private void setOffset(int x, int y) { _offX = x; _offY = y; }
  public void fill(Color c) { _g.setColor(c); _g.fillRect(0, 0, _width, _height); }
  public void renderString(String str, int x, int y) { renderString(str, x, y, 0xffffdd); }
  public void render(int s_idx, int x, int y, int dir) { render(_art.sprites[s_idx], x, y, dir, 0); }
  public void render(Sprite s, int x, int y, int dir) { render(s, x, y, dir, 0); }
  public void render(int s_idx, int x, int y, int dir, int template) { render(_art.sprites[s_idx], x, y, dir, template); }
  public void render9(int x, int y) { _g.drawImage(_art.nine.get(), x, y, null); }
  public void render(Game game) { _g.clearRect(0, 0, _width, _height); game.render(this); }
  public Graphics getGraphics() { return image.getGraphics(); }
}

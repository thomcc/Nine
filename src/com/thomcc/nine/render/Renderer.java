package com.thomcc.nine.render;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.thomcc.nine.Game;
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
  
  private int[] _pix;
  
  public BufferedImage image;
  
  private int _width, _height;
  private int _offX = 0, _offY = 0;
  private Graphics _g;
  private int _patW, _patH;
  private boolean[][] _floorPattern;
  public Color textColor = new Color(255, 255, 230);
  public final Sprite[] sprites;
  private BufferedImage _fontImg;
  private int[] _fontPix;
  public Renderer(int w, int h) {
    
    _width = w;
    _height = h;
    
    image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    
    _pix = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    
    _g = image.getGraphics();
    
    try { 
      _fontImg = ImageIO.read(Renderer.class.getResourceAsStream("/font.png")); 
    } catch (IOException e) { 
      throw new RuntimeException(e); 
    }
    _fontPix = _fontImg.getRGB(0, 0, _fontImg.getWidth(), _fontImg.getHeight(), null, 0, _fontImg.getWidth());
    
    Art a = new Art();
    
    sprites = a.sprites;
    
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
  
  public void render(Game game) {
    _g.clearRect(0, 0, _width, _height);
    game.render(this);
  }
  public void renderMinimap(Level l) {
    
    int mmW = 70; int mmH = 70;
    int mmXoff = _width-20-mmW;
    int mmYoff = 20;
    int[][] m = l.getMinimap(mmW, mmH);
    BufferedImage mmImg = new BufferedImage(mmW, mmH, BufferedImage.TYPE_INT_RGB);
    int[] pix = ((DataBufferInt)mmImg.getRaster().getDataBuffer()).getData();
    for (int y = 0; y < mmH; ++y)
      for (int x = 0; x < mmW; ++x) 
        switch (m[y][x]) {
        case 0: pix[x+y*mmW] = FLOOR; break;
        case 1: pix[x+y*mmW] = WALL_OUTER; break;
        case 2: pix[x+y*mmW] = WALL_INNER; break;
        }
    int lw = l.getWidth();
    int lh = l.getHeight();
    int xx = l.getPlayer().getX();//+1;
    int yy = l.getPlayer().getY();//+1;
    while (xx < 0) xx += lw;
    while (yy < 0) yy += lh;
    yy %= lh;
    xx %= lw;
    yy /= lh/mmH;
    xx /= lw/mmW;
    pix[xx+yy*mmW] = Art.PLAYERCOLOR;
    _g.setColor(Color.BLACK);
    _g.drawRect(mmXoff-1, mmYoff-1, mmW+1, mmH+1);
    _g.drawImage(mmImg, mmXoff, mmYoff, null);
  }
  
  public void render(ShipLevel l) {
    int[][] map = l.map;
    int lw = l.width;
    int lh = l.height;
    for (int y = 0; y < _height; ++y) {
      int yp = _offY + y;   // yp is the coordinate on the levels map
      int yy = _offY/4 + y; // yy is the coordinate on the background map 
      while (yp < 0) yp += lh;
      yp %= lh;
//      if (Game.fancyGraphics) {
        while (yy < 0) yy += _patH;
        yy %= _patH;
//      }
      int[] cellrow = map[yp]; 
      for (int x = 0; x < _width; ++x) {
        int xp = _offX + x;
        int xx = _offX/4 + x;
        while (xp < 0) xp += lw;
        xp %= lw;
        int cell = cellrow[xp];
        int col = FLOOR;
        // if it's a wall draw it as a wall
        if (cell == 2) col = WALL_INNER;
        else if (cell == 1) col = WALL_OUTER;
        // otherwise, render from the background pattern
        else /*if (Game.fancyGraphics)*/ {
          while (xx < 0) xx += _patW;
          xx %= _patW;
          if (_floorPattern[yy][xx]) col = DFLOOR;
          else col = FLOOR;
        }
        _pix[x+y*_width] = col;
      }
    }
  }
  
  public void render(Sprite s, int x, int y, int dir) {
    y -= _offY+s.size/2;
    x -= _offX+s.size/2;
    _g.drawImage(s.get(dir), x, y, null);
  }
  private void renderFontChar(int px, int py, int xf, int yf, int col) {
    //px -= _offX;
    //py -= _offY;
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
  public void renderString(String str, int x, int y) { renderString(str, x, y, 0xffffdd); }
  public void render(int s_idx, int x, int y, int dir) { render(sprites[s_idx], x, y, dir); }
  public Graphics getGraphics() { return image.getGraphics(); }
  public void centerAroundPlayer(Game g) {
    Player p = g.getPlayer();
    int xo = p.getX()-_width/2;
    int yo = p.getY()-_height/2;
    setOffset(xo, yo);
    g.setOffset(xo, yo);
  }
  private void setOffset(int x, int y) { _offX = x; _offY = y; }
}

package com.thomcc.nine.render;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.thomcc.nine.Game;
import com.thomcc.nine.entity.Player;
import com.thomcc.nine.level.*;
import com.thomcc.nine.level.gen.VoronoiNoise;

public class Renderer {
  private static final int FLOOR = 0x616786;
  private static final int DFLOOR;// = new Color(FLOOR).darker().getRGB();
  static { 
    float[] hsvf = new float[]{ 0, 0, 0 }; 
    Color.RGBtoHSB(0x61, 0x67, 0x86, hsvf);
    DFLOOR = Color.HSBtoRGB(hsvf[0], hsvf[1], hsvf[2]*0.9f);
  }
  private static final int WALL_OUTER = 0x222222;
  private static final int WALL_INNER = 0x2D81C3;//0x3a6d4f;
  private int[] _pix;
  public BufferedImage image;
  private int _width, _height;
  private int _offX, _offY;
  private Graphics _g;
  private int _patW, _patH;
  private boolean[][] _floorPattern;
  public Color textColor = new Color(255, 255, 230);
  public final Sprite[] sprites;
  public Renderer(int w, int h) {
    _offX = _offY = 0;
    _width = w;
    _height = h;
    image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    _pix = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    _g = image.getGraphics();
    Graphics2D g2 = (Graphics2D)_g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    Art a = new Art();
    sprites = a.sprites;
    _patW = _patH = 300;
    
    
    double[][] floorPat = new VoronoiNoise(_patW, _patH, 60).calculate(VoronoiNoise.DISTANCE_NORMAL);

    _floorPattern = new boolean[_patH][_patW];
    for (int y = 0; y < _patH; ++y) 
      for (int x = 0; x < _patW; ++x) 
        if (floorPat[y][x] < 0.05) _floorPattern[y][x] = true;
        else _floorPattern[y][x] = false;
    
  }
  
  public void render(Game game) {

    _g.clearRect(0, 0, _width, _height);
    Player p = game.getPlayer();
    Level l = game.getLevel();
    
    int xo = p.getX()-_width/2;
    int yo = p.getY()-_height/2;
    
    setOffset(xo, yo);

    game.setOffset(xo, yo);
    
    l.render(this);
    
    renderMinimap(l);
    _g.setColor(textColor);
    _g.drawString("Bullets: "+p.getFireCount(), 20, 20);
    _g.drawString("Health: "+p.health, 20, 35);
    
  }
  private void renderMinimap(Level l) {
    
    int mmW = 70; int mmH = 70;
    int mmXoff = _width-20-mmW;
    int mmYoff = 20;
    int[][] m = l.getMinimap(mmW, mmH);
    BufferedImage mmImg = new BufferedImage(mmW, mmH, BufferedImage.TYPE_INT_RGB);
    int[] pix = ((DataBufferInt)mmImg.getRaster().getDataBuffer()).getData();
    
    for (int y = 0; y < mmH; ++y) {
      for (int x = 0; x < mmW; ++x) {
        int pt = m[y][x];
        switch (pt) {
        case 0: pix[x+y*mmW] = FLOOR; break;
        case 1: pix[x+y*mmW] = WALL_OUTER; break;
        case 2: pix[x+y*mmW] = WALL_INNER; break;
        }
      }
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
  
  public void renderShipLevel(ShipLevel l) {
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
  
  public void render(int sidx, int x, int y, int dir) { render(sprites[sidx], x, y, dir); }
  public Graphics getGraphics() { return image.getGraphics(); }
  private void setOffset(int x, int y) { _offX = x; _offY = y; }
}

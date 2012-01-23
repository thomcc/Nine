package com.thomcc.nine.render;
import java.awt.Color;
import java.awt.Graphics;
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
  
  private BufferedImage[] _sprites;
  private int[] _pix;
  public BufferedImage image;
  private int _width, _height;
  private int _offX, _offY;
  //private Graphics _g;
  private int _patW, _patH;
  private boolean[][] _floorPattern;
  public Renderer(int w, int h) {
    _offX = _offY = 0;
    _width = w;
    _height = h;
    image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    _pix = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    //_g = image.getGraphics();
    _sprites = Art.generateDude();
    double[][] floorPat = new VoronoiNoise(300, 300, 60).calculate(VoronoiNoise.DISTANCE_NORMAL);
    _patW = 300;
    _patH = 300;
    _floorPattern = new boolean[_patH][_patW];
    for (int y = 0; y < _patH; ++y) 
      for (int x = 0; x < _patW; ++x) 
        if (floorPat[y][x] < 0.05) _floorPattern[y][x] = true;
        else _floorPattern[y][x] = false;
    
  }
  
  public void render(Game game) {

    Graphics g = image.getGraphics();
    g.clearRect(0, 0, _width, _height);
    
    Player p = game.getPlayer();
    Level l = game.getLevel();
    
    int xo = p.getX()-_width/2;
    int yo = p.getY()-_height/2;
    
    setOffset(xo, yo);

    game.setOffset(xo, yo);
    
    l.render(this);
    p.render(this);
    
    renderMinimap(l, g);
    g.dispose();
    
  }
  private void renderMinimap(Level l, Graphics g) {
    
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
    pix[xx+yy*mmW] = Art.COCKPIT;
    g.setColor(Color.BLACK);
    g.drawRect(mmXoff-1, mmYoff-1, mmW+1, mmH+1);
    g.drawImage(mmImg, mmXoff, mmYoff, null);
  }
  
  public void renderShipLevel(ShipLevel l) {
    int[][] map = l.map;
    int lw = l.width;
    int lh = l.height;
    for (int y = 0; y < _height; ++y) {
      int yp = _offY + y;
      int yy = _offY/4 + y;
      
      while (yp < 0) yp += lh;
      yp %= lh;
      if (Game.fancyGraphics) {
        //yy /= 4;
        while (yy < 0) yy += _patH;
        yy %= _patH;
      }
      int[] cellrow = map[yp]; 
      for (int x = 0; x < _width; ++x) {

        int xp = _offX + x;
        int xx = _offX/4 + x;
//        int xx = _offX + x;
//        int xp = xx;
        while (xp < 0) xp += lw;
        xp %= lw;
        
        int cell = cellrow[xp];
        
        int col;
        
        if (cell == 2) col = WALL_INNER;
        else if (cell == 1) col = WALL_OUTER;
        else if (Game.fancyGraphics) {
         // xx /= 4;
          while (xx < 0) xx += _patW;
          
          if (_floorPattern[yy][xx % _patW]) col = DFLOOR;
          else col = FLOOR;
        } else col = FLOOR;
        _pix[x+y*_width] = col;
      }
    }
    
    
  }
  public void renderPlayer(int x, int y, int dir) {
    y -= Player.SIZE/2+_offY;
    x -= Player.SIZE/2+_offX;
    image.getGraphics().drawImage(_sprites[dir], x, y, null);
  }
  
  public void renderPlayer(Player p, Graphics g) {
    int d = p.getDirection();
    int px = p.getX()-Player.SIZE/2-_offX;
    int py = p.getY()-Player.SIZE/2-_offY;
    
    g.drawImage(_sprites[d], px, py, null);
  }
  
  public Graphics getGraphics() { return image.getGraphics(); }
  private void setOffset(int x, int y) { _offX = x; _offY = y; }
}

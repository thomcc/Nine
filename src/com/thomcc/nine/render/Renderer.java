package com.thomcc.nine.render;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.thomcc.nine.Game;
import com.thomcc.nine.Player;
import com.thomcc.nine.level.*;

public class Renderer {
  private static final int FLOOR = 0xc1c7c6;
  private static final int WALL = 0x3a6d4f;
  
  private BufferedImage[] _sprites;
  private int[] _pix;
  public BufferedImage image;
  private int _width, _height;
  private int _offX, _offY;
  //private Graphics _g;
  public Renderer(int w, int h) {
    _offX = _offY = 0;
    _width = w;
    _height = h;
    image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    _pix = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    //_g = image.getGraphics();
    _sprites = Art.generateDude();
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
    //g.drawString(game.controlMode == Game.CONTROL_MODE_KEYBOARD ? "K" : "M", 20, 20);
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
        case 1: pix[x+y*mmW] = WALL; break;
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
    pix[xx+yy*mmW] = 0;
    g.setColor(Color.BLACK);
    g.drawRect(mmXoff-1, mmYoff-1, mmW+1, mmH+1);
    g.drawImage(mmImg, mmXoff, mmYoff, null);
  }
  
  public void renderShipLevel(ShipLevel l) {
    int[][] map = l.map;

    for (int y = 0; y < _height; ++y) {
      for (int x = 0; x < _width; ++x) {
        int xx = _offX + x;
        int yy = _offY + y;
        while (xx < 0) xx += l.width;
        while (yy < 0) yy += l.height;
        _pix[x+y*_width] = map[yy % l.height][xx % l.width] == 0 ? FLOOR : WALL;
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

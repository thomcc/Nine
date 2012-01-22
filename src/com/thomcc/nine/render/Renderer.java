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
  public BufferedImage image;
  private int _width, _height;
  private int _offX, _offY;
  private Graphics _g;
  public Renderer(int w, int h) {
    _offX = _offY = 0;
    _width = w;
    _height = h;
    image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    _g = image.getGraphics();
    _sprites = Art.generateDude();
  }
  
  public void render(Game game) {

    _g = image.getGraphics();
    _g.clearRect(0, 0, _width, _height);
    
    Player p = game.getPlayer();
    Level l = game.getLevel();
    
    int xo = p.getX()-_width/2;
    int yo = p.getY()-_height/2;
    
    setOffset(xo, yo);

    game.setOffset(xo, yo);
    
    l.render(this);
    
    renderPlayer(p, _g);
    
    renderGui(game, _g);
    
    _g.dispose();
    
  }
  public void renderShipLevel(ShipLevel l) {
    int[][] map = l.map;
    int[] pix = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    
    for (int y = 0; y < _height; ++y) {
      for (int x = 0; x < _width; ++x) {
        int xx = _offX + x;
        int yy = _offY + y;
        while (xx < 0) xx += l.width;
        while (yy < 0) yy += l.height;
        pix[x+y*_width] = map[yy % l.height][xx % l.width] == 0 ? FLOOR : WALL;
      }
    }
  }
  private void renderGui(Game game, Graphics g) {
    g.setColor(Color.BLACK);
    g.drawString(String.format("%.1f", game.getPlayer().getDistance()), 5, 20);
  }
  public void renderPlayer(Player p, Graphics g) {
    int d = p.getDirection();
    int px = p.getX()-Art.SIZE/2-_offX;
    int py = p.getY()-Art.SIZE/2-_offY;
    
    g.drawImage(_sprites[d], px, py, null);
    
  }
  public Graphics getGraphics() { return image.getGraphics(); }
  private void setOffset(int x, int y) { _offX = x; _offY = y; }
}

package com.thomcc.nine.render;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.thomcc.nine.Game;
import com.thomcc.nine.Player;
import com.thomcc.nine.level.*;

public class Renderer {
  
  
  private static final Color FLOORC = new Color(0xc1,0xc7,0xc6);//0xF5, 0xF5, 0xDC);
  private static final Color WALLC = new Color(0x3a, 0x6d, 0x4f);
  
  private static final int FLOOR = 0xc1c7c6;
  private static final int WALL = 0x3a6d4f;
  
  private static final Color FUCK = new Color(0xff, 0x00, 0xff);
  
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
    
   // if (xo < 0) xo = 0;
   // if (yo < 0) yo = 0;
    
   // if (xo > l.getWidth() - _width) xo = l.getWidth() - _width;
   // if (yo > l.getHeight() - _height) yo = l.getHeight() - _height; 
    
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
  public void renderMazeLevel(MazeLevel l) {

    int[][] map = l.map;
    int scale = 16;
    for (int y = 0; y < map.length; ++y) {
      
      int yp = y*scale-_offY;   
      if (yp < -1*scale || yp > _height) continue;
      
      for (int x = 0; x < map[0].length; ++x) {
        
        int xp = x*scale-_offX;
        
        if (xp < -1*scale || xp > _width) continue;
        
        int cell = map[y][x];
        
        if (cell == 0) _g.setColor(FLOORC);
        else if (cell == 1) _g.setColor(WALLC);
        else _g.setColor(FUCK);
        
        _g.fillRect(xp, yp, scale, scale);
      }
    }
  }
  public Graphics getGraphics() { return image.getGraphics(); }
  private void setOffset(int x, int y) { _offX = x; _offY = y; }
}

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
  public Renderer(int w, int h) {
    _offX = _offY = 0;
    _width = w;
    _height = h;
    image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    _sprites = Art.generateDude();
  }
  
  public void render(Game game) {

    Graphics g = image.getGraphics();
    g.clearRect(0, 0, _width, _height);
    Player p = game.getPlayer();
    Level l = game.getLevel();
    /*
    if (l instanceof MazeLevel) renderMazeLevel((MazeLevel)l, p);
    int xo = p.getX() - _width / 2;
    int yo = p.getY() - (_height - 8) / 2;
    
    if (xo < 0) xo = 0;
    
    if (yo < 0) yo = 0;
    
    if (xo > l.width * 16 - _width) 
      xo = l.width * 16 - _width;
    
    if (yo > l.height * 16 - _height)
      yo = l.height * 16 - _height;*/
    
    int xo = p.getX()-_width/2;
    int yo = p.getY()-_height/2;
    if (xo < 0) xo = 0;
    if (yo < 0) yo = 0;
    if (xo > l.getWidth() - _width) xo = l.getWidth() - _width;
    if (yo > l.getHeight() - _height) yo = l.getHeight() - _height; 
    setOffset(xo, yo);
    // TODO: make this not happen here
    game.setOffset(xo, yo);
    
    
    if (l instanceof MazeLevel) 
      renderMazeLevel((MazeLevel)l, g);
    else if (l instanceof ShipLevel) 
      renderShipLevel((ShipLevel)l);

    renderPlayer(p, g);
    
    renderGui(game, g);
    
    g.dispose();
  }
  private void renderShipLevel(ShipLevel l) {
    int[][] map = l.map;
    int[] pix = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    
    for (int y = 0; y < _height; ++y) {
      for (int x = 0; x < _width; ++x) {
        pix[x+y*_width] = map[_offY+y][_offX+x] == 0 ? FLOOR : WALL;
      }
    }
  }

  private void renderGui(Game game, Graphics g) {
    g.setColor(Color.BLACK);
    g.drawString(String.format("%.1f", game.getPlayer().getDistance()), 5, 20);
  }
  private void setOffset(int x, int y) {
    _offX = x; _offY = y;
  }

  public void renderPlayer(Player p, Graphics g) {
    int d = p.getDirection();
    int px = p.getX()-Art.SIZE/2-_offX;
    int py = p.getY()-Art.SIZE/2-_offY;
    
    g.drawImage(_sprites[d], px, py, null);
    
  }
  private void renderMazeLevel(MazeLevel l, Graphics g) {
    int[][] map = l.map;
    int scale = 16;
    for (int y = 0; y < map.length; ++y) {
      
      int yp = y*scale-_offY;   
      if (yp < -1*scale || yp > _height) continue;
      
      for (int x = 0; x < map[0].length; ++x) {
        
        int xp = x*scale-_offX;
        if (xp < -1*scale || xp > _width) continue;
        
        int cell = map[y][x];
        
        if (cell == 0) g.setColor(FLOORC);
        else if (cell == 1) g.setColor(WALLC);
        else g.setColor(FUCK);
        
        g.fillRect(xp, yp, scale, scale);
      }
    }
  }
}

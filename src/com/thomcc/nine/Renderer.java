package com.thomcc.nine;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.thomcc.nine.level.*;

public class Renderer {
  
  
  private static final Color FLOOR = new Color(0xF5, 0xF5, 0xDC);
  private static final Color WALL = new Color(0x8B, 0x5A, 0x2B);
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
    int xo = p.x - _width / 2;
    int yo = p.y - (_height - 8) / 2;
    if (xo < 0) xo = 0;
    if (yo < 0) yo = 0;
    if (xo > l.width*16 - _width) 
      xo = l.width * 16 - _width;
    if (yo > l.height*16 - _height)
      yo = l.height * 16 - _height;
    
    setOffset(xo, yo);
    game.setOffset(xo, yo);
    renderLevel(game.getLevel(), g);
    //setOffset(0, 0);
    renderPlayer(game.getPlayer(), g);
    
    g.dispose();
  }
  private void setOffset(int x, int y) {
    _offX = x; _offY = y;
  }

  public void renderPlayer(Player p, Graphics g) {
    int d = p.getDirection();
    int px = p.x-Art.SIZE/2-_offX;
    int py = p.y-Art.SIZE/2-_offY;
    
    g.drawImage(_sprites[d], px, py, null);
    
  }
  private void renderLevel(Level l, Graphics g) {
    int[][] map = l.map;
    int scale = 16;
    for (int y = 0; y < map.length; ++y) {
      
      int yp = y*scale-_offY;   
      if (yp < -1*scale || yp > _height) continue;
      
      for (int x = 0; x < map[0].length; ++x) {
        
        int xp = x*scale-_offX;
        if (xp < -1*scale || xp > _width) continue;
        
        int cell = map[y][x];
        
        if (cell == 0) g.setColor(FLOOR);
        else if (cell == 1) g.setColor(WALL);
        else g.setColor(FUCK);
        
        g.fillRect(xp, yp, scale, scale);
      }
    }
  }
}

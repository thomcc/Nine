package com.thomcc.nine.render;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.thomcc.nine.entity.Entity;
import com.thomcc.nine.level.Level;

public class Minimap {
  private int[][] _cachedmm;
  private int _cmmw, _cmmh;
  private int _xoff, _yoff;
  private int _xstep, _ystep;
  private Level _level;
  public Minimap(Level l) {
    _level = l;
    
    _cmmw = 60;
    _cmmh = 60;
    _cachedmm = new int[_cmmh][_cmmw];
    calcOffsets();
    redrawMinimap();
  }
  private void calcOffsets() {

    int xr = _level.width/_cmmw;
    int yr = _level.height/_cmmh;
    
    int xoff = (_level.width - xr * _cmmw)/2;
    int yoff = (_level.height - yr * _cmmh)/2;
    if (xoff < 0 || yoff < 0) {

      System.out.format("goddamnit (draw). ld: (%s, %s), step: (%s, %s), mmd: (%s, %s), off: (%s, %s) \n", _level.width, _level.height, xr, yr, _cmmw, _cmmh, xoff, yoff);
      --_cmmw;
      --_cmmh;
      calcOffsets();
    } else {
      _xoff = xoff;
      _yoff = yoff;
      _xstep = xr;
      _ystep = yr;
    }
    System.out.format("goddamnit (draw). ld: (%s, %s), step: (%s, %s), mmd: (%s, %s), off: (%s, %s) \n", _level.width, _level.height, xr, yr, _cmmw, _cmmh, xoff, yoff);

  }
  
  private void redrawMinimap() {
    int[][] map = _level.map;
    for (int y = 0; y < _cmmh; ++y) {
      for (int x = 0; x < _cmmw; ++x) {
        int yy = (int)((_yoff+(y+0.5)*_ystep));
        int xx = (int)((_xoff+(x+0.5)*_xstep));
        int val;
        if (_level.inBounds(xx, yy)) {
          val = map[yy][xx];
        } else {
          System.out.format("goddamnit (render). pix: (%s, %s)\n", xx, yy);
          val = 1;
        }
        _cachedmm[y][x] = val;
      }
    }
  }
  public BufferedImage getImage() {
    BufferedImage mmImg = new BufferedImage(_cmmw, _cmmh, BufferedImage.TYPE_INT_RGB);
    int[] pix = ((DataBufferInt)mmImg.getRaster().getDataBuffer()).getData();
    for (int y = 0; y < _cmmh; ++y)
      for (int x = 0; x < _cmmw; ++x) 
        switch (_cachedmm[y][x]) {
        case 0: pix[x+y*_cmmw] = Renderer.FLOOR; break;
        case -1: case 1: pix[x+y*_cmmw] = Renderer.WALL_OUTER; break;
        case 2: pix[x+y*_cmmh] = Renderer.WALL_INNER; break;
        }
    for (Entity e : _level.getEntities()) {
      if (!e.appearsOnMinimap()) continue;
      int col = e.getColor();
      int x = (int)(((e.x+e.size/2)-_xoff)/_xstep);
      int y = (int)(((e.y+e.size/2)-_yoff)/_ystep);
      if (x+y*_cmmw < pix.length) pix[x+y*_cmmw] = col;
    }
    return mmImg;
  }
}

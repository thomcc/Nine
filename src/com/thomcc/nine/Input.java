package com.thomcc.nine;

import java.awt.event.*;

public class Input implements KeyListener, MouseListener, MouseMotionListener {
  public int mouseX, mouseY;
  public boolean mouseDown;
  public boolean up, down, left, right, debug;
  
  public Input(Nine game) {
    mouseDown = up = down = left = right = false;
    mouseX = mouseY = -1;
    game.addMouseListener(this);
    game.addMouseMotionListener(this);
    game.addKeyListener(this);
  }
  
  private void onKey(KeyEvent e, boolean pressed) {
    switch (e.getKeyCode()) {
    case KeyEvent.VK_UP:
    case KeyEvent.VK_W:
    case KeyEvent.VK_NUMPAD8:
      up = pressed;
      break;
    case KeyEvent.VK_DOWN:
    case KeyEvent.VK_S:
    case KeyEvent.VK_NUMPAD2:
      down = pressed;
      break;
    case KeyEvent.VK_LEFT:
    case KeyEvent.VK_A:
    case KeyEvent.VK_NUMPAD4:
      left = pressed;
      break;
    case KeyEvent.VK_RIGHT:
    case KeyEvent.VK_D:
    case KeyEvent.VK_NUMPAD6:
      right = pressed;
      break;
    case KeyEvent.VK_ESCAPE:
      debug = pressed;
      break;
    }
  }
  public void setMouse(int x, int y) { mouseX = x/2; mouseY = y/2; }
  public void mousePressed(MouseEvent e) { mouseDown = true; }
  public void mouseReleased(MouseEvent e) { mouseDown = false; }
  public void mouseMoved(MouseEvent e) { setMouse(e.getX(), e.getY()); }
  public void mouseExited(MouseEvent e) { mouseX = -1; mouseY = -1; }
  public void mouseClicked(MouseEvent e) {}
  public void mouseEntered(MouseEvent e) {}
  public void keyTyped(KeyEvent e) {}
  public void mouseDragged(MouseEvent e) { setMouse(e.getX(), e.getY()); }
  public void keyPressed(KeyEvent e) { onKey(e, true); }
  public void keyReleased(KeyEvent e) { onKey(e, false); }
}
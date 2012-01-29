package com.thomcc.nine;

public class Settings {
  private boolean playSounds = true;
  private boolean showMinimap = true;
  public Settings() {}
  
  public boolean getPlaySounds() { return playSounds; }
  public void setPlaySounds(boolean ps) { playSounds = ps; }
  public boolean togglePlaySounds() { playSounds = !playSounds; return playSounds; }
  public boolean getShowMinimap() { return showMinimap; }
  public boolean toggleShowMinimap() { showMinimap = !showMinimap;  return showMinimap; }
  public void setShowMinimap(boolean sm) { showMinimap = sm; }
  
}

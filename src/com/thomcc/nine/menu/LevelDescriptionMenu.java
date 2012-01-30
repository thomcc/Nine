package com.thomcc.nine.menu;

import com.thomcc.nine.render.Renderer;


// 320 characters is about as large as i can get away with for the descriptions.
public class LevelDescriptionMenu extends PauseMenu {
  protected String[] description;
  public LevelDescriptionMenu(int level, String description) {
    title = "Level "+level;
    this.items = new MenuItem[0];
    this.description = splitup(description, 40);
    this.padding = 20;
    this.width = 40*Renderer.CHAR_WIDTH;
    this.height = this.description.length*Renderer.CHAR_HEIGHT;
  }
  
  protected void renderContent(Renderer r) {
    int x = getTextX();
    int y = getTextY();
    for (int i = 0; i < description.length; ++i) 
      r.renderString(description[i], x, y+i*Renderer.CHAR_HEIGHT);
  }
  public void clicked(int chosen) {
    g.unPause();
  }
}
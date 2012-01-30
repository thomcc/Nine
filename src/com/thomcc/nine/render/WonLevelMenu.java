package com.thomcc.nine.render;

import com.thomcc.nine.Game;
import com.thomcc.nine.Input;
import com.thomcc.nine.Sound;
import com.thomcc.nine.entity.Player;
import com.thomcc.nine.level.Level;

public class WonLevelMenu extends PauseMenu {
  public WonLevelMenu() {
    title = "Level Complete!";
    this.padding = 20;
    this.width = 40*Renderer.CHAR_WIDTH;
    this.height = 8*Renderer.CHAR_HEIGHT;
    this.items = new MenuItem[] {
        new MenuItem("Cool.",getTextX(), getTextY()+7*Renderer.CHAR_HEIGHT )
    }; 
  }
  public void init(Game game, Input input) {
    super.init(game, input);
    game.playSound(Sound.winLevel);
  }
  private boolean calced = false;
  private boolean added = false;
  private int thingsKilled;
  private int shotsFired;
  private int accuracy;
  private int initScore;
  private long speed;
  private int finalScore;
  private int totalScore;
  
  private void calc() {
    calced = true;
    Level l = g.getLevel();
    Player p = g.getPlayer();
    initScore = l.score;
    totalScore = g.score;
    shotsFired = p.shotsFired;
    thingsKilled = l._enemiesKilled;
    accuracy = (int)(100.0*((double)(shotsFired-thingsKilled))/(double)shotsFired);
    speed = l.getTime()/60;
    
    double secmul;
    if (speed < 25) secmul = 3;
    else if (speed < 50) secmul = 2;
    else if (speed < 100) secmul = 1.5;
    else if (speed < 200) secmul = 1.25;
    else if (speed < 500) secmul = 1.1;
    else secmul = 1;
    
    double accmul;
    
    if (accuracy > 100) accmul = 3;
    else if (accuracy > 75) accmul = 2;
    else if (accuracy > 50) accmul = 1.5;
    else accmul = 1;
    
    finalScore = (int)(initScore*accmul*secmul);
    g.score += finalScore;
  }
  public void renderContent(Renderer r) {
    if (!calced) calc();
    
    String[] content = new String[] {
        
        "Things killed: "+thingsKilled,
        "Shots Fired: "+shotsFired,
        "Accuracy: " + accuracy + "%",
        "Initial score: "+initScore,
        "Speed: "+speed,
        "Modified score: "+finalScore,
        "Total Score: "+totalScore
    };
    
    int xx = getTextX();
    int yy = getTextY();
    for (int i = 0; i < content.length; ++i) {
      r.renderString(content[i], xx, yy+i*Renderer.CHAR_HEIGHT);
    }
  }
  public void clicked(int chosen) {
    if (!added) {
      added = true;
      totalScore += finalScore;
      g.playSound(Sound.scoreUp);
    } else if (chosen >= 0) onSelect(chosen);
    
  }
  protected void onSelect(int which) {
    switch(which) {
    case 0: 
      g.unPause();
      g.start(g.levelNumber+1);
    }
  }
}

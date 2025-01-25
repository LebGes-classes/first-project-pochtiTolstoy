package tasks;

import java.lang.Runnable;
import game.Game;
import entity.Entity;
import level.Level;
import level.LevelSettings;
import gui.Button;

public class RunGameTask implements Runnable {
  private Button caller;

  public RunGameTask(Button caller) {
    this.caller = caller;
  }

  @Override
  public void run() {
    LevelSettings settings = new LevelSettings("Level 1", 4, 0);
    Level level = new Level(settings);
    Game game = new Game(level);
    game.run();
  }
}

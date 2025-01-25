package game;

import util.InputHandler;
import level.Level;

public class GameLogic {
  public void update(InputHandler input, Level level) {
    char key = input.getKeyValue();
    switch (key) {
      case InputHandler.KEY_W:
      case InputHandler.KEY_A:
      case InputHandler.KEY_S:
      case InputHandler.KEY_D:
        level.getPlayer().update(key);
        break;
      case InputHandler.KEY_M:
        level.getPlayer().attack();
        break;
    }
    if (level.getPlayer().outOfLocation() && !level.isLevelComplete()) {
      level.changeLocation();
    }
    level.updateEnemies();
  }
}

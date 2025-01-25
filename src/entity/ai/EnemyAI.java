package entity.ai;

import entity.EntityPlayer;
import entity.EnemyMouse;
import entity.ai.PathFinder;
import util.Position;
import util.GameMath;

public class EnemyAI {
  public static void update(int[][] matrix, EnemyMouse enemy, EntityPlayer player) {
    Position enemyPosition = null;
    Position enemyPos = enemy.getPosition();
    Position playerPos = player.getPosition();
    double distance = GameMath.calculateDistance(enemyPos, playerPos);
    if (enemy.getFovRadius() >= distance || enemy.isChasingPlayer()) {
      enemy.setChasingPlayer(true);
      enemyPosition = PathFinder.findPath(matrix, playerPos, enemyPos);
    }
    if (enemyPosition != null) {
      enemy.move(enemyPosition.getParent());
    }
  }
}

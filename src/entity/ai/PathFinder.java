package entity.ai;

import util.Position;
import util.Queue;
import gfx.Textures;

public abstract class PathFinder {
  private static final int[][] DIRECTIONS = {
    {-1,  0},  // LEFT
    { 1,  0},  // RIGHT
    { 0, -1},  // UP
    { 0,  1}   // DOWN
  };

  public static Position findPath(int[][] matrix, Position start, Position goal) {
    if (!isValid(start.getX(), start.getY(), matrix) ||
        !isValid(goal.getX(), goal.getY(), matrix)) {
      return null;
    }
    int rows = matrix.length;
    int cols = matrix[0].length;

    Queue queue = new Queue(rows * cols);
    queue.enqueue(start);

    boolean[][] visited = new boolean[rows][cols];
    visited[start.getY()][start.getX()] = true;

    while (!queue.isEmpty()) {
      Position current = queue.dequeue();
      if (current.equals(goal)) {
        return current;
      }
      for (int[] direction : DIRECTIONS) {
        int newX = current.getX() + direction[0];
        int newY = current.getY() + direction[1];
        if (isValid(newX, newY, matrix) && 
            Textures.isPathFinderPixel(matrix[newY][newX]) && 
            !visited[newY][newX]) {
          Position neighbor = new Position(newX, newY);
          neighbor.setParent(current);
          queue.enqueue(neighbor);
          visited[newY][newX] = true;
        }
      }
    }
    return null;
  }

  private static boolean isValid(int x, int y, int[][] matrix) {
    return x >= 0 && x < matrix[0].length && y >= 0 && y < matrix.length;
  }

  private static void traversePath(Position node) {
    if (node == null) return;
    while (node != null) {
      node = node.getParent();
    }
  }
}

package util;

public class Position {
  private int x; // position in the grid
  private int y; // position in the grid
  private Position parent;

  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Position node = (Position) obj;
    return x == node.getX() && y == node.getY();
  }

  public int getX() { return x; }

  public int getY() { return y; }

  public Position getParent() { return parent; }

  public void setParent(Position position) { parent = position; }
}

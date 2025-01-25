package entity;

import level.location.Location;
import util.Position;

public abstract class Entity {
  private int x;
  private int y;
  private int width;
  private int height;
  private int health;
  private int damage;
  private Location location;

  public Entity(Location location, int x, int y, 
      int width, int height, int health, int damage) {
    this.location = location;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.health = health;
    this.damage = damage;
  }

  public int getX() { return x; }
  public int getY() { return y; }
  protected Location getLocation() { return location; }

  protected void setX(int x) { this.x = x; }
  protected void setY(int y) { this.y = y; }
  protected void setLocation(Location location) {
    this.location = location;
  }

  protected boolean isEnoughSpace(int x, int y) {
    if (!isValidCoords(x, y)) return false;
    boolean enoughSpace = true;
    int[][] locationMatrix = getLocation().getLocationMatrix();
    for (int h = 0; h < height; ++h) {
      for (int w = 0; w < width; ++w) {
        if (locationMatrix[y + h][x + w - 1] != 0) {
          enoughSpace = false;
        }
      }
    }
    return enoughSpace;
  }

  protected boolean isValidCoords(int x, int y) {
    boolean validCoords = true;
    int[][] locationMatrix = location.getLocationMatrix();  
    for (int h = 0; h < height; ++h) {
      for (int w = 0; w < width; ++w) {
        if (!location.isValidCoord(x + w - 1, y + h)) {
          validCoords = false;
        }
      }
    }
    return validCoords;
  }

  public boolean outOfLocation() { return !isValidCoords(x, y); }

  protected boolean isMoveInOtherLocation(int x, int y) {
    int locationHeight = location.getHeight();
    return (y < 0 && isEnoughSpace(x, 0)) 
        || (y >= locationHeight && isEnoughSpace(x, locationHeight - 1))
        ;
  }

  protected void removeFromLocation(int x, int y) {
    if (!isValidCoords(x, y)) return;
    int[][] locationMatrix = location.getLocationMatrix();
    for (int h = 0; h < height; ++h) {
      for (int w = 0; w < width; ++w) {
        locationMatrix[y + h][x + w - 1] = 0;
      }
    }
  }

  public void setPosition(Position position) {
    setX(position.getX());
    setY(position.getY());
  }

  public Position getPosition() {
    return new Position(getX(), getY());
  }

  public int getHealth() { return health; }

  public void setHealth(int health) { this.health = health; }

  public int getDamage() { return damage; }

  public void takeDamage(int damage) { health -= damage; }
}

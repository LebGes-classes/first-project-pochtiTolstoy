package entity;

import util.*;
import gfx.Textures;
import level.location.Location;

public class EntityPlayer extends Entity {
  private enum State { UP, LEFT, DOWN, RIGHT };
  private enum SpawnType { TOP, BOTTOM };

  private static final int maxHealth = 8;
  private static final int width = 3;
  private static final int height = 1;
  private static final int health = 8;
  private static final int damage = 2;
  public static final int[][] texture = Textures.playerTexture;

  private State state;
  private int fovRadius;
  private boolean alive;

  public EntityPlayer(Location location) {
    super(location, location.getStartPathX() + width + 1, 
        location.getStartPathY(), width, height, health, damage);
    this.state = State.DOWN;
    this.fovRadius = 30;
    this.alive = true;
  }

  public Location getCurrLocation() { return getLocation(); }

  public void update(char keyPressed) {
    int x = getX();
    int y = getY();
    removeFromLocation(x, y);
    switch (keyPressed) {
      case InputHandler.KEY_W:
        setY((isValidMove(x, y - 1)) ? y - 1 : y);
        state = State.UP;
        break;
      case InputHandler.KEY_A:
        setX((isValidMove(x - 1, y)) ? x - 1 : x);
        state = State.LEFT;
        break;
      case InputHandler.KEY_S:
        setY((isValidMove(x, y + 1)) ? y + 1 : y);
        state = State.DOWN;
        break;
      case InputHandler.KEY_D:
        setX((isValidMove(x + 1, y)) ? x + 1 : x);
        state = State.RIGHT;
        break;
      case InputHandler.KEY_M:
        attack();
        break;
    }
    render(getX(), getY());
  }

  private void render(int x, int y) {
    if (!isValidCoords(x, y) || !isEnoughSpace(x, y)) return;
    int[][] locationMatrix = getLocation().getLocationMatrix();  
    switch (state) {
      case UP: 
        locationMatrix[y][x - 1] = texture[0][1];
        locationMatrix[y][x + 0] = texture[0][2];
        locationMatrix[y][x + 1] = texture[0][1];
        break;
      case LEFT: 
        locationMatrix[y][x - 1] = texture[0][0];
        locationMatrix[y][x + 0] = texture[0][1];
        locationMatrix[y][x + 1] = texture[0][1];
        break;
      case DOWN: 
        locationMatrix[y][x - 1] = texture[0][1];
        locationMatrix[y][x + 0] = texture[0][0];
        locationMatrix[y][x + 1] = texture[0][1];
        break;
      case RIGHT: 
        locationMatrix[y][x - 1] = texture[0][1];
        locationMatrix[y][x + 0] = texture[0][1];
        locationMatrix[y][x + 1] = texture[0][0];
        break;
    }
  }

  public void move() {
    removeFromLocation(getX(), getY());
    render(getX(), getY());
  }

  public void spawn() { render(getX(), getY()); }

  public void changeLocation(Location location) {
    int[] startPathCoords = location.getStartPathCoords();
    int[] endPathCoords = location.getEndPathCoords();
    int y = getY();

    if ((y < 0 && getLocation() == location) ||
        (y >= location.getHeight() && getLocation() != location)) {
      setY(startPathCoords[1]);
      state = State.DOWN;
    } else if ((y < 0 && getLocation() != location) || 
               (y > location.getHeight() && getLocation() == location)) {
      setY(endPathCoords[1]);
      state = State.UP;
    }
    setLocation(location);
    spawn();
  }

  private boolean isValidMove(int x, int y) {
    return isEnoughSpace(x, y) || isMoveInOtherLocation(x, y);
  }
  
  public void attack() {
    switch (state) {
      case UP:
        processAttack(getX(), getY() - 2);
        break;
      case LEFT:
        processAttack(getX() - 3, getY());
        break;
      case DOWN:
        processAttack(getX(), getY() + 2);
        break;
      case RIGHT:
        processAttack(getX() + 3, getY());
        break;
    }
  }

  private void processAttack(int x, int y) {
    if (!isValidCoords(x, y)) return;
    EnemyMouse[] enemies = getLocation().getEnemies();
    for (EnemyMouse enemy : enemies) {
      if (isEnemyHitted(x, y, enemy)) {
        enemy.takeDamage(this.getDamage());
        enemy.stun(this.getPosition());
        enemy.checkAlive();
      }
    }
  }

  public void reduceFovRadius() {
    int reductionAmount = 3;
    int minimumFovRadius = 5;
    this.fovRadius = Math.max(this.fovRadius - reductionAmount, minimumFovRadius);
  }

  public void checkAlive() {
    if (getHealth() <= 0) processDeath();
  }

  private void processDeath() {
    removeFromLocation(getX(), getY());
    setHealth(0);
    alive = false;
  }

  public void stun(Position enemyPosition) {
    State newState = determineStunState(enemyPosition);
    if (stunMove(newState)) state = newState;
    move();
  }

  private State determineStunState(Position enemyPosition) {
    int dx = enemyPosition.getX() - this.getX();
    int dy = enemyPosition.getY() - this.getY();
    State newState = (dx > 0) ? State.RIGHT : State.LEFT;
    if (dy != 0) newState = (dy > 0) ? State.DOWN : State.UP;
    return newState;
  }

  private boolean stunMove(State newState) {
    int currX = getX();
    int currY = getY();
    int newX = currX;
    int newY = currY;
    removeFromLocation(currX, currY);
    switch (newState) {
      case UP:    ++newY; break;
      case LEFT:  ++newX; break;
      case DOWN:  --newY; break;
      case RIGHT: --newX; break;
    }
    if (isEnoughSpace(newX, newY)) { 
      setPosition(new Position(newX, newY)); 
    }
    return true;
  }

  private boolean isEnemyHitted(int x, int y, EnemyMouse enemy) {
    boolean isHitted = false;
    for (int h = 0; h < 3; ++h) {
      for (int w = 0; w < 3; ++w) {
        if (getLocation().isValidCoord(x + w - 1, y + h - 1) &&
            x + w - 1 == enemy.getX() &&
            y + h - 1 == enemy.getY()) {
          isHitted = true;
        }
      }
    }
    return isHitted;
  }

  public int getFovRadius() { return fovRadius; }

  public boolean isAlive() { return alive; }

  public int getHealth() { return super.getHealth(); }

  public static int getMaxHealth() { return maxHealth; }
}

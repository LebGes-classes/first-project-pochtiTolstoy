package entity;

import java.util.Random;

import entity.Hitbox;
import entity.ai.EnemyAI;
import gfx.Textures;
import level.location.Location;
import util.*;

public class EnemyMouse extends Entity {
  private enum State { UP, LEFT, DOWN, RIGHT };
  private static final Hitbox ATTACK_HITBOX_LEFT = new Hitbox(2, 1, 3, 0);
  private static final Hitbox ATTACK_HITBOX_RIGHT = new Hitbox(2, 1, -2, 0);
  private static final Hitbox ATTACK_HITBOX_DOWN = new Hitbox(7, 2, 3, 0);
  private static final Hitbox ATTACK_HITBOX_UP = new Hitbox(7, 2, 3, 1);
  private static final Hitbox ENTITY_HITBOX_VERTICAL = new Hitbox(1, 1, 0, 0);
  private static final Hitbox ENTITY_HITBOX_HORIZONTAL = new Hitbox(3, 1, 1, 0);
  public static final int MAX_FOV = 25;
  public static final int MIN_FOV = 5;
  private static final int attackTick = 18;
  private static final int width = 3;
  private static final int height = 1;
  private static final int health = 3;
  private static final int damage = 1;
  public static final int[][] texture = Textures.mouseEnemyTexture;

  private EntityPlayer targetEntity;
  private State state;
  private Random random;
  private boolean draw;
  private boolean alive;
  private int fovRadius;
  private boolean chasingPlayer;
  private boolean stunned;
  private int currAttackTick;

  public EnemyMouse(Location location, EntityPlayer targetEntity) {
    super(location, 0, 0, width, height, health, damage);
    this.random = new Random();
    this.alive = true;
    this.draw = false;
    this.state = State.UP;
    this.fovRadius = MIN_FOV + random.nextInt(MAX_FOV - MIN_FOV + 1);
    this.chasingPlayer = false;
    this.stunned = false;
    this.targetEntity = targetEntity;
    this.currAttackTick = 0;
    spawn();
  }

  private void spawn() {
    setPosition(getRandomPosition());
    render(getX(), getY());
  }

  private Position getRandomPosition() {
    Position newPostion = new Position(-1, -1);
    while (!getLocation().isValidCoord(newPostion) || 
           !getLocation().isFreePath(newPostion)) {
      int newX = random.nextInt(getLocation().getWidth());
      int newY = random.nextInt(getLocation().getHeight());
      newPostion = new Position(newX, newY);
    }
    return newPostion;
  }

  public Location getCurrLocation() { return getLocation(); }

  public void update() {
    draw = !draw;
    if (!alive || !draw) return;
    if (stunned) {
      stunned = false;
      move();
      return;
    }
    EnemyAI.update(getLocation().getLocationMatrix(), this, targetEntity);
    if (currAttackTick++ == attackTick) {
      this.attack();
      currAttackTick = 0;
    }
  }

  private void attack() {
    if (isTargetEntityHitted(getX(), getY())) {
      targetEntity.takeDamage(this.getDamage());
      targetEntity.stun(this.getPosition());
      targetEntity.reduceFovRadius();
      targetEntity.checkAlive();
    }
  }

  private boolean isTargetEntityHitted(int x, int y) {
    Hitbox attackHitbox = getAttackHitboxForState();
    boolean isHitted = false;
    for (int h = 0; h < attackHitbox.getHeight(); ++h) {
      for (int w = 0; w < attackHitbox.getWidth(); ++w) {
        int targetX = x + w - attackHitbox.getShiftX();
        int targetY = y + h - attackHitbox.getShiftY();
        if (getLocation().isValidCoord(targetX, targetY) &&
            targetX == targetEntity.getX() &&
            targetY == targetEntity.getY()) {
          isHitted = true;
        }
      }
    }
    return isHitted;
  }

  private boolean stunMove(State newState) {
    int currX = getX();
    int currY = getY();
    if (!isValidCoords(currX, currY)) {
      return false;
    }
    removeFromLocation(currX, currY);
    Position newPosition = getStunPosition(newState);
    if (!isFreeSpace(newPosition, newState)) {
      return false;
    }
    setPosition(newPosition);
    return true;
  }

  private Position getStunPosition(State newState) {
    switch (newState) {
      case UP:
        return new Position(getX(), getY() + 1);
      case LEFT:
        return new Position(getX() + 1, getY());
      case DOWN:
        return new Position(getX(), getY() - 1);
      case RIGHT:
        return new Position(getX() - 1, getY());
      default:
        return new Position(-1, -1);
    }
  }
  
  public void move() {
    removeFromLocation(getX(), getY());
    render(getX(), getY());
  }

  public void move(Position newPosition) {
    if (newPosition == null) return;

    int currX = getX();
    int currY = getY();
    int newX = newPosition.getX();
    int newY = newPosition.getY();
    removeFromLocation(currX, currY);
    
    State newState = getNewState(newX, newY);
    if (isValidMove(newX, newY)) {
      state = newState;
    } else if (isHorizontalFree(currX, currY)) {
      state = newState;
    }
      
    setX(isValidMove(newX, newY) ? newX : currX);
    setY(isValidMove(newX, newY) ? newY : currY);
    render(getX(), getY());
  }

  private boolean isHorizontalFree() { 
    return isHorizontalFree(getX(), getY());
  }

  private boolean isHorizontalFree(Position position) { 
    return isHorizontalFree(position.getX(), position.getY());
  }

  private boolean isHorizontalFree(int x, int y) {
    int[][] matrix = getLocation().getLocationMatrix();
    return (matrix[y][x - 1] == 0) && 
           (matrix[y][x + 0] == 0) && 
           (matrix[y][x + 1] == 0);
  }

  private boolean isVerticalFree() { 
    return isVerticalFree(getX(), getY());
  }

  private boolean isVerticalFree(Position position) { 
    return isVerticalFree(position.getX(), position.getY());
  }

  private boolean isVerticalFree(int x, int y) {
    int[][] matrix = getLocation().getLocationMatrix();
    return (matrix[y][x] == 0);
  }

  private void render(int x, int y) {
    if (!isValidCoords(x, y) || !isEnoughSpace(x, y)) return;
    int[][] locationMatrix = getLocation().getLocationMatrix();
    switch (state) {
      case UP:
        locationMatrix[y][x] = 107;
        break;
      case LEFT:
        locationMatrix[y][x - 1] = 106;
        locationMatrix[y][x + 0] = 104;
        locationMatrix[y][x + 1] = 103;
        break;
      case DOWN:
        locationMatrix[y][x] = 108;
        break;
      case RIGHT:
        locationMatrix[y][x - 1] = 103;
        locationMatrix[y][x + 0] = 104;
        locationMatrix[y][x + 1] = 105;
        break;
    }
  }

  private boolean isValidMove(int x, int y) {
    return isEnoughSpace(x, y);
  }

  private boolean isFreeSpace(Position position, State state) {
    switch (state) {
      case UP:    
      case DOWN: 
        return isVerticalFree(position);
      case LEFT: 
      case RIGHT: 
        return isHorizontalFree(position);
      default:               
        return false;
    }
  }

  @Override
  protected boolean isEnoughSpace(int x, int y) {
    if (!isValidCoords(x, y)) return false;
    boolean enoughSpace = true;
    State newState = getNewState(x, y);
    int[][] locationMatrix = getLocation().getLocationMatrix();
    enoughSpace = (locationMatrix[y][x] == 0);
    if (newState == State.LEFT || newState == State.RIGHT) {
      for (int w = 0; w < width; ++w) {
        if (locationMatrix[y][x + w - 1] != 0) {
          enoughSpace = false; 
        }
      }
    }
    return enoughSpace;
  }
  
  @Override
  protected void removeFromLocation(int x, int y) {
    if (!isValidCoords(x, y)) return;
    int[][] locationMatrix = getLocation().getLocationMatrix();
    locationMatrix[y][x] = 0;
    if (state == State.LEFT || state == State.RIGHT) {
      for (int w = 0; w < width; ++w) {
        locationMatrix[y][x + w - 1] = 0;
      }
    }
  }

  private State getNewState(int newX, int newY) {
    int dx = newX - getX();
    int dy = newY - getY();
    State state = this.state;
    if (dy < 0) state = State.UP;
    if (dx < 0) state = State.LEFT;
    if (dy > 0) state = State.DOWN;
    if (dx > 0) state = State.RIGHT;
    return state;
  }

  public void checkAlive() {
    if (getHealth() <= 0) processDeath();
  }
  
  private void processDeath() {
    removeFromLocation(getX(), getY());
    setHealth(0);
    alive = false;
  }

  public boolean isAlive() { return alive; }

  public int getFovRadius() { return fovRadius; }

  public boolean isChasingPlayer() { return chasingPlayer; }

  public void setChasingPlayer(boolean chasing) { chasingPlayer = chasing; }

  public void stun(Position enemyPosition) {
    State newState = determineStunState(enemyPosition);
    if (stunMove(newState)) state = newState;
    stunned = true;
  }

  private State determineStunState(Position enemyPosition) {
    int dx = enemyPosition.getX() - this.getX();
    int dy = enemyPosition.getY() - this.getY();
    State newState = (dx > 0) ? State.RIGHT : State.LEFT;
    if (dy != 0) newState = (dy > 0) ? State.DOWN : State.UP;
    return newState;
  }

  public boolean isStunned() { return stunned; }

  public void setStunned(boolean stunned) { this.stunned = stunned; }

  public EntityPlayer getTargetEntity() { return targetEntity; }

  private Hitbox getAttackHitboxForState() {
    switch (state) {
      case LEFT:  
        return ATTACK_HITBOX_LEFT;
      case RIGHT: 
        return ATTACK_HITBOX_RIGHT;
      case DOWN:  
        return ATTACK_HITBOX_DOWN;
      case UP:    
        return ATTACK_HITBOX_UP;
      default:    
        return new Hitbox(0, 0, 0, 0);
    }
  }
}

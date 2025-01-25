package level.location;

import util.GameMetaData;
import level.generator.CaveGenerator;
import level.generator.PathGenerator;
import entity.EnemyMouse;
import entity.EntityPlayer;
import util.Position;
import gfx.Textures;

public class Location {
  private int locationMatrix[][];
  private String name;
  private int width;
  private int height;
  private LocationSettings settings;
  private EnemyMouse[] enemies;
  private int numEnemies;

  public Location(LocationSettings settings) {
    this.name = settings.getName();
    this.width = settings.getWidth();
    this.height = settings.getHeight();
    this.settings = settings;
    this.locationMatrix = new int[height][width];
    this.numEnemies = 20;
    generateLocation();
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public int[][] getLocationMatrix() {
    return locationMatrix;
  }

  public int[] getStartPathCoords() {
    return new int[]{getStartPathX(), getStartPathY()};
  }

  public int[] getEndPathCoords() {
    return new int[]{getEndPathX(), getEndPathY()};
  }

  public int getStartPathX() { return settings.getStartPathCoord(); }

  public int getStartPathY() { return 0; }

  public int getEndPathX() { return settings.getEndPathCoord(); }

  public int getEndPathY() { return height - 1; }

  private void generateLocation() {
    CaveGenerator caveGenerator = new CaveGenerator(settings, locationMatrix);
    caveGenerator.generateCave();

    PathGenerator pathGenerator = new PathGenerator(settings, locationMatrix);
    pathGenerator.generatePath();
  }

  public boolean isValidCoord(Position position) {
    return isValidCoord(position.getX(), position.getY());
  }

  public boolean isValidCoord(int x, int y) {
    return (x >= 0 && x < width && y >= 0 && y < height);
  }

  public boolean isFreePath(Position position) {
    return isFreePath(position.getX(), position.getY());
  }

  public boolean isFreePath(int x, int y) {
    return locationMatrix[y][x] == Textures.pathTexture[0][0];
  }

  public void generateEnemies(EntityPlayer player) {
    enemies = new EnemyMouse[numEnemies];
    for (int i = 0; i < numEnemies; ++i) {
      enemies[i] = new EnemyMouse(this, player);
    }
  }

  public void updateEnemies(EntityPlayer player) {
    for (int i = 0; i < numEnemies; ++i) {
      if (enemies[i].isAlive()) {
        enemies[i].update();
      } else {
        enemies[i] = new EnemyMouse(this, player);
      }
    }
  }

  public EnemyMouse[] getEnemies() { return enemies; }

  public String getName() { return name; }
}

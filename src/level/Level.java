package level;

import level.location.Location;
import level.location.LocationSettings;
import util.GameMetaData.PatternType;
import entity.EntityPlayer;

public class Level {
  private static final int locationWidth = 172;
  private static final int locationHeight = 48;

  private Location[] locations;
  private int numLocations;
  private int currLocationId;
  private EntityPlayer player;
  private boolean complete;

  public Level(LevelSettings settings) {
    this.numLocations = settings.getNumLocations();
    this.currLocationId = settings.getFirstLocationId();
    this.complete = false;
    locations = new Location[numLocations];
    generateLocations();
    generatePlayer();
    generateEnemies();
  }

  private void generateLocations() {
    LocationSettings locationSettings = new LocationSettings(
        "Cave", locationWidth, locationHeight);

    for (int i = 0; i < numLocations; ++i) {
      if (i == 0) locationSettings.setFirstLocation();
      if (i == numLocations - 1) locationSettings.setLastLocation();
      locations[i] = new Location(locationSettings);
      locationSettings = new LocationSettings(
          "Cave", locationWidth, locationHeight, locationSettings.getEndPathCoord());
    }
  }

  private void generateEnemies() {
    for (int i = 0; i < numLocations; ++i) {
      locations[i].generateEnemies(player);
    }
  }

  public void updateEnemies() {
    locations[currLocationId].updateEnemies(player);
  }

  private void generatePlayer() {
    this.player = new EntityPlayer(locations[currLocationId]);
    this.player.spawn();
  }

  public Location getCurrLocation() {
    return locations[currLocationId];
  }

  public void nextLocation() {
    if (currLocationId < numLocations - 1) {
      ++currLocationId;
    } else {
      complete = true;
    }
  }

  public void prevLocation() {
    if (currLocationId > 0) {
      --currLocationId;
    }
  }

  public void changeLocation() {
    if (player.getY() < 0) prevLocation();
    if (player.getY() >= locationHeight) nextLocation();
    player.changeLocation(locations[currLocationId]);
  }

  public boolean isLevelComplete() { return complete; }

  public EntityPlayer getPlayer() { return player; }

  public boolean isPlayerAlive() { return player.isAlive(); }
}

package level.location;

import java.util.Random;
import util.GameMetaData;
import util.GameMetaData.PatternType;
import util.GameMetaData.PathInfo;

public class LocationSettings {
  private static final int MIN_WIDTH = 11;
  private static final int MIN_HEIGHT = 1;
  private String name;
  private int width;
  private int height;
  private PatternType patternType;
  private int startPathCoord;
  private int endPathCoord;
  private boolean firstLocation;
  private boolean lastLocation;

  public LocationSettings(String name, int width, int height) {
    this.name = name;
    this.width = (width < MIN_WIDTH) ? MIN_WIDTH : width;
    this.height = (height < MIN_HEIGHT) ? MIN_HEIGHT : height;
    this.patternType = patternType.DEFAULT_PATTERN;
    this.startPathCoord = generateStartPathCoord();
  }

  public LocationSettings(String name, int width, int height, int startPathCoord) {
    this(name, width, height);
    this.startPathCoord = startPathCoord;
  }

  public String getName() {
    return name;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public PatternType getPatternType() {
    return patternType;
  }

  public int getStartPathCoord() {
    return startPathCoord;
  }

  public int getEndPathCoord() {
    return endPathCoord;
  }

  public void setEndPathCoord(int endPathCoord) {
    this.endPathCoord = endPathCoord;
  }

  private int generateStartPathCoord() {
    Random random = new Random();
    return PathInfo.leftOffset + random.nextInt(width - PathInfo.verticalWidth - PathInfo.leftOffset);
  }

  public void setLastLocation() { lastLocation = true; }
  
  public void setFirstLocation() { firstLocation = true; }

  public boolean isFirstLocation() { return firstLocation; }

  public boolean isLastLocation() { return lastLocation; }
}

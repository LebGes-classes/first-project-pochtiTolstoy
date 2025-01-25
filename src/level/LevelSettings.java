package level;

public class LevelSettings {
  private String name;  
  private int numLocations;
  private int firstLocationId;

  public LevelSettings(String name, int numLocations, int firstLocationId) {
    this.name = name;  
    this.numLocations = numLocations;
    this.firstLocationId = firstLocationId;
  }
  public int getNumLocations() {
    return numLocations;
  }

  public int getFirstLocationId() {
    return firstLocationId;
  }
}

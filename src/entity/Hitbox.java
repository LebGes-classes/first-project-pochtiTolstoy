package entity;

public class Hitbox {
  private final int width;
  private final int height;
  private final int shiftX;
  private final int shiftY;

  Hitbox(int width, int height, int shiftX, int shiftY) {
    this.width = width;
    this.height = height;
    this.shiftX = shiftX;
    this.shiftY = shiftY;
  }

  public int getWidth() { return width; }

  public int getHeight() { return height; }

  public int getShiftX() { return shiftX; }

  public int getShiftY() { return shiftY; }
}

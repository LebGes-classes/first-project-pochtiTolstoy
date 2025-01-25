package level.generator;

import java.util.Random;
import level.location.LocationSettings;
import util.GameMetaData.PatternType;
import util.GameMetaData.PixelType;
import util.GameMath;

public class CaveGenerator {
  private static final int numCavePixels = 10;
  private static final int offsetCavePixels = 2;

  private LocationSettings locationSettings;
  private int[][] locationMatrix;

  public CaveGenerator(LocationSettings locationSettings, int[][] locationMatrix) {
    this.locationSettings = locationSettings;
    this.locationMatrix = locationMatrix;
  }

  public void generateCave() {
    writePixelsToMatrix();
    processSmoothMatrix();
  }

  private void writePixelsToMatrix() {
    Random random = new Random();
    for (int row = 0; row < locationSettings.getHeight(); ++row) {
      for (int col = 0; col < locationSettings.getWidth(); ++col) {
        locationMatrix[row][col] = getPatternPixel(locationSettings.getPatternType(), random);
      }
    }
  }

  private void processSmoothMatrix() {
    int rows = locationSettings.getHeight();
    int cols = locationSettings.getWidth();
    for (int row = 0; row < rows; ++row) {
      for (int col = 0; col < cols; ++col) {
        writeSmoothPixel(row, col);
      }
    }
  }

  private int getPatternPixel(PatternType type, Random random) {
    int pixelValue = PixelType.UNDEFINED_PIXEL.getValue();
    switch (type) {
      case DEFAULT_PATTERN: 
        pixelValue = offsetCavePixels + random.nextInt(numCavePixels);
        break;
    }
    return pixelValue;
  }

  private void writeSmoothPixel(int row, int col) {
    int rows = locationSettings.getHeight();
    int cols = locationSettings.getWidth();
    int[] filterPixels = new int[4];
    int filterSize = 0;
    if (col > 0)
      filterPixels[filterSize++] = locationMatrix[row][col - 1];
    if (col < cols - 1)
      filterPixels[filterSize++] = locationMatrix[row][col + 1];
    if (row > 0)
      filterPixels[filterSize++] = locationMatrix[row - 1][col];
    if (row < rows - 1)
      filterPixels[filterSize++] = locationMatrix[row + 1][col];
    int newPixel = GameMath.getAvgFromArray(filterPixels, filterSize);
    for (int i = 0; i < filterSize; ++i) {
      if (GameMath.abs(locationMatrix[row][col] - filterPixels[i]) > 1) {
        locationMatrix[row][col] = GameMath.getAvgFromArray(filterPixels, filterSize);
        filterSize = 0;
      }
    }
  }
}

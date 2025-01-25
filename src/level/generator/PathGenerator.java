package level.generator;

import java.util.Random;
import java.io.FileReader;
import java.io.IOException;
import level.location.LocationSettings;
import util.GameMetaData.PathInfo;
import util.GameMetaData.TilesFileInfo;

public class PathGenerator {
  private static int minPathPercentage = 40;
  private static int maxPathPercentage = 60;
  private enum MovementType {
    VERTICAL_DOWN, 
    HORIZONTAL_LEFT, 
    HORIZONTAL_RIGHT,
    HVL_ROTATE,
    HVR_ROTATE,
    VHL_ROTATE,
    VHR_ROTATE,
    UNDEFINED_MOVE;

    private static final MovementType[][] associatedMovements;

    static {
      associatedMovements = new MovementType[values().length][];

      associatedMovements[VERTICAL_DOWN.ordinal()] = new MovementType[] {VERTICAL_DOWN, VHL_ROTATE, VHR_ROTATE};
      associatedMovements[HORIZONTAL_LEFT.ordinal()] = new MovementType[] {HORIZONTAL_LEFT, HVR_ROTATE};
      associatedMovements[HORIZONTAL_RIGHT.ordinal()] = new MovementType[] {HORIZONTAL_RIGHT, HVL_ROTATE};
      associatedMovements[HVL_ROTATE.ordinal()] = new MovementType[] {VERTICAL_DOWN, VHL_ROTATE, VHR_ROTATE};
      associatedMovements[HVR_ROTATE.ordinal()] = new MovementType[] {VERTICAL_DOWN, VHL_ROTATE, VHR_ROTATE};
      associatedMovements[VHL_ROTATE.ordinal()] = new MovementType[] {HORIZONTAL_LEFT, HVR_ROTATE};
      associatedMovements[VHR_ROTATE.ordinal()] = new MovementType[] {HORIZONTAL_RIGHT, HVL_ROTATE};
      associatedMovements[UNDEFINED_MOVE.ordinal()] = new MovementType[] {VERTICAL_DOWN};
    }

    public MovementType[] getAssociatedMovements() {
      return associatedMovements[this.ordinal()];
    }
  }

  private enum PathStyle {
    STRAIGHT, WAVING
  }

  private int currY;
  private int currX;
  private MovementType movement;
  private Random random;
  private LocationSettings locationSettings;
  int[][] backupLocationMatrix;
  int[][] newLocationMatrix;

  public PathGenerator(LocationSettings locationSettings, int[][] locationMatrix) {
    this.currY = 0;
    this.currX = locationSettings.getStartPathCoord();
    this.movement = MovementType.VERTICAL_DOWN;
    this.locationSettings = locationSettings;
    this.random = new Random();
    this.newLocationMatrix = locationMatrix;
    this.backupLocationMatrix = 
        new int[locationSettings.getHeight()][locationSettings.getWidth()];
    copyMatrix(this.backupLocationMatrix, this.newLocationMatrix);
  }

  public void generatePath() {
    processPathGeneration();
    closePathOutputs();
  }

  private void processPathGeneration() {
    reinitStartCoords();
    writeVerticalPath(PathStyle.STRAIGHT);
    fixNextPixelCoord();
    while (currY < locationSettings.getHeight()) {
      generateNewMovement();
      switch (movement) {
        case VERTICAL_DOWN: 
          writeVerticalPath(PathStyle.WAVING); 
          break;
        case HORIZONTAL_LEFT: 
        case HORIZONTAL_RIGHT: 
          writeHorizontalPath(PathStyle.WAVING);
          break;
        case VHR_ROTATE: 
          writeRotateFromFile(0, currX, currY);
          break;
        case HVR_ROTATE: 
          writeRotateFromFile(1, currX, currY);
          break;
        case VHL_ROTATE: 
          writeRotateFromFile(2, currX, currY);
          break;
        case HVL_ROTATE: 
          writeRotateFromFile(3, currX - PathInfo.verticalWidth + 1, currY);
          break;
      }
      fixNextPixelCoord(); 
    }
    double pathPixelsPercent = getPathPixelsPercent();
    if (pathPixelsPercent > maxPathPercentage) {
      copyMatrix(newLocationMatrix, backupLocationMatrix);
      pathPixelsPercent = getPathPixelsPercent();
    } else {
      copyMatrix(backupLocationMatrix, newLocationMatrix);
      locationSettings.setEndPathCoord(currX);
    }
    if (pathPixelsPercent < minPathPercentage) generatePath();
  }

  private void closePathOutputs() {
    int lastRow = locationSettings.getHeight() - 1;
    int cols = locationSettings.getWidth();
    for (int col = 0; col < cols; ++col) {
      int pixelValue = newLocationMatrix[lastRow][col];
      if (pixelValue == 0 && (col < currX || col > currX + PathInfo.verticalWidth - 1)) {
        newLocationMatrix[lastRow][col] = 1;
      }
    }
    newLocationMatrix[lastRow][currX] = 1;
    newLocationMatrix[lastRow][currX + PathInfo.verticalWidth - 1] = 1;
  }
  
  private void reinitStartCoords() {
    this.currX = locationSettings.getStartPathCoord();
    this.currY = 0;
  }

  private double getPathPixelsPercent() {
    double area = locationSettings.getWidth() * locationSettings.getHeight(); 
    int pathPixels = calculatePathPixels();
    return (pathPixels * 100.0) / area;
  }

  private int calculatePathPixels() {
    int counter = 0;
    for (int row = 0; row < locationSettings.getHeight(); ++row) {
      for (int col = 0; col < locationSettings.getWidth(); ++col) {
        if (newLocationMatrix[row][col] == 0) ++counter;
      }
    }
    return counter;
  }

  private void writeRotateFromFile(int tileId, int writeX, int writeY) {
    try (FileReader reader = new FileReader(TilesFileInfo.rotateTilesFilePath)) {
      int ch;
      while (reader.read() != '\n');
      while ((ch = reader.read()) != '-') {
        reader.skip(tileId * TilesFileInfo.rotateTilesOffset);
        for (int i = 0; i < PathInfo.verticalWidth; ++i) {
          if ((ch = reader.read()) != '?' && newLocationMatrix[writeY][writeX + i] != 0) {
            newLocationMatrix[writeY][writeX + i] = ch - '0';
          }
        }
        while (reader.read() != '\n');
        ++writeY;
      }
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Error while reading file: " + TilesFileInfo.rotateTilesFilePath);
      System.exit(-1);
    }
  }

  private void writeVerticalPath(PathStyle style) {
    switch (style) {
      case STRAIGHT:
        writeVerticalPathTile();
        break;
      case WAVING:
        shiftX();
        writeVerticalPathTile();
        break;
    }
  }

  private void writeHorizontalPath(PathStyle style) {
    switch (style) {
      case STRAIGHT:
        writeHorizontalPathTile();
        break;
      case WAVING:
        shiftY();
        writeHorizontalPathTile();
        break;
    }
  }

  private void writeVerticalPathTile() {
    if (newLocationMatrix[currY][currX] != 0)
      newLocationMatrix[currY][currX] = 1;
    if (newLocationMatrix[currY][currX + PathInfo.verticalWidth - 1] != 0)
      newLocationMatrix[currY][currX + PathInfo.verticalWidth - 1] = 1;
    for (int i = 1; i < PathInfo.verticalWidth - 1; ++i) {
      newLocationMatrix[currY][currX + i] = 0;
    }
  }

  private void writeHorizontalPathTile() {
    if (newLocationMatrix[currY][currX] != 0)
      newLocationMatrix[currY][currX] = 1;
    if (newLocationMatrix[currY + PathInfo.horizontalWidth - 1][currX] != 0)
      newLocationMatrix[currY + PathInfo.horizontalWidth - 1][currX] = 1;
    for (int i = 1; i < PathInfo.horizontalWidth - 1; ++i) {
      newLocationMatrix[currY + i][currX] = 0;
    }
  }

  private void shiftX() {
    currX += random.nextInt(3) - 1;
    if (currX < 0) currX = 0;
    if (currX > locationSettings.getWidth() - PathInfo.verticalWidth) {
      --currX;
    }
  }

  private void shiftY() {
    currY += random.nextInt(3) - 1;
    if (currY < 0) currY = 0;
    if (currY >= locationSettings.getHeight() - PathInfo.horizontalWidth) {
      --currY;
    }
  }

  private void fixNextPixelCoord() {
    switch (movement) {
      case VERTICAL_DOWN: 
        ++currY;
        break;
      case HORIZONTAL_LEFT: 
        ++currX;
        break;
      case HORIZONTAL_RIGHT: 
        --currX;
        break;
      case VHR_ROTATE: 
        --currX;
        break;
      case HVR_ROTATE: 
        currY += PathInfo.horizontalWidth;
        break;
      case VHL_ROTATE: 
        currX += PathInfo.verticalWidth;
        break;
      case HVL_ROTATE: 
        currX -= PathInfo.verticalWidth - 1;
        currY += PathInfo.horizontalWidth;
        break;
    }
  }

  private void generateNewMovement() {
    MovementType newMovement = MovementType.UNDEFINED_MOVE;
    MovementType[] moveArr = movement.getAssociatedMovements();
    int lenMoveArr = moveArr.length;
    int chance = 0;
    while (!isValidMovement(newMovement)) {
      chance = random.nextInt(100);
      switch (movement) {
        case HORIZONTAL_RIGHT:
        case HORIZONTAL_LEFT:
          if (chance < 99) {
            newMovement = movement;
          } else {
            newMovement = moveArr[random.nextInt(lenMoveArr)];
          }
          break;
        case VHR_ROTATE:
          if (chance < 99) {
            newMovement = MovementType.HORIZONTAL_RIGHT;
          } else {
            newMovement = moveArr[random.nextInt(lenMoveArr)];
          }
          break;
        case VHL_ROTATE:
          if (chance < 99) {
            newMovement = MovementType.HORIZONTAL_LEFT;
          } else {
            newMovement = moveArr[random.nextInt(lenMoveArr)];
          }
          break;
        default:
          newMovement = moveArr[random.nextInt(lenMoveArr)];
      }
    }
    movement = newMovement;
  }

  private boolean isValidMovement(MovementType newMovement) {
    boolean result = true;
    switch (newMovement) {
      case VERTICAL_DOWN:
        result = true;
        break;
      case HORIZONTAL_LEFT:
        if (currX >= locationSettings.getWidth() - PathInfo.verticalWidth) {
          result = false;
        }
        break;
      case HORIZONTAL_RIGHT:
        if (currX < PathInfo.verticalWidth) {
          result = false;
        }
        break;
      case VHR_ROTATE: 
        if (currX < PathInfo.verticalWidth || 
            currY >= locationSettings.getHeight() - PathInfo.horizontalWidth) {
          result = false;
        }
        break;
      case HVR_ROTATE: 
        if (currX > locationSettings.getWidth() - PathInfo.verticalWidth ||
            currY >= locationSettings.getHeight() - PathInfo.horizontalWidth) {
          result = false;
        }
        break;
      case VHL_ROTATE: 
        if (currX > locationSettings.getWidth() - 2 * PathInfo.verticalWidth ||
            currY >= locationSettings.getHeight() - PathInfo.horizontalWidth) {
          result = false;
        }
        break;
      case HVL_ROTATE: 
        if (currX < PathInfo.verticalWidth - 1 ||
            currY >= locationSettings.getHeight() - PathInfo.horizontalWidth) {
          result = false;
        }
        break;
      default:
        result = false;
    }
    return result;
  }

  private void copyMatrix(int[][] target, int[][] source) {
    for (int row = 0; row < locationSettings.getHeight(); ++row) {
      for (int col = 0; col < locationSettings.getWidth(); ++col) {
        target[row][col] = source[row][col];
      }
    }
  }
}

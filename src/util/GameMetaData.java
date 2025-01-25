package util;

public class GameMetaData {
  public enum PatternType {
    DEFAULT_PATTERN
  }

  public enum PixelType {
    UNDEFINED_PIXEL(-1), 
    ROAD_PIXEL(0),
    BORDER_PIXEL(1);

    private final int value;
    PixelType(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }

  public class TilesFileInfo {
    public static final String rotateTilesFilePath = "../res/rotate_tiles.txt";
    public static final int rotateTilesOffset = 10;

    public static final String focusedButtonsTilesFilePath = "../res/focused_buttons_tiles.txt";
    public static final String unfocusedButtonsTilesFilePath = "../res/unfocused_buttons_tiles.txt";
    public static final int buttonsTilesOffset = 19;

    public static final String helpTextFilePath = "../res/helpText.txt";
    public static final int helpTextWidth = 42;

    public static final String healthUIFilePath = "../res/health_ui.txt";
    public static final String emptyHealthUIFilePath = "../res/empty_health_ui.txt";
    public static final int healthUIWidth = 5;
  }

  public class PathInfo {
    public static final int verticalWidth = 9;
    public static final int horizontalWidth = 6;
    public static final int leftOffset = 1;
  }
}

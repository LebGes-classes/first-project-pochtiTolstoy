package gui;

import java.io.*;
import util.GameMetaData.TilesFileInfo;

public class Button {
  public enum ButtonType { PLAY, HELP, QUIT, OK }
  private static int width = 18;
  private static int height = 3;
  private int x, y;
  private ButtonType type;
  private Runnable action;
  private boolean pressed;
  private boolean focused;

  public Button(int x, int y, ButtonType type) {
    this.x = x;
    this.y = y;
    this.type = type;
    this.pressed = false;
    this.focused = false;
  }

  public void setTask(Runnable action) {
    this.action = action;
  }

  public void display(char[][] window) {
    int writeX = x;
    int writeY = y;
    String filePath = (focused) 
        ? TilesFileInfo.focusedButtonsTilesFilePath
        : TilesFileInfo.unfocusedButtonsTilesFilePath 
        ;
    try (FileReader reader = new FileReader(filePath)) {
      int ch;
      while (reader.read() != '\n');
      while ((ch = reader.read()) != '-') {
        reader.skip(type.ordinal() * TilesFileInfo.buttonsTilesOffset);
        for (int i = 0; i < width; ++i) {
          ch = reader.read();
          window[writeY][writeX + i] = (char) ch;
        }
        while (reader.read() != '\n');
        ++writeY;
      }
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Error while reading file: " + filePath);
      System.exit(-1);
    }
  }

  public void click() {
    if (action != null) {
      action.run();
    }
  }

  public boolean isPressed() { return pressed; }

  public void unfocus() { this.focused = false; }

  public void focus() { this.focused = true; }

  public void setPressed(boolean pressed) { this.pressed = pressed; }
}

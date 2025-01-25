package gfx;

import java.io.*;
import gui.Button;
import util.GameMetaData.TilesFileInfo;

public class RendererHelp {
  public static final int FPS = 60;
  private static final int width = 174;
  private static final int height = 62;
  private static final int textX = 72;
  private static final int textY = 17;

  private Button button;
  private PrintWriter out;
  private char[][] window;

  public RendererHelp(Button button) {
    this.button = button;
    this.out = new PrintWriter(System.out);
    this.window = new char[height][width];
    displayMenuBackground();
    displayHelpText();
  }

  private void displayHelpText() {
    int writeX = textX;
    int writeY = textY;
    String filePath = TilesFileInfo.helpTextFilePath;
    try (FileReader reader = new FileReader(filePath)) {
      int ch;    
      while (reader.read() != '\n');
      while ((ch = reader.read()) != '-') {
        for (int i = 0; i < TilesFileInfo.helpTextWidth; ++i) {
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

  public void render() {
    clearScreen(); 
    displayMenuButtons();
    for (int row = 0; row < height; ++row) {
      for (int col = 0; col < width; ++col) {
        out.print(window[row][col] + "");
      }
      out.print("\n");
    }
    out.flush();
  }

  private void clearScreen() {
    out.print(("\033[2J\033[1;1H"));
  }

  private void displayMenuButtons() {
    button.display(window);
  }

  private void displayMenuBackground() {
    for (int w = 0; w < width; ++w) {
      for (int h = 0; h < height; ++h) {
        window[h][w] = ' ';
      }
    }
    displayVerticalLine(0);
    displayVerticalLine(width - 1);
    displayHorizontalLine(0);
    displayHorizontalLine(height - 1);
  }

  private void displayVerticalLine(int currX) {
    for (int h = 0; h < height; ++h) {
      window[h][currX] = '|';
    }
  }

  private void displayHorizontalLine(int currY) {
    for (int w = 0; w < width; ++w) {
      window[currY][w] = '-';
    }
  }

  public char[][] getWindow() { return window; }

  public void destroy() {
    try {
      new ProcessBuilder("clear").inheritIO().start().waitFor();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}

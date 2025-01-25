package gfx;

import java.io.*;

import level.Level;
import level.location.Location;
import entity.EntityPlayer;
import util.GameMetaData.TilesFileInfo;

public class RendererUI {
  public static int FPS = 20; 
  public static final long NANOSECONDS_PER_FRAME = 1_000_000_000L / FPS;
  private static final int width = 174;
  private static final int height = 12;

  private PrintWriter out;
  private char[][] window;
  private Level level;
  
  public RendererUI(Level level, PrintWriter out) {
    this.level = level;
    this.out = out;
    this.window = new char[height][width];
    displayUIBorders();
    displayPlayerPlaceholder();
  }

  public void render() {
    displayHealthBar();
    for (int row = 0; row < height; ++row) {
      for (int col = 0; col < width; ++col) {
        out.print(window[row][col] + "");
      }
      out.print("\n");
    }
    out.flush();
  }

  private void displayPlayerPlaceholder() {
    String name = "Dog: '@'";
    for (int i = 0; i < name.length(); ++i) {
      window[1][2 + i] = name.charAt(i);
    }
    String health = "Health: ";
    for (int i = 0; i < health.length(); ++i) {
      window[5][2 + i] = health.charAt(i);
    }
  }

  private void displayHealthBar() {
    int writeX = 10;
    int writeY = 4;
    EntityPlayer player = level.getPlayer();
    int numHealth = player.getHealth();
    int maxHealth = EntityPlayer.getMaxHealth();
    int diff = maxHealth - numHealth;
    for (int i = 0; i < numHealth; ++i) {
      displayHealthTile(writeX + i * 6, writeY, TilesFileInfo.healthUIFilePath);
    }
    for (int i = maxHealth; i > numHealth; --i) {
      displayHealthTile(writeX + (i - 1) * 6, writeY, TilesFileInfo.emptyHealthUIFilePath);
    }
  }

  private void displayHealthTile(int writeX, int writeY, String filePath) {
    try (FileReader reader = new FileReader(filePath)) {
      int ch;
      while (reader.read() != '\n');
      while ((ch = reader.read()) != '#') {
        for (int i = 0; i < TilesFileInfo.healthUIWidth; ++i) {
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

  private void displayUIBorders() {
    for (int w = 0; w < width; ++w) {
      for (int h = 0; h < height; ++h) {
        window[h][w] = ' ';
      }
    }
    displayVerticalLine(0);
    displayVerticalLine(width - 1);
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
}

package gfx;

import java.util.Random;
import java.io.*;
import java.lang.Math;

import level.Level;
import level.location.Location;
import entity.EntityPlayer;
import gfx.Textures;
import gfx.Textures.TextureType;
import util.GameMath;
import gui.UI.UI;

public class RendererGame {
  public static final int FPS = 20;
  public static final long NANOSECONDS_PER_FRAME = 1_000_000_000L / FPS;
  private static final int FOV_RADIUS = 25; 
  private static final int WIDTH_SCREEN = 174;

  private Level level;
  private Random random;
  private PrintWriter out;
  private double distanceToPlayer;
  private int[][] matrix;
  private Location location;

  public RendererGame(Level level, PrintWriter out) {
    this.level = level;
    this.location = level.getCurrLocation();
    this.matrix = level.getCurrLocation().getLocationMatrix();
    this.random = new Random();
    // this.out = new PrintWriter(System.out);
    this.out = out;
    this.distanceToPlayer = 0.0;
  }

  public void render() {
    this.location = level.getCurrLocation();
    this.matrix = level.getCurrLocation().getLocationMatrix();
    char pixel = Textures.getPixelById(-1);
    EntityPlayer player = level.getPlayer();
    int playerX = player.getX();
    int playerY = player.getY();

    clearScreen();
    printHeaderLineUI();
    for (int row = 0; row < location.getHeight(); ++row) {
      printVerticalLineUI();
      for (int col = 0; col < location.getWidth(); ++col) {
        pixel = ' ';
        if (isPixelVisible(playerX, playerY, col, row, player.getFovRadius())) {
          pixel = Textures.getPixelById(matrix[row][col]);
        }
        out.print(pixel + "");
      }
      printVerticalLineUI();
      out.print('\n');
    }
    printHorizontalLineUI();
    // out.flush();
  }

  private boolean isPixelVisible(int x0, int y0, int x1, int y1, int radius) {
    TextureType textureType = Textures.getTypeById(matrix[y1][x1]);
    Location location = level.getCurrLocation();
    double dist = GameMath.calculateDistance(x0, y0, x1, y1);

    if (!location.isValidCoord(x0, y0) || !location.isValidCoord(x1, y1))
      return false;
    if (dist > radius) 
      return false;
    if (textureType == TextureType.CAVE_TEXTURE)
      return true;
    if (textureType == TextureType.PLAYER_TEXTURE)
      return true;

    int dx = Math.abs(x1 - x0);
    int dy = -Math.abs(y1 - y0);
    int sx = x0 < x1 ? 1 : -1;
    int sy = y0 < y1 ? 1 : -1;
    int error = dx + dy;
    while (true) {
      dist = GameMath.calculateDistance(x0, y0, x1, y1);
      if (x0 == x1 && y0 == y1)
        return true;
      if (matrix[y0][x0] == 1 && matrix[y1][x1] == 1 && dist < 6)
        return true;
      if (matrix[y0][x0] == 0 && matrix[y1][x1] == 1 && dist < 3)
        return true;
      if (matrix[y0][x0] == 1) return false;
      int e2 = 2 * error;
      if (e2 - dy > dx - e2) {
        error += dy;
        x0 += sx;
      } else {
        error += dx;
        y0 += sy;
      }
    }
  }

  public void destroy() {
    try {
      new ProcessBuilder("clear").inheritIO().start().waitFor();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void printHeaderLineUI() {
    String name = location.getName();
    int leftPadding = 3;
    for (int col = 0; col < leftPadding; ++col) {
      out.print('-');
    }
    out.print(name);
    for (int col = 0; col < WIDTH_SCREEN - name.length() - leftPadding; ++col) {
      out.print('-');
    }
    out.print('\n');
  }

  private void printHorizontalLineUI() {
    for (int col = 0; col < WIDTH_SCREEN; ++col) {
      out.print('-');
    }
    out.print('\n');
  }

  private void printVerticalLineUI() {
    out.print('|');
  }

  private void clearScreen() {
    out.print(("\033[2J\033[1;1H"));
  }
}

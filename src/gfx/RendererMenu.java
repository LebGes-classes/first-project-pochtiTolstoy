package gfx;

import java.io.*;
import gui.Button;
import gfx.RendererMenu;

public class RendererMenu {
  public static final int FPS = 60;
  private static int width = 174;
  private static int height = 62;

  private Button[] buttons;
  private PrintWriter out;
  private char[][] window;
  

  public RendererMenu(Button[] buttons) {
    this.buttons = buttons; 
    this.out = new PrintWriter(System.out);
    this.window = new char[height][width]; 
    displayMenuBackground();
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
    for (Button button : buttons) {
      button.display(window);
    }
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

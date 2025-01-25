package util;

import java.io.IOException;
import java.util.Scanner;
import java.io.*;

public class InputHandler {
  public static final char KEY_ENTER = '\n';
  public static final char KEY_QUIT = 'q';
  public static final char KEY_W = 'w';
  public static final char KEY_A = 'a';
  public static final char KEY_S = 's';
  public static final char KEY_D = 'd';
  public static final char KEY_M = 'm';
  public static final char KEY_N = 'n';
  public static final char KEY_P = 'p';
  public static final char KEY_UNDEFINED = '?';

  private char keyValue = KEY_UNDEFINED;

  public void processInput() {
    // no-enter-please
    try {
      setRawMode();
      if (System.in.available() > 0) {
        keyValue = (char) System.in.read(); 
        clearInputBuffer();
      } else {
        keyValue = '\0';
      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    } finally {
      try {
        resetTerminal();
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private void clearInputBuffer() throws IOException {
    while (System.in.available() > 0) {
      System.in.read();
    }
  }

  public char getKeyValue() {
    return keyValue;
  }

  public boolean pressedQuit() {
    return keyValue == KEY_QUIT;
  }

  public void destroy() {
    try {
      resetTerminal();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  // no-enter-please
  private static void setRawMode() throws IOException, InterruptedException {
    new ProcessBuilder("/bin/sh", "-c", "stty raw -echo </dev/tty").inheritIO().start().waitFor();
  }

  private static void resetTerminal() throws IOException, InterruptedException {
    new ProcessBuilder("/bin/sh", "-c", "stty sane </dev/tty").inheritIO().start().waitFor();
  }
}

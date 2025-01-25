package util;

import java.lang.Math;

public class GameMath {
  public static int getAvgFromArray(int[] array, int size) {
    double result = 0;    
    for (int i = 0; i < size; ++i)
      result += array[i];
    return toNearestInt(result / size);
  }

  public static int toNearestInt(double num) {
    return (num < (int) num + 0.5) ? (int) num : (int) (num + 1);
  }

  public static int abs(int num) {
    return (num < 0) ? -num : num;
  }

  public static double calculateDistance(Position p1, Position p2) {
    return calculateDistance(p1.getX(), p1.getY(), p2.getX(), p2.getY());
  }

  public static double calculateDistance(int x1, int y1, int x2, int y2) {
    return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow((y1 - y2) * 1.7, 2));
  }

  public static int euMod(int num, int rem) {
    num %= rem;
    if (num < 0) num += rem;
    return num;
  }
}

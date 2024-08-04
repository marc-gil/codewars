package dev.marcgil.codewars.kyu3;

import java.util.Arrays;

public class Spiralizor {

  /**
   * Your task, is to create a NxN spiral with a given size.
   * <p>
   * For example, spiral with size 5 should look like this:
   * <p>
   * 00000
   * ....0
   * 000.0
   * 0...0
   * 00000
   * and with the size 10:
   * <p>
   * 0000000000
   * .........0
   * 00000000.0
   * 0......0.0
   * 0.0000.0.0
   * 0.0..0.0.0
   * 0.0....0.0
   * 0.000000.0
   * 0........0
   * 0000000000
   * Return value should contain array of arrays, of 0 and 1, with the first row being composed of 1s. For example for given size 5 result should be:
   * <p>
   * [[1,1,1,1,1],[0,0,0,0,1],[1,1,1,0,1],[1,0,0,0,1],[1,1,1,1,1]]
   * Because of the edge-cases for tiny spirals, the size will be at least 5.
   * <p>
   * General rule-of-a-thumb is, that the snake made with '1' cannot touch to itself.
   */
  public static int[][] spiralize(int size) {
    int[][] array = createZeroFilledArray(size);

    boolean isMovementExecuted;
    int x = 0;
    int y = 0;
    do {
      isMovementExecuted = false;

      if (fieldIsAlreadyFilled(array[y][x])) {
        x++; //Move to the next 0 position
      }
      for (int i = x; i < size; i++) {
        if (isInitialLeftToRightSpiral(y) || canLeftToRightSpiralBeContinued(array, y, i)) {
          array[y][i] = 1;
          x = i;
          isMovementExecuted = true;
        }
      }
      if (!isMovementExecuted) {
        break;
      }

      if (fieldIsAlreadyFilled(array[y][x])) {
        y++; //Move to the next 0 position
      }
      for (int j = y; j < size; j++) {
        if (isInitialTopToBottomSpiral(size, x) || canTopToBottomSpiralBeContinued(array, j, x)) {
          array[j][x] = 1;
          y = j;
        }
      }

      if (fieldIsAlreadyFilled(array[y][x])) {
        x--; //Move to the next 0 position
      }
      for (int i = x; i >= 0; i--) {
        if (isInitialRightToLeftSpiral(size, y) || canRightToLeftSpiralBeContinued(array, y, i)) {
          array[y][i] = 1;
          x = i;
        }
      }

      if (fieldIsAlreadyFilled(array[y][x])) {
        y--; //Move to the next 0 position
      }
      for (int j = y; j >= 0; j--) {
        if (canBottomToTopSpiralBeContinued(array, j, x)) {
          array[j][x] = 1;
          y = j;
        }
      }
    } while (true);

    return array;
  }

  private static int[][] createZeroFilledArray(int size) {
    int[][] array = new int[size][size];
    for (int[] ints : array) {
      Arrays.fill(ints, 0);
    }
    return array;
  }

  private static boolean canBottomToTopSpiralBeContinued(int[][] array, int j, int x) {
    return (array[j][x] == 0 && array[j - 1][x] == 0) && (isInitialBottomToTopSpiral(x) || array[j+1][x - 1] == 0);
  }

  private static boolean canRightToLeftSpiralBeContinued(int[][] array, int y, int i) {
    return array[y][i] == 0 && array[y][i - 1] != 1
        && array[y - 1][i - 1] == 0;
  }

  private static boolean canTopToBottomSpiralBeContinued(int[][] array, int j, int x) {
    return array[j][x] == 0 && array[j + 1][x] != 1;
  }

  private static boolean canLeftToRightSpiralBeContinued(int[][] array, int y, int i) {
    return array[y][i] == 0 && array[y][i + 1] == 0
        && array[y + 1][i + 1] == 0;
  }

  private static boolean isInitialBottomToTopSpiral(int x) {
    return x == 0;
  }

  private static boolean isInitialRightToLeftSpiral(int size, int y) {
    return y == size - 1;
  }

  private static boolean isInitialTopToBottomSpiral(int size, int x) {
    return x == size - 1;
  }

  private static boolean isInitialLeftToRightSpiral(int y) {
    return y == 0;
  }

  private static boolean fieldIsAlreadyFilled(int fieldValue) {
    return fieldValue != 0;
  }

}

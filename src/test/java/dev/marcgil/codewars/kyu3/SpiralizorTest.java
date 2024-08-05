package dev.marcgil.codewars.kyu3;

import static java.lang.Integer.min;
import static java.util.stream.IntStream.range;
import static java.util.stream.IntStream.rangeClosed;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SpiralizorTest {

  @Test
  public void test5through50() {
    rangeClosed(5, 50).forEach(this::test);
  }

  @Test
  public void test100() {
    test(100);
  }

  private void test(int n) {
    int[][] expected = $Spiralizor.spiralize(n);
    int[][] actual = Spiralizor.spiralize(n);
    Assertions.assertArrayEquals(expected, actual, "Test for n = " + n);
  }

  private final static class $Spiralizor {

    private static int[][] spiralize(int n) {
      int[][] ary = range(0, n).mapToObj(i -> range(0, n).map(j -> {
        int min = min(min(i, j), min(n - i - 1, n - j - 1));
        return j == min && i == min + 1 ? min % 2 : 1 - min % 2;
      }).toArray()).toArray(int[][]::new);
      if (n % 2 == 0) {
        ary[n / 2][n / 2 - 1] = 0;
      }
      return ary;
    }
  }

}

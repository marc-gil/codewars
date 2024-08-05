package dev.marcgil.codewars.kyu6;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;
import org.junit.jupiter.api.Test;

class FindUniqTest {

  private final double precision = 0;

  @Test
  public void test1() {
    assertEquals(1, FindUniq.findUniq(new double[]{0, 1, 0}), precision);
    assertEquals(2, FindUniq.findUniq(new double[]{1, 1, 1, 2, 1, 1}), precision);
  }

  @Test
  public void test2() {
    // Basic tests (shuffled)
    assertEquals(3, FindUniq.findUniq(new double[]{4, 4, 4, 3, 4, 4, 4, 4}), precision);
    assertEquals(4, FindUniq.findUniq(new double[]{5, 5, 5, 5, 4, 5, 5, 5}), precision);
    assertEquals(5, FindUniq.findUniq(new double[]{6, 6, 6, 6, 6, 5, 6, 6}), precision);
    assertEquals(6, FindUniq.findUniq(new double[]{7, 7, 7, 7, 7, 7, 6, 7}), precision);
    // The last item
    assertEquals(7, FindUniq.findUniq(new double[]{8, 8, 8, 8, 8, 8, 8, 7}), precision);
    assertEquals(2, FindUniq.findUniq(new double[]{3, 3, 2, 3, 3, 3, 3, 3}), precision);
    assertEquals(1, FindUniq.findUniq(new double[]{2, 1, 2, 2, 2, 2, 2, 2}), precision);
    // The first item
    assertEquals(0, FindUniq.findUniq(new double[]{0, 1, 1, 1, 1, 1, 1, 1}), precision);
  }

  @Test
  public void test3() {
    // Very big number
    assertEquals(
        Math.pow(2, 40),
        FindUniq.findUniq(generateTestArr(Math.pow(2, 40), Math.pow(2, 50), 100)),
        precision
    );
    // Negative number
    assertEquals(
        -1,
        FindUniq.findUniq(generateTestArr(-1, 1, 1000)),
        precision
    );
    // Float number
    assertEquals(
        0.0000001,
        FindUniq.findUniq(generateTestArr(0.0000001, 0.0010001, 1000)),
        precision
    );
    // The first item but with random numbers
    Random rand = new Random();
    int number1 = rand.nextInt(100);
    int number2 = rand.nextInt(100);
    while (number1 == number2) {
      number2 = rand.nextInt(100);
    }
    assertEquals(number1, FindUniq.findUniq(
            new double[]{number1, number2, number2, number2, number2, number2, number2, number2}),
        precision);
        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
         WARNING FOR TRANSLATORS!
         Try to create as huge array as possible. It is very good,
         if solutions with sort will fail on timeout.
         Do it to force people use O(n) solutions
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    // Very big array
    assertEquals(
        42,
        FindUniq.findUniq(generateTestArr(42, 24, Integer.MAX_VALUE / 105)),
        precision
    );
  }

  static double[] generateTestArr(double answer, double mass, int length) {
    Random rand = new Random();
    int answerIndex = rand.nextInt(length);

    double[] arr = new double[length];

    for (int i = 0; i < arr.length; i++) {
      arr[i] = i == answerIndex ? answer : mass;
    }
    return arr;
  }

}

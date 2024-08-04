package dev.marcgil.codewars.kyu6;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FindUniq {

  /**
   * DESCRIPTION: There is an array with some numbers. All numbers are equal except for one. Try to
   * find it!
   * <p>
   * Kata.findUniq(new double[]{ 1, 1, 1, 2, 1, 1 }); =/> 2 <br>
   * Kata.findUniq(new double[]{ 0, 0, 0.55, 0, 0 }); // => 0.55 <br>
   * It’s guaranteed that array contains at least 3 numbers.
   * <p>
   * The tests contain some very huge arrays, so think about performance.
   */
  public static double findUniq(double[] arr) {
    Map<Double, List<Double>> numberMap = Arrays.stream(arr)
        .boxed()
        .collect(Collectors.groupingBy(Function.identity()));

    return numberMap.entrySet().stream()
        .filter(entry -> entry.getValue().size() == 1)
        .map(Map.Entry::getKey)
        .findFirst()
        .orElseThrow();
  }

}

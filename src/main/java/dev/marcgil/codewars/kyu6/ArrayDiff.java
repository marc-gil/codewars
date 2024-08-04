package dev.marcgil.codewars.kyu6;

import java.util.Arrays;

public class ArrayDiff {

  /**
   * DESCRIPTION:
   * Your goal in this kata is to implement a difference function, which subtracts one list from another and returns the result.
   * <p>
   * It should remove all values from list a, which are present in list b keeping their order.
   * <p>
   * ArrayDiff.arrayDiff(new int[] {1, 2}, new int[] {1}) => new int[] {2} <br>
   * If a value is present in b, all of its occurrences must be removed from the other:
   * <p>
   * ArrayDiff.arrayDiff(new int[] {1, 2, 2, 2, 3}, new int[] {2}) => new int[] {1, 3}
   */
  public static int[] arrayDiff(int[] a, int[] b) {
    return Arrays.stream(a)
        .filter(valueA -> Arrays.stream(b).noneMatch(valueB -> valueA == valueB))
        .toArray();
  }

}

package dev.marcgil.codewars.kyu4;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

class ConwayLifeTest {

  private void checkGlider(int[][][] gliders, int[][] check, int generation) {
    int[][] res = ConwayLife.getGeneration(gliders[0], generation);
    assertArrayEquals(res, gliders[generation % 4]);
    assertArrayEquals(gliders[0], check);
  }

  @Test
  public void testGliders() {
    int[][][] gliders = {
        {{1, 0, 0},
            {0, 1, 1},
            {1, 1, 0}},
        {{0, 1, 0},
            {0, 0, 1},
            {1, 1, 1}},
        {{1, 0, 1},
            {0, 1, 1},
            {0, 1, 0}},
        {{0, 0, 1},
            {1, 0, 1},
            {0, 1, 1}}
    };
    int[][] check = {{1, 0, 0},
        {0, 1, 1},
        {1, 1, 0}};
    for (int i : new int[]{0, 1, 2, 3, 40}) {
      checkGlider(gliders, check, i);
    }
  }

  @Test
  public void testTwoGliders() {
    int[][][] twoGliders = {
        {{1, 1, 1, 0, 0, 0, 1, 0},
            {1, 0, 0, 0, 0, 0, 0, 1},
            {0, 1, 0, 0, 0, 1, 1, 1}},
        {{1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1}}
    };
    int[][] res = ConwayLife.getGeneration(twoGliders[0], 16);
    assertArrayEquals(res, twoGliders[1]);
  }
}
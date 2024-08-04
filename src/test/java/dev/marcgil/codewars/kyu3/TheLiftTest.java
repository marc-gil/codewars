package dev.marcgil.codewars.kyu3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

class TheLiftTest {

  @Test
  public void testUp() {
    final int[][] queues = {
        new int[0], // G
        new int[0], // 1
        new int[]{5,5,5}, // 2
        new int[0], // 3
        new int[0], // 4
        new int[0], // 5
        new int[0], // 6
    };
    final int[] result = TheLift.theLift(queues,5);
    assertArrayEquals(new int[]{0,2,5,0}, result);
  }

  @Test
  public void testDown() {
    final int[][] queues = {
        new int[0], // G
        new int[0], // 1
        new int[]{1,1}, // 2
        new int[0], // 3
        new int[0], // 4
        new int[0], // 5
        new int[0], // 6
    };
    final int[] result = TheLift.theLift(queues,5);
    assertArrayEquals(new int[]{0,2,1,0}, result);
  }

  @Test
  public void testUpAndUp() {
    final int[][] queues = {
        new int[0], // G
        new int[]{3}, // 1
        new int[]{4}, // 2
        new int[0], // 3
        new int[]{5}, // 4
        new int[0], // 5
        new int[0], // 6
    };
    final int[] result = TheLift.theLift(queues,5);
    assertArrayEquals(new int[]{0,1,2,3,4,5,0}, result);
  }

  @Test
  public void testDownAndDown() {
    final int[][] queues = {
        new int[0], // G
        new int[]{0}, // 1
        new int[0], // 2
        new int[0], // 3
        new int[]{2}, // 4
        new int[]{3}, // 5
        new int[0], // 6
    };
    final int[] result = TheLift.theLift(queues,5);
    assertArrayEquals(new int[]{0,5,4,3,2,1,0}, result);
  }

  @Test
  public void testUpAndDown() {
    final int[][] queues = {
        new int[]{3}, // G
        new int[]{2}, // 1
        new int[]{0}, // 2
        new int[]{2}, // 3
        new int[0], // 4
        new int[0], // 5
        new int[]{5}, // 6
    };
    final int[] result = TheLift.theLift(queues,5);
    assertArrayEquals(new int[]{0,1,2,3,6,5,3,2,0}, result);
  }

  @Test
  public void testYoYo() {
    final int[][] queues = {
        new int[0], // G
        new int[0], // 1
        new int[]{4,4,4,4}, // 2
        new int[0], // 3
        new int[]{2,2,2,2}, // 4
        new int[0], // 5
        new int[0], // 6
    };
    final int[] result = TheLift.theLift(queues,2);
    assertArrayEquals(new int[]{0,2,4,2,4,2,0}, result);
  }

  @Test
  public void testEnterOnGroundFloor() {
    final int[][] queues = {
        new int[]{1,2,3,4}, // G
        new int[0], // 1
        new int[0], // 2
        new int[0], // 3
        new int[0], // 4
        new int[0], // 5
        new int[0], // 6
    };
    final int[] result = TheLift.theLift(queues,5);
    assertArrayEquals(new int[]{0,1,2,3,4,0}, result);
  }

  @Test
  public void testLiftFullUp() {
    final int[][] queues = {
        new int[]{3,3,3,3,3,3}, // G
        new int[0], // 1
        new int[0], // 2
        new int[0], // 3
        new int[0], // 4
        new int[0], // 5
        new int[0], // 6
    };
    final int[] result = TheLift.theLift(queues,5);
    assertArrayEquals(new int[]{0,3,0,3,0}, result);
  }

  @Test
  public void testLiftFullDown() {
    final int[][] queues = {
        new int[0], // G
        new int[0], // 1
        new int[0], // 2
        new int[]{1,1,1,1,1,1,1,1,1,1,1}, // 3
        new int[0], // 4
        new int[0], // 5
        new int[0], // 6
    };
    final int[] result = TheLift.theLift(queues,5);
    assertArrayEquals(new int[]{0,3,1,3,1,3,1,0}, result);
  }

  @Test
  public void testLiftFullUpAndDown() {
    final int[][] queues = {
        new int[]{3,3,3,3,3,3}, // G
        new int[0], // 1
        new int[0], // 2
        new int[0], // 3
        new int[0], // 4
        new int[]{4,4,4,4,4,4}, // 5
        new int[0], // 6
    };
    final int[] result = TheLift.theLift(queues,5);
    assertArrayEquals(new int[]{0,3,5,4,0,3,5,4,0}, result);
  }

  @Test
  public void testTrickyQueues() {
    final int[][] queues = {
        new int[0], // G
        new int[]{0,0,0,6}, // 1
        new int[0], // 2
        new int[0], // 3
        new int[0], // 4
        new int[]{6,6,0,0,0,6}, // 5
        new int[0], // 6
    };
    final int[] result = TheLift.theLift(queues,5);
    assertArrayEquals(new int[]{0,1,5,6,5,1,0,1,0}, result);
  }

  @Test
  public void testHighlander() {
    final int[][] queues = {
        new int[0], // G
        new int[]{2}, // 1
        new int[]{3,3,3}, // 2
        new int[]{1}, // 3
        new int[0], // 4
        new int[0], // 5
        new int[0], // 6
    };
    final int[] result = TheLift.theLift(queues,1); // there can be only one
    assertArrayEquals(new int[]{0,1,2,3,1,2,3,2,3,0}, result);
  }

  @Test
  public void testFireDrill() {
    final int[][] queues = {
        new int[0], // G
        new int[]{0,0,0,0}, // 1
        new int[]{0,0,0,0}, // 2
        new int[]{0,0,0,0}, // 3
        new int[]{0,0,0,0}, // 4
        new int[]{0,0,0,0}, // 5
        new int[]{0,0,0,0}, // 6
    };
    final int[] result = TheLift.theLift(queues,5);
    assertArrayEquals(new int[]{0,6,5,4,3,2,1,0,5,4,3,2,1,0,4,3,2,1,0,3,2,1,0,1,0}, result);
  }

  @Test
  public void testEmpty() {
    final int[][] queues = {
        new int[0], // G
        new int[0], // 1
        new int[0], // 2
        new int[0], // 3
        new int[0], // 4
        new int[0], // 5
        new int[0], // 6
    };
    final int[] result = TheLift.theLift(queues,5);
    assertArrayEquals(new int[]{0}, result);
  }

  @Test
  public void testBugs() {

    // Reported by widelec9 for Python
    final int[][] queues = {
        new int[]{8, 8, 6},//G
        new int[]{8, 3, 4, 7}, //1
        new int[0], //2
        new int[]{2, 6, 8, 5}, //3
        new int[0], //4
        new int[]{0, 8, 8, 4, 1}, //5
        new int[0], //6
        new int[]{3, 2, 2, 3}, //7
        new int[0] //8
    };
    final int[] result = TheLift.theLift(queues,9);
    assertArrayEquals(new int[]{0, 1, 3, 4, 5, 6, 7, 8, 7, 5, 4, 3, 2, 1, 0}, result);
    //assertArrayEquals(new int[]{0, 1, 3, 5, 8, 7, 5, 3, 2, 0, 1, 3, 5, 6, 8, 7, 5, 3, 2, 1, 3, 4, 5, 6, 8, 5, 4, 0, 1, 3, 5, 7, 8, 5, 1, 3, 5, 8, 0}, result);
  }
}
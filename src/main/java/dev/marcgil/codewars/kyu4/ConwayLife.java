package dev.marcgil.codewars.kyu4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConwayLife {

  /**
   * Given a 2D array and a number of generations, compute n timesteps of ConwayLife's Game of Life.
   * <p>
   * The rules of the game are:
   * <p>
   * Any live cell with fewer than two live neighbours dies, as if caused by underpopulation.
   * Any live cell with more than three live neighbours dies, as if by overcrowding.
   * Any live cell with two or three live neighbours lives on to the next generation.
   * Any dead cell with exactly three live neighbours becomes a live cell.
   * Each cell's neighborhood is the 8 cells immediately around it (i.e. Moore Neighborhood).
   * The universe is infinite in both the x and y dimensions and all cells are initially dead - except for those specified in the arguments.
   * The return value should be a 2d array cropped around all of the living cells.
   * (If there are no living cells, then return [[]].)
   * <p>
   * For illustration purposes, 0 and 1 will be represented as ░░ and ▓▓ blocks respectively
   * (PHP: plain black and white squares).
   * You can take advantage of the htmlize function to get a text representation of the universe, e.g.:
   * <p>
   * System.out.println(LifeDebug.htmlize(cells));
   */
  public static int[][] getGeneration(int[][] cells, int generations) {
    Map<Position, Cell> cellDistribution = new HashMap<>();
    for (int i = 0; i < cells.length; i++) {
      for (int j = 0; j < cells[i].length; j++) {
        cellDistribution.put(Position.of(j, i), Cell.of(cells[i][j]));
      }
    }
    addNeighbours(cellDistribution);

    for (int i = 0; i < generations; i++) {
      cellDistribution = getNextGeneration(cellDistribution);
      addNeighbours(cellDistribution);
    }
    return cropGrid(cellDistribution);
  }

  private static void addNeighbours(Map<Position, Cell> cellDistribution) {
    cellDistribution.forEach((key, value) -> value
        .setNeighbours(getNeighbours(key, cellDistribution)));
  }

  private static List<Cell> getNeighbours(Position position, Map<Position, Cell> cellDistribution) {
    return getNeighboursPositions(position).stream()
        .map(neighbourPosition -> cellDistribution.getOrDefault(neighbourPosition, Cell.dead()))
        .toList();
  }

  private static List<Position> getNeighboursPositions(Position p) {
    List<Position> neighbours = new ArrayList<>();
    for (int x = -1; x < 2; x++) {
      for (int y = -1; y < 2; y++) {
        if (x != 0 || y != 0) {
          neighbours.add(Position.of(p.x() + x, p.y() + y));
        }
      }
    }
    return neighbours;
  }

  private static Map<Position, Cell> getNextGeneration(Map<Position, Cell> cellDistribution) {
    Map<Position, Cell> nextGenerationCellDistribution = new HashMap<>();
    Set<Position> positionsToEvaluate = new HashSet<>(cellDistribution.keySet());
    for (Position position : cellDistribution.keySet()) {
      positionsToEvaluate.addAll(getNeighboursPositions(position));
    }

    positionsToEvaluate.forEach(position -> {
      Cell cell = cellDistribution.getOrDefault(position, Cell.dead());
      if (!cellDistribution.containsKey(position)) {
        cell.setNeighbours(getNeighbours(position, cellDistribution));
      }
      Cell nextGenerationCell = cell.nextGenerationCell();
      nextGenerationCellDistribution.put(position, nextGenerationCell);
    });

    return nextGenerationCellDistribution;
  }

  private static int[][] cropGrid(Map<Position, Cell> cellDistribution) {
    if (cellDistribution.isEmpty()) {
      return new int[][]{{}};
    }

    int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
    int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;

    for (Map.Entry<Position, Cell> entry : cellDistribution.entrySet()) {
      if (entry.getValue().isAlive()) {
        Position pos = entry.getKey();
        minX = Math.min(minX, pos.x());
        maxX = Math.max(maxX, pos.x());
        minY = Math.min(minY, pos.y());
        maxY = Math.max(maxY, pos.y());
      }
    }

    int rows = maxX - minX + 1;
    int cols = maxY - minY + 1;
    int[][] croppedGrid = new int[cols][rows];

    for (Map.Entry<Position, Cell> entry : cellDistribution.entrySet()) {
      Position pos = entry.getKey();
      if (entry.getValue().isAlive()) {
        croppedGrid[pos.y() - minY][pos.x() - minX] = 1;
      }
    }
    return croppedGrid;
  }

  private record Position(int x, int y) {

    public static Position of(int x, int y) {
      return new Position(x, y);
    }
  }

  private static class Cell {

    private List<Cell> neighbours;
    private final State state;

    public static Cell dead() {
      return new Cell(State.DEAD);
    }

    public static Cell of(int value) {
      return new Cell(value == 0 ? State.DEAD : State.ALIVE);
    }

    private Cell(State state) {
      this.state = state;
    }

    private enum State {
      DEAD, ALIVE
    }

    public void setNeighbours(List<Cell> neighbours) {
      this.neighbours = neighbours;
    }

    public boolean isAlive() {
      return state == State.ALIVE;
    }

    public Cell nextGenerationCell() {
      long livingNeighbours = neighbours.stream().filter(Cell::isAlive).count();
      if (this.isAlive()) {
        return livingNeighbours == 2 || livingNeighbours == 3 ? new Cell(State.ALIVE) : new Cell(State.DEAD);
      } else {
        return livingNeighbours == 3 ? new Cell(State.ALIVE) : new Cell(State.DEAD);
      }
    }
  }

}

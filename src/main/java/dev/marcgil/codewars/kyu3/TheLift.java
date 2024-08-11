package dev.marcgil.codewars.kyu3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class TheLift {

  /**
   * Synopsis
   * A multi-floor building has a Lift in it.
   * <p>
   * People are queued on different floors waiting for the Lift.
   * <p>
   * Some people want to go up. Some people want to go down.
   * <p>
   * The floor they want to go to is represented by a number (i.e. when they enter the Lift this is the button they will press)
   * <pre>
   * BEFORE (people waiting in queues)               AFTER (people at their destinations)
   *                    +--+                                          +--+
   *   /----------------|  |----------------\        /----------------|  |----------------\
   * 10|                |  | 1,4,3,2        |      10|             10 |  |                |
   *   |----------------|  |----------------|        |----------------|  |----------------|
   * 9 |                |  | 1,10,2         |       9|                |  |                |
   *   |----------------|  |----------------|        |----------------|  |----------------|
   * 8 |                |  |                |       8|                |  |                |
   *   |----------------|  |----------------|        |----------------|  |----------------|
   * 7 |                |  | 3,6,4,5,6      |       7|                |  |                |
   *   |----------------|  |----------------|        |----------------|  |----------------|
   * 6 |                |  |                |       6|          6,6,6 |  |                |
   *   |----------------|  |----------------|        |----------------|  |----------------|
   * 5 |                |  |                |       5|            5,5 |  |                |
   *   |----------------|  |----------------|        |----------------|  |----------------|
   * 4 |                |  | 0,0,0          |       4|          4,4,4 |  |                |
   *   |----------------|  |----------------|        |----------------|  |----------------|
   * 3 |                |  |                |       3|            3,3 |  |                |
   *   |----------------|  |----------------|        |----------------|  |----------------|
   * 2 |                |  | 4              |       2|          2,2,2 |  |                |
   *   |----------------|  |----------------|        |----------------|  |----------------|
   * 1 |                |  | 6,5,2          |       1|            1,1 |  |                |
   *   |----------------|  |----------------|        |----------------|  |----------------|
   * G |                |  |                |       G|          0,0,0 |  |                |
   *   |====================================|        |====================================|
   * </pre>
   * <h1>Rules</h1>
   * <h2>Lift Rules</h2>
   * The Lift only goes up or down!
   * Each floor has both UP and DOWN Lift-call buttons (except top and ground floors which have only DOWN and UP respectively)
   * The Lift never changes direction until there are no more people wanting to get on/off in the direction it is already travelling
   * When empty the Lift tries to be smart. For example,
   * If it was going up then it will continue up to collect the highest floor person wanting to go down
   * If it was going down then it will continue down to collect the lowest floor person wanting to go up
   * The Lift has a maximum capacity of people
   * When called, the Lift will stop at a floor even if it is full, although unless somebody gets off nobody else can get on!
   * If the lift is empty, and no people are waiting, then it will return to the ground floor
   * <h2>People Rules</h2>
   * People are in "queues" that represent their order of arrival to wait for the Lift
   * All people can press the UP/DOWN Lift-call buttons
   * Only people going the same direction as the Lift may enter it
   * Entry is according to the "queue" order, but those unable to enter do not block those behind them that can
   * If a person is unable to enter a full Lift, they will press the UP/DOWN Lift-call button again after it has departed without them
   * <h2>Kata Task</h2>
   * Get all the people to the floors they want to go to while obeying the Lift rules and the People rules
   * Return a list of all floors that the Lift stopped at (in the order visited!)
   * NOTE: The Lift always starts on the ground floor (and people waiting on the ground floor may enter immediately)
   * <p>
   * <h1>I/O</h1>
   * <h2>Input</h2>
   * queues a list of queues of people for all floors of the building.
   * The height of the building varies
   * 0 = the ground floor
   * Not all floors have queues
   * Queue index [0] is the "head" of the queue
   * Numbers indicate which floor the person wants go to
   * capacity maximum number of people allowed in the lift
   * Parameter validation - All input parameters can be assumed OK. No need to check for things like:
   * <p>
   * People wanting to go to floors that do not exist
   * People wanting to take the Lift to the floor they are already on
   * Buildings with < 2 floors
   * Basements
   * <h2>Output</h2>
   * A list of all floors that the Lift stopped at (in the order visited!)
   */
  public static int[] theLift(final int[][] queues, final int capacity) {
    return new Lift(capacity).lift(queues);
  }

  private static class Lift {

    private final int capacity;
    private final List<Integer> floorHistoric;
    private final List<Integer> passengers;
    private Direction direction;
    private int currentFloor;
    private int[][] queues;

    enum Direction {
      UP, DOWN
    }

    public Lift(int capacity) {
      this.capacity = capacity;
      this.direction = Direction.UP;
      this.currentFloor = 0;
      this.passengers = new ArrayList<>();
      this.floorHistoric = new ArrayList<>();
    }

    public int[] lift(final int[][] queues) {
      this.queues = queues;
      do {
        if (hasToStopAtThisFloor(currentFloor)) {
          getPassengersOut(currentFloor);
          calculateNextDirection();
          addPassengers(queues[currentFloor]);
          addFloorToHistoric(currentFloor);
        }
        if (isBuildingQueueEmpty() && isLiftEmpty() && currentFloor == 0) {
          break;
        }
        if (direction == Direction.UP) {
          currentFloor++;
        } else if (direction == Direction.DOWN) {
          currentFloor--;
        }
      } while (true);
      return floorHistoric.stream().mapToInt(Integer::intValue).toArray();
    }

    private boolean hasToStopAtThisFloor(int actualFloor) {
      return floorHasEnteringPeople(actualFloor) || passengerRequestedThisFloor(actualFloor)
          || actualFloor == 0;
    }

    private boolean floorHasEnteringPeople(int actualFloor) {
      return switch (direction) {
        case UP -> queueIsNotEmpty(actualFloor) && (someoneRequestedToGoUp(actualFloor) ||
            (isHighestFloorWithPeopleWaiting(actualFloor) && passengers.isEmpty()));
        case DOWN -> queueIsNotEmpty(actualFloor) && (someoneRequestedToGoDown(actualFloor) ||
            (isLowestFloorWithPeopleWaiting(actualFloor) && passengers.isEmpty()));
      };
    }

    private boolean queueIsNotEmpty(int actualFloor) {
      return getFloorQueue(actualFloor).length > 0;
    }

    private boolean someoneRequestedToGoUp(int actualFloor) {
      return IntStream.of(getFloorQueue(actualFloor))
          .anyMatch(requestedFloor -> requestedFloor > actualFloor);
    }

    private boolean isHighestFloorWithPeopleWaiting(int actualFloor) {
      return !someoneFromHigherFloorRequestedElevator(actualFloor);
    }

    private boolean someoneRequestedToGoDown(int actualFloor) {
      return IntStream.of(getFloorQueue(actualFloor))
          .anyMatch(requestedFloor -> requestedFloor < actualFloor);
    }

    private boolean isLowestFloorWithPeopleWaiting(int actualFloor) {
      return !someoneFromLowerFloorRequestedElevator(actualFloor);
    }

    private void getPassengersOut(int actualFloor) {
      while (passengers.contains(actualFloor)) {
        passengers.remove(Integer.valueOf(actualFloor));
      }
    }

    private void calculateNextDirection() {
      if (isLiftEmpty()) { //If it's not empty we keep same direction
        switch (direction) {
          case UP -> {
            if (someoneFromHigherFloorRequestedElevator(currentFloor)) {
              direction = Direction.UP;
            } else {
              int[] queue = getFloorQueue(currentFloor);
              if (queue.length > 0) {
                direction = calculateDirectionBasedOnFirstQueue(queue, currentFloor);
              } else {
                direction = Direction.DOWN;
              }
            }
          }
          case DOWN -> {
            if (someoneFromLowerFloorRequestedElevator(currentFloor)) {
              direction = Direction.DOWN;
            } else {
              int[] queue = getFloorQueue(currentFloor);
              if (queue.length > 0) {
                direction = calculateDirectionBasedOnFirstQueue(queue, currentFloor);
              } else {
                direction = someoneFromHigherFloorRequestedElevator(currentFloor) ? Direction.UP
                    : Direction.DOWN;
              }
            }
          }
        }
      }
    }

    private Direction calculateDirectionBasedOnFirstQueue(int[] queue, int actualFloor) {
      return queue[0] > actualFloor ? Direction.UP : Direction.DOWN;
    }

    private void addFloorToHistoric(int actualFloor) {
      floorHistoric.add(actualFloor);
    }

    private boolean someoneFromLowerFloorRequestedElevator(int actualFloor) {
      for (int i = 0; i < actualFloor; i++) {
        if (getFloorQueue(i).length != 0) {
          return true;
        }
      }
      return false;
    }

    private boolean someoneFromHigherFloorRequestedElevator(int actualFloor) {
      for (int[] queue : Arrays.stream(queues).skip(actualFloor + 1).toList()) {
        if (queue.length != 0) {
          return true;
        }
      }
      return false;
    }

    private boolean passengerRequestedThisFloor(int actualFloor) {
      return passengers.contains(actualFloor);
    }

    private int[] getFloorQueue(int actualFloor) {
      return queues[actualFloor];
    }

    private boolean isBuildingQueueEmpty() {
      for (int[] queue : queues) {
        if (queue.length != 0) {
          return false;
        }
      }
      return true;
    }

    private void addPassengers(int[] floorQueue) {
      if (isFull()) {
        return;
      }
      IntStream.Builder remainingQueue = IntStream.builder();
      for (int passengerDestinationFloor : floorQueue) {
        if (passengers.size() < capacity) {
          if (this.direction == Direction.UP && passengerDestinationFloor > currentFloor) {
            this.passengers.add(passengerDestinationFloor);
          } else if (this.direction == Direction.DOWN && passengerDestinationFloor < currentFloor) {
            this.passengers.add(passengerDestinationFloor);
          } else {
            remainingQueue.add(passengerDestinationFloor);
          }
        } else {
          remainingQueue.add(passengerDestinationFloor);
        }
      }
      queues[currentFloor] = remainingQueue.build().toArray();
    }

    private boolean isFull() {
      return passengers.size() == capacity;
    }

    boolean isLiftEmpty() {
      return passengers.isEmpty();
    }

  }

}

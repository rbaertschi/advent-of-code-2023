package ch.ebynaqon.aoc.aoc23;

import java.util.Arrays;
import java.util.HashMap;

public class ParabolicReflectorDish {
    private final int rows;
    private final int columns;

    public boolean[][] rollingRockMap() {
        return rollingRockMap;
    }

    public boolean[][] blockerMap() {
        return blockerMap;
    }

    private final boolean[][] rollingRockMap;
    private final boolean[][] blockerMap;

    public ParabolicReflectorDish(boolean[][] rollingRockMap, boolean[][] blockerMap) {
        this.rollingRockMap = rollingRockMap;
        this.blockerMap = blockerMap;
        this.rows = rollingRockMap.length;
        this.columns = rollingRockMap[0].length;
    }

    public static ParabolicReflectorDish parse(String input) {
        String[] lines = input.split("\n");
        int rows = lines.length;
        int columns = lines[0].length();
        boolean[][] rollingRockMap = new boolean[rows][columns];
        boolean[][] blockerMap = new boolean[rows][columns];
        for (int row = 0; row < rows; row++) {
            var line = lines[row];
            for (int column = 0; column < columns; column++) {
                if (line.charAt(column) == '#') {
                    blockerMap[row][column] = true;
                } else if (line.charAt(column) == 'O') {
                    rollingRockMap[row][column] = true;
                }
            }
        }
        return new ParabolicReflectorDish(rollingRockMap, blockerMap);
    }

    public long calculateStressAfterOneTiltNorth() {
        tiltNorth();
        return stressOnNorth();
    }

    private int stressOnNorth() {
        int totalWeight = 0;
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (rollingRockMap[row][column]) {
                    totalWeight += rows - row;
                }
            }
        }
        return totalWeight;
    }

    public int calculateStressAfterABillionCycles(int targetCycles1) {
        var firstOccurrence = new HashMap<Integer, Integer>();
        int targetCycles = targetCycles1;
        for (int currentCycle = 1; currentCycle <= targetCycles; currentCycle++) {
            cycle();
            int hash = computeHash(rollingRockMap);
            if (firstOccurrence.containsKey(hash)) {
                int lastCycle = firstOccurrence.get(hash);
                int remainingCycles = targetCycles - currentCycle;
                int loopLength = currentCycle - lastCycle;
                int remainingCyclesToCompute = remainingCycles % loopLength;
                for (int i = 0; i < remainingCyclesToCompute; i++) {
                    cycle();
                }
                return stressOnNorth();
            }
            firstOccurrence.put(hash, currentCycle);
        }
        return stressOnNorth();
    }

    private int computeHash(boolean[][] rollingRockMap) {
        int hash = 0;
        for (boolean[] row : rollingRockMap) {
            hash += 89 * Arrays.hashCode(row);
        }
        return hash;
    }

    private void cycle() {
        tiltNorth();
        tiltWest();
        tiltSouth();
        tiltEast();
    }

    private void tiltNorth() {
        for (int column = 0; column < columns; column++) {
            int newRow = 0;
            for (int row = 0; row < rows; row++) {
                if (blockerMap[row][column]) {
                    newRow = row + 1;
                } else if (rollingRockMap[row][column]) {
                    rollingRockMap[row][column] = false;
                    rollingRockMap[newRow][column] = true;
                    newRow = newRow + 1;
                }
            }
        }
    }

    private void tiltSouth() {
        for (int column = 0; column < columns; column++) {
            int newRow = rows - 1;
            for (int row = rows - 1; row >= 0; row--) {
                if (blockerMap[row][column]) {
                    newRow = row - 1;
                } else if (rollingRockMap[row][column]) {
                    rollingRockMap[row][column] = false;
                    rollingRockMap[newRow][column] = true;
                    newRow = newRow - 1;
                }
            }
        }
    }

    private void tiltWest() {
        for (int row = 0; row < rows; row++) {
            int newColumn = 0;
            for (int column = 0; column < columns; column++) {
                if (blockerMap[row][column]) {
                    newColumn = column + 1;
                } else if (rollingRockMap[row][column]) {
                    rollingRockMap[row][column] = false;
                    rollingRockMap[row][newColumn] = true;
                    newColumn = newColumn + 1;
                }
            }
        }
    }

    private void tiltEast() {
        for (int row = 0; row < rows; row++) {
            int newColumn = columns - 1;
            for (int column = columns - 1; column >= 0; column--) {
                if (blockerMap[row][column]) {
                    newColumn = column - 1;
                } else if (rollingRockMap[row][column]) {
                    rollingRockMap[row][column] = false;
                    rollingRockMap[row][newColumn] = true;
                    newColumn = newColumn - 1;
                }
            }
        }
    }

}

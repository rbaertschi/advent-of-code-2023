package ch.ebynaqon.aoc.aoc23;

import java.util.ArrayList;
import java.util.Arrays;

public class CosmicExpansion {
    public static String expand(String input) {
        String[] lines = input.split("\n");
        var linesToExpand = new ArrayList<Integer>();
        var columnsToExpand = new ArrayList<Integer>();
        int rows = lines.length;
        int columns = lines[0].length();
        for (int i = 0; i < rows; i++) {
            boolean allSpace = Arrays.stream(lines[i].split("")).allMatch("."::equals);
            if (allSpace) {
                linesToExpand.add(i);
            }
        }
        for (int i = 0; i < columns; i++) {
            var column = i;
            boolean allSpace = Arrays.stream(lines).allMatch(line -> line.charAt(column) == '.');
            if (allSpace) {
                columnsToExpand.add(i);
            }
        }
        var expanded = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            var lineBuilder = new StringBuilder();
            for (int j = 0; j < columns; j++) {
                lineBuilder.append(lines[i].charAt(j));
                if (columnsToExpand.contains(j)) {
                    lineBuilder.append(lines[i].charAt(j));
                }
            }
            expanded.append(lineBuilder).append("\n");
            if (linesToExpand.contains(i)) {
                expanded.append(lineBuilder).append("\n");
            }
        }
        return expanded.toString().trim();
    }

    public static int shortestPaths(String input) {
        String[] lines = input.split("\n");
        int rows = lines.length;
        int columns = lines[0].length();
        var galaxyPositions = new ArrayList<Position>();
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (lines[row].charAt(column) == '#') {
                    galaxyPositions.add(new Position(row, column));
                }
            }
        }
        var sumOfShortestPaths = 0;
        for (int firstGalaxy = 0; firstGalaxy < galaxyPositions.size(); firstGalaxy++) {
            for (int secondGalaxy = firstGalaxy + 1; secondGalaxy < galaxyPositions.size(); secondGalaxy++) {
                Position firstGalaxyPosition = galaxyPositions.get(firstGalaxy);
                Position secondGalaxyPosition = galaxyPositions.get(secondGalaxy);
                sumOfShortestPaths += firstGalaxyPosition.manhattanDistanceTo(secondGalaxyPosition);
            }
        }
        return sumOfShortestPaths;
    }

    public record Position(int row, int col) {
        public int manhattanDistanceTo(Position other) {
            return Math.abs(other.row() - row()) + Math.abs(other.col() - col());
        }
    }
}

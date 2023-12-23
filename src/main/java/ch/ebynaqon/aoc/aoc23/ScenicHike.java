package ch.ebynaqon.aoc.aoc23;

import java.util.HashSet;
import java.util.List;

public class ScenicHike {

    private final String[] lines;
    private final int rows;
    private final int columns;

    public ScenicHike(String input) {
        lines = input.split("\n");
        rows = lines.length;
        columns = lines[0].length();
    }

    private int findLongestPath(Position start, Position end, HashSet<Position> visitedWaypoints) {
        var currentPosition = start;
        int numberOfSteps = 0;
        var visitedPositions = new HashSet<>();
        while (!currentPosition.equals(end)) {
            visitedPositions.add(currentPosition);
            numberOfSteps++;
            List<Position> neighbours = currentPosition.neighbours()
                    .stream()
                    .filter(this::isInside)
                    .filter(point -> symbolAtPosition(point) != '#')
                    .filter(point -> !visitedPositions.contains(point))
                    .filter(point -> !visitedWaypoints.contains(point))
                    .toList();
            if (neighbours.size() > 1) {
                var currentWaypoint = currentPosition;
                return neighbours.stream()
                               .filter(neighbour -> this.canMove(currentWaypoint, neighbour))
                               .mapToInt(neighbour -> {
                                   var visitedWaypointsWithCurrent = new HashSet<>(visitedWaypoints);
                                   visitedWaypointsWithCurrent.add(currentWaypoint);
                                   return findLongestPath(neighbour, end, visitedWaypointsWithCurrent);
                               })
                               .max()
                               .getAsInt() + numberOfSteps;
            } else if (neighbours.size() == 1) {
                currentPosition = neighbours.getFirst();
            } else {
                return 0;
            }
        }
        return numberOfSteps;
    }

    private boolean canMove(Position from, Position to) {
        return switch (symbolAtPosition(to)) {
            case '.' -> true;
            case '<' -> to.row() == from.row() && to.column() < from.column();
            case '>' -> to.row() == from.row() && to.column() > from.column();
            case '^' -> to.row() < from.row() && to.column() == from.column();
            case 'v' -> to.row() > from.row() && to.column() == from.column();
            default ->
                    throw new IllegalStateException("Unexpected symbol '%s' at position %s".formatted(symbolAtPosition(to), to));
        };
    }

    private boolean isInside(Position position) {
        return position.row() >= 0 && position.column() >= 0
               && position.row() < rows && position.column() < columns;
    }

    public int findLongestPath() {
        return findLongestPath(findStartingPosition(), findEndPosition(), new HashSet<>());
    }

    private Position findEndPosition() {
        for (int column = 0; column < columns; column++) {
            if (symbolAtPosition(rows - 1, column) == '.') {
                return new Position(rows - 1, column);
            }
        }
        throw new IllegalStateException("Could not find end position!");
    }

    private char symbolAtPosition(int row, int column) {
        return lines[row].charAt(column);
    }

    private char symbolAtPosition(Position position) {
        return symbolAtPosition(position.row(), position.column());
    }

    private Position findStartingPosition() {
        return new Position(0, 1);
    }

    public record Position(int row, int column) {
        public List<Position> neighbours() {
            return List.of(
                    new Position(row - 1, column),
                    new Position(row, column + 1),
                    new Position(row + 1, column),
                    new Position(row, column - 1)
            );
        }
    }
}

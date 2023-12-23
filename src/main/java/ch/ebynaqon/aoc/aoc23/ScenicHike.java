package ch.ebynaqon.aoc.aoc23;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ScenicHike {

    private final String[] lines;
    private final int rows;
    private final int columns;
    private final boolean considerSlopes;

    public ScenicHike(String input, boolean considerSlopes) {
        lines = input.split("\n");
        rows = lines.length;
        columns = lines[0].length();
        this.considerSlopes = considerSlopes;
    }

    private HashSet<Segment> findSegments(Position lastWaypoint, Position start, Position end, HashSet<Position> visitedWaypoints) {
        var currentPosition = start;
        int numberOfSteps = 0;
        var visitedPositions = new HashSet<Position>();
        visitedPositions.add(lastWaypoint);
        boolean isForward = canMove(lastWaypoint, currentPosition);
        boolean isBackward = canMove(currentPosition, lastWaypoint);
        while (!currentPosition.equals(end)) {
            visitedPositions.add(currentPosition);
            numberOfSteps++;
            List<Position> neighbours = currentPosition.neighbours()
                    .stream()
                    .filter(this::isInside)
                    .filter(point -> symbolAtPosition(point) != '#')
                    .filter(point -> !visitedPositions.contains(point))
                    .toList();
            if (neighbours.size() > 1) {
                var segments = new HashSet<Segment>();

                var currentWaypoint = currentPosition;
                if (!considerSlopes || isForward) {
                    segments.add(new Segment(lastWaypoint, currentWaypoint, numberOfSteps, visitedPositions));
                }
                if (!considerSlopes || isBackward) {
                    segments.add(new Segment(currentWaypoint, lastWaypoint, numberOfSteps, visitedPositions));
                }
                if (!visitedWaypoints.contains(currentWaypoint)) {
                    visitedWaypoints.add(currentWaypoint);
                    segments.addAll(neighbours.stream()
                            .flatMap(neighbour ->
                                    findSegments(currentWaypoint, neighbour, end, visitedWaypoints).stream()
                            )
                            .toList());
                }
                return segments;
            } else if (neighbours.size() == 1) {
                var nextPosition = neighbours.getFirst();
                isForward &= canMove(currentPosition, nextPosition);
                isBackward &= canMove(nextPosition, currentPosition);
                currentPosition = nextPosition;
            } else {
                return new HashSet<>();
            }
        }
        visitedPositions.add(end);
        return new HashSet<>(List.of(new Segment(lastWaypoint, end, numberOfSteps, visitedPositions)));
    }

    public int findLongestPath() {
        Position start = findStartingPosition();
        Position end = findEndPosition();
        var segments = findSegments(start, start, end, new HashSet<>());
        var segmentsByPosition = new HashMap<Position, List<Segment>>();
        for (var segment : segments) {
            segmentsByPosition.putIfAbsent(segment.start(), new ArrayList<>());
            segmentsByPosition.get(segment.start()).add(segment);
        }
        return findLongestPath(start, end, segmentsByPosition, new ArrayList<>());
    }

    private int findLongestPath(Position start, Position end, HashMap<Position, List<Segment>> segments, List<Position> visited) {
        int longestPath = -1;
        var nextVisited = new ArrayList<>(visited);
        nextVisited.add(start);
        for (var segment : segments.get(start)) {
            if (!visited.contains(segment.end())) {
                if (segment.end().equals(end)) {
                    return segment.length();
                }
                int longestPathAfterSegment = findLongestPath(segment.end(), end, segments, nextVisited);
                if (longestPathAfterSegment > -1) {
                    longestPath = Math.max(longestPath, longestPathAfterSegment + segment.length());
                }
            }
        }
        return longestPath;
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

    private char symbolAtPosition(int row, int column) {
        return lines[row].charAt(column);
    }

    private char symbolAtPosition(Position position) {
        return symbolAtPosition(position.row(), position.column());
    }

    private boolean isInside(Position position) {
        return position.row() >= 0 && position.column() >= 0
               && position.row() < rows && position.column() < columns;
    }

    private Position findStartingPosition() {
        for (int column = 0; column < columns; column++) {
            if (symbolAtPosition(0, column) == '.') {
                return new Position(0, column);
            }
        }
        throw new IllegalStateException("Could not find start position!");
    }

    private Position findEndPosition() {
        for (int column = 0; column < columns; column++) {
            if (symbolAtPosition(rows - 1, column) == '.') {
                return new Position(rows - 1, column);
            }
        }
        throw new IllegalStateException("Could not find end position!");
    }

    public record Segment(Position start, Position end, int length, HashSet<Position> visitedPositions) {
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

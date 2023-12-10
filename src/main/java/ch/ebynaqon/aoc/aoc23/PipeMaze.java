package ch.ebynaqon.aoc.aoc23;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ch.ebynaqon.aoc.aoc23.PipeMaze.Tile.*;
import static java.util.function.Predicate.not;

public record PipeMaze(List<List<Tile>> tiles) {

    public static PipeMaze parse(String input) {
        return new PipeMaze(
                Arrays.stream(input.split("\n"))
                        .map(line -> Arrays.stream(line.split(""))
                                .map(PipeMaze::parseTile)
                                .toList())
                        .toList()
        );
    }

    public static Tile parseTile(String c) {
        return switch (c) {
            case "S" -> Tile.START;
            case "|" -> Tile.TOP_BOTTOM;
            case "-" -> Tile.LEFT_RIGHT;
            case "F" -> Tile.BOTTOM_RIGHT;
            case "7" -> Tile.BOTTOM_LEFT;
            case "J" -> Tile.TOP_LEFT;
            case "L" -> Tile.TOP_RIGHT;
            default -> Tile.NONE;
        };
    }

    public long findMaxDistanceFromStart() {
        var startPosition = getStartPosition();
        var leftPathPosition = findNextPosition(startPosition, null, true);
        var rightPathPosition = findNextPosition(startPosition, leftPathPosition, true);
        var prevLeftPathPosition = startPosition;
        var prevRightPathPosition = startPosition;

        long maxPositions = (long) tiles().get(0).size() * tiles.size();
        long distance = 1;
        while (distance < maxPositions) {
            var nextLeftPathPosition = findNextPosition(leftPathPosition, prevLeftPathPosition, false);
            var nextRightPathPosition = findNextPosition(rightPathPosition, prevRightPathPosition, false);

            if (leftPathPosition.equals(rightPathPosition)
                || nextLeftPathPosition.equals(rightPathPosition)
                || nextRightPathPosition.equals(leftPathPosition)) {
                break;
            }

            prevLeftPathPosition = leftPathPosition;
            prevRightPathPosition = rightPathPosition;
            leftPathPosition = nextLeftPathPosition;
            rightPathPosition = nextRightPathPosition;
            distance++;
        }

        return distance;
    }

    public int getNumberOfTilesInsideLoop(boolean printTiles) {
        var leftPositions = new HashSet<Position>();
        var rightPositions = new HashSet<Position>();
        var loop = new ArrayList<Position>();
        var startPosition = getStartPosition();
        var curPosition = startPosition;
        loop.add(curPosition);
        var nextPosition = findNextPosition(startPosition, null, true);
        var firstPosition = nextPosition;
        while (!nextPosition.equals(startPosition)) {
            var previousPosition = curPosition;
            curPosition = nextPosition;
            loop.add(curPosition);
            nextPosition = findNextPosition(curPosition, previousPosition, false);
            leftPositions.remove(nextPosition);
            rightPositions.remove(nextPosition);

            LeftRight leftRight = getLeftAndRightPosition(previousPosition, curPosition, nextPosition);
            leftRight.left().stream().filter(this::isInsideGrid).filter(not(loop::contains)).forEach(leftPositions::add);
            leftRight.right().stream().filter(this::isInsideGrid).filter(not(loop::contains)).forEach(rightPositions::add);
        }
        LeftRight leftRight = getLeftAndRightPosition(curPosition, nextPosition, firstPosition);
        leftRight.left().stream().filter(this::isInsideGrid).filter(not(loop::contains)).forEach(leftPositions::add);
        leftRight.right().stream().filter(this::isInsideGrid).filter(not(loop::contains)).forEach(rightPositions::add);

        fill(loop, leftPositions, rightPositions);

        if (printTiles) {
            print(loop, leftPositions, rightPositions);
        }
        return isOutside(rightPositions) ? leftPositions.size() : rightPositions.size();
    }

    private boolean isOutside(HashSet<Position> leftPositions) {
        int rows = tiles().size();
        int cols = tiles().getFirst().size();
        return leftPositions.stream().anyMatch(position ->
                position.row() == 0 || position.row() >= rows - 1 ||
                position.col() == 0 || position.col() >= cols
        );
    }

    private void print(Collection<Position> loop, Collection<Position> leftPositions, Collection<Position> rightPositions) {
        StringBuilder output = new StringBuilder();
        var row = 0;
        HashSet<Position> printedPositions = new HashSet<>();
        for (var line : tiles()) {
            var col = 0;
            for (var tile : line) {
                var pos = new Position(row, col);
                printedPositions.add(pos);
                output.append(
                        loop.contains(pos)
                                ? "*"
                                : rightPositions.contains(pos)
                                ? "R"
                                : leftPositions.contains(pos)
                                ? "L"
                                : "?");
                col++;
            }
            output.append("\n");
            row++;
        }
        System.out.println(output);
        System.out.printf("Total: %d%n", tiles().size() * tiles().getFirst().size());
        System.out.printf("Loop: %d%n", loop.size());
        System.out.printf("Left: %d%n", leftPositions.size());
        System.out.printf("Right: %d%n", rightPositions.size());
        System.out.println(rightPositions.stream().filter(not(printedPositions::contains)).toList());
    }

    private void fill(ArrayList<Position> loop, HashSet<Position> leftPositions, HashSet<Position> rightPositions) {
        var unfilled = new HashSet<Position>();
        leftPositions.stream().flatMap(p -> getUnfilled(loop, leftPositions, rightPositions, p).stream())
                .forEach(unfilled::add);
        while (!unfilled.isEmpty()) {
            Position position = unfilled.iterator().next();
            leftPositions.add(position);
            unfilled.addAll(getUnfilled(loop, leftPositions, rightPositions, position));
            unfilled.remove(position);
        }
        rightPositions.stream().flatMap(p -> getUnfilled(loop, leftPositions, rightPositions, p).stream())
                .forEach(unfilled::add);
        while (!unfilled.isEmpty()) {
            Position position = unfilled.iterator().next();
            rightPositions.add(position);
            unfilled.addAll(getUnfilled(loop, leftPositions, rightPositions, position));
            unfilled.remove(position);
        }

    }

    private Set<Position> getUnfilled(ArrayList<Position> loop, HashSet<Position> leftPositions, HashSet<Position> rightPositions, Position startPosition) {
        return Stream.of(startPosition.up(), startPosition.down(), startPosition.left(), startPosition.right())
                .filter(Objects::nonNull)
                .filter(this::isInsideGrid)
                .filter(not(loop::contains))
                .filter(not(leftPositions::contains))
                .filter(not(rightPositions::contains))
                .collect(Collectors.toSet());
    }

    public LeftRight getLeftAndRightPosition(Position previous, Position current, Position next) {
        if (previous.row() == current.row() && current.row() == next.row()) {
            // moving on one row
            if (previous.col() < next.col()) {
                // moving right
                return new LeftRight(List.of(current.up()), List.of(current.down()));
            } else {
                // moving left
                return new LeftRight(List.of(current.down()), List.of(current.up()));
            }
        } else if (previous.col() == current.col() && current.col() == next.col()) {
            // moving on one column
            if (previous.row() < next.row()) {
                // moving down
                return new LeftRight(List.of(current.right()), List.of(current.left()));
            } else {
                // moving up
                return new LeftRight(List.of(current.left()), List.of(current.right()));
            }
        } else {
            // it is a corner
            if (next.row() < previous.row()) {
                // moving up
                if (next.col() > previous.col()) {
                    // moving right
                    if (previous.col() == current.col()) {
                        return new LeftRight(List.of(current.left(), current.up()), List.of());
                    } else {
                        return new LeftRight(List.of(), List.of(current.down(), current.right()));
                    }
                } else {
                    // moving left
                    if (previous.col() == current.col()) {
                        return new LeftRight(List.of(), List.of(current.up(), current.right()));
                    } else {
                        return new LeftRight(List.of(current.down(), current.left()), List.of());
                    }
                }
            } else {
                // moving down
                if (next.col() > previous.col()) {
                    // moving right
                    if (previous.col() == current.col()) {
                        return new LeftRight(List.of(), List.of(current.left(), current.down()));
                    } else {
                        return new LeftRight(List.of(current.right(), current.up()), List.of());
                    }
                } else {
                    // moving left
                    if (previous.col() == current.col()) {
                        return new LeftRight(List.of(current.right(), current.down()), List.of());
                    } else {
                        return new LeftRight(List.of(), List.of(current.left(), current.up()));
                    }
                }
            }
        }
    }

    public record LeftRight(List<Position> left, List<Position> right) {
    }

    private Position findNextPosition(Position curPosition, Position excludedPosition, boolean isStartMove) {
        Position nextPosition;
        Tile tile;
        var currentTile = getTile(curPosition);
        if (isStartMove || TOP_CONNECTED.contains(currentTile)) {
            nextPosition = curPosition.up();
            tile = getTile(nextPosition);
            if (tile != null && BOTTOM_CONNECTED.contains(tile) && !nextPosition.equals(excludedPosition)) {
                return nextPosition;
            }
        }
        if (isStartMove || BOTTOM_CONNECTED.contains(currentTile)) {
            nextPosition = curPosition.down();
            tile = getTile(nextPosition);
            if (tile != null && TOP_CONNECTED.contains(tile) && !nextPosition.equals(excludedPosition)) {
                return nextPosition;
            }
        }
        if (isStartMove || LEFT_CONNECTED.contains(currentTile)) {
            nextPosition = curPosition.left();
            tile = getTile(nextPosition);
            if (tile != null && RIGHT_CONNECTED.contains(tile) && !nextPosition.equals(excludedPosition)) {
                return nextPosition;
            }
        }
        if (isStartMove || RIGHT_CONNECTED.contains(currentTile)) {
            nextPosition = curPosition.right();
            tile = getTile(nextPosition);
            if (tile != null && LEFT_CONNECTED.contains(tile) && !nextPosition.equals(excludedPosition)) {
                return nextPosition;
            }
        }
        throw new RuntimeException("No new Position could be found starting at %s!".formatted(curPosition));
    }

    private Tile getTile(Position position) {
        if (isInsideGrid(position)) {
            return tiles.get(position.row).get(position.col);
        }
        return null;
    }

    private boolean isInsideGrid(Position position) {
        return position.row >= 0 && position.row < tiles().size()
               && position.col >= 0 && position.col < tiles.getFirst().size();
    }

    private Position getStartPosition() {
        int curRow;
        int curCol = 0;
        for (curRow = 0; curRow < tiles.size(); curRow++) {
            curCol = tiles.get(curRow).indexOf(Tile.START);
            if (curCol > -1) break;
        }
        return new Position(curRow, curCol);
    }

    public record Position(int row, int col) {
        public Position up() {
            return new Position(row - 1, col);
        }

        public Position down() {
            return new Position(row + 1, col);
        }

        public Position left() {
            return new Position(row, col - 1);
        }

        public Position right() {
            return new Position(row, col + 1);
        }
    }

    public enum Tile {
        NONE,
        START,
        TOP_BOTTOM,
        LEFT_RIGHT,
        BOTTOM_RIGHT,
        BOTTOM_LEFT,
        TOP_RIGHT,
        TOP_LEFT;

        public static final List<Tile> BOTTOM_CONNECTED = List.of(TOP_BOTTOM, BOTTOM_LEFT, BOTTOM_RIGHT, START);
        public static final List<Tile> TOP_CONNECTED = List.of(TOP_BOTTOM, TOP_LEFT, TOP_RIGHT, START);
        public static final List<Tile> LEFT_CONNECTED = List.of(LEFT_RIGHT, TOP_LEFT, BOTTOM_LEFT, START);
        public static final List<Tile> RIGHT_CONNECTED = List.of(LEFT_RIGHT, TOP_RIGHT, BOTTOM_RIGHT, START);
    }
}

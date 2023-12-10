package ch.ebynaqon.aoc.aoc23;

import java.util.Arrays;
import java.util.List;

import static ch.ebynaqon.aoc.aoc23.PipeMaze.Tile.*;

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
        if (position.row >= 0 && position.row < tiles().size()
            && position.col >= 0 && position.col < tiles.get(0).size()) {
            return tiles.get(position.row).get(position.col);
        }
        return null;
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

        public static final List<Tile> BOTTOM_CONNECTED = List.of(TOP_BOTTOM, BOTTOM_LEFT, BOTTOM_RIGHT);
        public static final List<Tile> TOP_CONNECTED = List.of(TOP_BOTTOM, TOP_LEFT, TOP_RIGHT);
        public static final List<Tile> LEFT_CONNECTED = List.of(LEFT_RIGHT, TOP_LEFT, BOTTOM_LEFT);
        public static final List<Tile> RIGHT_CONNECTED = List.of(LEFT_RIGHT, TOP_RIGHT, BOTTOM_RIGHT);
    }
}

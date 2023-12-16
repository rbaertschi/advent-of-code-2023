package ch.ebynaqon.aoc.aoc23;

import java.util.*;

import static ch.ebynaqon.aoc.aoc23.FloorWillBeLava.Direction.*;

public record FloorWillBeLava(List<List<Tile>> tiles) {

    public static FloorWillBeLava parse(String input) {
        var tiles = Arrays.stream(input.split("\n"))
                .map(line -> Arrays.stream(line.split(""))
                        .map(tileChar -> (Tile) switch (tileChar) {
                            case "/" -> new DiagonalRightMirror();
                            case "\\" -> new DiagonalLeftMirror();
                            case "|" -> new VerticalSplitter();
                            case "-" -> new HorizontalSplitter();
                            default -> new EmptyTile();
                        })
                        .toList()
                )
                .toList();
        return new FloorWillBeLava(tiles);
    }

    public Tile getTileAt(Position position) {
        int rows = tiles().size();
        int columns = tiles().getFirst().size();
        if (position.row() < 0 || position.row() >= rows) {
            return null;
        }
        if (position.column() < 0 || position.column() >= columns) {
            return null;
        }
        return tiles.get(position.row()).get(position.column());
    }

    public long countEnergizedTiles(Position initialPosition, Direction initialDirection) {
        reset();
        Queue<PositionAndDirection> positionsToVisit = new ArrayDeque<>();
        positionsToVisit.add(new PositionAndDirection(initialPosition, initialDirection));
        while (!positionsToVisit.isEmpty()) {
            var positionAndDirection = positionsToVisit.poll();
            var position = positionAndDirection.position();
            Tile tile = getTileAt(position);
            if (tile != null) {
                tile.outgoingFrom(positionAndDirection.direction())
                        .stream()
                        .map(direction -> new PositionAndDirection(position.move(direction), direction))
                        .forEach(positionsToVisit::add);
            }
        }
        return tiles().stream().mapToLong(tile -> tile.stream().filter(Tile::isVisited).count()).sum();
    }

    private void reset() {
        tiles().forEach(line -> line.forEach(Tile::reset));
    }

    public long getMaxEnergizedTiles() {
        int rows = tiles().size();
        int columns = tiles().getFirst().size();
        long maxEnergized = 0;
        for (int row = 0; row < rows; row++) {
            long energizedTiles = countEnergizedTiles(new Position(row, 0), RIGHT);
            if (energizedTiles > maxEnergized) {
                maxEnergized = energizedTiles;
            }
            energizedTiles = countEnergizedTiles(new Position(row, columns - 1), LEFT);
            if (energizedTiles > maxEnergized) {
                maxEnergized = energizedTiles;
            }
        }
        for (int column = 0; column < columns; column++) {
            long energizedTiles = countEnergizedTiles(new Position(0, column), DOWN);
            if (energizedTiles > maxEnergized) {
                maxEnergized = energizedTiles;
            }
            energizedTiles = countEnergizedTiles(new Position(rows - 1, column), UP);
            if (energizedTiles > maxEnergized) {
                maxEnergized = energizedTiles;
            }
        }
        return maxEnergized;
    }

    public enum Direction {
        UP, LEFT, DOWN, RIGHT;
    }

    record PositionAndDirection(Position position, Direction direction) {
    }

    public record Position(int row, int column) {
        public Position move(Direction towards) {
            return switch (towards) {
                case UP -> new Position(row - 1, column);
                case DOWN -> new Position(row + 1, column);
                case LEFT -> new Position(row, column - 1);
                case RIGHT -> new Position(row, column + 1);
            };
        }
    }

    interface Tile {
        List<Direction> outgoingFrom(Direction incomming);

        boolean isVisited();

        void reset();
    }

    static class EmptyTile implements Tile {
        private final Set<Direction> visitedDirections = new HashSet<>();
        private boolean isVisited = false;

        @Override
        public List<Direction> outgoingFrom(Direction incomming) {
            isVisited = true;
            if (visitedDirections.contains(incomming)) {
                return List.of();
            } else {
                visitedDirections.add(incomming);
                return outgoingDirections(incomming);
            }
        }

        protected List<Direction> outgoingDirections(Direction incomming) {
            return switch (incomming) {
                case UP -> List.of(UP);
                case DOWN -> List.of(DOWN);
                case LEFT -> List.of(LEFT);
                case RIGHT -> List.of(RIGHT);
            };
        }

        public boolean isVisited() {
            return isVisited;
        }

        @Override
        public void reset() {
            isVisited = false;
            visitedDirections.clear();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof EmptyTile tile)) return false;
            return tile.getClass().equals(getClass());
        }

        @Override
        public int hashCode() {
            return Objects.hash(visitedDirections, isVisited);
        }
    }

    static class VerticalSplitter extends EmptyTile implements Tile {
        @Override
        protected List<Direction> outgoingDirections(Direction incomming) {
            return switch (incomming) {
                case UP -> List.of(UP);
                case DOWN -> List.of(DOWN);
                case LEFT -> List.of(UP, DOWN);
                case RIGHT -> List.of(UP, DOWN);
            };
        }
    }

    static class HorizontalSplitter extends EmptyTile implements Tile {
        @Override
        protected List<Direction> outgoingDirections(Direction incomming) {
            return switch (incomming) {
                case UP -> List.of(LEFT, RIGHT);
                case DOWN -> List.of(LEFT, RIGHT);
                case LEFT -> List.of(LEFT);
                case RIGHT -> List.of(RIGHT);
            };
        }
    }

    /**
     * Represents the '/' mirror oriented from bottom left to top right.
     */
    static class DiagonalRightMirror extends EmptyTile implements Tile {
        @Override
        protected List<Direction> outgoingDirections(Direction incomming) {
            return switch (incomming) {
                case UP -> List.of(RIGHT);
                case DOWN -> List.of(LEFT);
                case LEFT -> List.of(DOWN);
                case RIGHT -> List.of(UP);
            };
        }
    }

    /**
     * Represents the '\' mirror oriented from top left to bottom right.
     */
    static class DiagonalLeftMirror extends EmptyTile implements Tile {
        @Override
        protected List<Direction> outgoingDirections(Direction incomming) {
            return switch (incomming) {
                case UP -> List.of(LEFT);
                case DOWN -> List.of(RIGHT);
                case LEFT -> List.of(UP);
                case RIGHT -> List.of(DOWN);
            };
        }
    }

}

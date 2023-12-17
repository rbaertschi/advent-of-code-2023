package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.helper.CollectionHelper;

import java.util.*;
import java.util.stream.Stream;

public class ClumsyCrucible {

    private final int rows;
    private final List<List<Integer>> heatLoss;
    private final List<List<Integer>> leastHeatLoss;
    private final int cols;

    public ClumsyCrucible(String input) {
        this.heatLoss = Arrays.stream(input.split("\n"))
                .map(line -> Arrays.stream(line.split(""))
                        .map(Integer::parseInt)
                        .toList()
                )
                .toList();
        rows = heatLoss.size();
        cols = heatLoss.getFirst().size();
        leastHeatLoss = new ArrayList<>(rows);
        for (int row = 0; row < rows; row++) {
            List<Integer> rowData = new ArrayList<>(cols);
            for (int column = 0; column < cols; column++) {
                rowData.add(Integer.MAX_VALUE / 2);
            }
            leastHeatLoss.add(rowData);
        }
    }

    public int heatLossAt(Position position) {
        return heatLoss.get(position.row()).get(position.column());
    }

    public int leastHeatLossAt(Position position) {
        return leastHeatLoss.get(position.row()).get(position.column());
    }

    public int setLeastHeatLossAt(Position position, int heatLoss) {
        return leastHeatLoss.get(position.row()).set(position.column(), heatLoss);
    }

    public int leastHeatlossPath() {
        return leastHeatlossPath(new Position(0,0), new Position(rows - 1, cols - 1));
    }

    public int leastHeatlossPath(Position start, Position end) {
        PriorityQueue<Path> paths = new PriorityQueue<>(Comparator.comparing(Path::predictedHeatLoss));
        var curPath = new Path(List.of(start), 0, end.distance(start));
        while (!end.equals(curPath.positions().getLast())) {
            List<Position> previousPositions = curPath.positions();
            int accumulatedHeatLoss = curPath.accumulatedHeatLoss();
            for (Position nextPosition : nextPositions(previousPositions, rows, cols)) {
                int nextHeatLoss = accumulatedHeatLoss + heatLossAt(nextPosition);
                if (nextHeatLoss <= leastHeatLossAt(nextPosition) + 18) {
                    if (nextHeatLoss < leastHeatLossAt(nextPosition)) {
                        setLeastHeatLossAt(nextPosition, nextHeatLoss);
                    }
                    Path path = new Path(
                            CollectionHelper.append(previousPositions, nextPosition),
                            nextHeatLoss,
                            nextHeatLoss + end.distance(nextPosition)
                    );
                    paths.add(path);
                }
            }
            curPath = paths.poll();
            if (curPath == null) break;
        }
        return curPath.accumulatedHeatLoss();
    }

    public static List<Position> nextPositions(List<Position> positions, int rows, int cols) {
        Position lastPosition = positions.getLast();
        return Stream.of(
                        new Position(lastPosition.row() + 1, lastPosition.column()),
                        new Position(lastPosition.row() - 1, lastPosition.column()),
                        new Position(lastPosition.row(), lastPosition.column() + 1),
                        new Position(lastPosition.row(), lastPosition.column() - 1)
                )
                .filter(position -> isValidPath(positions, position, rows, cols))
                .toList();
    }

    private static boolean isValidPath(List<Position> path, Position last, int rows, int cols) {
        if (last.row() < 0 || last.row() > rows - 1
            || last.column() < 0 || last.column() > cols - 1) {
            // out of bounds
            return false;
        }
        if (path.size() >= 2 && last.equals(path.get(path.size() - 2))) {
            // hard turn
            return false;
        }
        if (path.stream().anyMatch(prevPos -> prevPos.equals(last))) {
            // return to previous point
            return false;
        }
        if (path.size() >= 4) {
            var lastPositions = List.of(
                    path.get(path.size() - 4),
                    path.get(path.size() - 3),
                    path.get(path.size() - 2),
                    path.get(path.size() - 1)
            );
            if (lastPositions.stream().map(Position::column).allMatch(column -> column == last.column())) {
                // more than 3 steps in same column
                return false;
            }
            if (lastPositions.stream().map(Position::row).allMatch(row -> row == last.row())) {
                // more than 3 steps in same row
                return false;
            }
        }
        return true;
    }

    public record Position(int row, int column) {
        public int distance(Position other) {
            return Math.abs(other.row() - row()) + Math.abs(other.column() - column());
        }
    }

    public record Path(List<Position> positions, int accumulatedHeatLoss, int predictedHeatLoss) {
    }
}

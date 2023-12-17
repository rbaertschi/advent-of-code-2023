package ch.ebynaqon.aoc.aoc23;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClumsyCrucible {

    private final int rows;
    private final List<List<Integer>> heatLoss;
    private final List<List<Integer>> leastHeatLossVertical;
    private final List<List<Integer>> leastHeatLossHorizontal;
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
        leastHeatLossVertical = new ArrayList<>(rows);
        leastHeatLossHorizontal = new ArrayList<>(rows);
        for (int row = 0; row < rows; row++) {
            List<Integer> rowDataVertical = new ArrayList<>(cols);
            List<Integer> rowDataHorizontal = new ArrayList<>(cols);
            for (int column = 0; column < cols; column++) {
                rowDataVertical.add(Integer.MAX_VALUE / 2);
                rowDataHorizontal.add(Integer.MAX_VALUE / 2);
            }
            leastHeatLossVertical.add(rowDataVertical);
            leastHeatLossHorizontal.add(rowDataHorizontal);
        }
    }

    public int heatLossAt(Position position) {
        return heatLossAt(position.row(), position.column());
    }

    private Integer heatLossAt(int row, int column) {
        return heatLoss.get(row).get(column);
    }

    public int leastHeatLoss(int minMove, int maxMove) {
        Position start = new Position(0, 0);
        leastHeatLossHorizontal.get(start.row()).set(start.column(), 0);
        leastHeatLossVertical.get(start.row()).set(start.column(), 0);
        Position end = new Position(rows - 1, cols - 1);
        var candidates = new ArrayDeque<Position>();
        var nextPosition = start;
        while (nextPosition != null) {
            int row = nextPosition.row();
            int col = nextPosition.column();
            int nextHeatLoss = leastHeatLossHorizontal.get(row).get(col);
            for (int nextRow = row + 1; nextRow <= Math.min(row + maxMove, rows - 1); nextRow++) {
                nextHeatLoss += heatLoss.get(nextRow).get(col);
                if (nextRow >= row + minMove && nextHeatLoss < leastHeatLossVertical.get(nextRow).get(col)) {
                    candidates.add(new Position(nextRow, col));
                    leastHeatLossVertical.get(nextRow).set(col, nextHeatLoss);
                }
            }
            nextHeatLoss = leastHeatLossHorizontal.get(row).get(col);
            for (int nextRow = row - 1; nextRow >= Math.max(row - maxMove, 0); nextRow--) {
                nextHeatLoss += heatLoss.get(nextRow).get(col);
                if (nextRow <= row - minMove && nextHeatLoss < leastHeatLossVertical.get(nextRow).get(col)) {
                    candidates.add(new Position(nextRow, col));
                    leastHeatLossVertical.get(nextRow).set(col, nextHeatLoss);
                }
            }
            nextHeatLoss = leastHeatLossVertical.get(row).get(col);
            for (int nextCol = col + 1; nextCol <= Math.min(col + maxMove, cols - 1); nextCol++) {
                nextHeatLoss += heatLoss.get(row).get(nextCol);
                if (nextCol >= col + minMove && nextHeatLoss < leastHeatLossHorizontal.get(row).get(nextCol)) {
                    candidates.add(new Position(row, nextCol));
                    leastHeatLossHorizontal.get(row).set(nextCol, nextHeatLoss);
                }
            }
            nextHeatLoss = leastHeatLossVertical.get(row).get(col);
            for (int nextCol = col - 1; nextCol >= Math.max(col - maxMove, 0); nextCol--) {
                nextHeatLoss += heatLoss.get(row).get(nextCol);
                if (nextCol <= col - minMove && nextHeatLoss < leastHeatLossHorizontal.get(row).get(nextCol)) {
                    candidates.add(new Position(row, nextCol));
                    leastHeatLossHorizontal.get(row).set(nextCol, nextHeatLoss);
                }
            }
            nextPosition = candidates.poll();
        }
        return Math.min(leastHeatLossHorizontal.get(end.row()).get(end.column()),
                leastHeatLossVertical.get(end.row()).get(end.column()));
    }

    public record Position(int row, int column) {
    }

}

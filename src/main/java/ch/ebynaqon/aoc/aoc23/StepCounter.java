package ch.ebynaqon.aoc.aoc23;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class StepCounter {

    private final String[] lines;
    private final int rows;
    private final int columns;
    private final HashMap<CacheKey, Integer> cache;

    public StepCounter(String input) {
        lines = input.split("\n");
        rows = lines.length;
        columns = lines[0].length();
        cache = new HashMap<>();
    }

    public long numberOfReachablePlotsInInfiniteGarden(int stepGoal) {
        long totalReachable = 0L;
        int gardenOffset = Math.max(0, stepGoal - 65) / 131 + (Math.max(0, stepGoal - 65) % 131 > 0 ? 1 : 0);
        for (int gardenRow = -gardenOffset; gardenRow <= gardenOffset; gardenRow++) {
            int gardenColumnOffset = gardenOffset - Math.abs(gardenRow);
            for (int gardenColumn = gardenColumnOffset + 1; gardenColumn > Math.max(0, gardenColumnOffset - 2); gardenColumn--) {
                totalReachable += getReachableInGarden(stepGoal, new Garden(gardenRow, -gardenColumn));
                totalReachable += getReachableInGarden(stepGoal, new Garden(gardenRow, gardenColumn));
            }
            var remainingColumns = gardenColumnOffset - 2;
            // multiples of 2 in repeating space
            if (remainingColumns % 2 == 1) {
                totalReachable += getReachableInGarden(stepGoal, new Garden(gardenRow, -remainingColumns));
                totalReachable += getReachableInGarden(stepGoal, new Garden(gardenRow, remainingColumns));
                remainingColumns--;
            }
            if (remainingColumns >= 2) {
                long multiplier = remainingColumns / 2;
                totalReachable += multiplier * getReachableInGarden(stepGoal, new Garden(gardenRow, -1));
                totalReachable += multiplier * getReachableInGarden(stepGoal, new Garden(gardenRow, 1));
                totalReachable += multiplier * getReachableInGarden(stepGoal, new Garden(gardenRow, -2));
                totalReachable += multiplier * getReachableInGarden(stepGoal, new Garden(gardenRow, 2));
            }
            // center
            totalReachable += getReachableInGarden(stepGoal, new Garden(gardenRow, 0));
        }
        return totalReachable;
    }

    private int getReachableInGarden(int stepGoal, Garden garden) {
        int reachableInGarden;
        int remainingDistance = stepGoal - garden.distanceFromStart();
        int distanceInGarden = Math.min(2 * 131, remainingDistance);
        if (distanceInGarden < 0) {
            return 0;
        }
        if (distanceInGarden == 0) {
            return 1;
        }
        Plot startingPlot = garden.startingPlot();
//        boolean isEven = garden.isEven() ^ (distanceInGarden % 2 == 0);
        boolean isEven = remainingDistance % 2 == 0;
        CacheKey key = new CacheKey(startingPlot, distanceInGarden, isEven);
        if (cache.containsKey(key)) {
            reachableInGarden = cache.get(key);
        } else {
            reachableInGarden = numberOfReachablePlots(distanceInGarden, startingPlot, isEven, false);
            cache.put(key, reachableInGarden);
        }
        return reachableInGarden;
    }

    public record CacheKey(Plot startingPlot, int numberOfSteps, boolean isEven) {
    }

    public record Garden(int row, int column) {
        public boolean isEven() {
            return (Math.abs(row()) + Math.abs(column())) % 2 == 0;
        }

        public Plot startingPlot() {
            return new Plot(
                    row < 0 ? 130 : row == 0 ? 65 : 0,
                    column < 0 ? 130 : column == 0 ? 65 : 0
            );
        }

        public int distanceFromStart() {
            int rowDistance = Math.abs(row) > 0 ? 66 + (Math.abs(row) - 1) * 131 : 0;
            int columnDistance = Math.abs(column) > 0 ? 66 + (Math.abs(column) - 1) * 131 : 0;
            return rowDistance + columnDistance;
        }
    }

    public int numberOfReachablePlots(int stepGoal, Plot startingPlot, boolean isEven, boolean infinite) {
        var plotsReachedWithEvenSteps = new HashSet<Plot>();
        var plotsReachedWithOddSteps = new HashSet<Plot>();
        var plotsToCheck = new HashSet<Plot>();
        plotsReachedWithEvenSteps.add(startingPlot);
        plotsToCheck.add(startingPlot);
        for (int step = 1; step <= stepGoal; step++) {
            var nextPlotsToCheck = new HashSet<Plot>();
            var isEvenStep = (step % 2) == 0;
            for (var plot : plotsToCheck) {
                for (var neighbourPlot : infinite ? plot.getNeighbours() : plot.getNeighboursWithinBounds(rows, columns)) {
                    if (lines[(neighbourPlot.row() % rows + rows) % rows].charAt((neighbourPlot.column() % columns + columns) % columns) != '#') {
                        if (isEvenStep && !plotsReachedWithEvenSteps.contains(neighbourPlot)) {
                            plotsReachedWithEvenSteps.add(neighbourPlot);
                            nextPlotsToCheck.add(neighbourPlot);
                        }
                        if (!isEvenStep && !plotsReachedWithOddSteps.contains(neighbourPlot)) {
                            plotsReachedWithOddSteps.add(neighbourPlot);
                            nextPlotsToCheck.add(neighbourPlot);
                        }
                    }
                }
            }
            plotsToCheck = nextPlotsToCheck;
        }
        return isEven ? plotsReachedWithEvenSteps.size() : plotsReachedWithOddSteps.size();
    }

    public static Plot findStartingPlot(String input) {
        String[] lines = input.split("\n");
        for (int row = 0; row < lines.length; row++) {
            var line = lines[row];
            for (int column = 0; column < line.length(); column++) {
                if (line.charAt(column) == 'S') {
                    return new Plot(row, column);
                }
            }
        }
        throw new IllegalStateException("Could not find starting plot!");
    }

    public record Plot(int row, int column) {
        public List<Plot> getNeighboursWithinBounds(int rows, int columns) {
            return getNeighbours()
                    .stream()
                    .filter(plot -> plot.isWithinBounds(rows, columns))
                    .toList();
        }

        private List<Plot> getNeighbours() {
            return List.of(
                    new Plot(row() + 1, column()),
                    new Plot(row() - 1, column()),
                    new Plot(row(), column() + 1),
                    new Plot(row(), column() - 1)
            );
        }

        public boolean isWithinBounds(int rows, int columns) {
            return row() >= 0 && row() < rows && column() >= 0 && column() < columns;
        }
    }
}

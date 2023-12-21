package ch.ebynaqon.aoc.aoc23;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

public class StepCounter {
    public static int numberOfReachablePlots(String input, int stepGoal) {
        String[] lines = input.split("\n");
        int rows = lines.length;
        int columns = lines[0].length();
        var plotsReachedWithEvenSteps = new HashSet<Plot>();
        var plotsReachedWithOddSteps = new HashSet<Plot>();
        var plotsToCheck = new HashSet<Plot>();
        var startingPlot = findStartingPlot(lines);
        plotsReachedWithEvenSteps.add(startingPlot);
        plotsToCheck.add(startingPlot);
        for (int step = 1; step <= stepGoal; step++) {
            var nextPlotsToCheck = new HashSet<Plot>();
            var isEvenStep = (step % 2) == 0;
            for (var plot : plotsToCheck) {
                for (var neighbourPlot : plot.getNeighboursWithinBounds(rows, columns)) {
                    if (lines[neighbourPlot.row()].charAt(neighbourPlot.column()) != '#') {
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
        return plotsReachedWithEvenSteps.size();
    }

    private static Plot findStartingPlot(String[] lines) {
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
            return Stream.of(
                            new Plot(row() + 1, column()),
                            new Plot(row() - 1, column()),
                            new Plot(row(), column() + 1),
                            new Plot(row(), column() - 1)
                    )
                    .filter(plot -> plot.isWithinBounds(rows, columns))
                    .toList();
        }

        public boolean isWithinBounds(int rows, int columns) {
            return row() >= 0 && row() < rows && column() >= 0 && column() < columns;
        }
    }
}

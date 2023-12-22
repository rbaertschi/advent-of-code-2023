package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.StepCounter.Garden;
import ch.ebynaqon.aoc.aoc23.StepCounter.Plot;
import ch.ebynaqon.aoc.aoc23.helper.TestHelper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class StepCounterTest {
    @Test
    void getNumberOfReachableSpacesForExample() {
        var input = """
                ...........
                .....###.#.
                .###.##..#.
                ..#.#...#..
                ....#.#....
                .##..S####.
                .##..#...#.
                .......##..
                .##.#.####.
                .##..##.##.
                ...........
                """.trim();

        Plot startingPlot = StepCounter.findStartingPlot(input);
        var actual = new StepCounter(input).numberOfReachablePlots(6, startingPlot, true, false);

        assertThat(actual).isEqualTo(16);
    }

    @Test
    void getNumberOfReachableSpacesForPart1() {
        var input = TestHelper.readInput("/day21-step-counter.txt").trim();

        Plot startingPlot = StepCounter.findStartingPlot(input);
        var actual = new StepCounter(input).numberOfReachablePlots(64, startingPlot, true, false);

        assertThat(actual).isEqualTo(3574);
    }

    @ParameterizedTest
    @MethodSource("startingPlots")
    void getNumberOfReachableSpacesFromEntryPoints(Plot startingPlot) {
        var input = TestHelper.readInput("/day21-step-counter.txt").trim();

        int stepGoal = startingPlot.row() == 65 || startingPlot.column() == 65 ? 130 + 66 : 2 * 130;
        var actual = new StepCounter(input).numberOfReachablePlots(stepGoal, startingPlot, true, false);

        assertThat(actual).isEqualTo((startingPlot.row() + startingPlot.column()) % 2 == 0
                ? 7327 : 7336);
    }

    public static Stream<Arguments> startingPlots() {
        return Stream.of(
                Arguments.of(new Plot(0, 0)),
                Arguments.of(new Plot(0, 65)),
                Arguments.of(new Plot(0, 130)),
                Arguments.of(new Plot(65, 0)),
                Arguments.of(new Plot(65, 65)),
                Arguments.of(new Plot(65, 130)),
                Arguments.of(new Plot(130, 0)),
                Arguments.of(new Plot(130, 65)),
                Arguments.of(new Plot(130, 130))
        );
    }

    @ParameterizedTest
    @MethodSource("gardenAndStartingPosition")
    void startingPositionForGarden(Garden garden, Plot startingPlot) {
        assertThat(garden.startingPlot()).isEqualTo(startingPlot);
    }

    public static Stream<Arguments> gardenAndStartingPosition() {
        return Stream.of(
                Arguments.of(new Garden(-1, -1), new Plot(130, 130)),
                Arguments.of(new Garden(-1, 0), new Plot(130, 65)),
                Arguments.of(new Garden(-1, 1), new Plot(130, 0)),
                Arguments.of(new Garden(0, -1), new Plot(65, 130)),
                Arguments.of(new Garden(0, 0), new Plot(65, 65)),
                Arguments.of(new Garden(0, 1), new Plot(65, 0)),
                Arguments.of(new Garden(1, -1), new Plot(0, 130)),
                Arguments.of(new Garden(1, 0), new Plot(0, 65)),
                Arguments.of(new Garden(1, 1), new Plot(0, 0))
        );
    }

    @ParameterizedTest
    @MethodSource("gardenAndDistanceFromStart")
    void distanceFromStartForGarden(Garden garden, int distance) {
        assertThat(garden.distanceFromStart()).isEqualTo(distance);
    }

    public static Stream<Arguments> gardenAndDistanceFromStart() {
        return Stream.of(
                Arguments.of(new Garden(-1, -1), 2 * 66),
                Arguments.of(new Garden(-1, 0), 66),
                Arguments.of(new Garden(-1, 1), 2 * 66),
                Arguments.of(new Garden(0, -1), 66),
                Arguments.of(new Garden(0, 0), 0),
                Arguments.of(new Garden(0, 1), 66),
                Arguments.of(new Garden(1, -1), 2 * 66),
                Arguments.of(new Garden(1, 0), 66),
                Arguments.of(new Garden(1, 1), 2 * 66),
                Arguments.of(new Garden(2, 3), (66 + 131) + (66 + 2 * 131))
        );
    }

    @Test
    void numberOfReachablePlotsInInfiniteGarden() {
        var input = TestHelper.readInput("/day21-step-counter.txt").trim();

        var actual = new StepCounter(input).numberOfReachablePlotsInInfiniteGarden(26501365);

        assertThat(actual).isEqualTo(600090522932119L);
    }

    @ParameterizedTest
    @MethodSource("stepGoalsForComparison")
    @Disabled
    void comparingAlgorithms(int stepGoal) {
        var input = TestHelper.readInput("/day21-step-counter.txt").trim();

        Plot startingPlot = StepCounter.findStartingPlot(input);
        var actual = new StepCounter(input).numberOfReachablePlotsInInfiniteGarden(stepGoal);
        var expected = new StepCounter(input).numberOfReachablePlots(stepGoal, startingPlot, stepGoal % 2 == 0, true);

        assertThat(actual).isEqualTo(expected);
    }

    public static Stream<Arguments> stepGoalsForComparison() {
        return IntStream.range(0, 7)
                .flatMap(i -> IntStream.range(i * 131 + 60, i * 131 + 70))
                .mapToObj(Arguments::of);
    }

}

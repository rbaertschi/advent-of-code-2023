package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.helper.TestHelper;
import org.junit.jupiter.api.Test;

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

        var actual = StepCounter.numberOfReachablePlots(input, 6);

        assertThat(actual).isEqualTo(16);
    }

    @Test
    void getNumberOfReachableSpacesForPart1() {
        var input = TestHelper.readInput("/day21-step-counter.txt").trim();

        var actual = StepCounter.numberOfReachablePlots(input, 64);

        assertThat(actual).isEqualTo(3574);
    }
}

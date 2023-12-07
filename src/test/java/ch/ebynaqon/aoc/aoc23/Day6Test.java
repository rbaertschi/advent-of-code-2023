package ch.ebynaqon.aoc.aoc23;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day6Test {
    @Test
    void parseInput() {
        var input = """
                Time:      7  15   30
                Distance:  9  40  200
                """.trim();

        var actual = Day6.parse(input);

        assertThat(actual).isEqualTo(List.of(
                new Day6.Race(7,9),
                new Day6.Race(15,40),
                new Day6.Race(30,200)
        ));
    }

    @Test
    void winningTimesForFirstExampleRace() {
        var race = new Day6.Race(7, 9);

        var actual = race.winningTimes();

        assertThat(actual).hasSameElementsAs(List.of(2L, 3L, 4L, 5L));
    }

    @Test
    void sloveForExampleInput() throws IOException, URISyntaxException {
        var input = """
                Time:      7  15   30
                Distance:  9  40  200
                """.trim();

        var actual = Day6.solve(input);

        assertThat(actual).isEqualTo(288);
    }

    @Test
    void sloveForExampleInputPart2() throws IOException, URISyntaxException {
        var input = """
                Time:      71530
                Distance:  940200
                """.trim();

        var actual = Day6.solve(input);

        assertThat(actual).isEqualTo(71503);
    }

    @Test
    void sloveForPuzzleInput() throws IOException, URISyntaxException {
        var input = TestHelper.readInput("/day6-input.txt").trim();

        var actual = Day6.solve(input);

        assertThat(actual).isEqualTo(1731600);
    }

    @Test
    void sloveForPuzzleInput2() throws IOException, URISyntaxException {
        var input = TestHelper.readInput("/day6-input.txt").trim();

        var actual = Day6.solveWithoutSpaces(input);

        assertThat(actual).isEqualTo(40087680);
    }
}

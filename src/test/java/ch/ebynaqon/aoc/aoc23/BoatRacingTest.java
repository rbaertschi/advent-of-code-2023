package ch.ebynaqon.aoc.aoc23;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BoatRacingTest {
    @Test
    void parseInput() {
        var input = """
                Time:      7  15   30
                Distance:  9  40  200
                """.trim();

        var actual = BoatRacing.parse(input);

        assertThat(actual).isEqualTo(List.of(
                new BoatRacing.Race(7, 9),
                new BoatRacing.Race(15, 40),
                new BoatRacing.Race(30, 200)
        ));
    }

    @Test
    void countWinningTimesForFirstExampleRace() {
        var race = new BoatRacing.Race(7, 9);

        var actual = race.countWinningTimes();

        assertThat(actual).isEqualTo(4);
    }

    @Test
    void sloveForExampleInput() {
        var input = """
                Time:      7  15   30
                Distance:  9  40  200
                """.trim();

        var actual = BoatRacing.solve(input);

        assertThat(actual).isEqualTo(288);
    }

    @Test
    void sloveForExampleInputPart2() {
        var input = """
                Time:      71530
                Distance:  940200
                """.trim();

        var actual = BoatRacing.solve(input);

        assertThat(actual).isEqualTo(71503);
    }

    @Test
    void sloveForPuzzleInput() {
        var input = TestHelper.readInput("/day6-input.txt").trim();

        var actual = BoatRacing.solve(input);

        assertThat(actual).isEqualTo(1731600);
    }

    @Test
    void sloveForPuzzleInput2() {
        var input = TestHelper.readInput("/day6-input.txt").trim()
                .replaceAll("[ ]+", "");
        System.out.println(input);
        var actual = BoatRacing.solve(input);

        assertThat(actual).isEqualTo(40087680);
    }
}

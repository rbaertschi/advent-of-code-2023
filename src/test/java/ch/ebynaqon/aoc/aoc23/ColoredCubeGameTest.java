package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.helper.TestHelper;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static ch.ebynaqon.aoc.aoc23.ColoredCubeGame.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ColoredCubeGameTest {

    @Test
    void parse() {
        var line = "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green";

        var actual = parseLine(line);

        assertThat(actual).isEqualTo(new Game(1, List.of(
                new Draw(4, 0, 3),
                new Draw(1, 2, 6),
                new Draw(0, 2, 0)
        )));
    }

    @Test
    void sumUpValidGameIdsFromExample() {
        var input = Arrays.asList("""
                Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
                Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
                Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
                Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
                Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
                """.trim().split("\n"));

        var actual = sumUpValidGameIds(input);

        assertThat(actual).isEqualTo(8);
    }

    @Test
    void sumUpValidGameIdsForStep1() {
        var inputs = TestHelper.readInputLines("/day2-games-with-draws.txt");

        var actual = sumUpValidGameIds(inputs);

        assertThat(actual).isEqualTo(2105);
    }


    @Test
    void calculateSumOfPowerOfMinimumDrawsFromExample() {
        var input = Arrays.asList("""
                Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
                Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
                Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
                Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
                Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
                """.trim().split("\n"));

        var actual = sumUpPowerOfMinimalDraws(input);

        assertThat(actual).isEqualTo(2286);
    }

    @Test
    void calculateSumOfPowerOfMinimumDrawsForStep2() {
        var inputs = TestHelper.readInputLines("/day2-games-with-draws.txt");

        var actual = sumUpPowerOfMinimalDraws(inputs);

        assertThat(actual).isEqualTo(72422);
    }

}

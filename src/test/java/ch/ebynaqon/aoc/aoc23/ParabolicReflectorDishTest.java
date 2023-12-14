package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.helper.TestHelper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ParabolicReflectorDishTest {
    @Test
    void parseInput() {
        var input = """
                ...
                O#O
                OO.
                """.trim();

        var actual = ParabolicReflectorDish.parse(input);

        assertThat(actual).isEqualTo(new ParabolicReflectorDish(input));
    }

    @Test
    void solveSmallExample() {
        var input = """
                ...
                O#O
                OO.
                """.trim();

        var actual = ParabolicReflectorDish.parse(input).solve();

        assertThat(actual).isEqualTo(9);
    }

    @Test
    void solveExample() {
        var input = """
                O....#....
                O.OO#....#
                .....##...
                OO.#O....O
                .O.....O#.
                O.#..O.#.#
                ..O..#O..O
                .......O..
                #....###..
                #OO..#....
                """.trim();

        var actual = ParabolicReflectorDish.parse(input).solve();

        assertThat(actual).isEqualTo(136);
    }

    @Test
    void solvePart1() {
        var input = TestHelper.readInput("/day14-input.txt").trim();

        var actual = ParabolicReflectorDish.parse(input).solve();

        assertThat(actual).isEqualTo(106990);
    }
}

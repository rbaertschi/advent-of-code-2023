package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.helper.TestHelper;
import org.junit.jupiter.api.Test;

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

        assertThat(actual.rollingRockMap()).isEqualTo(new boolean[][]{
                {false, false, false},
                {true, false, true},
                {true, true, false}
        });
        assertThat(actual.blockerMap()).isEqualTo(new boolean[][]{
                {false, false, false},
                {false, true, false},
                {false, false, false}
        });
    }

    @Test
    void solveSmallExample() {
        var input = """
                ...
                O#O
                OO.
                """.trim();

        var actual = ParabolicReflectorDish.parse(input).calculateStressAfterOneTiltNorth();

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

        var actual = ParabolicReflectorDish.parse(input).calculateStressAfterOneTiltNorth();

        assertThat(actual).isEqualTo(136);
    }

    @Test
    void solvePart1() {
        var input = TestHelper.readInput("/day14-input.txt").trim();

        var actual = ParabolicReflectorDish.parse(input).calculateStressAfterOneTiltNorth();

        assertThat(actual).isEqualTo(106990);
    }

    @Test
    void solvePart2() {
        var input = TestHelper.readInput("/day14-input.txt").trim();

        int b = ParabolicReflectorDish.parse(input).calculateStressAfterABillionCycles(1_000_000_000);

        assertThat(b).isEqualTo(100531);
    }

    @Test
    void solveExamplePart2() {
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

        var actual = ParabolicReflectorDish.parse(input).calculateStressAfterABillionCycles(1_000_000_000);

        assertThat(actual).isEqualTo(64);
    }

}

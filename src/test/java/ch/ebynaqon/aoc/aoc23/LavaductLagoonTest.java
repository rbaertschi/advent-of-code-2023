package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.LavaductLagoon.DiggingInstruction;
import ch.ebynaqon.aoc.aoc23.helper.TestHelper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static ch.ebynaqon.aoc.aoc23.LavaductLagoon.Direction.*;
import static org.assertj.core.api.Assertions.assertThat;

class LavaductLagoonTest {
    @Test
    void parse() {
        var input = """
                D 6 (#70c710)
                L 5 (#0dc571)
                U 2 (#5713f0)
                R 2 (#d2c081)
                """.trim();

        var actual = LavaductLagoon.parse(input);

        assertThat(actual.getInstructions()).isEqualTo(List.of(
                new DiggingInstruction(DOWN, 6),
                new DiggingInstruction(LEFT, 5),
                new DiggingInstruction(UP, 2),
                new DiggingInstruction(RIGHT, 2)
        ));
    }

    @Test
    void solveExample() {
        var input = """
                R 6 (#70c710)
                D 5 (#0dc571)
                L 2 (#5713f0)
                D 2 (#d2c081)
                R 2 (#59c680)
                D 2 (#411b91)
                L 5 (#8ceee2)
                U 2 (#caa173)
                L 1 (#1b58a2)
                U 2 (#caa171)
                R 2 (#7807d2)
                U 3 (#a77fa3)
                L 2 (#015232)
                U 2 (#7a21e3)
                """.trim();

        var actual = LavaductLagoon.parse(input).getDigVolume();

        assertThat(actual).isEqualTo(62);
    }

    @Test
    void solvePart1() {
        var input = TestHelper.readInput("/day17-lavaduct-dig-plan.txt").trim();

        var actual = LavaductLagoon.parse(input).getDigVolume();

        assertThat(actual).isEqualTo(76387);
    }
}

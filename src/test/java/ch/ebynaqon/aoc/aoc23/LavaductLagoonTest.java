package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.LavaductLagoon.DiggingInstruction;
import ch.ebynaqon.aoc.aoc23.LavaductLagoon.Line;
import ch.ebynaqon.aoc.aoc23.LavaductLagoon.Position;
import ch.ebynaqon.aoc.aoc23.helper.TestHelper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static ch.ebynaqon.aoc.aoc23.LavaductLagoon.Direction.*;
import static ch.ebynaqon.aoc.aoc23.LavaductLagoon.getInsideOnLine;
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

        assertThat(actual.instructions()).isEqualTo(List.of(
                new DiggingInstruction(DOWN, 6),
                new DiggingInstruction(LEFT, 5),
                new DiggingInstruction(UP, 2),
                new DiggingInstruction(RIGHT, 2)
        ));
    }

    @Test
    void parseFromHex() {
        var input = """
                D 6 (#800001)
                L 5 (#0b0002)
                U 2 (#005003)
                R 2 (#000300)
                """.trim();

        var actual = LavaductLagoon.parseFromHex(input);

        assertThat(actual.instructions()).isEqualTo(List.of(
                new DiggingInstruction(DOWN, 8 * 16 * 16 * 16 * 16),
                new DiggingInstruction(LEFT, 11 * 16 * 16 * 16),
                new DiggingInstruction(UP, 5 * 16 * 16),
                new DiggingInstruction(RIGHT, 3 * 16)
        ));
    }

    @Test
    void getDigVolumeForExample() {
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
    void getDigVolumeForSimpleRectangle() {
        var input = """
                U 10 (#70c710)
                R 2 (#0dc571)
                D 10 (#5713f0)
                L 2 (#d2c081)
                """.trim();

        var actual = LavaductLagoon.parse(input).getDigVolume();

        assertThat(actual).isEqualTo(33);
    }

    @Test
    void getDigVolumeForStair() {
        /*
          XXX
          XXX
        XXXXX
        XXXXX
        XXXXX

        */
        var input = """
                U 2 (#70c710)
                R 2 (#0dc571)
                U 2 (#70c710)
                R 2 (#0dc571)
                D 4 (#5713f0)
                L 4 (#d2c081)
                """.trim();

        var actual = LavaductLagoon.parse(input).getDigVolume();

        assertThat(actual).isEqualTo(21);
    }

    @Test
    void getDigVolumeForTetrisLikeShape() {
        /*
           XXXXX
           XXXXX
        XXXXXXXX
        XXXXXXXX
        XXX  XXX
        XXX
        */
        var input = """
                U 3 (#70c710)
                R 3 (#0dc571)
                U 2 (#70c710)
                R 4 (#0dc571)
                D 4 (#5713f0)
                L 2 (#d2c081)
                U 1 (#d2c081)
                L 3 (#d2c081)
                D 2 (#d2c081)
                L 2 (#d2c081)
                """.trim();

        var actual = LavaductLagoon.parse(input).getDigVolume();

        assertThat(actual).isEqualTo(35);
    }

    @Test
    void getDigVolumeForPart1() {
        var input = TestHelper.readInput("/day18-lavaduct-dig-plan.txt").trim();

        var actual = LavaductLagoon.parse(input).getDigVolume();

        assertThat(actual).isEqualTo(76387);
    }

    @Test
    void getDigVolumeForPart2() {
        var input = TestHelper.readInput("/day18-lavaduct-dig-plan.txt").trim();

        var actual = LavaductLagoon.parseFromHex(input).getDigVolume();

        assertThat(actual).isEqualTo(250022188522074L);
    }

    @Test
    void horizontalLineIntersectsOnMatchingY() {
        Line line = new Line(new Position(0, 0), new Position(0, 1), false);

        assertThat(line.intersects(0)).isTrue();
    }

    @Test
    void verticalLineIntersectsBetweenStartAndEndButNotAtStartAndEnd() {
        Line line = new Line(new Position(-1, 0), new Position(1, 0), false);

        assertThat(line.intersects(-1)).isFalse();
        assertThat(line.intersects(0)).isTrue();
        assertThat(line.intersects(1)).isFalse();
    }

    @Test
    void insideCountHorizontalLineOnly() {
        var lines = List.of(
                new Line(new Position(0, 0), new Position(0, 10), true)
        );

        assertThat(getInsideOnLine(0, lines)).isEqualTo(11);
    }

    @Test
    void insideCountHorizontalAndVerticalLine() {
        var lines = List.of(
                new Line(new Position(0, 0), new Position(0, 10), false),
                new Line(new Position(10, 20), new Position(-10, 20), false)
        );

        assertThat(getInsideOnLine(0, lines)).isEqualTo(21);
    }

    @Test
    void insideCountVerticalAndHorizontalLine() {
        var lines = List.of(
                new Line(new Position(10, 0), new Position(-10, 0), false),
                new Line(new Position(0, 10), new Position(0, 20), false)
        );

        assertThat(getInsideOnLine(0, lines)).isEqualTo(21);
    }

    @Test
    void insideCountHorizontalBetweenVerticals() {
        var lines = List.of(
                new Line(new Position(-10, 0), new Position(10, 0), false),
                new Line(new Position(0, 10), new Position(0, 20), true),
                new Line(new Position(10, 30), new Position(-10, 30), false)
        );

        assertThat(getInsideOnLine(0, lines)).isEqualTo(31);
    }

    @Test
    void insideCountWithTwoHorizontalLines() {
        var lines = List.of(
                new Line(new Position(0, 0), new Position(0, 10), false),
                new Line(new Position(0, 20), new Position(0, 30), false)
        );

        assertThat(getInsideOnLine(0, lines)).isEqualTo(31);
    }
}

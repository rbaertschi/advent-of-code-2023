package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.helper.TestHelper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static ch.ebynaqon.aoc.aoc23.PipeMaze.Tile.*;
import static org.assertj.core.api.Assertions.assertThat;

class PipeMazeTest {
    @Test
    void parseExample() {
        var input = """
                .....
                .F-7.
                .S.|.
                .L-J.
                .....
                """.trim();

        var actual = PipeMaze.parse(input);

        assertThat(actual).isEqualTo(new PipeMaze(List.of(
                List.of(NONE, NONE, NONE, NONE, NONE),
                List.of(NONE, BOTTOM_RIGHT, LEFT_RIGHT, BOTTOM_LEFT, NONE),
                List.of(NONE, START, NONE, TOP_BOTTOM, NONE),
                List.of(NONE, TOP_RIGHT, LEFT_RIGHT, TOP_LEFT, NONE),
                List.of(NONE, NONE, NONE, NONE, NONE)
        )));
    }

    @Test
    void findMaxDistanceFromStartForSimpleExample() {
        var input = """
                .....
                .F-7.
                .S.|.
                .L-J.
                .....
                """.trim();

        var actual = PipeMaze.parse(input).findMaxDistanceFromStart();

        assertThat(actual).isEqualTo(4);
    }

    @Test
    void getNumberOfTilesInsideLoopForSimpleExample() {
        var input = """
                ..F7.
                .FJ|.
                SJ.L7
                |F--J
                LJ...
                """.trim();

        var actual = PipeMaze.parse(input).getNumberOfTilesInsideLoop(false);

        assertThat(actual).isEqualTo(1);
    }

    @Test
    void findMaxDistanceFromStartForSlightlyMoreComplexExample() {
        var input = """
                ..F7.
                .FJ|.
                SJ.L7
                |F--J
                LJ...
                """.trim();

        var actual = PipeMaze.parse(input).findMaxDistanceFromStart();

        assertThat(actual).isEqualTo(8);
    }

    @Test
    void solvePuzzle1() {
        var input = TestHelper.readInput("/day10-pipe-maze.txt").trim();

        var actual = PipeMaze.parse(input).findMaxDistanceFromStart();

        assertThat(actual).isEqualTo(6860);
    }

    @Test
    void solvePuzzle2() {
        var input = TestHelper.readInput("/day10-pipe-maze.txt").trim();

        var actual = PipeMaze.parse(input).getNumberOfTilesInsideLoop(false);

        assertThat(actual).isEqualTo(343);
    }
}

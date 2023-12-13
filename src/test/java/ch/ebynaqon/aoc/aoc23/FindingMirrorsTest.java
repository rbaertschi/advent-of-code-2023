package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.helper.TestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class FindingMirrorsTest {

    public static final String EXAMPLE1 = """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.
            """.trim();

    @Test
    void reverseString() {
        assertThat(FindingMirrors.reverseString("12345")).isEqualTo("54321");
    }

    @Test
    void testMirrorPoint() {
        var input = "#.##..##.";

        var actual = FindingMirrors.isMirrorPoint(input, 5);

        assertThat(actual).isTrue();
    }

    @ParameterizedTest
    @MethodSource("findMirrorPointsExamples")
    void findMirrorPoints(String input, Set<Integer> mirrorPoints) {
        var actual = FindingMirrors.findMirrorPoints(input);
        assertThat(actual).isEqualTo(mirrorPoints);
    }

    public static Stream<Arguments> findMirrorPointsExamples() {
        return Stream.of(
                Arguments.of("#.##..##.", Set.of(5, 7)),
                Arguments.of("##......#", Set.of(1, 5)),
                Arguments.of("#.#.##", Set.of(5))
        );
    }

    @Test
    void findCommonMirrorPoints() {
        List<String> input = List.of("#.##..##.",
                "..#.##.#.",
                "##......#",
                "##......#",
                "..#.##.#.",
                "..##..##.",
                "#.#.##.#."
        );

        var commonMirrorPoints = FindingMirrors.findCommonMirrorPoints(input);

        assertThat(commonMirrorPoints).isEqualTo(Set.of(5));
    }

    @Test
    void extractColumns() {
        List<String> input = List.of("#.##..##.",
                "..#.##.#.",
                "##......#",
                "##......#",
                "..#.##.#.",
                "..##..##.",
                "#.#.##.#."
        );

        var actual = FindingMirrors.extractColumns(input);

        assertThat(actual).isEqualTo(List.of(
                "#.##..#",
                "..##...",
                "##..###",
                "#....#.",
                ".#..#.#",
                ".#..#.#",
                "#....#.",
                "##..###",
                "..##..."
        ));
    }

    @Test
    void calculateMirrorScoreForExampleWithColumnMirror() {
        var input = """
                #.##..##.
                ..#.##.#.
                ##......#
                ##......#
                ..#.##.#.
                ..##..##.
                #.#.##.#.
                """.trim();

        var actual = FindingMirrors.calculateMirrorScore(input);

        assertThat(actual).isEqualTo(5);
    }

    @Test
    void calculateMirrorScoreForExampleWithMirrorInLastColumn() {
        var input = """
                #.####...
                ..#..#.##
                ##....#..
                ##....#..
                ..#..#.##
                ..####...
                #.#..#.##
                """.trim();

        var actual = FindingMirrors.calculateMirrorScore(input);

        assertThat(actual).isEqualTo(8);
    }

    @Test
    void calculateMirrorScoreForExampleWithRowMirror() {
        var input = """
                #...##..#
                #....#..#
                ..##..###
                #####.##.
                #####.##.
                ..##..###
                #....#..#
                """.trim();

        var actual = FindingMirrors.calculateMirrorScore(input);

        assertThat(actual).isEqualTo(400);
    }

    @Test
    void calculateMirrorScoreForExampleWithRowMirrorOnFirstLine() {
        var input = """
                #####.##.
                #####.##.
                #...##..#
                #....#..#
                ..##..###
                ..##..#.#
                #....#..#
                """.trim();

        var actual = FindingMirrors.calculateMirrorScore(input);

        assertThat(actual).isEqualTo(100);
    }

    @Test
    void calculateMirrorScoreForExampleWithRowMirrorOnLastLine() {
        var input = """
                #...##..#
                #....#..#
                ..##..###
                ..##..#.#
                #....#..#
                #####.##.
                #####.##.
                """.trim();

        var actual = FindingMirrors.calculateMirrorScore(input);

        assertThat(actual).isEqualTo(600);
    }

    @Test
    void calculateMirrorScoreForExampleWithThreeRowsAndFourColumnsOfSameCharacter() {
        var input = """
                ####
                ####
                ####
                """.trim();

        var actual = FindingMirrors.calculateMirrorScore(input);

        assertThat(actual).isEqualTo(1 + 2 + 3 + 100 + 200);
    }

    @Test
    void calculateMirrorScoresForPuzzle1() {
        var input = TestHelper.readInput("/day13-mirrors.txt").trim();

        var actual = FindingMirrors.sumOfMirrorScores(input);

        assertThat(actual).isEqualTo(36015);
    }
}

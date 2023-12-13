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

        var actual = FindingMirrors.calculateMirrorScore(input, false);

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

        var actual = FindingMirrors.calculateMirrorScore(input, false);

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

        var actual = FindingMirrors.calculateMirrorScore(input, false);

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

        var actual = FindingMirrors.calculateMirrorScore(input, false);

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

        var actual = FindingMirrors.calculateMirrorScore(input, false);

        assertThat(actual).isEqualTo(600);
    }

    @Test
    void calculateMirrorScoreForExampleWithThreeRowsAndFourColumnsOfSameCharacter() {
        var input = """
                ####
                ####
                ####
                """.trim();

        var actual = FindingMirrors.calculateMirrorScore(input, false);

        assertThat(actual).isEqualTo(1 + 2 + 3 + 100 + 200);
    }

    @Test
    void calculateMirrorScoresForPuzzle1() {
        var input = TestHelper.readInput("/day13-mirrors.txt").trim();

        var actual = FindingMirrors.sumOfMirrorScores(input, false);

        assertThat(actual).isEqualTo(36015);
    }

    @Test
    void calculateMirrorScoresWithSmudgeForPuzzle2() {
        var input = TestHelper.readInput("/day13-mirrors.txt").trim();

        var actual = FindingMirrors.sumOfMirrorScores(input, true);

        assertThat(actual).isEqualTo(65640L);
    }

    @Test
    void calculateMirrorScoresWithSmudgeForExample() {
        var input = """
                #.##..##.
                ..#.##.#.
                ##......#
                ##......#
                ..#.##.#.
                ..##..##.
                #.#.##.#.
                                
                #...##..#
                #....#..#
                ..##..###
                #####.##.
                #####.##.
                ..##..###
                #....#..#
                """.trim();

        var actual = FindingMirrors.sumOfMirrorScores(input, true);

        assertThat(actual).isEqualTo(400);
    }

    @Test
    void generateInputVariations() {
        var input = """
                #.
                .#
                """.trim();

        var actual = FindingMirrors.generateInputVariations(input, false).toList();

        assertThat(actual).isEqualTo(List.of(
                input
        ));
    }

    @ParameterizedTest
    @MethodSource("smudgePositions")
    void inputWithSmudgeAt(int row, int col, String expected) {
        var input = """
                #.
                .#
                """.trim();
        var actual = FindingMirrors.inputWithSmudgeAt(input, row, col);

        assertThat(actual).isEqualTo(expected);
    }

    public static Stream<Arguments> smudgePositions() {
        return Stream.of(
                Arguments.of(0, 0, "..\n.#"),
                Arguments.of(0, 1, "##\n.#"),
                Arguments.of(1, 0, "#.\n##"),
                Arguments.of(1, 1, "#.\n..")
        );
    }
}

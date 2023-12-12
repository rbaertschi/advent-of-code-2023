package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.helper.TestHelper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HotSpringsTest {
    @Test
    void parse() {
        var input = ".???#??????#. 6,1";

        HotSprings.Report actual = HotSprings.parseLine(input);

        assertThat(actual).isEqualTo(new HotSprings.Report(".???#??????#.", List.of(6, 1)));
    }

    @Test
    void explodePatterns() {
        var report = new HotSprings.Report("???", List.of(1));

        long patterns = report.generateSpacings();

        assertThat(patterns).isEqualTo(3);
    }

    @Test
    void explodePatternsLonger() {
        var report = new HotSprings.Report("?????", List.of(1, 1));

        long patterns = report.generateSpacings();

        assertThat(patterns).isEqualTo(6);
    }

    @Test
    void solveSimpleExample() {
        var report = new HotSprings.Report("???.###", List.of(1, 1, 3));

        long patterns = report.solve();

        assertThat(patterns).isEqualTo(1);
    }

    @Test
    void solveAnotherExample() {
        var report = new HotSprings.Report("##???#??.???##?.???", List.of(4,1,1,5,1,1));

        long patterns = report.solve();

        assertThat(patterns).isEqualTo(2);
    }

    @Test
    void generatePatternFromSpacing() {
        assertThat(HotSprings.Report.patternFrom(List.of(0, 3, 2), List.of(1, 2)))
                .isEqualTo("#....##..");
        assertThat(HotSprings.Report.patternFrom(List.of(0, 0, 0), List.of(1, 1)))
                .isEqualTo("#.#");
        assertThat(HotSprings.Report.patternFrom(List.of(1, 0, 0), List.of(1, 1)))
                .isEqualTo(".#.#");
        assertThat(HotSprings.Report.patternFrom(List.of(0, 1, 0), List.of(1, 1)))
                .isEqualTo("#..#");
        assertThat(HotSprings.Report.patternFrom(List.of(0, 0, 1), List.of(1, 1)))
                .isEqualTo("#.#.");
        assertThat(HotSprings.Report.patternFrom(List.of(1, 1, 1, 1), List.of(1, 1, 1)))
                .isEqualTo(".#..#..#.");
    }

    @Test
    void patternMatching() {
        boolean actual = HotSprings.Report.matchesPattern(
                HotSprings.Report.patternFrom(List.of(0, 3, 2), List.of(1, 2)), "#..???#.."
        );
        assertThat(actual).isEqualTo(true);
    }

    @Test
    void solveExample() {
        var input = """
                ???.### 1,1,3
                .??..??...?##. 1,1,3
                ?#?#?#?#?#?#?#? 1,3,1,6
                ????.#...#... 4,1,1
                ????.######..#####. 1,6,5
                ?###???????? 3,2,1
                """.trim();

        var sum = HotSprings.findSumOfArrangements(input, false);

        assertThat(sum).isEqualTo(21);
    }

    @Test
    void solvePart1() {
        var input = TestHelper.readInput("/day12-hotsprings.txt").trim();

        var patterns = HotSprings.findSumOfArrangements(input, false);

        assertThat(patterns).isEqualTo(7599L);
    }

    @Test
    void solvePart2() {
        var input = TestHelper.readInput("/day12-hotsprings.txt").trim();

        var patterns = HotSprings.findSumOfArrangements(input, true);

        assertThat(patterns).isEqualTo(15454556629917L);
    }
}

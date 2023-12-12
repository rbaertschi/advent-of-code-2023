package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.helper.TestHelper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HotSpringsTest {
    @Test
    void parseOneRecord() {
        var input = ".???#??????#. 6,1";

        HotSprings.Report actual = HotSprings.parseLine(input);

        assertThat(actual).isEqualTo(new HotSprings.Report(".???#??????#.", List.of(6, 1)));
    }

    @Test
    void countMatchingPatternsWithThreeWildcardsAndOneBrokenPart() {
        var report = new HotSprings.Report("???", List.of(1));

        long patterns = report.countMatchingPatterns();

        assertThat(patterns).isEqualTo(3);
    }

    @Test
    void countMatchingPatternsWithSixWildcardsAndTwoIndividualBrokenParts() {
        var report = new HotSprings.Report("?????", List.of(1, 1));

        long patterns = report.countMatchingPatterns();

        assertThat(patterns).isEqualTo(6);
    }

    @Test
    void countMatchingPatternsForExampleInput() {
        var report = new HotSprings.Report("???.###", List.of(1, 1, 3));

        long patterns = report.countMatchingPatterns();

        assertThat(patterns).isEqualTo(1);
    }

    @Test
    void countMatchingPatternsWithComplexExample() {
        var report = new HotSprings.Report("##???#??.???##?.???", List.of(4,1,1,5,1,1));

        long patterns = report.countMatchingPatterns();

        assertThat(patterns).isEqualTo(2);
    }

    @Test
    void sumOfMatchingPatternsForExample() {
        var input = """
                ???.### 1,1,3
                .??..??...?##. 1,1,3
                ?#?#?#?#?#?#?#? 1,3,1,6
                ????.#...#... 4,1,1
                ????.######..#####. 1,6,5
                ?###???????? 3,2,1
                """.trim();

        var sum = HotSprings.sumOfMatchingPatterns(input, false);

        assertThat(sum).isEqualTo(21);
    }

    @Test
    void sumOfMatchingPatternsForPart1() {
        var input = TestHelper.readInput("/day12-hotsprings.txt").trim();

        var patterns = HotSprings.sumOfMatchingPatterns(input, false);

        assertThat(patterns).isEqualTo(7599L);
    }

    @Test
    void sumOfMatchingPatternsForPart2() {
        var input = TestHelper.readInput("/day12-hotsprings.txt").trim();

        var patterns = HotSprings.sumOfMatchingPatterns(input, true);

        assertThat(patterns).isEqualTo(15454556629917L);
    }
}

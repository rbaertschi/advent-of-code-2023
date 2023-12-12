package ch.ebynaqon.aoc.aoc23;

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

        List<List<Integer>> patterns = report.explode();

        assertThat(patterns).isEqualTo(List.of(
                List.of(0,2),
                List.of(1,1),
                List.of(2,0)
        ));
    }

    @Test
    void explodePatternsLonger() {
        var report = new HotSprings.Report("?????", List.of(1,1));

        List<List<Integer>> patterns = report.explode();

        assertThat(patterns).isEqualTo(List.of(
                List.of(0,2),
                List.of(1,1),
                List.of(2,0)
        ));
    }
}

package ch.ebynaqon.aoc.aoc23.helper;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class StatisticsHelperTest {
    @Test
    void histogramWithIntegers() {
        assertThat(StatisticsHelper.histogram(List.of(1, 2, 2, 2, 3, 7, 8, 8)))
                .isEqualTo(Map.of(
                        1, 1,
                        2, 3,
                        3, 1,
                        7, 1,
                        8, 2
                ));
    }

    @Test
    void histogramWithStrings() {
        assertThat(StatisticsHelper.histogram(List.of("HELLOWORLD".split(""))))
                .isEqualTo(Map.of(
                        "H", 1,
                        "E", 1,
                        "L", 3,
                        "O", 2,
                        "W", 1,
                        "R", 1,
                        "D", 1
                ));
    }

}

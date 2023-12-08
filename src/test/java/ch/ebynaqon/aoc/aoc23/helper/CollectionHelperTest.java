package ch.ebynaqon.aoc.aoc23.helper;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CollectionHelperTest {
    @Test
    void mergeMaxValue() {
        var actual = CollectionHelper.mergeMaxValue(Map.of(
                4, 23,
                99, 2,
                13, 21
        ), Map.of(
                3, 11,
                4, 20,
                99, 7
        ));

        assertThat(actual).isEqualTo(Map.of(
                3, 11,
                4, 23,
                99, 7,
                13, 21
        ));
    }

    @Test
    void mergeMaxValue_withNegativeNumberAndPositiveNumber_yieldsPositiveNumber() {
        var actual = CollectionHelper.mergeMaxValue(
                Map.of(99, -2322),
                Map.of(99, 3)
        );

        assertThat(actual).isEqualTo(Map.of(99, 3));
    }

    @Test
    void mergeMaxValue_withEmptyMaps_yieldsEmptyMap() {
        var actual = CollectionHelper.mergeMaxValue(Map.of(), Map.of());

        assertThat(actual).isEqualTo(Map.of());
    }

}

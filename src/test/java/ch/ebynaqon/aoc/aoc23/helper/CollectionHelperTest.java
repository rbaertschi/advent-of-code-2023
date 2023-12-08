package ch.ebynaqon.aoc.aoc23.helper;

import ch.ebynaqon.aoc.aoc23.helper.CollectionHelper.IndexAndValue;
import org.junit.jupiter.api.Test;

import java.util.List;
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

    @Test
    void zipWithIndex_withEmptyList_yieldsEmptyList() {
        var actual = CollectionHelper.zipWithIndex(List.of());

        assertThat(actual).isEqualTo(List.of());
    }

    @Test
    void zipWithIndex_withSomeValues_yieldsListWithIndexedValues() {
        var actual = CollectionHelper.zipWithIndex(
                List.of("Hello", "World", "foo", "bar")
        );

        assertThat(actual).isEqualTo(List.of(
                new IndexAndValue<>(0, "Hello"),
                new IndexAndValue<>(1, "World"),
                new IndexAndValue<>(2, "foo"),
                new IndexAndValue<>(3, "bar")
        ));
    }
}

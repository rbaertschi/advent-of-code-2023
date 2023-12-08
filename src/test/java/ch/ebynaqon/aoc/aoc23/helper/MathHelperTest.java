package ch.ebynaqon.aoc.aoc23.helper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class MathHelperTest {

    @Test
    void leastCommonMultiple() {
        long actual = MathHelper.leastCommonMultiple(List.of(79 * 307, 61 * 307));

        assertThat(actual).isEqualTo(79 * 61 * 307);
    }

    @Test
    void histogram() {
        assertThat(MathHelper.histogram(List.of(1, 2, 2, 2, 3, 7, 8, 8)))
                .isEqualTo(Map.of(
                        1, 1,
                        2, 3,
                        3, 1,
                        7, 1,
                        8, 2
                ));
    }

    @ParameterizedTest
    @MethodSource("factorizeExamples")
    void factorize(int number, List<Integer> factors) {
        assertThat(MathHelper.factorize(number)).isEqualTo(factors);
    }

    public static Stream<Arguments> factorizeExamples() {
        return Stream.of(
                Arguments.of(1, List.of(1)),
                Arguments.of(2, List.of(1, 2)),
                Arguments.of(3, List.of(1, 3)),
                Arguments.of(4, List.of(1, 2, 2)),
                Arguments.of(5, List.of(1, 5)),
                Arguments.of(6, List.of(1, 2, 3)),
                Arguments.of(7, List.of(1, 7)),
                Arguments.of(7 * 13 * 23, List.of(1, 7, 13, 23))
        );
    }
}

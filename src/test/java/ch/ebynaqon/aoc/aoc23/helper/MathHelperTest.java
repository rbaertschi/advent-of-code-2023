package ch.ebynaqon.aoc.aoc23.helper;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class MathHelperTest {

    @ParameterizedTest
    @MethodSource("leastCommonMultipleExamples")
    void leastCommonMultiple(int a, int b, long lcm) {
        long actual = MathHelper.leastCommonMultiple(List.of(a, b));

        assertThat(actual).isEqualTo(lcm);
    }

    static Stream<Arguments> leastCommonMultipleExamples() {
        return Stream.of(
                Arguments.of(1, 1, 1),
                Arguments.of(2, 1, 2),
                Arguments.of(2 * 3, 5, 2 * 3 * 5),
                Arguments.of(79 * 307, 61 * 307, 79 * 61 * 307),
                Arguments.of(2 * 3 * 5 * 7 * 11 * 13, 7 * 11 * 13 * 17 * 19 * 23, 2 * 3 * 5 * 7 * 11 * 13 * 17 * 19 * 23)
        );
    }

    @ParameterizedTest
    @MethodSource("factorizeExamples")
    void factorize(int number, List<Integer> factors) {
        assertThat(MathHelper.factorize(number)).isEqualTo(factors);
    }

    static Stream<Arguments> factorizeExamples() {
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

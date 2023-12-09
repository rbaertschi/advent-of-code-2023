package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.MirageMaintenance.Sequence;
import ch.ebynaqon.aoc.aoc23.helper.TestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class MirageMaintenanceTest {

    public static final String EXAMPLE = """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
            """;

    @Test
    void parseExample() {
        var input = EXAMPLE.trim();

        var actual = MirageMaintenance.parse(input);

        assertThat(actual).isEqualTo(new MirageMaintenance(List.of(
                new Sequence(List.of(0L, 3L, 6L, 9L, 12L, 15L)),
                new Sequence(List.of(1L, 3L, 6L, 10L, 15L, 21L)),
                new Sequence(List.of(10L, 13L, 16L, 21L, 30L, 45L))
        )));
    }

    @ParameterizedTest
    @MethodSource("simpleSequences")
    void predictSimpleSequence(List<Long> numbers, long prediction) {
        Sequence sequence = new Sequence(numbers);

        var actual = sequence.predictNext();

        assertThat(actual).isEqualTo(prediction);
    }

    public static Stream<Arguments> simpleSequences() {
        return Stream.of(
                Arguments.of(List.of(0L, 3L, 6L, 9L, 12L, 15L), 18),
                Arguments.of(List.of(1L, 3L, 6L, 10L, 15L, 21L), 28),
                Arguments.of(List.of(10L, 13L, 16L, 21L, 30L, 45L), 68)
        );
    }

    @Test
    void solveExample() {
        var input = EXAMPLE.trim();

        var actual = MirageMaintenance.parse(input).sumOfNextPredictions();

        assertThat(actual).isEqualTo(114L);
    }

    @Test
    void solvePart1() {
        var input = TestHelper.readInput("/day9-mirage-maintenance.txt").trim();

        var actual = MirageMaintenance.parse(input).sumOfNextPredictions();

        assertThat(actual).isEqualTo(1898776583L);
    }
}

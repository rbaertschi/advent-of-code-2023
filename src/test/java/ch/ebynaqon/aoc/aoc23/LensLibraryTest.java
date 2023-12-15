package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.helper.TestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class LensLibraryTest {
    public static Stream<Arguments> stepsAndHashes() {
        return Stream.of(
                Arguments.of("rn=1", 30),
                Arguments.of("cm-", 253),
                Arguments.of("qp=3", 97),
                Arguments.of("cm=2", 47),
                Arguments.of("qp-", 14),
                Arguments.of("pc=4", 180),
                Arguments.of("ot=9", 9),
                Arguments.of("ab=5", 197),
                Arguments.of("pc-", 48),
                Arguments.of("pc=6", 214),
                Arguments.of("ot=7", 231)
        );
    }

    @ParameterizedTest
    @MethodSource("stepsAndHashes")
    void verifyHashing(String input, int hash) {
        var actual = new LensLibrary.Step(input).hash();

        assertThat(actual).isEqualTo(hash);
    }

    @Test
    void sumOfHashesForExample() {
        var input = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7";

        var actual = LensLibrary.sumOfHashes(input);

        assertThat(actual).isEqualTo(1320);
    }

    @Test
    void sumOfHashesForPart1() {
        var input = TestHelper.readInput("/day15-initialization-steps.txt").trim();

        var actual = LensLibrary.sumOfHashes(input);

        assertThat(actual).isEqualTo(516804);
    }

    @Test
    void focusingPowerForExample() {
        var input = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7";

        var actual = LensLibrary.calculateFocusingPower(input);

        assertThat(actual).isEqualTo(145);
    }

    @Test
    void focusingPowerForPart2() {
        var input = TestHelper.readInput("/day15-initialization-steps.txt").trim();

        var actual = LensLibrary.calculateFocusingPower(input);

        assertThat(actual).isEqualTo(231844);
    }

}

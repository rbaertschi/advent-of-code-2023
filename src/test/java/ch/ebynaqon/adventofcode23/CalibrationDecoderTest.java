package ch.ebynaqon.adventofcode23;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


class CalibrationDecoderTest {
    public static Stream<Arguments> decodingExamples() {
        return Stream.of(
                Arguments.of("1abc2", 12),
                Arguments.of("pqr3stu8vwx", 38),
                Arguments.of("a1b2c3d4e5f", 15),
                Arguments.of("treb7uchet", 77)
        );
    }

    @ParameterizedTest
    @MethodSource("decodingExamples")
    void decodeSimpleValue(String encodedText, int decodedValue) {
        var encoded = new EncodedCalibrationValue(encodedText);

        var decoded = encoded.decode();

        assertThat(decoded).isEqualTo(decodedValue);
    }

    @Test
    void solvePuzzle1() throws URISyntaxException, IOException {
        var inputs = Files.readAllLines(Path.of(getClass().getResource("/day1-encoded-calibration-values.txt").toURI()));

        var result = inputs.stream().map(EncodedCalibrationValue::new)
                .mapToInt(EncodedCalibrationValue::decode)
                .sum();

        assertThat(result).isEqualTo(54697);
    }
}

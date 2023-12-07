package ch.ebynaqon.aoc.aoc23;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.net.URISyntaxException;
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

    public static Stream<Arguments> decodingExamplesWithWrittenNumbers() {
        return Stream.of(
                Arguments.of("two1nine", 29),
                Arguments.of("eightwothree", 83),
                Arguments.of("abcone2threexyz", 13),
                Arguments.of("xtwone3four", 24),
                Arguments.of("4nineeightseven2", 42),
                Arguments.of("zoneight234", 14),
                Arguments.of("7pqrstsixteen", 76)
        );
    }

    @ParameterizedTest
    @MethodSource("decodingExamples")
    void decodeValuesWithWrittenNumbers(String encodedText, int decodedValue) {
        var encoded = new EncodedCalibrationValue(encodedText);

        var decoded = encoded.decodeWithWrittenNumbers();

        assertThat(decoded).isEqualTo(decodedValue);
    }

    @Test
    void solvePuzzle1() {
        var inputs = TestHelper.readInputLines("/day1-encoded-calibration-values.txt");

        var result = inputs.stream().map(EncodedCalibrationValue::new)
                .mapToInt(EncodedCalibrationValue::decode)
                .sum();

        assertThat(result).isEqualTo(54697);
    }

    @Test
    void solvePuzzle2() {
        var inputs = TestHelper.readInputLines("/day1-encoded-calibration-values.txt");

        var result = inputs.stream().map(EncodedCalibrationValue::new)
                .mapToInt(EncodedCalibrationValue::decodeWithWrittenNumbers)
                .sum();

        assertThat(result).isEqualTo(54885);
    }

}

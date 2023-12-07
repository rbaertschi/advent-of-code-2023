package ch.ebynaqon.aoc.aoc23;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TestHelper {
    static List<String> readInputLines(String resourceName) {
        try {
            return Files.readAllLines(Path.of(CalibrationDecoderTest.class.getResource(resourceName).toURI()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static String readInput(String resourceName) {
        try {
            return Files.readString(Path.of(CalibrationDecoderTest.class.getResource(resourceName).toURI()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

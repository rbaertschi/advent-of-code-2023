package ch.ebynaqon.aoc.aoc23;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TestHelper {
    static List<String> readInputLines(String resourceName) throws IOException, URISyntaxException {
        return Files.readAllLines(Path.of(CalibrationDecoderTest.class.getResource(resourceName).toURI()));
    }
    static String readInput(String resourceName) throws IOException, URISyntaxException {
        return Files.readString(Path.of(CalibrationDecoderTest.class.getResource(resourceName).toURI()));
    }
}

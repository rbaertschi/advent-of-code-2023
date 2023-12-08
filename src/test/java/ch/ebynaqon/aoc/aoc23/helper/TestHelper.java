package ch.ebynaqon.aoc.aoc23.helper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TestHelper {
    public static List<String> readInputLines(String resourceName) {
        try {
            return Files.readAllLines(Path.of(TestHelper.class.getResource(resourceName).toURI()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String readInput(String resourceName) {
        try {
            return Files.readString(Path.of(TestHelper.class.getResource(resourceName).toURI()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package ch.ebynaqon.aoc.aoc23;

import java.util.List;
import java.util.stream.Stream;

public record EncodedCalibrationValue(String encodedText) {

    public static final int ZERO = Character.valueOf('0').charValue();
    public static final List<Mapping> WRITTEN_NUMBERS = List.of(
            new Mapping("one", 1),
            new Mapping("two", 2),
            new Mapping("three", 3),
            new Mapping("four", 4),
            new Mapping("five", 5),
            new Mapping("six", 6),
            new Mapping("seven", 7),
            new Mapping("eight", 8),
            new Mapping("nine", 9)
    );
    public static final List<Mapping> NUMERIC_NUMBERS = List.of(
            new Mapping("0", 0),
            new Mapping("1", 1),
            new Mapping("2", 2),
            new Mapping("3", 3),
            new Mapping("4", 4),
            new Mapping("5", 5),
            new Mapping("6", 6),
            new Mapping("7", 7),
            new Mapping("8", 8),
            new Mapping("9", 9)
    );
    public static final List<Mapping> WRITTEN_AND_NUMERIC_NUMBERS = Stream.concat(NUMERIC_NUMBERS.stream(), WRITTEN_NUMBERS.stream()).toList();

    public int decode() {
        return extracted(NUMERIC_NUMBERS);
    }

    public int decodeWithWrittenNumbers() {
        return extracted(WRITTEN_AND_NUMERIC_NUMBERS);
    }

    private int extracted(List<Mapping> numericNumbers) {
        int firstPosition = encodedText.length() + 1;
        int lastPosition = -1;
        Integer first = null;
        Integer last = null;
        for (var mapping : numericNumbers) {
            int pos = encodedText.indexOf(mapping.text());
            if (pos > -1 && pos < firstPosition) {
                firstPosition = pos;
                first = mapping.value();
            }
            pos = encodedText.lastIndexOf(mapping.text());
            if (pos > -1 && pos > lastPosition) {
                lastPosition = pos;
                last = mapping.value();
            }
        }
        return first * 10 + last;
    }

    public record Mapping(String text, Integer value) {
    }
}

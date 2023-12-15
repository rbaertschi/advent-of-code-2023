package ch.ebynaqon.aoc.aoc23;

import java.util.Arrays;

public class LensLibrary {

    public static int hash(String input) {
        int result = 0;
        for (int i = 0; i < input.length(); i++) {
            result += input.codePointAt(i);
            result *= 17;
            result = result % 256;
        }
        return result;
    }

    public static long sumOfHashes(String input) {
        return Arrays.stream(input.split(","))
                .mapToLong(LensLibrary::hash)
                .sum();
    }
}

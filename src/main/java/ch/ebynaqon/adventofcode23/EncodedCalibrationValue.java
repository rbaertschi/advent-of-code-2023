package ch.ebynaqon.adventofcode23;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.OptionalInt;
import java.util.stream.Stream;

public record EncodedCalibrationValue(String encodedText) {

    public static final int ZERO = Character.valueOf('0').charValue();

    public int decode() {
        char[] chars = encodedText.toCharArray();
        Integer firstNumber = null;
        Integer lastNumber = null;
        for (int i = 0; i < chars.length && firstNumber == null; i++) {
            if (Character.isDigit(chars[i])) {
                firstNumber = ((int) chars[i]) - ZERO;
            }
        }
        for (int i = chars.length - 1; i >= 0 && lastNumber == null; i--) {
            if (Character.isDigit(chars[i])) {
                lastNumber = (int) chars[i] - ZERO;
            }
        }
        return firstNumber * 10 + lastNumber;
    }
}

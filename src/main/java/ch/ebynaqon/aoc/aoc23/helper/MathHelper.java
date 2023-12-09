package ch.ebynaqon.aoc.aoc23.helper;

import java.util.ArrayList;
import java.util.List;

public class MathHelper {
    public static long leastCommonMultiple(List<Integer> numbers) {
        return numbers.stream().mapToLong(Long::valueOf).reduce(1L, MathHelper::leastCommonMultiple);
    }

    public static long leastCommonMultiple(long a, long b) {
        return (a * b) / greatestCommonDenominator(a, b);
    }

    private static long greatestCommonDenominator(long a, long b) {
        if (b == 0) return a;
        return greatestCommonDenominator(b, a % b);
    }

    public static List<Integer> factorize(Integer number) {
        ArrayList<Integer> factors = new ArrayList<>();
        factors.add(1);
        var factor = 2;
        var remainder = number;
        while (remainder > 1 && factor <= number) {
            if (remainder % factor == 0) {
                factors.add(factor);
                remainder = remainder / factor;
            } else {
                factor++;
            }
        }
        return factors;
    }
}

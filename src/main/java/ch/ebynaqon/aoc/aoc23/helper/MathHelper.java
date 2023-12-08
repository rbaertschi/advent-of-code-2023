package ch.ebynaqon.aoc.aoc23.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MathHelper {
    public static long leastCommonMultiple(List<Integer> numbers) {
        return numbers.stream().map(MathHelper::factorize)
                .map(MathHelper::histogram)
                .reduce(new HashMap<>(), CollectionHelper::mergeMaxValue)
                .entrySet()
                .stream()
                .mapToLong(entry -> (long) entry.getKey() * entry.getValue())
                .reduce(1, Math::multiplyExact);
    }

    public static Map<Integer, Integer> histogram(List<Integer> numbers) {
        var histogram = new HashMap<Integer, Integer>();
        for (var number : numbers) {
            histogram.put(number, histogram.getOrDefault(number, 0) + 1);
        }
        return histogram;
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

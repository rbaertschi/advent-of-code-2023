package ch.ebynaqon.aoc.aoc23.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsHelper {
    public static <T> Map<T, Integer> histogram(List<T> numbers) {
        var histogram = new HashMap<T, Integer>();
        for (var number : numbers) {
            histogram.put(number, histogram.getOrDefault(number, 0) + 1);
        }
        return histogram;
    }
}

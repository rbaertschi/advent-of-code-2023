package ch.ebynaqon.aoc.aoc23.helper;

import java.util.HashMap;
import java.util.Map;

public class CollectionHelper {
    public static Map<Integer, Integer> mergeMaxValue(Map<Integer, Integer> a, Map<Integer, Integer> b) {
        var merged = new HashMap<>(a);
        for (var entry : b.entrySet()) {
            merged.put(entry.getKey(), Math.max(merged.getOrDefault(entry.getKey(), Integer.MIN_VALUE), entry.getValue()));
        }
        return merged;
    }
}

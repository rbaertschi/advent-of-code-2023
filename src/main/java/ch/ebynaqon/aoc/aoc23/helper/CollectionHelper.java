package ch.ebynaqon.aoc.aoc23.helper;

import ch.ebynaqon.aoc.aoc23.ClumsyCrucible;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionHelper {
    public static <T> Map<T, Integer> mergeMaxValue(Map<T, Integer> a, Map<T, Integer> b) {
        var merged = new HashMap<>(a);
        for (var entry : b.entrySet()) {
            merged.put(entry.getKey(), Math.max(merged.getOrDefault(entry.getKey(), Integer.MIN_VALUE), entry.getValue()));
        }
        return merged;
    }

    public static <T> List<IndexAndValue<T>> zipWithIndex(List<T> values) {
        var indexAndValues = new ArrayList<IndexAndValue<T>>();
        for (int index = 0; index < values.size(); index++) {
            indexAndValues.add(new IndexAndValue<>(index, values.get(index)));
        }
        return indexAndValues;
    }

    public static <T> List<T> concat(List<T> a, List<T> b) {
        ArrayList<T> combined = new ArrayList<>(a);
        combined.addAll(b);
        return combined;
    }

    public static <T> List<T> append(List<T> list, T next) {
        var result = new ArrayList<>(list);
        result.add(next);
        return result;
    }

    public record IndexAndValue<T>(int index, T value) {}
}

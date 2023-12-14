package ch.ebynaqon.aoc.aoc23;

import java.util.ArrayList;
import java.util.List;

public record ParabolicReflectorDish(String rockDistribution) {
    public static ParabolicReflectorDish parse(String input) {
        return new ParabolicReflectorDish(input);
    }

    public long solve() {
        int totalWeight = 0;
        for (var column : columns()) {
            int columnLength = column.length();
            int nextWeight = columnLength;
            for (int i = 0; i < columnLength; i++) {
                if (column.charAt(i) == '#') {
                    nextWeight = columnLength - 1 - i;
                } else if (column.charAt(i) == 'O') {
                    totalWeight += nextWeight--;
                }
            }
        }
        return totalWeight;
    }

    public List<String> columns() {
        String[] lines = rockDistribution().split("\n");
        int numberOfColumns = lines[0].length();
        var columns = new ArrayList<String>();
        for (int i = 0; i < numberOfColumns; i++) {
            var columnBuilder = new StringBuilder(lines.length);
            for (var line : lines) {
                columnBuilder.append(line.charAt(i));
            }
            columns.add(columnBuilder.toString());
        }
        return columns;
    }
}

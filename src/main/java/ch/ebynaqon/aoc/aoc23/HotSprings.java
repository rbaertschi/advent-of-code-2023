package ch.ebynaqon.aoc.aoc23;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class HotSprings {
    public static Report parseLine(String input) {
        String[] patternAndParts = input.split("\\s+");
        assert patternAndParts.length == 2;
        List<Integer> brokenParts = Arrays.stream(patternAndParts[1].split(",")).map(Integer::parseInt).toList();
        return new Report(patternAndParts[0].trim(), brokenParts);
    }

    public static long findSumOfArrangements(String input, boolean duplicate) {
        return Arrays.stream(input.split("\n"))
                .map(HotSprings::parseLine)
                .map(report -> {
                    if (duplicate) {
                        var duplicated = new ArrayList<Integer>();
                        for (int i = 0; i < 5; i++) {
                            duplicated.addAll(report.brokenParts());
                        }
                        String duplicatedPattern = (report.conditions() + "?").repeat(5);
                        return new Report(duplicatedPattern.substring(0, duplicatedPattern.length() - 1), duplicated);
                    }
                    return report;
                })
                .mapToLong(Report::solve)
                .sum();
    }

    public record Report(String conditions, List<Integer> brokenParts) {
        public long generateSpacings() {
            var lookupTable = new HashMap<String, Long>();
            int length = conditions.length();
            int totalBrokenParts = brokenParts().stream().mapToInt(Integer::intValue).sum();
            int totalSpaces = length - totalBrokenParts;
            int numberOfGaps = brokenParts().size() - 1;
            int movableSpaces = totalSpaces - numberOfGaps;
            int numberOfSpacePositions = numberOfGaps + 2;
            return distribute(movableSpaces, numberOfSpacePositions, 0, lookupTable, 0);
        }

        private long distribute(int space, int locations,
                                int brokenIndex, HashMap<String, Long> lookupTable, int offset) {
            if (locations == 1) {
                String finalGeneratedPattern = ".".repeat(space);
                if (matchesEndOfPattern(finalGeneratedPattern, conditions())) {
                    return 1;
                }
                return 0;
            }
            var distributions = 0L;
            for (int i = 0; i <= space; i++) {
                int nextSpace = space - i;
                int nextLocations = locations - 1;
                String lookupKey = "%d-%d".formatted(nextSpace, nextLocations);
                String currentPrefix = ".".repeat(i + (brokenIndex == 0 ? 0 : 1)) + "#".repeat(brokenParts().get(brokenIndex));
                if (matchesPatternAtOffset(currentPrefix, conditions(), offset)) {
                    var nestedDistributions = 0L;
                    if (lookupTable.containsKey(lookupKey)) {
                        nestedDistributions = lookupTable.get(lookupKey);
                    } else {
                        nestedDistributions = distribute(nextSpace, nextLocations, brokenIndex + 1, lookupTable, offset + currentPrefix.length());
                        lookupTable.put(lookupKey, nestedDistributions);
                    }
                    distributions += nestedDistributions;
                }
            }
            return distributions;
        }

        public long solve() {
            return generateSpacings();
        }

        public static boolean matchesPattern(String generatedPattern, String pattern) {
            for (int i = 0; i < generatedPattern.length(); i++) {
                if (!(pattern.charAt(i) == '?' || generatedPattern.charAt(i) == pattern.charAt(i)))
                    return false;
            }
            return true;
        }

        public static boolean matchesEndOfPattern(String generatedPattern, String pattern) {
            return matchesPatternAtOffset(generatedPattern, pattern, pattern.length() - generatedPattern.length());
        }

        public static boolean matchesPatternAtOffset(String generatedPattern, String pattern, int offset) {
            for (int generatedIndex = 0; generatedIndex < generatedPattern.length(); generatedIndex++) {
                int patternIndex = generatedIndex + offset;
                if (!(pattern.charAt(patternIndex) == '?' || generatedPattern.charAt(generatedIndex) == pattern.charAt(patternIndex)))
                    return false;
            }
            return true;
        }

        public static String patternFrom(List<Integer> spacing, List<Integer> broken) {
            StringBuilder pattern = new StringBuilder();
            pattern.append(".".repeat(spacing.getFirst()));
            for (int i = 0; i < broken.size(); i++) {
                boolean isAfterSpace = i == (broken.size() - 1);
                pattern.append("#".repeat(broken.get(i)))
                        .append(".".repeat(spacing.get(i + 1) + (isAfterSpace ? 0 : 1)));
            }
            return pattern.toString();
        }

    }
}

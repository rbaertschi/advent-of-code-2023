package ch.ebynaqon.aoc.aoc23;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HotSprings {
    public static Report parseLine(String input) {
        String[] patternAndParts = input.split("\\s+");
        assert patternAndParts.length == 2;
        List<Integer> brokenParts = Arrays.stream(patternAndParts[1].split(",")).map(Integer::parseInt).toList();
        return new Report(patternAndParts[0].trim(), brokenParts);
    }

    public static long sumOfMatchingPatterns(String input, boolean unfold) {
        return Arrays.stream(input.split("\n"))
                .map(HotSprings::parseLine)
                .map(report -> unfold ? unfold(report) : report)
                .mapToLong(Report::countMatchingPatterns)
                .sum();
    }

    private static Report unfold(Report report) {
        var duplicated = new ArrayList<Integer>();
        for (int i = 0; i < 5; i++) {
            duplicated.addAll(report.brokenParts());
        }
        String duplicatedPattern = (report.pattern() + "?").repeat(5);
        return new Report(duplicatedPattern.substring(0, duplicatedPattern.length() - 1), duplicated);
    }

    public record Report(String pattern, List<Integer> brokenParts) {
        public long countMatchingPatterns() {
            var lookupTable = new HashMap<String, Long>();
            int length = pattern.length();
            int totalBrokenParts = brokenParts().stream().mapToInt(Integer::intValue).sum();
            int totalSpaces = length - totalBrokenParts;
            int numberOfGaps = brokenParts().size() - 1;
            int movableSpaces = totalSpaces - numberOfGaps;
            int numberOfSpacePositions = numberOfGaps + 2;
            return countMatchesForSubset(movableSpaces, numberOfSpacePositions, 0, lookupTable, 0);
        }

        private long countMatchesForSubset(int remainingSpaces, int remainingLocations, int currentGroupOfBrokenParts, HashMap<String, Long> lookupTable, int offset) {
            if (remainingLocations == 1) {
                return matchesPatternAtOffset(".".repeat(remainingSpaces), this.pattern(), offset) ? 1 : 0;
            }
            var numberOfMatchingPatterns = 0L;
            for (int numberOfSpacesInCurrentPosition = 0; numberOfSpacesInCurrentPosition <= remainingSpaces; numberOfSpacesInCurrentPosition++) {
                int nextRemainingSpaces = remainingSpaces - numberOfSpacesInCurrentPosition;
                int nextRemainingLocations = remainingLocations - 1;
                String lookupKey = "%d-%d".formatted(nextRemainingSpaces, nextRemainingLocations);
                int mandatorySpaceAtCurrentLocation = currentGroupOfBrokenParts == 0 ? 0 : 1;
                String currentPattern = ".".repeat(numberOfSpacesInCurrentPosition + mandatorySpaceAtCurrentLocation) + "#".repeat(brokenParts().get(currentGroupOfBrokenParts));
                if (matchesPatternAtOffset(currentPattern, pattern(), offset)) {
                    if (lookupTable.containsKey(lookupKey)) {
                        numberOfMatchingPatterns += lookupTable.get(lookupKey);
                    } else {
                        var numberOfMatchingPatternsForRemainingSpaces = countMatchesForSubset(
                                nextRemainingSpaces, nextRemainingLocations, currentGroupOfBrokenParts + 1, lookupTable, offset + currentPattern.length());
                        lookupTable.put(lookupKey, numberOfMatchingPatternsForRemainingSpaces);
                        numberOfMatchingPatterns += numberOfMatchingPatternsForRemainingSpaces;
                    }
                }
            }
            return numberOfMatchingPatterns;
        }

        public static boolean matchesPatternAtOffset(String generatedPattern, String pattern, int offset) {
            for (int generatedIndex = 0; generatedIndex < generatedPattern.length(); generatedIndex++) {
                int patternIndex = generatedIndex + offset;
                if (!(pattern.charAt(patternIndex) == '?' || generatedPattern.charAt(generatedIndex) == pattern.charAt(patternIndex)))
                    return false;
            }
            return true;
        }

    }
}

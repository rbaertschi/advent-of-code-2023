package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.helper.CollectionHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HotSprings {
    public static Report parseLine(String input) {
        String[] patternAndParts = input.split("\\s+");
        assert patternAndParts.length == 2;
        List<Integer> brokenParts = Arrays.stream(patternAndParts[1].split(",")).map(Integer::parseInt).toList();
        return new Report(patternAndParts[0].trim(), brokenParts);
    }

    public static long findSumOfArrangements(String input) {
        return Arrays.stream(input.split("\n"))
                .map(HotSprings::parseLine)
                .mapToLong(Report::solve)
                .sum();
    }

    public record Report(String conditions, List<Integer> brokenParts) {
        public List<List<Integer>> generateSpacings() {
            int length = conditions.length();
            int totalBrokenParts = brokenParts().stream().mapToInt(Integer::intValue).sum();
            int totalSpaces = length - totalBrokenParts;
            int numberOfGaps = brokenParts().size() - 1;
            int movableSpaces = totalSpaces - numberOfGaps;
            int numberOfSpacePositions = numberOfGaps + 2;
            return distribute(movableSpaces, numberOfSpacePositions);
        }

        private List<List<Integer>> distribute(int space, int locations) {
            System.out.println("%d, %d".formatted(space, locations));
            if (locations == 1) return List.of(List.of(space));
            var distributions = new ArrayList<List<Integer>>();
            for (int i = 0; i <= space; i++) {
                List<Integer> prototype = List.of(i);
                distribute(space - i, locations - 1).stream()
                        .map(list -> CollectionHelper.concat(prototype, list))
                        .forEach(distributions::add);
            }
            return distributions;
        }

        public long solve() {
            List<List<Integer>> lists = generateSpacings();
            long count = lists.stream()
                    .filter(spacing -> matchesPattern(spacing, brokenParts(), conditions()))
                    .count();
            System.out.println("%d of %d generated patterns match '%s'".formatted(count, lists.size(), conditions()));
            return count;
        }

        public static boolean matchesPattern(List<Integer> spacing, List<Integer> broken, String pattern) {
            String generatedPattern = patternFrom(spacing, broken);
            for (int i = 0; i < generatedPattern.length(); i++) {
                if (!(pattern.charAt(i) == '?' || generatedPattern.charAt(i) == pattern.charAt(i)))
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
            String string = pattern.toString();
//            System.out.println("%s + %s -> %s".formatted(spacing, broken, string));
            return string;
        }

    }
}

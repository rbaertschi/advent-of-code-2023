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
        return new Report(patternAndParts[0], brokenParts);
    }

    public record Report(String conditions, List<Integer> brokenParts) {
        public List<List<Integer>> explode2() {
            int length = conditions.length();
            int totalBrokenParts = brokenParts().stream().mapToInt(Integer::intValue).sum();
            int totalSpaces = length - totalBrokenParts;
            int numberOfGaps = brokenParts().size() - 1;
            int movableSpaces = totalSpaces - numberOfGaps;
            int[] remainingSpaces = new int[numberOfGaps + 2];
            remainingSpaces[0] = movableSpaces;


            for (int spacesBefore = 0; spacesBefore <= movableSpaces; spacesBefore++) {
                for (int spacesAfter = 0; spacesAfter <= movableSpaces - spacesBefore; spacesAfter++) {
                    int remainingGapSpace = movableSpaces - spacesBefore - spacesAfter;
                    int[] gapSpaces = new int[numberOfGaps];
                    for (int gap = 0; gap < numberOfGaps; gap++) {
                        for (int gapSpace = 0; gapSpace < remainingGapSpace; gapSpace++) {
                            gapSpaces[gap] = gapSpace;
                        }
                    }
                }
            }
            return List.of();
        }

        public List<List<Integer>> explode() {
            int length = conditions.length();
            int totalBrokenParts = brokenParts().stream().mapToInt(Integer::intValue).sum();
            int totalSpaces = length - totalBrokenParts;
            int numberOfGaps = brokenParts().size() - 1;
            int movableSpaces = totalSpaces - numberOfGaps;
            int numberOfSpacePositions = numberOfGaps + 2;
            return distribute(movableSpaces, numberOfSpacePositions);
        }

        private List<List<Integer>> distribute(int space, int locations) {
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
    }
}

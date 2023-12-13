package ch.ebynaqon.aoc.aoc23;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FindingMirrors {
    public static long sumOfMirrorScores(String input, boolean withSmudge) {
        return Arrays.stream(input.split("\n\n"))
                .mapToLong(subInput -> calculateMirrorScore(subInput, withSmudge)
                )
                .sum();
    }

    public static long calculateMirrorScore(String input, boolean withSmudge) {
        int originalScore = FindingMirrors.calculateMirrorScoreForOneInput(input, null);
        if (withSmudge) {
            return generateInputVariations(input, withSmudge)
                    .map(unsmudgedInput -> calculateMirrorScoreForOneInput(unsmudgedInput, originalScore))
                    .filter(i -> i > 0)
                    .findFirst()
                    .orElse(0);
        }
        return originalScore;
    }

    public static int calculateMirrorScoreForOneInput(String input, Integer excludedScore) {
        List<String> rows = Arrays.asList(input.split("\n"));
        List<String> columns = extractColumns(rows);
        return findCommonMirrorPoints(columns).stream()
                .map(i -> i * 100)
                .filter(i -> !Objects.equals(i, excludedScore))
                .findFirst().or(() -> findCommonMirrorPoints(rows).stream()
                        .filter(i1 -> !Objects.equals(i1, excludedScore))
                        .findFirst()).orElse(0);
    }

    static Stream<String> generateInputVariations(String input, boolean withSmudge) {
        if (!withSmudge) {
            return Stream.of(input);
        }
        String[] lines = input.split("\n");
        int lineLength = lines[0].length();
        int lineCount = lines.length;
        int numberOfVariations = lineLength * lineCount;
        return IntStream.range(0, numberOfVariations)
                .mapToObj(smudgePosition -> {
                    int smudgeRow = smudgePosition / lineLength;
                    int smudgeCol = smudgePosition % lineLength;
                    return FindingMirrors.inputWithSmudgeAt(input, smudgeRow, smudgeCol);
                });
    }

    public static List<String> extractColumns(List<String> lines) {
        var result = new ArrayList<String>();
        int columnLength = lines.getFirst().length();
        for (int column = 0; column < columnLength; column++) {
            var columnString = new StringBuilder(columnLength);
            for (String line : lines) {
                columnString.append(line.charAt(column));
            }
            result.add(columnString.toString());
        }
        return result;
    }

    public static Set<Integer> findCommonMirrorPoints(List<String> input) {
        Set<Integer> commonMirrorPoints = findMirrorPoints(input.getFirst());
        for (int i = 1; i < input.size(); i++) {
            var mirrorPoints = findMirrorPoints(input.get(i));
            commonMirrorPoints = commonMirrorPoints.stream()
                    .filter(mirrorPoints::contains)
                    .collect(Collectors.toSet());
        }
        return commonMirrorPoints;
    }

    static String inputWithSmudgeAt(String input, int smudgeRow, int smudgeCol) {
        int lineLength = input.indexOf("\n");
        int smudgePosition = smudgeRow * (lineLength + 1) + smudgeCol;
        return input.substring(0, smudgePosition)
               + (input.charAt(smudgePosition) == '#' ? "." : "#")
               + input.substring(smudgePosition + 1);
    }

    public static Set<Integer> findMirrorPoints(String input) {
        HashSet<Integer> mirrorPoints = new HashSet<>();
        for (int mirrorPoint = 1; mirrorPoint < input.length(); mirrorPoint++) {
            if (isMirrorPoint(input, mirrorPoint)) {
                mirrorPoints.add(mirrorPoint);
            }
        }
        return mirrorPoints;
    }

    public static boolean isMirrorPoint(String input, int mirrorPoint) {
        int maxDistance = Math.max(0, Math.min(mirrorPoint, input.length() - mirrorPoint));
        var string1 = input.substring(mirrorPoint - maxDistance, mirrorPoint);
        var string2 = input.substring(mirrorPoint, mirrorPoint + maxDistance);
        return string1.equals(reverseString(string2));
    }

    public static String reverseString(String input) {
        var result = new StringBuilder(input.length());
        for (int i = input.length() - 1; i >= 0; i--) {
            result.append(input.charAt(i));
        }
        return result.toString();
    }

}

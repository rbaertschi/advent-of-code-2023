package ch.ebynaqon.aoc.aoc23;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class FindingMirrors {

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

    public static int calculateMirrorScoreForOneInput(String input) {
        List<String> rows = Arrays.asList(input.split("\n"));
        List<String> columns = extractColumns(rows);
        Set<Integer> columnMirrorPoints = findCommonMirrorPoints(rows);
        Set<Integer> rowMirrorPoints = findCommonMirrorPoints(columns);
        int score = columnMirrorPoints.stream().mapToInt(i -> i).sum()
                 + rowMirrorPoints.stream().mapToInt(i -> i * 100).sum();
        if (score > 0) {
            System.out.println(input);
            System.out.println("---------");
        }
        return score;
    }

    public static long sumOfMirrorScores(String input, boolean withSmudge) {
        return Arrays.stream(input.split("\n\n"))
                .mapToLong(subInput -> calculateMirrorScore(subInput, withSmudge)
                )
                .sum();
    }

    public static long calculateMirrorScore(String input, boolean withSmudge) {
        List<Integer> mirrorScores = generateInputVariations(input, withSmudge)
                .map(FindingMirrors::calculateMirrorScoreForOneInput)
                .filter(i -> i > 0)
                .toList();
        System.out.println(mirrorScores);
        return mirrorScores.stream().mapToLong(i->i)
                .max()
                .getAsLong();
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

    static String inputWithSmudgeAt(String input, int smudgeRow, int smudgeCol) {
        int lineLength = input.indexOf("\n");
        int smudgePosition = smudgeRow * (lineLength + 1) + smudgeCol;
        return input.substring(0, smudgePosition)
               + (input.charAt(smudgePosition) == '#' ? "." : "#")
               + input.substring(smudgePosition + 1);
    }
}

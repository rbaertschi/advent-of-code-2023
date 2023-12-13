package ch.ebynaqon.aoc.aoc23;

import java.util.*;
import java.util.stream.Collectors;

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

    public static int calculateMirrorScore(String input) {
        List<String> rows = Arrays.asList(input.split("\n"));
        List<String> columns = extractColumns(rows);
        Set<Integer> columnMirrorPoints = findCommonMirrorPoints(rows);
        Set<Integer> rowMirrorPoints = findCommonMirrorPoints(columns);
        return columnMirrorPoints.stream().mapToInt(i -> i).sum()
               + rowMirrorPoints.stream().mapToInt(i -> i * 100).sum();
    }

    public static long sumOfMirrorScores(String input) {
        return Arrays.stream(input.split("\n\n"))
                .mapToLong(FindingMirrors::calculateMirrorScore)
                .sum();
    }
}

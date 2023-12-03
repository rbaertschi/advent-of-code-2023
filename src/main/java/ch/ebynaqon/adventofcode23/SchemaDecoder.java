package ch.ebynaqon.adventofcode23;


import java.util.ArrayList;
import java.util.List;

public class SchemaDecoder {

    public static final List<String> NUMBERS = List.of(
            "0",
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9"
    );
    private final Line[] lines;

    public static Line parse(String line) {
        String[] chars = line.split("");
        String numberBuffer = "";
        ArrayList<Number> numbers = new ArrayList<>();
        ArrayList<Integer> symbolPositions = new ArrayList<>();
        int curPos = 0;
        for (String cur : chars) {
            if (NUMBERS.contains(cur)) {
                numberBuffer += cur;
            } else {
                if (!numberBuffer.isEmpty()) {
                    var newNumber = new Number(Integer.parseInt(numberBuffer), curPos - numberBuffer.length(), curPos - 1);
                    numbers.add(newNumber);
                }
                numberBuffer = "";
                if (!cur.equals(".")) {
                    symbolPositions.add(curPos);
                }
            }
            curPos++;
        }
        if (!numberBuffer.isEmpty()) {
            var newNumber = new Number(Integer.parseInt(numberBuffer), curPos - numberBuffer.length(), curPos - 1);
            numbers.add(newNumber);
        }
        return new Line(numbers, symbolPositions);
    }

    public SchemaDecoder(String input) {
        String[] stringLines = input.split("\n");
        lines = new Line[stringLines.length];
        for (int i = 0; i < stringLines.length; i++) {
            lines[i] = parse(stringLines[i]);
        }
    }

    public List<Integer> findPartNumbers() {
        ArrayList<Number> partNumbers = new ArrayList<>();
        for (int curLine = 0; curLine < lines.length; curLine++) {
            List<Number> numbers = lines[curLine].numbers();
            for (var number : numbers) {
                if (hasSymbolAdjacentTo(number, curLine)) {
                    partNumbers.add(number);
                }
            }

        }
        return partNumbers.stream().map(Number::value).toList();
    }

    private boolean hasSymbolAdjacentTo(Number number, int curLine) {
        if (curLine > 0) {
            if (lines[curLine - 1].symbolPositions.stream().anyMatch(symbol -> isAdjacent(number, symbol))) {
                return true;
            }
        }
        if (lines[curLine].symbolPositions.stream().anyMatch(symbol -> isAdjacent(number, symbol))) {
            return true;
        }
        if (curLine < lines.length - 1) {
            if (lines[curLine + 1].symbolPositions.stream().anyMatch(symbol -> isAdjacent(number, symbol))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isAdjacent(Number number, Integer symbol) {
        return (symbol >= number.startPosition() - 1) && (symbol <= number.endPosition() + 1);
    }

    public int sumOfPartNumbers() {
        return findPartNumbers().stream().mapToInt(i -> i).sum();
    }

    public record Line(List<Number> numbers, List<Integer> symbolPositions) {
    }

    public record Number(int value, int startPosition, int endPosition) {
    }
}

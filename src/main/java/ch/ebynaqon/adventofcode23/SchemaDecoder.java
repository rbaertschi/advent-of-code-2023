package ch.ebynaqon.adventofcode23;


import java.util.*;

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
        ArrayList<Symbol> symbols = new ArrayList<>();
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
                    symbols.add(new Symbol(curPos, cur, new HashSet<>()));
                }
            }
            curPos++;
        }
        if (!numberBuffer.isEmpty()) {
            var newNumber = new Number(Integer.parseInt(numberBuffer), curPos - numberBuffer.length(), curPos - 1);
            numbers.add(newNumber);
        }
        return new Line(numbers, symbols);
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

    public Integer sumOfGearRatios() {
        findPartNumbers();
        return Arrays.stream(lines).flatMap(line -> line.symbols().stream())
                .filter(symbol -> symbol.adjacentNumbers().size() == 2)
                .mapToInt(symbol -> symbol.adjacentNumbers().stream().mapToInt(Number::value).reduce(1, (acc, cur) -> acc * cur))
                .sum();
    }

    private boolean hasSymbolAdjacentTo(Number number, int curLine) {
        ArrayList<Symbol> symbolsToCheck = new ArrayList<>(lines[curLine].symbols());
        if (curLine > 0) {
            symbolsToCheck.addAll(lines[curLine - 1].symbols());
        }
        if (curLine < lines.length - 1) {
            symbolsToCheck.addAll(lines[curLine + 1].symbols());
        }
        boolean hasAdjacentSymbol = false;
        for (var symbol:symbolsToCheck         ) {
            if (isAdjacent(number, symbol.position())) {
                hasAdjacentSymbol = true;
                symbol.adjacentNumbers().add(number);
            }
        }
        return hasAdjacentSymbol;
    }

    private static boolean isAdjacent(Number number, Integer symbol) {
        return (symbol >= number.startPosition() - 1) && (symbol <= number.endPosition() + 1);
    }

    public int sumOfPartNumbers() {
        return findPartNumbers().stream().mapToInt(i -> i).sum();
    }

    public record Line(List<Number> numbers, List<Symbol> symbols) {
    }

    public record Number(int value, int startPosition, int endPosition) {
    }

    public record Symbol(int position, String type, Set<Number> adjacentNumbers) {
    }
}

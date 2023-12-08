package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.helper.MathHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class HauntedWasteland {
    public static MapWithInstructions parse(String input) {
        String[] directionsAndNodes = input.split("\n\n");
        List<Direction> directions = Arrays.stream(directionsAndNodes[0].split("")).map(Direction::of).toList();
        HashMap<String, Instruction> instructions = new HashMap<>();
        Arrays.stream(directionsAndNodes[1].split("\n"))
                .map(line -> line.replaceAll("[=(),]", "").trim().split("\\s+"))
                .forEach(parts -> instructions.put(parts[0], new Instruction(parts[1], parts[2])));
        return new MapWithInstructions(directions, instructions);
    }

    public static long solve(String input) {
        return parse(input).numberOfSteps("AAA", "ZZZ"::equals);
    }

    public static long solveGhostly(String input) {
        MapWithInstructions map = parse(input);
        Predicate<String> endsWithZ = it -> it.endsWith("Z");
        Stream<String> startPositions = map.instructions().keySet().stream()
                .filter(it -> it.endsWith("A"));
        return MathHelper.leastCommonMultiple(
                startPositions
                        .map(startPosition -> map.numberOfSteps(startPosition, endsWithZ))
                        .toList());
    }

    public record MapWithInstructions(List<Direction> directions, Map<String, Instruction> instructions) {
        public int numberOfSteps(String startNode, Predicate<String> isDone) {
            var curPosition = startNode;
            var curInstruction = 0;
            while (!isDone.test(curPosition)) {
                Direction direction = directions().get(curInstruction % directions().size());
                Instruction instruction = instructions().get(curPosition);
                curPosition = direction.equals(Direction.LEFT) ? instruction.leftNode() : instruction.rightNode();
                curInstruction++;
            }
            return curInstruction;
        }
    }

    public record Instruction(String leftNode, String rightNode) {
    }

    public enum Direction {
        LEFT, RIGHT;

        public static Direction of(String text) {
            return "L".equals(text) ? LEFT : RIGHT;
        }
    }

}

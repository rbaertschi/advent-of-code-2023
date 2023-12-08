package ch.ebynaqon.aoc.aoc23;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HauntedWasteland {
    public static MapWithInstructions parse(String input) {
        String[] directionsAndNodes = input.split("\n\n");

        List<Direction> directions = Arrays.stream(directionsAndNodes[0].split("")).map(Direction::of).toList();
        HashMap<String, Instruction> instructions = new HashMap<>();
        Arrays.stream(directionsAndNodes[1].split("\n"))
                .map(line -> line.replaceAll("[=(),]", "").trim().split("\\s+"))
                .forEach(parts -> instructions.put(parts[0], new Instruction(parts[1],parts[2])));
        return new MapWithInstructions(directions, instructions);
    }

    public static long solve(String input) {
        MapWithInstructions map = parse(input);
        var curPosition = "AAA";
        var curInstruction = 0;
        while (!curPosition.equals("ZZZ")) {
            Direction direction = map.directions().get(curInstruction % map.directions().size());
            Instruction instruction = map.instructions().get(curPosition);
            curPosition = direction.equals(Direction.LEFT) ? instruction.leftNode() : instruction.rightNode();
            curInstruction++;
        }
        return curInstruction;
    }

    public static long solveGhostly(String input) {
        MapWithInstructions map = parse(input);
        var curPositions = map.instructions().keySet().stream().filter(it -> it.endsWith("A")).toList();
        long curInstructionForGhost = 0;
        while (!curPositions.stream().allMatch(it -> it.endsWith("Z"))) {
            var curInstruction = curInstructionForGhost;
            curPositions = curPositions.stream().map(curPosition -> {
                Direction direction = map.directions().get((int) (curInstruction % map.directions().size()));
                Instruction instruction = map.instructions().get(curPosition);
                return  direction.equals(Direction.LEFT) ? instruction.leftNode() : instruction.rightNode();
            }).toList();
            curInstructionForGhost++;
            if (curPositions.stream().anyMatch(it -> it.endsWith("Z"))) {
                System.out.printf("%d: %s%n", curInstructionForGhost, curPositions);
            }
        }
        return curInstructionForGhost;
    }

    public record Instruction(String leftNode, String rightNode) {
    }
    /*
    * 2902 - 2*338 -> 2225
    * TVZ 304
    * FNZ 394
    * DPZ 407
    * ZZZ 338
    * FPZ 329
    * PKZ 453
    * */

    public record MapWithInstructions(List<Direction> directions, Map<String, Instruction> instructions) {
    }

    public enum Direction {
        LEFT, RIGHT;

        public static Direction of(String text) {
            return "L".equals(text) ? LEFT : RIGHT;
        }
    }
}

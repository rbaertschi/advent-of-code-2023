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
                .forEach(parts -> instructions.put(parts[0], new Instruction(parts[1], parts[2])));
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

    public static long numberOfStepsFromGivenPositionToPositionEndingInZ(String input, String startPosition) {
        MapWithInstructions map = parse(input);
        var curPosition = startPosition;
        var curInstruction = 0;
        while (!curPosition.endsWith("Z") || curInstruction == 0) {
            Direction direction = map.directions().get(curInstruction % map.directions().size());
            Instruction instruction = map.instructions().get(curPosition);
            curPosition = direction.equals(Direction.LEFT) ? instruction.leftNode() : instruction.rightNode();
            curInstruction++;
        }
        System.out.println(curPosition);
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
                return direction.equals(Direction.LEFT) ? instruction.leftNode() : instruction.rightNode();
            }).toList();
            curInstructionForGhost++;
            if (curPositions.stream().anyMatch(it -> it.endsWith("Z"))) {
                System.out.printf("%d: %s%n", curInstructionForGhost, curPositions);
            }
        }
        return curInstructionForGhost;
    }

    public static long solveGhostly2(String input) {
        MapWithInstructions map = parse(input);
        List<String> startPositions = map.instructions().keySet().stream().filter(it -> it.endsWith("A")).toList();
        var curPosition = startPositions.get(0);
        HashMap<Integer, Integer> repetitions = new HashMap<>();
        int lastZPosition = -1;
        var curInstruction = 0;
        while (true) {
            // 0 : 48505 - 24252 = 24253 -> 70 * 307
            // 1 : 37453 - 18726 = 18727 -> 61 * 307
            // 2 : 36225 - 18112 = 18113 -> 59 * 307
            // 3 : 43593 - 21796 = 21797 -> 71 * 307
            // 4 : 44821 - 22410 = 22411 -> 73 * 307
            // 5 : 32541 - 16270 = 16271 -> 53 * 307
            // 70*61*59*71*73*53 *307 = 69204919070
            // 69204919070 * 307 -1 = 21245910154490

            // 24253*18727*18113*21797*22411*16271
            int curPositionInDirections = (curInstruction % map.directions().size());
            Direction direction = map.directions().get(curPositionInDirections);
            Instruction instruction = map.instructions().get(curPosition);
            curPosition = direction.equals(Direction.LEFT) ? instruction.leftNode() : instruction.rightNode();
            if (curPosition.endsWith("Z")) {
                System.out.println("%d %d".formatted(curInstruction, curPositionInDirections));
                if (lastZPosition > -1) {
                    if (repetitions.containsKey(lastZPosition)) {
                        break;
                    }
                    repetitions.put(lastZPosition, curPositionInDirections);
                }
                lastZPosition = curPositionInDirections;
            }
            curInstruction++;
        }
        System.out.println(repetitions);
        return curInstruction;
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

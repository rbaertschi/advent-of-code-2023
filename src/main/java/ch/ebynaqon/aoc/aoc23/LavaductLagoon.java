package ch.ebynaqon.aoc.aoc23;

import java.util.*;
import java.util.function.Predicate;

import static ch.ebynaqon.aoc.aoc23.LavaductLagoon.Direction.*;

public class LavaductLagoon {

    private final List<DiggingInstruction> instructions;

    public static LavaductLagoon parse(String input) {
        var instructions = Arrays.stream(input.split("\n"))
                .map(DiggingInstruction::parse)
                .toList();
        return new LavaductLagoon(instructions);
    }

    public LavaductLagoon(List<DiggingInstruction> instructions) {
        this.instructions = instructions;
    }

    public List<DiggingInstruction> getInstructions() {
        return instructions;
    }

    public int getDigVolume() {
        int minRow = 0;
        int maxRow = 0;
        int minColumn = 0;
        int maxColumn = 0;
        Position currentPosition = new Position(0, 0);
        List<Position> digPath = new ArrayList<>();
        digPath.add(currentPosition);
        for (var instruction : instructions) {
            for (int distance = 1; distance <= instruction.distance(); distance++) {
                currentPosition = currentPosition.move(instruction.direction());
                digPath.add(currentPosition);
                maxColumn = Math.max(maxColumn, currentPosition.column());
                minColumn = Math.min(minColumn, currentPosition.column());
                maxRow = Math.max(maxRow, currentPosition.row());
                minRow = Math.min(minRow, currentPosition.row());
            }
        }
        System.out.printf("Bounding Box (%d,%d) -> (%d,%d)%n", minRow, minColumn, maxRow, maxColumn);
        digPath = shiftPositions(digPath, minRow, minColumn);

        int rows = maxRow - minRow + 1;
        int columns = maxColumn - minColumn + 1;

        HashSet<Position> inside = new HashSet<>(digPath);
        HashSet<Position> outside = new HashSet<>();
        for (int row = 0; row < rows; row++) {
            Position position = new Position(row, 0);
            if (!inside.contains(position)) {
                outside.add(position);
            }
            position = new Position(row, columns - 1);
            if (!inside.contains(position)) {
                outside.add(position);
            }
        }
        for (int column = 0; column < columns; column++) {
            Position position = new Position(0, column);
            if (!inside.contains(position)) {
                outside.add(position);
            }
            position = new Position(rows - 1, column);
            if (!inside.contains(position)) {
                outside.add(position);
            }
        }

        Predicate<Position> isValidPosition = position -> position.row() >= 0
                                                          && position.row() < rows
                                                          && position.column() >= 0
                                                          && position.column() < columns;

        Queue<Position> positionsToCheck = new ArrayDeque<>(outside);
        Position nextPosition = positionsToCheck.poll();
        while (nextPosition != null) {
            System.out.println(nextPosition);
            var positionToCheck = nextPosition.move(UP);
            if (isValidPosition.test(positionToCheck) && !outside.contains(positionToCheck) && !inside.contains(positionToCheck)) {
                outside.add(positionToCheck);
                positionsToCheck.add(positionToCheck);
            }
            positionToCheck = nextPosition.move(DOWN);
            if (isValidPosition.test(positionToCheck) && !outside.contains(positionToCheck) && !inside.contains(positionToCheck)) {
                outside.add(positionToCheck);
                positionsToCheck.add(positionToCheck);
            }
            positionToCheck = nextPosition.move(LEFT);
            if (isValidPosition.test(positionToCheck) && !outside.contains(positionToCheck) && !inside.contains(positionToCheck)) {
                outside.add(positionToCheck);
                positionsToCheck.add(positionToCheck);
            }
            positionToCheck = nextPosition.move(RIGHT);
            if (isValidPosition.test(positionToCheck) && !outside.contains(positionToCheck) && !inside.contains(positionToCheck)) {
                outside.add(positionToCheck);
                positionsToCheck.add(positionToCheck);
            }
            nextPosition = positionsToCheck.poll();
        }

        return rows * columns - outside.size();
    }

    private static List<Position> shiftPositions(List<Position> digPath, int minRow, int minColumn) {
        return digPath.stream().map(position ->
                        new Position(position.row() - minRow, position.column() - minColumn))
                .toList();
    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT;

        public static Direction parse(String input) {
            return switch (input) {
                case "U" -> UP;
                case "D" -> DOWN;
                case "L" -> LEFT;
                case "R" -> RIGHT;
                default -> throw new IllegalArgumentException("%s does not represent a direction".formatted(input));
            };
        }
    }

    public record Position(int row, int column) {
        public Position move(Direction direction) {
            return switch (direction) {
                case UP -> new Position(row() - 1, column());
                case DOWN -> new Position(row() + 1, column());
                case LEFT -> new Position(row(), column() - 1);
                case RIGHT -> new Position(row(), column() + 1);
            };
        }
    }

    public record DiggingInstruction(Direction direction, int distance) {
        static DiggingInstruction parse(String input) {
            String[] parts = input.split("\\s+");
            return new DiggingInstruction(Direction.parse(parts[0]), Integer.parseInt(parts[1]));
        }
    }
}

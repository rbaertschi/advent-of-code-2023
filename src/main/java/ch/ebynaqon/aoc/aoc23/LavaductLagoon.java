package ch.ebynaqon.aoc.aoc23;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public record LavaductLagoon(List<DiggingInstruction> instructions) {
    public static LavaductLagoon parse(String input) {
        var instructions = Arrays.stream(input.split("\n"))
                .map(DiggingInstruction::parse)
                .toList();
        return new LavaductLagoon(instructions);
    }

    public static LavaductLagoon parseFromHex(String input) {
        var instructions = Arrays.stream(input.split("\n"))
                .map(DiggingInstruction::parseFromHex)
                .toList();
        return new LavaductLagoon(instructions);
    }

    public long getDigVolume() {
        Position curPosition = new Position(0, 0);
        List<Line> lines = new ArrayList<>();
        var yValues = new HashSet<Integer>();
        yValues.add(curPosition.y());
        for (int i = 0; i < instructions.size(); i++) {
            var previousDirection = instructions.get((i + instructions.size() - 1) % instructions.size()).direction();
            var instruction = instructions.get(i);
            var nextDirection = instructions.get((i + 1) % instructions.size()).direction();
            var changesDirection = !previousDirection.equals(nextDirection);
            Position nextPosition = curPosition.move(instruction.direction, instruction.distance);
            lines.add(new Line(curPosition, nextPosition, changesDirection));
            yValues.add(curPosition.y());
            curPosition = nextPosition;
        }
        long volume = 0L;
        long previousY = Long.MIN_VALUE;
        var increasingYValues = yValues.stream().sorted().toList();
        for (var y : increasingYValues) {
            if (y - 1 > previousY) {
                volume += getInsideOnLine(y - 1, lines) * (y - 1 - previousY);
            }
            volume += getInsideOnLine(y, lines);
            previousY = y;
        }
        return volume;
    }

    public static long getInsideOnLine(int y, List<Line> lines) {
        long insideOnLine = 0;
        long previousX = Long.MIN_VALUE;
        List<Line> intersectingLines = lines.stream()
                .filter(line -> line.intersects(y))
                .sorted(Comparator.comparing(line -> line.from().x()))
                .toList();
        boolean isInside = false;
        boolean previousWasHorizontal = false;
        for (var line : intersectingLines) {
            if (isInside) {
                insideOnLine += Math.max(0, line.minX() - 1 - previousX);
                if (!line.isHorizontal()) insideOnLine += 1;
                if (!previousWasHorizontal) insideOnLine += 1;
            }
            if (line.isHorizontal()) {
                insideOnLine += line.length();
                if (!line.changesDirection()) {
                    isInside = !isInside;
                }
                previousWasHorizontal = true;
            } else {
                isInside = !isInside;
                previousWasHorizontal = false;
            }
            previousX = line.maxX();
        }
        return insideOnLine;
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

        public static Direction parseFromNumber(String input) {
            return switch (input) {
                case "3" -> UP;
                case "1" -> DOWN;
                case "2" -> LEFT;
                case "0" -> RIGHT;
                default -> throw new IllegalArgumentException("%s does not represent a direction".formatted(input));
            };
        }
    }

    public record Position(int y, int x) {

        public Position move(Direction direction, int distance) {
            return switch (direction) {
                case UP -> new Position(y - distance, x);
                case DOWN -> new Position(y + distance, x);
                case LEFT -> new Position(y, x - distance);
                case RIGHT -> new Position(y, x + distance);
            };
        }
    }

    public record Line(Position from, Position to, boolean changesDirection) {
        public boolean intersects(int y) {
            return isHorizontal() && from.y == y
                   || Math.min(from.y, to.y) < y && Math.max(from.y, to.y) > y;
        }

        public boolean isHorizontal() {
            return from.y == to.y;
        }

        public long length() {
            return isHorizontal()
                    ? maxX() - minX() + 1
                    : Math.max(from.y, to.y) - Math.min(from.y, to.y) + 1;
        }

        private int maxX() {
            return Math.max(from.x, to.x);
        }

        private int minX() {
            return Math.min(from.x, to.x);
        }

    }

    public record DiggingInstruction(Direction direction, int distance) {
        static DiggingInstruction parse(String input) {
            String[] parts = input.split("\\s+");
            return new DiggingInstruction(Direction.parse(parts[0]), Integer.parseInt(parts[1]));
        }

        static DiggingInstruction parseFromHex(String input) {
            String[] parts = input.split("[()]");
            Direction direction = Direction.parseFromNumber(parts[1].substring(6));
            int distance = Integer.parseInt(parts[1].substring(1, 6), 16);
            return new DiggingInstruction(direction, distance);
        }
    }
}

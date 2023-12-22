package ch.ebynaqon.aoc.aoc23;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class SandSlabs {

    public static final RestingBrick GROUND = new RestingBrick(
            new Point(0, 0, 0),
            new Point(9, 9, 0),
            new HashSet<>(),
            new HashSet<>()
    );

    public static int countBricksSafeToDisintegrate(String input) {
        var fallingBricks = parse(input);
        var restingBricks = afterFall(fallingBricks);
        int count = 0;
        for (var restingBrick : restingBricks) {
            boolean isSafeToDisintegrate = restingBrick.restedOnBy()
                    .stream()
                    .allMatch(other -> other.restingOn().size() > 1);
            if (isSafeToDisintegrate) {
                count++;
            }
        }
        return count;
    }

    public static List<Brick> parse(String input) {
        return Arrays.stream(input.split("\n"))
                .map(Brick::parse)
                .toList();
    }

    public static List<RestingBrick> afterFall(List<Brick> fallingBricks) {
        List<Brick> sortedFallingBricks = fallingBricks.stream()
                .sorted(Comparator.comparing(brick -> Math.min(brick.start.z, brick.end.z)))
                .toList();
        RestingBrick[][] highestBrickAtPosition = new RestingBrick[10][10];
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                highestBrickAtPosition[x][y] = GROUND;
            }
        }
        var restingBricks = new ArrayList<RestingBrick>();
        for (var fallingBrick : sortedFallingBricks) {
            var restingOn = new HashSet<RestingBrick>();
            for (var position : fallingBrick.getBottomLayerPositions()) {
                int x = position.x();
                int y = position.y();
                restingOn.add(highestBrickAtPosition[x][y]);
            }
            int restingHeight = restingOn.stream()
                    .mapToInt(RestingBrick::topHeight)
                    .max()
                    .getAsInt();
            var actuallyRestingOn = restingOn.stream()
                    .filter(brick -> brick.topHeight() == restingHeight)
                    .collect(Collectors.toSet());
            Brick fallenBrick = fallingBrick.atHeight(restingHeight + 1);
            RestingBrick restingBrick = new RestingBrick(
                    fallenBrick.start,
                    fallenBrick.end,
                    actuallyRestingOn,
                    new HashSet<>()
            );
            actuallyRestingOn.forEach(base -> base.restedOnBy().add(restingBrick));
            for (var position : fallenBrick.getBottomLayerPositions()) {
                int x = position.x();
                int y = position.y();
                highestBrickAtPosition[x][y] = restingBrick;
            }
            restingBricks.add(restingBrick);
        }
        return restingBricks;
    }

    public static final class RestingBrick {
        private final Point start;
        private final Point end;
        private final Set<RestingBrick> restingOn;
        private final Set<RestingBrick> restedOnBy;

        public RestingBrick(Point start, Point end, Set<RestingBrick> restingOn, Set<RestingBrick> restedOnBy) {
            this.start = start;
            this.end = end;
            this.restingOn = restingOn;
            this.restedOnBy = restedOnBy;
        }

        public int topHeight() {
            return Math.max(start.z, end.z);
        }

        public Point start() {
            return start;
        }

        public Point end() {
            return end;
        }

        public Set<RestingBrick> restingOn() {
            return restingOn;
        }

        public Set<RestingBrick> restedOnBy() {
            return restedOnBy;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (RestingBrick) obj;
            return Objects.equals(this.start, that.start) &&
                   Objects.equals(this.end, that.end);
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end);
        }

        @Override
        public String toString() {
            return "RestingBrick[" +
                   "start=" + start + ", " +
                   "end=" + end + ", " +
                   "restingOn=" + restingOn.size() + ", " +
                   "restedOnBy=" + restedOnBy.size() + ']';
        }

    }

    public record Brick(Point start, Point end) {

        public static Brick parse(String input) {
            String[] startAndEnd = input.split("~");
            return new Brick(Point.parse(startAndEnd[0]), Point.parse(startAndEnd[1]));
        }

        public List<Point> getBottomLayerPositions() {
            ArrayList<Point> points = new ArrayList<>();
            for (int x = Math.min(start.x, end.x); x <= Math.max(start.x, end.x); x++) {
                for (int y = Math.min(start.y, end.y); y <= Math.max(start.y, end.y); y++) {
                    points.add(new Point(x, y, Math.min(start.z, end.z)));
                }
            }
            return points;
        }

        public Brick atHeight(int bottomZ) {
            int currentBottomZ = Math.min(start.z, end.z);
            int zOffset = currentBottomZ - bottomZ;
            return new Brick(
                    new Point(start.x, start.y, start.z - zOffset),
                    new Point(end.x, end.y, end.z - zOffset)
            );
        }
    }

    public record Point(int x, int y, int z) {
        public static Point parse(String input) {
            String[] xYZ = input.split(",");
            return new Point(parseInt(xYZ[0]), parseInt(xYZ[1]), parseInt(xYZ[2]));
        }
    }
}

package ch.ebynaqon.aoc.aoc23;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class HailstoneCollider {

    private final List<HailstonePath> hailstones;

    public HailstoneCollider(String input) {
        hailstones = Arrays.stream(input.split("\n"))
                .map(HailstonePath::parse)
                .toList();
    }

    public long intersectionsIn(Vector from, Vector to) {
        long count = 0;
        for (int i = 0; i < hailstones.size(); i++) {
            for (int j = i + 1; j < hailstones.size(); j++) {
                HailstonePath hailstoneA = hailstones.get(i);
                HailstonePath hailstoneB = hailstones.get(j);
                count += hailstoneA.intersectionWith(hailstoneB)
                        .filter(intersection ->
                                intersection.x >= from.x && intersection.x <= to.x
                                && intersection.y >= from.y && intersection.y <= to.y
                        )
                        .stream()
                        .count();
            }
        }
        return count;
    }

    private HailstonePath calculateInitialStoneThrow() {
        var smallestOffsets = new ArrayList<PositionAndTime>();
        for (int i = 0; i < hailstones.size(); i++) {
            for (int j = i + 1; j < hailstones.size(); j++) {
                PositionAndTime offsetAndTime = hailstones.get(i).smallestOffset(hailstones.get(j));
                double time = offsetAndTime.time;
                var midpoint = hailstones.get(j).position.add(hailstones.get(j).velocity.times(time)).add(offsetAndTime.position.times(0.5));
                smallestOffsets.add(new PositionAndTime(midpoint, time));
            }
        }
        return calculateOptimizedThrow(smallestOffsets);
    }

    public long sumOfCoordinatesOfOptimalStartPositionForStoneThrow() {
        Vector position = calculateOptimalStoneThrow().position;
        return (long) (position.x + position.y + position.z);
    }

    public HailstonePath calculateOptimalStoneThrow() {
        HailstonePath stoneThrow = calculateInitialStoneThrow();
        double bestScore = Double.MAX_VALUE;
        HailstonePath bestThrow = stoneThrow;
        for (int i = 0; i < 1_000; i++) {
            var scoreAndCorrectedPath = calculateScoreAndCorrectedPath(stoneThrow);
            if (scoreAndCorrectedPath.score == 0) {
                return stoneThrow;
            }
            if (scoreAndCorrectedPath.score < bestScore) {
                bestScore = scoreAndCorrectedPath.score;
                bestThrow = stoneThrow;
                stoneThrow = scoreAndCorrectedPath.corrected;
            } else {
                // try rounding the values
                stoneThrow = new HailstonePath(scoreAndCorrectedPath.corrected.position.round(), scoreAndCorrectedPath.corrected.velocity.round());
            }
        }
        return bestThrow;
    }

    private ScoreAndCorrectedPath calculateScoreAndCorrectedPath(HailstonePath stoneThrow) {
        var closestPointsOnHailstonePathsAndTime = new ArrayList<PositionAndTime>();
        var offsets = new ArrayList<Vector>();
        for (var hailstone : hailstones) {
            PositionAndTime offsetAndTime = stoneThrow.smallestOffset(hailstone);
            offsets.add(offsetAndTime.position);
            double time = offsetAndTime.time;
            var midpoint = hailstone.position.add(hailstone.velocity.times(time));
            closestPointsOnHailstonePathsAndTime.add(new PositionAndTime(midpoint, time));
        }
        var score = offsets.stream().mapToDouble(Vector::length).sum();
        var optimizedThrow = calculateOptimizedThrow(closestPointsOnHailstonePathsAndTime);
        return new ScoreAndCorrectedPath(score, optimizedThrow);
    }

    private static HailstonePath calculateOptimizedThrow(ArrayList<PositionAndTime> positionsAndTime) {
        var center = positionsAndTime.stream()
                .reduce(new PositionAndTime(new Vector(0, 0, 0), 0), PositionAndTime::add)
                .times(1d / positionsAndTime.size());
        var startDirection = positionsAndTime.stream()
                .map(positionAndTime -> positionAndTime.position.minus(center.position).times(1d / (positionAndTime.time - center.time)))
                .reduce(new Vector(0, 0, 0), Vector::add)
                .times(1d / positionsAndTime.size());
        var startPosition = center.position.minus(startDirection.times(center.time));
        return new HailstonePath(startPosition, startDirection);
    }

    public record ScoreAndCorrectedPath(double score, HailstonePath corrected) {
    }

    public record PositionAndTime(Vector position, double time) {
        public PositionAndTime add(PositionAndTime other) {
            return new PositionAndTime(position.add(other.position), time + other.time);
        }

        public PositionAndTime times(double scale) {
            return new PositionAndTime(position.times(scale), time * scale);
        }
    }

    public record HailstonePath(Vector position, Vector velocity) {

        public PositionAndTime smallestOffset(HailstonePath other) {
            var highTime = Math.pow(2, 40);
            var lowTime = 0d;
            while (highTime - lowTime >= 2) {
                var highOffset = position.add(velocity.times(highTime)).minus(other.position.add(other.velocity.times(highTime)));
                var lowOffset = position.add(velocity.times(lowTime)).minus(other.position.add(other.velocity.times(lowTime)));
                var midTime = lowTime + (highTime - lowTime) / 2;
                if (highOffset.length() > lowOffset.length()) {
                    highTime = midTime;
                } else {
                    lowTime = midTime;
                }
            }
            var highOffset = position.add(velocity.times(highTime)).minus(other.position.add(other.velocity.times(highTime)));
            var lowOffset = position.add(velocity.times(lowTime)).minus(other.position.add(other.velocity.times(lowTime)));
            if (highOffset.length() > lowOffset.length()) {
                return new PositionAndTime(lowOffset, lowTime);
            } else {
                return new PositionAndTime(highOffset, highTime);
            }
        }

        public Optional<Vector> intersectionWith(HailstonePath other) {
            Optional<Double> t2 = Optional.empty();
            if (velocity.x == 0) {
                t2 = Optional.of((position.x - other.position.x) / other.velocity.x);
            } else {
                //(p1.y - p2.y + (v1.y / v1.x) * (p2.x - p1.x)) / (v2.y - (v1.y / v1.x) * v2.x) = t2
                double velocityFraction = velocity.y / velocity.x;
                double divisor = other.velocity.y - velocityFraction * other.velocity.x;
                if (divisor != 0) {
                    t2 = Optional.of((position.y - other.position.y + velocityFraction * (other.position.x - position.x)) / divisor);
                }
            }
            return t2.filter(time2 -> time2 >= 0)
                    .filter(time2 -> /* time1 = */
                            (velocity.x != 0
                                    ? (other.position.x - position.x + other.velocity.x * time2) / velocity.x
                                    : (other.position.y - position.y + other.velocity.y * time2) / velocity.y
                            ) > 0)
                    .map(time2 -> other.position.add(other.velocity.times(time2)));
        }

        public static HailstonePath parse(String input) {
            String[] positionAndVelocity = input.split("@");
            return new HailstonePath(Vector.parse(positionAndVelocity[0].trim()), Vector.parse(positionAndVelocity[1].trim()));
        }
    }

    public record Vector(double x, double y, double z) {
        public static Vector parse(String input) {
            String[] xyz = input.split(",");
            return new Vector(
                    Long.parseLong(xyz[0].trim()),
                    Long.parseLong(xyz[1].trim()),
                    Long.parseLong(xyz[2].trim())
            );
        }

        public Vector times(double scalar) {
            return new Vector(x * scalar, y * scalar, z * scalar);
        }

        public Vector add(Vector other) {
            return new Vector(x + other.x, y + other.y, z + other.z);
        }

        public Vector minus(Vector other) {
            return add(other.times(-1));
        }

        public Double length() {
            return Math.sqrt(x * x + y * y + z * z);
        }

        public Vector round() {
            return new Vector(Math.round(x), Math.round(y), Math.round(z));
        }
    }
}

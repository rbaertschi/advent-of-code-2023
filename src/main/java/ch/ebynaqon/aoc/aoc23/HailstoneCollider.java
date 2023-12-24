package ch.ebynaqon.aoc.aoc23;

import java.util.Arrays;
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

    public record HailstonePath(Vector position, Vector velocity) {
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
    }
}

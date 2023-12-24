package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.HailstoneCollider.HailstonePath;
import ch.ebynaqon.aoc.aoc23.HailstoneCollider.Vector;
import ch.ebynaqon.aoc.aoc23.helper.TestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

class HailstoneColliderTest {

    @ParameterizedTest
    @MethodSource("hailstonesAndIntersection")
    void pathIntersection(HailstonePath a, HailstonePath b, Optional<Vector> expected) {
        var actual = a.intersectionWith(b);

        assertThat(actual.isPresent()).isEqualTo(expected.isPresent());
        expected.ifPresent(expectedVector -> {
            assertThat(actual.get().x()).isEqualTo(expected.get().x(), offset(0.001));
            assertThat(actual.get().y()).isEqualTo(expected.get().y(), offset(0.001));
        });
    }

    public static Stream<Arguments> hailstonesAndIntersection() {
        return Stream.of(
                Arguments.of(
                        /*
                        Hailstone A: 19, 13, 30 @ -2, 1, -2
                        Hailstone B: 18, 19, 22 @ -1, -1, -2
                        Hailstones' paths will cross inside the test area (at x=14.333, y=15.333).
                        */
                        new HailstonePath(new Vector(19, 13, 30), new Vector(-2, 1, -2)),
                        new HailstonePath(new Vector(18, 19, 22), new Vector(-1, -1, -2)),
                        Optional.of(new Vector(14.333, 15.333, 0))
                ),
                Arguments.of(
                        /*
                        Hailstone A: 19, 13, 30 @ -2, 1, -2
                        Hailstone B: 20, 25, 34 @ -2, -2, -4
                        Hailstones' paths will cross inside the test area (at x=11.667, y=16.667).
                        */
                        new HailstonePath(new Vector(19, 13, 30), new Vector(-2, 1, -2)),
                        new HailstonePath(new Vector(20, 25, 34), new Vector(-2, -2, -4)),
                        Optional.of(new Vector(11.667, 16.667, 0))
                ),
                Arguments.of(
                        /*
                        Hailstone A: 19, 13, 30 @ -2, 1, -2
                        Hailstone B: 12, 31, 28 @ -1, -2, -1
                        Hailstones' paths will cross outside the test area (at x=6.2, y=19.4).
                        */
                        new HailstonePath(new Vector(19, 13, 30), new Vector(-2, 1, -2)),
                        new HailstonePath(new Vector(12, 31, 28), new Vector(-1, -2, -1)),
                        Optional.of(new Vector(6.2, 19.4, 0))
                ),
                Arguments.of(
                        /*
                        Hailstone A: 19, 13, 30 @ -2, 1, -2
                        Hailstone B: 20, 19, 15 @ 1, -5, -3
                        Hailstones' paths crossed in the past for hailstone A.
                        */
                        new HailstonePath(new Vector(19, 13, 30), new Vector(-2, 1, -2)),
                        new HailstonePath(new Vector(20, 19, 15), new Vector(1, -5, -3)),
                        Optional.empty()
                ),
                Arguments.of(
                        /*
                        Hailstone A: 18, 19, 22 @ -1, -1, -2
                        Hailstone B: 20, 25, 34 @ -2, -2, -4
                        Hailstones' paths are parallel; they never intersect.
                        */
                        new HailstonePath(new Vector(18, 19, 22), new Vector(-1, -1, -2)),
                        new HailstonePath(new Vector(20, 25, 34), new Vector(-2, -2, -4)),
                        Optional.empty()
                ),
                Arguments.of(
                        /*
                        Hailstone A: 18, 19, 22 @ -1, -1, -2
                        Hailstone B: 20, 25, 34 @ -2, -2, -4
                        Hailstones' paths are parallel; they never intersect.
                        */
                        new HailstonePath(new Vector(18, 19, 22), new Vector(-1, -1, -2)),
                        new HailstonePath(new Vector(20, 25, 34), new Vector(-2, -2, -4)),
                        Optional.empty()
                ),
                Arguments.of(
                        /*
                        Hailstone A: 18, 19, 22 @ -1, -1, -2
                        Hailstone B: 20, 19, 15 @ 1, -5, -3
                        Hailstones' paths crossed in the past for both hailstones.
                        */
                        new HailstonePath(new Vector(18, 19, 22), new Vector(-1, -1, -2)),
                        new HailstonePath(new Vector(20, 19, 15), new Vector(1, -5, -3)),
                        Optional.empty()
                ),
                Arguments.of(
                        /*
                        Hailstone A: 18, 19, 22 @ -1, -1, -2
                        Hailstone B: 12, 31, 28 @ -1, -2, -1
                        Hailstones' paths will cross outside the test area (at x=-6, y=-5).
                        */
                        new HailstonePath(new Vector(18, 19, 22), new Vector(-1, -1, -2)),
                        new HailstonePath(new Vector(12, 31, 28), new Vector(-1, -2, -1)),
                        Optional.of(new Vector(-6, -5, 0))
                )
        );
        /*
        Hailstone A: 20, 25, 34 @ -2, -2, -4
        Hailstone B: 12, 31, 28 @ -1, -2, -1
        Hailstones' paths will cross outside the test area (at x=-2, y=3).

        Hailstone A: 20, 25, 34 @ -2, -2, -4
        Hailstone B: 20, 19, 15 @ 1, -5, -3
        Hailstones' paths crossed in the past for hailstone B.

        Hailstone A: 12, 31, 28 @ -1, -2, -1
        Hailstone B: 20, 19, 15 @ 1, -5, -3
        Hailstones' paths crossed in the past for both hailstones.
        */
    }

    @Test
    void findIntersectionsInSearchAreaForExample() {
        var input = """
                19, 13, 30 @ -2,  1, -2
                18, 19, 22 @ -1, -1, -2
                20, 25, 34 @ -2, -2, -4
                12, 31, 28 @ -1, -2, -1
                20, 19, 15 @  1, -5, -3
                """.trim();

        var actual = new HailstoneCollider(input)
                .intersectionsIn(
                        new Vector(7, 7, 0),
                        new Vector(27, 27, 0)
                );

        assertThat(actual).isEqualTo(2);
    }

    @Test
    void findIntersectionsInSearchAreaForPart1() {
        var input = TestHelper.readInput("/day24-hailstones.txt").trim();

        var actual = new HailstoneCollider(input)
                .intersectionsIn(
                        new Vector(200000000000000L, 200000000000000L, 0),
                        new Vector(400000000000000L, 400000000000000L, 0)
                );

        assertThat(actual).isEqualTo(19976);
    }
}

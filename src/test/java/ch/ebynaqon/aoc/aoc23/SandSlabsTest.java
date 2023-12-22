package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.SandSlabs.Brick;
import ch.ebynaqon.aoc.aoc23.SandSlabs.Point;
import ch.ebynaqon.aoc.aoc23.SandSlabs.RestingBrick;
import ch.ebynaqon.aoc.aoc23.helper.TestHelper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static ch.ebynaqon.aoc.aoc23.SandSlabs.GROUND;
import static org.assertj.core.api.Assertions.assertThat;

class SandSlabsTest {
    @Test
    void parse() {
        var input = """
                1,0,1~1,2,1
                0,0,2~2,0,2
                0,2,3~2,2,3
                """.trim();

        var actual = SandSlabs.parse(input);

        assertThat(actual).isEqualTo(List.of(
                new Brick(new Point(1, 0, 1), new Point(1, 2, 1)),
                new Brick(new Point(0, 0, 2), new Point(2, 0, 2)),
                new Brick(new Point(0, 2, 3), new Point(2, 2, 3))
        ));
    }
    @Test
    void bricksAtRest() {
        var input = List.of(
                new Brick(new Point(1, 1, 1), new Point(1, 1, 1)),
                new Brick(new Point(1, 1, 10), new Point(1, 1, 10))
        );
        var actual = SandSlabs.afterFall(input);

        assertThat(actual).isEqualTo(List.of(
                new RestingBrick(new Point(1, 1, 1), new Point(1, 1, 1), Set.of(GROUND), Set.of(actual.get(1))),
                new RestingBrick(new Point(1, 1, 2), new Point(1, 1, 2), Set.of(actual.getFirst()), Set.of())
        ));
    }

    @Test
    void countBricksToDisintegrateForExample() {
        var input = """
                1,0,1~1,2,1
                0,0,2~2,0,2
                0,2,3~2,2,3
                0,0,4~0,2,4
                2,0,5~2,2,5
                0,1,6~2,1,6
                1,1,8~1,1,9
                """.trim();

        var actual = SandSlabs.countBricksSafeToDisintegrate(input);

        assertThat(actual).isEqualTo(5);
    }

    @Test
    void countBricksToDisintegrateForPart1() {
        var input = TestHelper.readInput("/day22-sand-slabs.txt").trim();

        var actual = SandSlabs.countBricksSafeToDisintegrate(input);

        assertThat(actual).isEqualTo(411);
    }
}

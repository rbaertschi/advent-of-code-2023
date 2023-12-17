package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.ClumsyCrucible.Position;
import ch.ebynaqon.aoc.aoc23.helper.TestHelper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClumsyCrucibleTest {
    @Test
    void parse() {
        var input = """
                241
                321
                325
                """.trim();

        var actual = new ClumsyCrucible(input);

        assertThat(actual.heatLossAt(new Position(0, 0))).isEqualTo(2);
        assertThat(actual.heatLossAt(new Position(0, 1))).isEqualTo(4);
        assertThat(actual.heatLossAt(new Position(0, 2))).isEqualTo(1);
        assertThat(actual.heatLossAt(new Position(1, 0))).isEqualTo(3);
        assertThat(actual.heatLossAt(new Position(1, 1))).isEqualTo(2);
        assertThat(actual.heatLossAt(new Position(1, 2))).isEqualTo(1);
        assertThat(actual.heatLossAt(new Position(2, 0))).isEqualTo(3);
        assertThat(actual.heatLossAt(new Position(2, 1))).isEqualTo(2);
        assertThat(actual.heatLossAt(new Position(2, 2))).isEqualTo(5);
    }

    @Test
    void leastHeatLoss() {
        var input = """
                241
                321
                325
                """.trim();

        var actual = new ClumsyCrucible(input).leastHeatLoss(1, 3);

        assertThat(actual).isEqualTo(11);
    }

    @Test
    void leastHeatLossForExample() {
        var input = """
                2413432311323
                3215453535623
                3255245654254
                3446585845452
                4546657867536
                1438598798454
                4457876987766
                3637877979653
                4654967986887
                4564679986453
                1224686865563
                2546548887735
                4322674655533
                """.trim();

        var actual = new ClumsyCrucible(input).leastHeatLoss(1, 3);

        assertThat(actual).isEqualTo(102);
    }

    @Test
    void leastHeatLossWithUltraCrucibleForExample() {
        var input = """
                2413432311323
                3215453535623
                3255245654254
                3446585845452
                4546657867536
                1438598798454
                4457876987766
                3637877979653
                4654967986887
                4564679986453
                1224686865563
                2546548887735
                4322674655533
                """.trim();

        var actual = new ClumsyCrucible(input).leastHeatLoss(4, 10);

        assertThat(actual).isEqualTo(94);
    }

    @Test
    void leastHeatLossForPart1() {
        var input = TestHelper.readInput("/day17-clumsy-crucible.txt").trim();

        var actual = new ClumsyCrucible(input).leastHeatLoss(1, 3);

        assertThat(actual).isEqualTo(866);
    }

    @Test
    void leastHeatLossForPart2() {
        var input = TestHelper.readInput("/day17-clumsy-crucible.txt").trim();

        var actual = new ClumsyCrucible(input).leastHeatLoss(4, 10);

        assertThat(actual).isEqualTo(1010);
    }

}

package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.FloorWillBeLava.*;
import ch.ebynaqon.aoc.aoc23.helper.TestHelper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static ch.ebynaqon.aoc.aoc23.FloorWillBeLava.Direction.*;
import static org.assertj.core.api.Assertions.assertThat;

class FloorWillBeLavaTest {
    @Test
    void emptyTileLetsLightStraightThrough() {
        EmptyTile testObj = new EmptyTile();
        assertThat(testObj.outgoingFrom(LEFT)).isEqualTo(List.of(LEFT));
        assertThat(testObj.outgoingFrom(RIGHT)).isEqualTo(List.of(RIGHT));
        assertThat(testObj.outgoingFrom(UP)).isEqualTo(List.of(UP));
        assertThat(testObj.outgoingFrom(DOWN)).isEqualTo(List.of(DOWN));
    }

    @Test
    void emptyTileBlocksLightFromDirectionAfterOnePass() {
        EmptyTile testObj = new EmptyTile();
        assertThat(testObj.outgoingFrom(LEFT)).isEqualTo(List.of(LEFT));
        assertThat(testObj.outgoingFrom(LEFT)).isEqualTo(List.of());
    }

    @Test
    void parse() {
        var input = """
                .\\/
                -.|
                """.trim();

        var actual = FloorWillBeLava.parse(input);

        assertThat(actual).isEqualTo(new FloorWillBeLava(
                List.of(
                        List.of(new EmptyTile(), new DiagonalLeftMirror(), new DiagonalRightMirror()),
                        List.of(new HorizontalSplitter(), new EmptyTile(), new VerticalSplitter())
                )
        ));
    }

    @Test
    void countEnergizedTilesForExample() {
        var input = """
                .|...\\....
                |.-.\\.....
                .....|-...
                ........|.
                ..........
                .........\\
                ..../.\\\\..
                .-.-/..|..
                .|....-|.\\
                ..//.|....
                """.trim();

        var actual = FloorWillBeLava.parse(input).countEnergizedTiles(new Position(0, 0), RIGHT);

        assertThat(actual).isEqualTo(46);
    }

    @Test
    void getMaxEnergizedTilesForExample() {
        var input = """
                .|...\\....
                |.-.\\.....
                .....|-...
                ........|.
                ..........
                .........\\
                ..../.\\\\..
                .-.-/..|..
                .|....-|.\\
                ..//.|....
                """.trim();

        var actual = FloorWillBeLava.parse(input).getMaxEnergizedTiles();

        assertThat(actual).isEqualTo(51);
    }

    @Test
    void countEnergizedTilesForPart1() {
        var input = TestHelper.readInput("/day16-laser-grid.txt").trim();

        var actual = FloorWillBeLava.parse(input).countEnergizedTiles(new Position(0, 0), RIGHT);

        assertThat(actual).isEqualTo(7951);
    }

    @Test
    void getMaxEnergizedTilesForPart2() {
        var input = TestHelper.readInput("/day16-laser-grid.txt").trim();

        var actual = FloorWillBeLava.parse(input).getMaxEnergizedTiles();

        assertThat(actual).isEqualTo(8148);
    }
}

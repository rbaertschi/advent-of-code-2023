package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.SchemaDecoder.Line;
import ch.ebynaqon.aoc.aoc23.SchemaDecoder.Number;
import ch.ebynaqon.aoc.aoc23.SchemaDecoder.Symbol;
import ch.ebynaqon.aoc.aoc23.helper.TestHelper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SchemaDecoderTest {
    @Test
    void lineWithOnlyNumbers() {
        var line = "467..114..";

        var actual = SchemaDecoder.parse(line);

        assertThat(actual).isEqualTo(new Line(List.of(
                new Number(467, 0, 2),
                new Number(114, 5, 7)
        ), List.of()));
    }

    @Test
    void lineWithNumberAndSymbol() {
        var line = "617*......";

        var actual = SchemaDecoder.parse(line);

        assertThat(actual).isEqualTo(new Line(List.of(
                new Number(617, 0, 2)
        ), List.of(new Symbol(3, "*", Set.of()))));
    }

    @Test
    void findPartNumbers() {
        var input = """
                467..114..
                ...*......
                ..35..633.
                ......#...
                617*......
                .....+.58.
                ..592.....
                ......755.
                ...$.*....
                .664.598..
                """.trim();

        var actual = new SchemaDecoder(input).findPartNumbers();

        assertThat(actual).isEqualTo(List.of(467, 35, 633, 617, 592, 755, 664, 598));
    }

    @Test
    void findGearRatio() {
        var input = """
                467..114..
                ...*......
                ..35..633.
                ......#...
                617*......
                .....+.58.
                ..592.....
                ......755.
                ...$.*....
                .664.598..
                """.trim();

        var actual = new SchemaDecoder(input).sumOfGearRatios();

        assertThat(actual).isEqualTo(467835);
    }

    @Test
    void sumOfPartNumbersForExample() {
        var input = """
                467..114..
                ...*......
                ..35..633.
                ......#...
                617*......
                .....+.58.
                ..592.....
                ......755.
                ...$.*....
                .664.598..
                """.trim();

        var actual = new SchemaDecoder(input).sumOfPartNumbers();

        assertThat(actual).isEqualTo(4361);
    }

    @Test
    void sumOfPartNumbersForPuzzleInputPart1() {
        var input = TestHelper.readInput("/day3-schematic-of-part-numbers.txt").trim();

        var actual = new SchemaDecoder(input).sumOfPartNumbers();

        assertThat(actual).isEqualTo(529618);
    }

    @Test
    void sumOfGearRatiosForPuzzleInputPart2() {
        var input = TestHelper.readInput("/day3-schematic-of-part-numbers.txt").trim();

        var actual = new SchemaDecoder(input).sumOfGearRatios();

        assertThat(actual).isEqualTo(77509019);
    }
}

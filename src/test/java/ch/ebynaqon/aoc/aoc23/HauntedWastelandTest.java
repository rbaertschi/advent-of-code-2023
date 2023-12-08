package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.HauntedWasteland.Instruction;
import ch.ebynaqon.aoc.aoc23.helper.TestHelper;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ch.ebynaqon.aoc.aoc23.HauntedWasteland.Direction.LEFT;
import static ch.ebynaqon.aoc.aoc23.HauntedWasteland.Direction.RIGHT;
import static org.assertj.core.api.Assertions.assertThat;

class HauntedWastelandTest {

    public static final String EXAMPLE1 = """
            RL
                            
            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)
            """;

    @Test
    void parseExample1() {
        var input = EXAMPLE1.trim();

        var actual = HauntedWasteland.parse(input);

        assertThat(actual).isEqualTo(
                new HauntedWasteland.MapWithInstructions(
                        List.of(RIGHT, LEFT),
                        new HashMap<>(Map.of(
                                "AAA", new Instruction("BBB", "CCC"),
                                "BBB", new Instruction("DDD", "EEE"),
                                "CCC", new Instruction("ZZZ", "GGG"),
                                "DDD", new Instruction("DDD", "DDD"),
                                "EEE", new Instruction("EEE", "EEE"),
                                "GGG", new Instruction("GGG", "GGG"),
                                "ZZZ", new Instruction("ZZZ", "ZZZ")
                        ))
                )
        );
    }

    @Test
    void solveExample1() {
        var input = EXAMPLE1.trim();

        var actual = HauntedWasteland.solve(input);

        assertThat(actual).isEqualTo(2);
    }

    @Test
    void solveGhostExample() {
        var input = """
                LR
                                
                11A = (11B, XXX)
                11B = (XXX, 11Z)
                11Z = (11B, XXX)
                22A = (22B, XXX)
                22B = (22C, 22C)
                22C = (22Z, 22Z)
                22Z = (22B, 22B)
                XXX = (XXX, XXX)
                """.trim();

        var actual = HauntedWasteland.solveGhostly(input);

        assertThat(actual).isEqualTo(6);
    }

    @Test
    void solvePart1() {
        var input = TestHelper.readInput("/day8-haunted-wasteland.txt");

        var actual = HauntedWasteland.solve(input);

        assertThat(actual).isEqualTo(21797);
    }

    @Test
    void solveGhostlyPart2() {
        var input = TestHelper.readInput("/day8-haunted-wasteland.txt");

        var actual = HauntedWasteland.solveGhostly(input);

        assertThat(actual).isEqualTo(23977527174353L);
    }
}

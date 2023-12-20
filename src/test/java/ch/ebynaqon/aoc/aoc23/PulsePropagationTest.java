package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.PulsePropagation.Broadcaster;
import ch.ebynaqon.aoc.aoc23.PulsePropagation.Conjunction;
import ch.ebynaqon.aoc.aoc23.PulsePropagation.FlipFlop;
import ch.ebynaqon.aoc.aoc23.PulsePropagation.Pulse;
import ch.ebynaqon.aoc.aoc23.PulsePropagation.PulseType;
import ch.ebynaqon.aoc.aoc23.helper.TestHelper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PulsePropagationTest {
    @Test
    void parse() {
        var input = """
                broadcaster -> a, b, c
                %a -> b
                %b -> c
                %c -> inv
                &inv -> a
                """.trim();

        var actual = PulsePropagation.parse(input);

        assertThat(actual).isEqualTo(new PulsePropagation(
                List.of(
                        new Broadcaster("broadcaster", List.of("a", "b", "c")),
                        new FlipFlop("a", List.of("b")),
                        new FlipFlop("b", List.of("c")),
                        new FlipFlop("c", List.of("inv")),
                        new Conjunction("inv", List.of("a"))
                )
        ));
    }

    @Test
    void highPulseToFlipFlopYieldsNoPulse() {
        var testObj = new FlipFlop("a", List.of("b"));
        var pulse = new Pulse(PulseType.HIGH, "broadcaster", "a");

        var actual = testObj.apply(pulse);

        assertThat(actual).isEqualTo(List.of());
    }

    @Test
    void lowPulseToFlipFlopYieldsAlternatingHighAndLowPulse() {
        var testObj = new FlipFlop("a", List.of("b"));
        var pulse = new Pulse(PulseType.LOW, "broadcaster", "a");

        assertThat(testObj.apply(pulse)).isEqualTo(List.of(new Pulse(PulseType.HIGH, "a", "b")));
        assertThat(testObj.apply(pulse)).isEqualTo(List.of(new Pulse(PulseType.LOW, "a", "b")));
        assertThat(testObj.apply(pulse)).isEqualTo(List.of(new Pulse(PulseType.HIGH, "a", "b")));
        assertThat(testObj.apply(pulse)).isEqualTo(List.of(new Pulse(PulseType.LOW, "a", "b")));
    }

    @Test
    void conjunction() {
        var testObj = new Conjunction("c", List.of("d"));
        testObj.setInputModuleNames(List.of("a", "b"));

        // updates 'a' to high but still remembers 'b' low -> sends high
        assertThat(testObj.apply(new Pulse(PulseType.HIGH, "a", "c")))
                .isEqualTo(List.of(
                        new Pulse(PulseType.HIGH, "c", "d")
                ));

        // updates 'b' to high and thus remembers all as high -> sends low
        assertThat(testObj.apply(new Pulse(PulseType.HIGH, "b", "c")))
                .isEqualTo(List.of(
                        new Pulse(PulseType.LOW, "c", "d")
                ));

        // updates 'a' to low so now not all are high -> sends low
        assertThat(testObj.apply(new Pulse(PulseType.LOW, "a", "c")))
                .isEqualTo(List.of(
                        new Pulse(PulseType.HIGH, "c", "d")
                ));
    }

    @Test
    void getPulseScoreAfter1000ButtonPressesForFirstExample() {
        var input = """
                broadcaster -> a, b, c
                %a -> b
                %b -> c
                %c -> inv
                &inv -> a
                """.trim();

        var actual = PulsePropagation.parse(input).getPulseScoreAfter1000ButtonPresses();

        assertThat(actual).isEqualTo(32000000);
    }

    @Test
    void getPulseScoreAfter1000ButtonPressesForSecondExample() {
        var input = """
                broadcaster -> a
                %a -> inv, con
                &inv -> b
                %b -> con
                &con -> output
                """.trim();

        var actual = PulsePropagation.parse(input).getPulseScoreAfter1000ButtonPresses();

        assertThat(actual).isEqualTo(11687500);
    }

    @Test
    void getPulseScoreAfter1000ButtonPressesForPart1() {
        var input = TestHelper.readInput("/day20-pulse-propagation.txt").trim();

        var actual = PulsePropagation.parse(input).getPulseScoreAfter1000ButtonPresses();

        assertThat(actual).isEqualTo(712543680);
    }

}

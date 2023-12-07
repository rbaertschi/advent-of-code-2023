package ch.ebynaqon.adventofcode23;

import ch.ebynaqon.adventofcode23.CamelCards.Hand;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static ch.ebynaqon.adventofcode23.CamelCards.Hand.ONE_PAIR;
import static org.assertj.core.api.Assertions.assertThat;

class CamelCardsTest {
    @Test
    void parseExampleInput() {
        var input = """
                32T3K 765
                T55J5 684
                KK677 28
                KTJJT 220
                QQQJA 483
                """.trim();

        var actual = CamelCards.parse(input);

        assertThat(actual).isEqualTo(List.of(
                new Hand("32T3K", 765),
                new Hand("T55J5", 684),
                new Hand("KK677", 28),
                new Hand("KTJJT", 220),
                new Hand("QQQJA", 483)
        ));
    }

    @Test
    void scoreIndividualCards() {
        int actual = new Hand("32T3K", 0).scoreCards();
        assertThat(actual).isEqualTo(0x2192c);
    }

    @Test
    void scoreKind() {
        int actual = new Hand("32T3K", 0).scoreKind();
        assertThat(actual).isEqualTo(ONE_PAIR);
    }
    @Test
    void scoreHand() {
        int actual = new Hand("32T3K", 0).score();
        assertThat(actual).isEqualTo(0x2192c + ONE_PAIR);
    }

    @Test
    void solveExampleInput() {
        var input = """
                32T3K 765
                T55J5 684
                KK677 28
                KTJJT 220
                QQQJA 483
                """.trim();

        var actual = CamelCards.solve(input);

        assertThat(actual).isEqualTo(6440);
    }

    @Test
    void solvePuzzleInputPart1() throws IOException, URISyntaxException {
        var input = TestHelper.readInput("/day7-camel-cards.txt").trim();

        var actual = CamelCards.solve(input);

        assertThat(actual).isEqualTo(248105065);
    }
}

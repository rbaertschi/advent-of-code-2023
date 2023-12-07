package ch.ebynaqon.aoc.aoc23;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

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
                new CamelCards.Hand("32T3K", 765),
                new CamelCards.Hand("T55J5", 684),
                new CamelCards.Hand("KK677", 28),
                new CamelCards.Hand("KTJJT", 220),
                new CamelCards.Hand("QQQJA", 483)
        ));
    }

    @Test
    void scoreIndividualCards() {
        long actual = new CamelCards.Hand("32T3K", 0).scoreCards();
        assertThat(actual).isEqualTo(0x32a3d);
    }

    @Test
    void scoreKind() {
        long actual = new CamelCards.Hand("32T3K", 0).scoreKind();
        assertThat(actual).isEqualTo(CamelCards.Hand.ONE_PAIR);
    }

    @Test
    void scoreHand() {
        long actual = new CamelCards.Hand("32T3K", 0).score();
        assertThat(actual).isEqualTo(0x32a3d + CamelCards.Hand.ONE_PAIR);
    }

    @Test
    void scoreHandWithJoker() {
        long actual = new CamelCards.Hand("32?3K", 0).score();
        assertThat(actual).isEqualTo(0x3213d + CamelCards.Hand.THREE_OF_A_KIND);
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
    void solveExampleInputWithJoker() {
        var input = """
                32T3K 765
                T55J5 684
                KK677 28
                KTJJT 220
                QQQJA 483
                """.trim().replaceAll("J", "?");

        var actual = CamelCards.solve(input);

        assertThat(actual).isEqualTo(5905);
    }

    @Test
    void solvePuzzleInputPart1() {
        var input = TestHelper.readInput("/day7-camel-cards.txt").trim();

        var actual = CamelCards.solve(input);

        assertThat(actual).isEqualTo(248105065);
    }

    @Test
    void solvePuzzleInputPart2() {
        var input = TestHelper.readInput("/day7-camel-cards.txt").trim().replaceAll("J", "?");

        var actual = CamelCards.solve(input);

        assertThat(actual).isEqualTo(249515436);
    }
}

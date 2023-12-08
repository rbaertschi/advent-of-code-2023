package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.helper.TestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

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

    @ParameterizedTest
    @MethodSource("handsAndKindScores")
    void scoreKind(String cards, long kindScore) {
        long actual = new CamelCards.Hand(cards, 0).scoreKind();
        assertThat(actual).isEqualTo(kindScore);
    }

    public static Stream<Arguments> handsAndKindScores() {
        return Stream.of(
                // five of a kind
                Arguments.of("77777", CamelCards.Hand.FIVE_OF_A_KIND),
                Arguments.of("777?7", CamelCards.Hand.FIVE_OF_A_KIND),
                Arguments.of("?77?7", CamelCards.Hand.FIVE_OF_A_KIND),
                Arguments.of("?7??7", CamelCards.Hand.FIVE_OF_A_KIND),
                Arguments.of("????7", CamelCards.Hand.FIVE_OF_A_KIND),
                Arguments.of("?????", CamelCards.Hand.FIVE_OF_A_KIND),
                // four of a kind
                Arguments.of("77771", CamelCards.Hand.FOUR_OF_A_KIND),
                Arguments.of("777?1", CamelCards.Hand.FOUR_OF_A_KIND),
                Arguments.of("?77?1", CamelCards.Hand.FOUR_OF_A_KIND),
                Arguments.of("?7??1", CamelCards.Hand.FOUR_OF_A_KIND),
                // full house
                Arguments.of("32323", CamelCards.Hand.FULL_HOUSE),
                Arguments.of("32?23", CamelCards.Hand.FULL_HOUSE),
                // three of a kind
                Arguments.of("32T33", CamelCards.Hand.THREE_OF_A_KIND),
                Arguments.of("32?3T", CamelCards.Hand.THREE_OF_A_KIND),
                Arguments.of("?2T33", CamelCards.Hand.THREE_OF_A_KIND),
                Arguments.of("32T??", CamelCards.Hand.THREE_OF_A_KIND),
                // two pairs
                Arguments.of("32T3T", CamelCards.Hand.TWO_PAIRS),
                // one pair
                Arguments.of("32T3K", CamelCards.Hand.ONE_PAIR),
                Arguments.of("32T?K", CamelCards.Hand.ONE_PAIR),
                // individual cards
                Arguments.of("93671", CamelCards.Hand.INDIVIDUAL_CARD)
        );
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

    @Test
    void name() {
        new CamelCards.Hand("ABCBAA", 0).scoreKind();
    }
}

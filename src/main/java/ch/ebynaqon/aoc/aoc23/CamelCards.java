package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.helper.CollectionHelper;
import ch.ebynaqon.aoc.aoc23.helper.StatisticsHelper;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CamelCards {
    public static List<Hand> parse(String input) {
        return Arrays.stream(input.split("\n"))
                .map(line -> line.split("\\s+"))
                .map(handAndBet -> new Hand(handAndBet[0], Integer.parseInt(handAndBet[1])))
                .toList();
    }

    public static long solve(String input) {
        return CollectionHelper.zipWithIndex(
                        parse(input).stream()
                                .sorted(Comparator.comparing(Hand::score))
                                .toList()
                ).stream()
                .mapToLong(idxVal -> (idxVal.index() + 1) * idxVal.value().bet())
                .sum();

    }

    public record Hand(String cards, long bet) {
        public static long INDIVIDUAL_CARD = 0x000000;
        public static long ONE_PAIR = 0x100000;
        public static long TWO_PAIRS = 0x200000;
        public static long THREE_OF_A_KIND = 0x300000;
        public static long FULL_HOUSE = 0x400000;
        public static long FOUR_OF_A_KIND = 0x500000;
        public static long FIVE_OF_A_KIND = 0x600000;

        private static long[] POSITION_WEIGHT = {0x10000, 0x1000, 0x100, 0x10, 0x1};

        /*
        6 - Five of a kind  - where all five cards have the same label: AAAAA
        5 - Four of a kind  - where four cards have the same label and one card has a different label: AA8AA
        4 - Full house      - where three cards have the same label, and the remaining two cards share a different label: 23332
        3 - Three of a kind - where three cards have the same label, and the remaining two cards are each different from any other card in the hand: TTT98
        2 - Two pair        - where two cards share one label, two other cards share a second label, and the remaining card has a third label: 23432
        1 - One pair        - where two cards share one label, and the other three cards have a different label from the pair and each other: A23A4
        0 - Single card
        * */
        public long score() {
            return scoreKind() + scoreCards();
        }

        public long scoreKind() {
            var histogram = StatisticsHelper.histogram(List.of(cards().split("")));
            var jokers = histogram.getOrDefault("?", 0);
            var counts = histogram.entrySet().stream()
                    .filter(e -> !"?".equals(e.getKey()))
                    .map(Map.Entry::getValue)
                    .sorted(Comparator.reverseOrder())
                    .toList();
            if (jokers == 5) {
                return FIVE_OF_A_KIND;
            }
            return switch (counts.getFirst()) {
                case 5 -> FIVE_OF_A_KIND;
                case 4 -> jokers == 1
                        ? FIVE_OF_A_KIND
                        : FOUR_OF_A_KIND;
                case 3 -> jokers == 2
                        ? FIVE_OF_A_KIND
                        : jokers == 1
                        ? FOUR_OF_A_KIND
                        : counts.get(1) == 2
                        ? FULL_HOUSE
                        : THREE_OF_A_KIND;
                case 2 -> jokers == 3
                        ? FIVE_OF_A_KIND
                        : jokers == 2
                        ? FOUR_OF_A_KIND
                        : jokers == 1 && counts.get(1) == 2
                        ? FULL_HOUSE
                        : jokers == 1
                        ? THREE_OF_A_KIND
                        : counts.get(1) == 2
                        ? TWO_PAIRS
                        : ONE_PAIR;
                default -> jokers == 4
                        ? FIVE_OF_A_KIND
                        : jokers == 3
                        ? FOUR_OF_A_KIND
                        : jokers == 2
                        ? THREE_OF_A_KIND
                        : jokers == 1
                        ? ONE_PAIR
                        : INDIVIDUAL_CARD;
            };
        }

        public long scoreCards() {
            return CollectionHelper.zipWithIndex(Arrays.asList(cards().split("")))
                    .stream()
                    .mapToLong(indexAndCard -> scoreCard(indexAndCard.value()) * POSITION_WEIGHT[indexAndCard.index()])
                    .sum();
        }

        private long scoreCard(String card) {
            return switch (card) {
                case "A" -> 0xe;
                case "K" -> 0xd;
                case "Q" -> 0xc;
                case "J" -> 0xb;
                case "T" -> 0xa;
                case "9" -> 0x9;
                case "8" -> 0x8;
                case "7" -> 0x7;
                case "6" -> 0x6;
                case "5" -> 0x5;
                case "4" -> 0x4;
                case "3" -> 0x3;
                case "2" -> 0x2;
                case "?" -> 0x1;
                default -> 0;
            };
        }
    }
}

package ch.ebynaqon.adventofcode23;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CamelCards {
    public static List<Hand> parse(String input) {
        return Arrays.stream(input.split("\n"))
                .map(line -> line.split("\\s+"))
                .map(handAndBet -> new Hand(handAndBet[0], Integer.parseInt(handAndBet[1])))
                .toList();
    }

    public static long solve(String input) {
        List<Hand> hands = parse(input).stream().sorted(Comparator.comparing(Hand::score)).toList();
        long sum = 0;
        for (int i = 0; i < hands.size(); i++) {
            sum += (i + 1) * hands.get(i).bet();
        }
        return sum;
    }

    public record Hand(String cards, long bet) {
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
        High card
        * */
        public long score() {
            return scoreKind() + scoreCards();
        }

        public long scoreKind() {
            var sortedCards = Arrays.stream((cards().split(""))).sorted().toList();
            var previousCard = "";
            var sameCardCount = 0;
            var pairs = 0;
            var threeOfAKind = 0;
            var fourOfAKind = 0;
            var fiveOfAKind = 0;
            var jokers = 0;
            for (var card : sortedCards) {
                if (card.equals("?")) {
                    jokers++;
                } else if (card.equals(previousCard)) {
                    sameCardCount++;
                } else {
                    if (sameCardCount == 2) {
                        pairs++;
                    } else if (sameCardCount == 3) {
                        threeOfAKind++;
                    } else if (sameCardCount == 4) {
                        fourOfAKind++;
                    } else if (sameCardCount == 5) {
                        fiveOfAKind++;
                    }
                    sameCardCount = 1;
                }
                previousCard = card;
            }
            if (sameCardCount == 2) {
                pairs++;
            } else if (sameCardCount == 3) {
                threeOfAKind++;
            } else if (sameCardCount == 4) {
                fourOfAKind++;
            } else if (sameCardCount == 5) {
                fiveOfAKind++;
            }
            if (fiveOfAKind == 1) {
                return FIVE_OF_A_KIND;
            }
            if (fourOfAKind == 1) {
                if (jokers == 1) return FIVE_OF_A_KIND;
                return FOUR_OF_A_KIND;
            }
            if (threeOfAKind == 1) {
                if (jokers == 2) return FIVE_OF_A_KIND;
                if (jokers == 1) return FOUR_OF_A_KIND;
                if (pairs == 1) {
                    return FULL_HOUSE;
                } else {
                    return THREE_OF_A_KIND;
                }
            }
            if (pairs == 2) {
                if (jokers == 1) return FULL_HOUSE;
                return TWO_PAIRS;
            }
            if (pairs == 1) {
                if (jokers == 3) return FIVE_OF_A_KIND;
                if (jokers == 2) return FOUR_OF_A_KIND;
                if (jokers == 1) return THREE_OF_A_KIND;
                return ONE_PAIR;
            }
            if (jokers == 5) return FIVE_OF_A_KIND;
            if (jokers == 4) return FIVE_OF_A_KIND;
            if (jokers == 3) return FOUR_OF_A_KIND;
            if (jokers == 2) return THREE_OF_A_KIND;
            if (jokers == 1) return ONE_PAIR;
            return 0;
        }

        public long scoreCards() {
            String[] individualCards = cards().split("");
            long sum = 0;
            for (int i = 0; i < individualCards.length; i++) {
                sum += scoreCard(individualCards[i]) * POSITION_WEIGHT[i];
            }
            return sum;
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

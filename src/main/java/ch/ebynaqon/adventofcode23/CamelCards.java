package ch.ebynaqon.adventofcode23;

import java.util.*;

public class CamelCards {
    public static List<Hand> parse(String input) {
        return Arrays.stream(input.split("\n"))
                .map(line -> line.split("\\s+"))
                .map(handAndBet -> new Hand(handAndBet[0], Integer.parseInt(handAndBet[1])))
                .toList();
    }

    public static int solve(String input) {
        List<Hand> hands = parse(input).stream().sorted(Comparator.comparing(Hand::score)).toList();
        int sum = 0;
        for (int i = 0; i < hands.size(); i++) {
            sum += (i + 1) * hands.get(i).bet();
        }
        return sum;
    }

    public record Hand(String cards, int bet) {
        public static int ONE_PAIR = 0x100000;
        public static int TWO_PAIRS = 0x200000;
        public static int THREE_OF_A_KIND = 0x300000;
        public static int FULL_HOUSE = 0x400000;
        public static int FOUR_OF_A_KIND = 0x500000;
        public static int FIVE_OF_A_KIND = 0x600000;

        private static int[] POSITION_WEIGHT = {0x10000,0x1000,0x100,0x10,0x1};

        /*
        6 - Five of a kind  - where all five cards have the same label: AAAAA
        5 - Four of a kind  - where four cards have the same label and one card has a different label: AA8AA
        4 - Full house      - where three cards have the same label, and the remaining two cards share a different label: 23332
        3 - Three of a kind - where three cards have the same label, and the remaining two cards are each different from any other card in the hand: TTT98
        2 - Two pair        - where two cards share one label, two other cards share a second label, and the remaining card has a third label: 23432
        1 - One pair        - where two cards share one label, and the other three cards have a different label from the pair and each other: A23A4
        High card
        * */
        public int score() {
            return scoreKind() + scoreCards();
        }

        public int scoreKind() {
            var sortedCards = Arrays.stream((cards().split(""))).sorted().toList();
            var previousCard = "";
            var sameCardCount = 0;
            var pairs = 0;
            var threeOfAKind = 0;
            var fourOfAKind = 0;
            var fiveOfAKind = 0;
            for (var card: sortedCards             ) {
                if (card.equals(previousCard)) {
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
            if (fiveOfAKind > 0) {
                return FIVE_OF_A_KIND;
            }
            if (fourOfAKind > 0) {
                return FOUR_OF_A_KIND;
            }
            if (threeOfAKind > 0) {
                if (pairs > 0) {
                    return FULL_HOUSE;
                } else {
                    return THREE_OF_A_KIND;
                }
            }
            if (pairs == 2) return TWO_PAIRS;
            if (pairs == 1) return ONE_PAIR;
            return 0;
        }

        public int scoreCards() {
            String[] individualCards = cards().split("");
            int sum = 0;
            for (int i = 0; i < individualCards.length; i++) {
                sum += scoreCard(individualCards[i]) * POSITION_WEIGHT[i];
            }
            return sum;
        }

        private int scoreCard(String card) {
            return switch (card) {
                case "A" -> 0xd;
                case "K" -> 0xc;
                case "Q" -> 0xb;
                case "J" -> 0xa;
                case "T" -> 0x9;
                case "9" -> 0x8;
                case "8" -> 0x7;
                case "7" -> 0x6;
                case "6" -> 0x5;
                case "5" -> 0x4;
                case "4" -> 0x3;
                case "3" -> 0x2;
                case "2" -> 0x1;
                default -> 0;
            };
        }
    }
}

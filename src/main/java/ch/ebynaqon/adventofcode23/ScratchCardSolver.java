package ch.ebynaqon.adventofcode23;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ScratchCardSolver {
    public static int solveAllGames(String text) {
        return Arrays.stream(text.split("\n"))
                .mapToInt(ScratchCardSolver::solveOneGame).sum();
    }
    public static int solveAllGamesWitCardMultiplication(String text) {
        String[] games = text.split("\n");
        int numberOfScratchcards = 0;
        HashMap<Integer, Integer> gameMultipliers = new HashMap<>();
        for (int gameNumber = 0; gameNumber < games.length; gameNumber++) {
            int numberOfWinningNumbers = getNumberOfWinningNumbers(games[gameNumber]);
            int multiplier = gameMultipliers.getOrDefault(gameNumber, 1);
            updateMultipliers(gameMultipliers, gameNumber, numberOfWinningNumbers, multiplier);
            numberOfScratchcards += multiplier;
//            System.out.println(gameNumber + ": " + multiplier);
        }
        return numberOfScratchcards;
    }

    private static void updateMultipliers(HashMap<Integer, Integer> multipliers, int gameNumber, int score, int multiplier) {
        for (int i = gameNumber + 1; i <= gameNumber + score; i++) {
            Integer prev = multipliers.getOrDefault(i, 1);
            multipliers.put(i, prev  + multiplier);
//            System.out.println(gameNumber + ": (" + i + ") " + prev +  " -> " + (prev * 2));
        }
    }

    public static int solveOneGame(String line) {
        long numberOfWinningNumbers = getNumberOfWinningNumbers(line);
        return numberOfWinningNumbers == 0 ? 0 : Double.valueOf(Math.pow(2, numberOfWinningNumbers - 1)).intValue();
    }

    private static int getNumberOfWinningNumbers(String line) {
        String[] winningAndOwnNumberText = line.split(":")[1].split("\\|");
        List<Integer> winningNumbers = parseNumbers(winningAndOwnNumberText[0]);
        List<Integer> ownNumbers = parseNumbers(winningAndOwnNumberText[1]);
        return (int) ownNumbers.stream().filter(winningNumbers::contains).count();
    }

    private static List<Integer> parseNumbers(String numbersText) {
        return Arrays.stream(numbersText.trim().split(" "))
                .filter(s -> !s.isBlank())
                .map(Integer::valueOf)
                .toList();
    }
}

package ch.ebynaqon.adventofcode23;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

class ScratchCardSolverTest {
    @Test
    void parseOneLine() {
        var line = "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53";

        var actual = ScratchCardSolver.solveOneGame(line);

        assertThat(actual).isEqualTo(8);
    }

    @Test
    void solveExample() {
        var input = """
                Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
                Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
                Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
                Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
                Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
                Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
                """;

        var actual = ScratchCardSolver.solveAllGames(input);

        assertThat(actual).isEqualTo(13);
    }

    @Test
    void solvePart1() throws IOException, URISyntaxException {
        var input = TestHelper.readInput("/day4-scratch-games.txt").trim();

        var actual = ScratchCardSolver.solveAllGames(input);

        assertThat(actual).isEqualTo(28538);
    }

    @Test
    void solveExampleWithNumberOfScratchCardsWon() {
        var input = """
                Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
                Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
                Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
                Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
                Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
                Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
                """;

        var actual = ScratchCardSolver.solveAllGamesWitCardMultiplication(input);

        assertThat(actual).isEqualTo(30);
    }

    @Test
    void solvePart2WithNumberOfScratchCardsWon() throws IOException, URISyntaxException {
        var input = TestHelper.readInput("/day4-scratch-games.txt").trim();

        var actual = ScratchCardSolver.solveAllGamesWitCardMultiplication(input);

        assertThat(actual).isEqualTo(9425061);
    }

}

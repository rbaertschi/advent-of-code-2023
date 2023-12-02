package ch.ebynaqon.adventofcode23;

import java.util.Arrays;
import java.util.List;

public class ColoredCubeGame {
    public static Game parseLine(String line) {
        String[] gameIdAndDraws = line.split(":");
        String[] gamePrefixAndId = gameIdAndDraws[0].split(" ");
        int gameId = Integer.parseInt(gamePrefixAndId[1]);
        String[] drawTexts = gameIdAndDraws[1].split(";");
        List<Draw> draws = Arrays.stream(drawTexts)
                .map(ColoredCubeGame::parseDraw)
                .toList();
        return new Game(gameId, draws);
    }

    private static Draw parseDraw(String drawText) {
        String[] colorsDraws = drawText.split(",");
        int red = 0;
        int green = 0;
        int blue = 0;
        for (var colorDraw : colorsDraws) {
            String[] countAndColor = colorDraw.trim().split(" ");
            int count = Integer.parseInt(countAndColor[0]);
            if ("red".equals(countAndColor[1])) red = count;
            if ("green".equals(countAndColor[1])) green = count;
            if ("blue".equals(countAndColor[1])) blue = count;
        }
        return new Draw(red, green, blue);
    }

    public static int sumUpValidGameIds(List<String> lines) {
        return lines.stream()
                .map(ColoredCubeGame::parseLine)
                .filter(game -> game.hasValidDrawsFor(12, 13, 14))
                .mapToInt(Game::id)
                .sum();
    }

    public static int sumUpPowerOfMinimalDraws(List<String> lines) {
        return lines.stream()
                .map(ColoredCubeGame::parseLine)
                .mapToInt(Game::minimalDrawPower)
                .sum();
    }

    public record Game(int id, List<Draw> draws) {
        public boolean hasValidDrawsFor(int maxRed, int maxGreen, int maxBlue) {
            return draws.stream().allMatch(draw ->
                    draw.red() <= maxRed && draw.green() <= maxGreen && draw.blue() <= maxBlue
            );
        }

        public int minimalDrawPower() {
            int red = 0;
            int green = 0;
            int blue = 0;
            for (var draw : draws) {
                red = Math.max(red, draw.red);
                green = Math.max(green, draw.green);
                blue = Math.max(blue, draw.blue);
            }
            return red * green * blue;
        }
    }

    public record Draw(int red, int green, int blue) {
    }
}

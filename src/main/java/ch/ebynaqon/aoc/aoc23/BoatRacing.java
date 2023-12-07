package ch.ebynaqon.aoc.aoc23;

import java.util.ArrayList;
import java.util.List;

// 7ms -> distance = (7x - x*x) 1mm
// d(distance)=7-2x=0 -> 7 = 2x -> x = 7 / 2
public class BoatRacing {
    public static List<Race> parse(String input) {
        String[] lines = input.split("\n");
        String[] times = lines[0].split(":")[1].trim().split("\\s+");
        String[] distances = lines[1].split(":")[1].trim().split("\\s+");
        ArrayList<Race> races = new ArrayList<>();
        for (int i = 0; i < times.length; i++) {
            races.add(new Race(Integer.parseInt(times[i]), Integer.parseInt(distances[i])));
        }
        return races;
    }

    public static List<Race> parseWithoutSpaces(String input) {
        String[] lines = input.split("\n");
        return List.of(
                new Race(
                        Long.parseLong(lines[0].split(":")[1].trim().replaceAll("\\s", "")),
                        Long.parseLong(lines[1].split(":")[1].trim().replaceAll("\\s", ""))
                )
        );
    }

    public static int solve(String input) {
        List<Race> races = parse(input);
        return races.stream().map(Race::winningTimes)
                .mapToInt(List::size)
                .reduce(1, (acc, cur) -> acc * cur);
    }

    public static int solveWithoutSpaces(String input) {
        List<Race> races = parseWithoutSpaces(input);
        return races.stream().map(Race::winningTimes)
                .mapToInt(List::size)
                .reduce(1, (acc, cur) -> acc * cur);
    }

    public record Race(long time, long maxDistance) {
        public List<Long> winningTimes() {
            var winningTimes = new ArrayList<Long>();
            var bestTime = time() / 2;
            for (long i = bestTime; i < time(); i++) {
                var distance = (time - i) * i;
                if (distance > maxDistance()) {
                    winningTimes.add(i);
                } else {
                    break;
                }
            }
            for (long i = bestTime - 1; i > 0; i--) {
                var distance = (time - i) * i;
                if (distance > maxDistance()) {
                    winningTimes.add(i);
                } else {
                    break;
                }
            }
            return winningTimes;
        }
    }
}

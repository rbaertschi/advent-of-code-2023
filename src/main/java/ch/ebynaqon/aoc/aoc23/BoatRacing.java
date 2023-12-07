package ch.ebynaqon.aoc.aoc23;

import java.util.ArrayList;
import java.util.List;

// 7ms -> distance = (7x - x*x) 1mm
// d(distance)=7-2x=0 -> 7 = 2x -> x = 7 / 2
// d = t * x - 1 * x * x  ->  0 = - 1 * x^2 + t * x - d  ->  a=-1, b=t, c=-d
// x1 = (-b - sqrt(b*b - 4ac)) / 2a = (t + sqrt(t*t - 4d)) / 2
// x2 = (-b + sqrt(b*b - 4ac)) / 2a = (t - sqrt(t*t - 4d)) / 2
public class BoatRacing {
    public static List<Race> parse(String input) {
        String[] lines = input.split("\n");
        String[] times = lines[0].split(":")[1].trim().split("\\s+");
        String[] distances = lines[1].split(":")[1].trim().split("\\s+");
        ArrayList<Race> races = new ArrayList<>();
        for (int i = 0; i < times.length; i++) {
            races.add(new Race(Long.parseLong(times[i]), Long.parseLong(distances[i])));
        }
        return races;
    }

    public static long solve(String input) {
        List<Race> races = parse(input);
        return races.stream().mapToLong(Race::countWinningTimes)
                .reduce(1, Math::multiplyExact);
    }

    private static long calculateDistance(long totalTime, long waitTime) {
        return ((totalTime - waitTime) * waitTime);
    }

    public record Race(long time, long maxDistance) {
        public Long countWinningTimes() {
            var discriminator = time() * time() - 4 * maxDistance();
            // x1 = (t + sqrt(t*t - 4d)) / 2
            var lower = (long) ((time() - Math.sqrt(discriminator)) / 2);
            // x2 = (t - sqrt(t*t - 4d)) / 2
            var upper = (long) ((time() + Math.sqrt(discriminator)) / 2);
            if (calculateDistance(time(), lower) <= maxDistance()) lower++;
            if (calculateDistance(time(), upper) <= maxDistance()) upper--;
            return upper - lower + 1;
        }
    }
}

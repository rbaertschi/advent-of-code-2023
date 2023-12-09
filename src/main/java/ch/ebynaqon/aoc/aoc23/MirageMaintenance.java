package ch.ebynaqon.aoc.aoc23;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record MirageMaintenance(List<Sequence> sequences) {
    public static MirageMaintenance parse(String input) {
        List<Sequence> sequences = Arrays.stream(input.split("\n"))
                .map(Sequence::of)
                .toList();
        return new MirageMaintenance(sequences);
    }

    public long sumOfNextPredictions() {
        return sequences().stream().mapToLong(Sequence::predictNext).sum();
    }

    public record Sequence(List<Long> numbers) {
        public static Sequence of(String line) {
            return new Sequence(Arrays.stream(line.split("\\s+"))
                    .map(Long::parseLong)
                    .toList());
        }

        public long predictNext() {
            if (isAllZeros()){
                return 0L;
            }
            return numbers().getLast() + differences().predictNext();
        }

        public Sequence differences() {
            ArrayList<Long> differences = new ArrayList<>();
            for (int i = 1; i < numbers().size(); i++) {
                var difference = numbers().get(i) - numbers().get(i-1);
                differences.add(difference);
            }
            return new Sequence(differences);
        }

        private boolean isAllZeros() {
            return numbers.stream().allMatch(number -> number == 0);
        }
    }
}

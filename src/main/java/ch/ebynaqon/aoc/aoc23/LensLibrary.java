package ch.ebynaqon.aoc.aoc23;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class LensLibrary {

    public static long sumOfHashes(String input) {
        return Arrays.stream(input.split(","))
                .map(Step::new)
                .mapToLong(Step::hash)
                .sum();
    }

    public static int calculateFocusingPower(String input) {
        var steps = Arrays.stream(input.split(","))
                .map(Step::new)
                .toList();
        var boxes = IntStream.range(0, 256)
                .mapToObj(i -> (List<Lens>) new ArrayList<Lens>())
                .toList();
        distributeLenses(steps, boxes);
        return calculateFocusingPower(boxes);
    }

    private static int calculateFocusingPower(List<List<Lens>> boxes) {
        int focusingPower = 0;
        for (int boxNumber = 0; boxNumber < boxes.size(); boxNumber++) {
            var box = boxes.get(boxNumber);
            for (int slotNumber = 0; slotNumber < box.size(); slotNumber++) {
                int currentFocusingPower =
                        (boxNumber + 1)
                        * (slotNumber + 1)
                        * box.get(slotNumber).focalLength();
                focusingPower += currentFocusingPower;
            }
        }
        return focusingPower;
    }

    private static void distributeLenses(List<Step> steps, List<List<Lens>> boxes) {
        for (var step : steps) {
            var box = boxes.get(step.labelHash());
            var label = step.label();
            if (step.isDeletion()) {
                for (int i = 0; i < box.size(); i++) {
                    if (box.get(i).label().equals(label)) {
                        box.remove(i);
                        break;
                    }
                }
            } else {
                boolean added = false;
                var focalLength = step.focalLength();
                Lens newLens = new Lens(label, focalLength);
                for (int i = 0; i < box.size(); i++) {
                    if (box.get(i).label().equals(label)) {
                        box.set(i, newLens);
                        added = true;
                        break;
                    }
                }
                if (!added) {
                    box.add(newLens);
                }
            }
        }
    }

    record Lens(String label, int focalLength) {
    }

    public record Step(String instruction) {
        public int hash() {
            return computeHash(instruction);
        }

        private static int computeHash(String text) {
            int result = 0;
            for (int i = 0; i < text.length(); i++) {
                result += text.codePointAt(i);
                result *= 17;
                result = result % 256;
            }
            return result;
        }

        public boolean isDeletion() {
            return instruction.endsWith("-");
        }

        public String label() {
            return instruction.split("[-=]")[0];
        }

        public int labelHash() {
            return computeHash(label());
        }

        public int focalLength() {
            return Integer.parseInt(instruction.split("=")[1]);
        }
    }
}

package ch.ebynaqon.aoc.aoc23;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public record SortingPartsUsingWorkflows(List<Workflow> workflows, List<Part> parts) {
    public static SortingPartsUsingWorkflows parse(String input) {
        String[] rulesAndParts = input.split("\n\n");
        List<Workflow> workflows = Arrays.stream(rulesAndParts[0].split("\n"))
                .map(Workflow::parse)
                .toList();
        List<Part> parts = Arrays.stream(rulesAndParts[1].split("\n"))
                .map(Part::parse)
                .toList();
        return new SortingPartsUsingWorkflows(workflows, parts);
    }

    public int getScoreOfAcceptedParts() {
        var acceptedParts = new ArrayList<Part>();
        var partsToCheck = new ArrayDeque<>(parts().stream()
                .map(part -> new PartAndWorkflow(part, "in")).toList());
        var nextPart = partsToCheck.poll();
        var workflowsByLabel = new HashMap<String, Workflow>();
        for (var workflow : workflows()) {
            workflowsByLabel.put(workflow.label(), workflow);
        }
        while (nextPart != null) {
            Workflow workflow = workflowsByLabel.get(nextPart.workflow());
            Part part = nextPart.part();
            String nextWorkflow = workflow.rules().stream().filter(rule -> rule.accepts(part)).findFirst().get().targetWorkflow();
            if (nextWorkflow.equals("A")) {
                acceptedParts.add(part);
            } else if (nextWorkflow.equals("R")) {
                // rejected -> just ignore the part
            } else {
                partsToCheck.add(new PartAndWorkflow(part, nextWorkflow));
            }
            nextPart = partsToCheck.poll();
        }
        return getScore(acceptedParts);
    }

    public long getNumberOfAcceptedPartValues() {
        var acceptedRanges = new ArrayList<PartRange>();
        var rangesToCheck = new ArrayDeque<PartRange>();
        var nextRange = new PartRange(new ValueRange(1, 4000), new ValueRange(1, 4000), new ValueRange(1, 4000), new ValueRange(1, 4000), "in");
        var workflowsByLabel = new HashMap<String, Workflow>();
        for (var workflow : workflows()) {
            workflowsByLabel.put(workflow.label(), workflow);
        }
        while (nextRange != null) {
            var workflow = workflowsByLabel.get(nextRange.workflow());
            List<PartRange> mappedRanges = workflow.mapRange(nextRange);
            for (var mappedRange : mappedRanges) {
                if (mappedRange.workflow().equals("A")) {
                    acceptedRanges.add(mappedRange);
                } else if (!mappedRange.workflow().equals("R")) {
                    rangesToCheck.add(mappedRange);
                }
            }
            nextRange = rangesToCheck.poll();
        }
        return acceptedRanges.stream().mapToLong(PartRange::size).sum();
    }

    private int getScore(List<Part> acceptedParts) {
        return acceptedParts.stream().mapToInt(Part::score).sum();
    }

    record PartAndWorkflow(Part part, String workflow) {
    }

    public record PartRangeMappingResult(PartRange mapped, PartRange remaining) {
    }

    public record Workflow(String label, List<Rule> rules) {
        public static Workflow parse(String input) {
            // px{a<2006:qkq,m>2090:A,rfg}
            String[] labelAndRules = input.split("[{}]");
            List<Rule> rules = Arrays.stream(labelAndRules[1].split(","))
                    .map(Rule::parse)
                    .toList();
            return new Workflow(labelAndRules[0], rules);
        }

        public List<PartRange> mapRange(PartRange range) {
            var mappedRanges = new ArrayList<PartRange>();
            var remainingRange = range;
            for (var rule : rules()) {
                var result = rule.mapRange(remainingRange);
                if (result.mapped() != null) {
                    mappedRanges.add(result.mapped());
                }
                remainingRange = result.remaining();
                if (remainingRange == null) {
                    break;
                }
            }
            return mappedRanges;
        }
    }

    public interface Rule {
        boolean accepts(Part part);

        String targetWorkflow();

        PartRangeMappingResult mapRange(PartRange range);

        static Rule parse(String input) {
            String[] conditionAndTargetWorkflow = input.split(":");
            String targetWorkflow = conditionAndTargetWorkflow[conditionAndTargetWorkflow.length - 1];
            if (conditionAndTargetWorkflow.length == 1) {
                return new Default(targetWorkflow);
            } else {
                String condition = conditionAndTargetWorkflow[0];
                String[] propertyAndThreshold = condition.split("[<>]");
                String property = propertyAndThreshold[0];
                int threshold = Integer.parseInt(propertyAndThreshold[1]);
                if (condition.contains(">")) {
                    return new MoreThan(property, threshold, targetWorkflow);
                } else {
                    return new LessThan(property, threshold, targetWorkflow);
                }
            }
        }

        default Integer extractValue(Part part, String property) {
            return switch (property) {
                case "x" -> part.x();
                case "m" -> part.m();
                case "a" -> part.a();
                default -> part.s();
            };
        }

        default ValueRange extractRange(PartRange part, String property) {
            return switch (property) {
                case "x" -> part.x();
                case "m" -> part.m();
                case "a" -> part.a();
                default -> part.s();
            };
        }

        default PartRangeMappingResult splitRange(PartRange range, ValueRange mappedRange, ValueRange unmappedRange, String property, String workflow) {
            return switch (property) {
                case "x" -> new PartRangeMappingResult(
                        new PartRange(mappedRange, range.m(), range.a(), range.s(), workflow),
                        new PartRange(unmappedRange, range.m(), range.a(), range.s(), range.workflow())
                );
                case "m" -> new PartRangeMappingResult(
                        new PartRange(range.x(), mappedRange, range.a(), range.s(), workflow),
                        new PartRange(range.x(), unmappedRange, range.a(), range.s(), range.workflow())
                );
                case "a" -> new PartRangeMappingResult(
                        new PartRange(range.x(), range.m(), mappedRange, range.s(), workflow),
                        new PartRange(range.x(), range.m(), unmappedRange, range.s(), range.workflow())
                );
                default -> new PartRangeMappingResult(
                        new PartRange(range.x(), range.m(), range.a(), mappedRange, workflow),
                        new PartRange(range.x(), range.m(), range.a(), unmappedRange, range.workflow())
                );
            };
        }

        record LessThan(String property, int threshold, String targetWorkflow) implements Rule {
            @Override
            public boolean accepts(Part part) {
                return extractValue(part, this.property()) < threshold;
            }

            @Override
            public PartRangeMappingResult mapRange(PartRange range) {
                ValueRange valueRange = extractRange(range, property());
                if (valueRange.to() < threshold) {
                    return new PartRangeMappingResult(
                            new PartRange(range.x(), range.m(), range.a(), range.s(), targetWorkflow()),
                            null
                    );
                } else if (valueRange.from() >= threshold) {
                    return new PartRangeMappingResult(null, range);
                }
                ValueRange mappedRange = new ValueRange(valueRange.from(), threshold - 1);
                ValueRange unmappedRange = new ValueRange(threshold, valueRange.to());
                return splitRange(range, mappedRange, unmappedRange, property(), targetWorkflow());
            }

        }

        record MoreThan(String property, int threshold, String targetWorkflow) implements Rule {
            @Override
            public boolean accepts(Part part) {
                return extractValue(part, this.property()) > threshold;
            }

            @Override
            public PartRangeMappingResult mapRange(PartRange range) {
                ValueRange valueRange = extractRange(range, property());
                if (valueRange.from() > threshold) {
                    return new PartRangeMappingResult(
                            new PartRange(range.x(), range.m(), range.a(), range.s(), targetWorkflow()),
                            null
                    );
                } else if (valueRange.to() <= threshold) {
                    return new PartRangeMappingResult(null, range);
                }
                ValueRange mappedRange = new ValueRange(threshold + 1, valueRange.to());
                ValueRange unmappedRange = new ValueRange(valueRange.from(), threshold);
                return splitRange(range, mappedRange, unmappedRange, property(), targetWorkflow());
            }
        }

        record Default(String targetWorkflow) implements Rule {
            @Override
            public boolean accepts(Part part) {
                return true;
            }

            @Override
            public PartRangeMappingResult mapRange(PartRange range) {
                return new PartRangeMappingResult(
                        new PartRange(range.x(), range.m(), range.a(), range.s(), targetWorkflow()),
                        null
                );
            }
        }
    }

    public record Part(int x, int m, int a, int s) {
        public static Part parse(String input) {
            // {x=787,m=2655,a=1222,s=2876}
            int x = 0;
            int m = 0;
            int a = 0;
            int s = 0;
            for (var characteristic : input.substring(1, input.length() - 1).split(",")) {
                String[] propertyAndValue = characteristic.split("=");
                int value = Integer.parseInt(propertyAndValue[1]);
                switch (propertyAndValue[0]) {
                    case "x" -> x = value;
                    case "m" -> m = value;
                    case "a" -> a = value;
                    default -> s = value;
                }
            }
            return new Part(x, m, a, s);
        }

        public int score() {
            return x + m + a + s;
        }
    }

    public record PartRange(ValueRange x, ValueRange m, ValueRange a, ValueRange s, String workflow) {
        public long size() {
            return x.size() * m.size() * a.size() * s.size();
        }
    }

    public record ValueRange(int from, int to) {
        public long size() {
            return to - from + 1;
        }
    }
}

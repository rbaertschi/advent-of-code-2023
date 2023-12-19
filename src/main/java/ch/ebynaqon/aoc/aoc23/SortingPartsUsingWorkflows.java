package ch.ebynaqon.aoc.aoc23;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

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

    private int getScore(List<Part> acceptedParts) {
        return acceptedParts.stream().mapToInt(Part::score).sum();
    }

    record PartAndWorkflow(Part part, String workflow) {
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
    }

    public interface Rule {
        boolean accepts(Part part);

        String targetWorkflow();

        static Rule parse(String input) {
            String[] conditionAndTargetWorkflow = input.split(":");
            String targetWorkflow = conditionAndTargetWorkflow[conditionAndTargetWorkflow.length - 1];
            if (conditionAndTargetWorkflow.length == 1) {
                return new Default(targetWorkflow);
            } else {
                String condition = conditionAndTargetWorkflow[0];
                String[] propertyAndThreshold = condition.split("[<>]");
                Function<Part, Integer> extractor = switch (propertyAndThreshold[0]) {
                    case "x" -> Part.PROPERTY_X;
                    case "m" -> Part.PROPERTY_M;
                    case "a" -> Part.PROPERTY_A;
                    default -> Part.PROPERTY_S;
                };
                int threshold = Integer.parseInt(propertyAndThreshold[1]);
                if (condition.contains(">")) {
                    return new MoreThan(extractor, threshold, targetWorkflow);
                } else {
                    return new LessThan(extractor, threshold, targetWorkflow);
                }
            }
        }

        record LessThan(Function<Part, Integer> extractor, int threshold, String targetWorkflow) implements Rule {
            @Override
            public boolean accepts(Part part) {
                return extractor.apply(part) < threshold;
            }
        }

        record MoreThan(Function<Part, Integer> extractor, int threshold, String targetWorkflow) implements Rule {
            @Override
            public boolean accepts(Part part) {
                return extractor.apply(part) > threshold;
            }
        }

        record Default(String targetWorkflow) implements Rule {
            @Override
            public boolean accepts(Part part) {
                return true;
            }
        }
    }

    public record Part(int x, int m, int a, int s) {
        public static Function<Part, Integer> PROPERTY_X = Part::x;
        public static Function<Part, Integer> PROPERTY_M = Part::m;
        public static Function<Part, Integer> PROPERTY_A = Part::a;
        public static Function<Part, Integer> PROPERTY_S = Part::s;

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
}

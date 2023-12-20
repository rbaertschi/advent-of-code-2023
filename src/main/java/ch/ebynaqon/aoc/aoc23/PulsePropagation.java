package ch.ebynaqon.aoc.aoc23;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public record PulsePropagation(List<Module> modules) {
    public static PulsePropagation parse(String input) {
        var modules = Arrays.stream(input.split("\n"))
                .map(Module::parse)
                .toList();
        for (var mod : modules) {
            if (mod instanceof Conjunction conjunctionModule) {
                String name = conjunctionModule.name();
                var inputModuleNames = modules.stream()
                        .filter(module -> module.targetModuleNames().contains(name))
                        .map(Module::name)
                        .toList();
                conjunctionModule.setInputModuleNames(inputModuleNames);
            }
        }
        return new PulsePropagation(modules);
    }

    public long getPulseScoreAfter1000ButtonPresses() {
        var modulesByName = new HashMap<String, Module>();
        for (var module : modules()) {
            modulesByName.put(module.name(), module);
        }
        long highPulses = 0;
        long lowPulses = 0;
        for (int i = 0; i < 1000; i++) {
            var pulses = List.of(
                    new Pulse(PulseType.LOW, "button", "broadcaster")
            );
            lowPulses += 1;
            while (!pulses.isEmpty()) {
                var nextPulses = new ArrayList<Pulse>();
                for (var pulse : pulses) {
                    Module module = modulesByName.get(pulse.targetModuleName());
                    if (module != null) {
                        List<Pulse> newPulses = module.apply(pulse);
                        long newLowPulses = newPulses.stream().filter(newPulse -> newPulse.type() == PulseType.LOW).count();
                        long newHighPulses = newPulses.size() - newLowPulses;
                        lowPulses += newLowPulses;
                        highPulses += newHighPulses;
                        nextPulses.addAll(newPulses);
                    }
                }
                pulses = nextPulses;
            }
        }
        return highPulses * lowPulses;
    }

    public interface Module {
        String name();

        List<String> targetModuleNames();

        List<Pulse> apply(Pulse pulse);

        static Module parse(String input) {
            String[] moduleTypeAndNameAndTargetModules = input.split("->");
            String moduleTypeAndName = moduleTypeAndNameAndTargetModules[0].trim();
            List<String> targetModules = Arrays.stream(moduleTypeAndNameAndTargetModules[1].trim().split(","))
                    .map(String::trim)
                    .toList();
            if (moduleTypeAndName.equals("broadcaster")) {
                return new Broadcaster(moduleTypeAndName, targetModules);
            } else if (moduleTypeAndName.startsWith("%")) {
                return new FlipFlop(moduleTypeAndName.substring(1), targetModules);
            } else if (moduleTypeAndName.startsWith("&")) {
                return new Conjunction(moduleTypeAndName.substring(1), targetModules);
            } else {
                throw new IllegalArgumentException("%s is not a valid module definition!".formatted(input));
            }
        }
    }

    public record Broadcaster(String name, List<String> targetModuleNames) implements Module {
        @Override
        public List<Pulse> apply(Pulse pulse) {
            return targetModuleNames().stream()
                    .map(targetModule -> new Pulse(pulse.type(), name(), targetModule))
                    .toList();
        }
    }

    public static final class FlipFlop implements Module {
        private final String name;
        private final List<String> targetModuleNames;
        private boolean isOn;

        public FlipFlop(String name, List<String> targetModuleNames) {
            this.name = name;
            this.targetModuleNames = targetModuleNames;
            this.isOn = false;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public List<String> targetModuleNames() {
            return targetModuleNames;
        }

        @Override
        public List<Pulse> apply(Pulse pulse) {
            if (pulse.type() == PulseType.LOW) {
                isOn = !isOn;
                if (isOn) {
                    return targetModuleNames().stream()
                            .map(targetModuleName -> new Pulse(PulseType.HIGH, name(), targetModuleName))
                            .toList();
                } else {
                    return targetModuleNames().stream()
                            .map(targetModuleName -> new Pulse(PulseType.LOW, name(), targetModuleName))
                            .toList();
                }
            } else {
                return List.of();
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (FlipFlop) obj;
            return Objects.equals(this.name, that.name) &&
                   Objects.equals(this.targetModuleNames, that.targetModuleNames);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, targetModuleNames);
        }

        @Override
        public String toString() {
            return "FlipFlop[" +
                   "name=" + name + ", " +
                   "targetModuleName=" + targetModuleNames + ']';
        }

    }

    public static final class Conjunction implements Module {
        private final String name;
        private final List<String> targetModuleNames;
        private final HashMap<String, PulseType> lastPulseTypeFromModule;

        public Conjunction(String name, List<String> targetModuleNames) {
            this.name = name;
            this.targetModuleNames = targetModuleNames;
            lastPulseTypeFromModule = new HashMap<>();
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public List<String> targetModuleNames() {
            return targetModuleNames;
        }

        @Override
        public List<Pulse> apply(Pulse pulse) {
            lastPulseTypeFromModule.put(pulse.sourceModuleName(), pulse.type());
            if (lastPulseTypeFromModule.values().stream().anyMatch(Predicate.isEqual(PulseType.LOW))) {
                return targetModuleNames().stream()
                        .map(targetModuleName -> new Pulse(PulseType.HIGH, name(), targetModuleName))
                        .toList();
            } else {
                return targetModuleNames().stream()
                        .map(targetModuleName -> new Pulse(PulseType.LOW, name(), targetModuleName))
                        .toList();
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Conjunction) obj;
            return Objects.equals(this.name, that.name) &&
                   Objects.equals(this.targetModuleNames, that.targetModuleNames);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, targetModuleNames);
        }

        @Override
        public String toString() {
            return "Conjunction[" +
                   "name=" + name + ", " +
                   "targetModuleName=" + targetModuleNames + ']';
        }

        public void setInputModuleNames(List<String> inputModuleNames) {
            for (var moduleName : inputModuleNames) {
                lastPulseTypeFromModule.put(moduleName, PulseType.LOW);
            }
        }
    }

    public record Pulse(PulseType type, String sourceModuleName, String targetModuleName) {
    }

    public enum PulseType {HIGH, LOW}
}

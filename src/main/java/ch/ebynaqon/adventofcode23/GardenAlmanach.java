package ch.ebynaqon.adventofcode23;

import java.util.*;

public record GardenAlmanach(List<Long> seeds, List<Conversion> conversions) {

    public static GardenAlmanach of(String input) {
        var sections = input.split("\n\n");
        var seeds = Arrays.stream(sections[0].substring("seeds:".length()).trim().split("\\s+"))
                .map(Long::valueOf).toList();
        var conversions = Arrays.stream(sections).dropWhile(section -> section.startsWith("seeds:"))
                .map(section -> new Conversion(
                        Arrays.stream(section.split("\n"))
                                .dropWhile(line -> line.endsWith("map:"))
                                .map(RangeMapping::of)
                                .toList())
                ).toList();
        return new GardenAlmanach(seeds, conversions);
    }

    public List<ValueRange> seedRanges() {
        int rangeCount = seeds().size() / 2;
        ArrayList<ValueRange> ranges = new ArrayList<>(rangeCount);
        for (int i = 0; i < rangeCount; i++) {
            ranges.add(new ValueRange(seeds().get(2 * i), seeds().get(2 * i + 1)));
        }
        return ranges;
    }

    public List<Long> getSeedLocations() {
        return seeds().stream().map(this::mapSeedToLocation).toList();
    }

    public Long getClosestSeedLocation() {
        return getSeedLocations().stream().mapToLong(i -> i).min().getAsLong();
    }

    public Long getClosestSeedLocationFromRanges() {
        return getSeedLocationRanges().stream().mapToLong(ValueRange::start).min().getAsLong();
    }

    public void sanityCheck() {
        conversions().forEach(Conversion::sanityCheck);
    }

    private Long mapSeedToLocation(Long seed) {
        var curValue = seed;
        for (var conversion : conversions()) {
            curValue = conversion.mapValue(curValue).orElse(curValue);
        }
        return curValue;
    }

    private List<ValueRange> getSeedLocationRanges() {
        var curValue = seedRanges();
        for (var conversion : conversions()) {
            curValue = conversion.mapValueRanges(curValue);
        }
        return curValue;
    }

    public record Conversion(List<RangeMapping> ranges) {
        public Optional<Long> mapValue(Long curMappingValue) {
            return ranges().stream()
                    .filter(rangeMapping -> rangeMapping.contains(curMappingValue))
                    .map(rangeMapping -> rangeMapping.map(curMappingValue))
                    .findFirst();
        }

        public void sanityCheck() {
            var start = -1L;
            for (var mapping : sortedRanges()) {
                if (mapping.sourceStart() <= start)
                    throw new RuntimeException("Mapping [%s, %s] overlaps previous value %s".formatted(mapping.sourceStart(), mapping.length(), start));
                start = mapping.sourceStart() + mapping.length() - 1;
            }
        }

        private List<RangeMapping> sortedRanges() {
            return ranges().stream().sorted(Comparator.comparing(RangeMapping::sourceStart)).toList();
        }

        public List<ValueRange> mapValueRanges(List<ValueRange> valueRanges) {
            var unmapped = new ArrayList<>(valueRanges);
            var mapped = new ArrayList<ValueRange>();
            List<RangeMapping> rangeMappings = sortedRanges();
            while (!unmapped.isEmpty()) {
                ValueRange cur = unmapped.removeFirst();
                var mapping = rangeMappings.stream()
                        .filter(rangeMapping -> rangeMapping.intersects(cur))
                        .findFirst();
                if (mapping.isPresent()) {
                    var result = mapping.get().mapRange(cur);
                    mapped.add(result.result());
                    unmapped.addAll(result.remainder());
                } else {
                    mapped.add(cur);
                }
            }
            return mapped.stream().sorted(Comparator.comparing(ValueRange::start)).toList();
        }

    }

    public record RangeMapping(Long destStart, Long sourceStart, Long length) {
        public static RangeMapping of(String line) {
            var parts = line.split("\\s+");
            return new RangeMapping(
                    Long.valueOf(parts[0]),
                    Long.valueOf(parts[1]),
                    Long.valueOf(parts[2])
            );
        }

        public boolean contains(Long sourceValue) {
            return sourceStart() <= sourceValue && sourceEnd() >= sourceValue;
        }

        private long sourceEnd() {
            return sourceStart() + length() - 1;
        }

        public Long map(Long sourceValue) {
            return sourceValue - sourceStart() + destStart();
        }

        public RangeMappingResult mapRange(ValueRange valueRange) {
            ArrayList<ValueRange> remainder = new ArrayList<>();
            if (valueRange.start() < sourceStart()) {
                remainder.add(new ValueRange(valueRange.start(), sourceStart() - valueRange.start()));
            }
            if (valueRange.end() > sourceEnd()) {
                remainder.add(new ValueRange(sourceEnd() + 1, valueRange.end() - sourceEnd()));
            }
            var start = Math.max(sourceStart(), valueRange.start());
            var end = Math.min(sourceEnd(), valueRange.end());
            var length = end - start + 1;
            return new RangeMappingResult(new ValueRange(start - sourceStart() + destStart(), length), remainder);
        }

        public boolean intersects(ValueRange range) {
            boolean startsAfter = range.start() > sourceEnd();
            boolean endsBefore = range.end() < sourceStart();
            return !(startsAfter || endsBefore);
        }


    }

    public record RangeMappingResult(ValueRange result, List<ValueRange> remainder){}

    public record ValueRange(Long start, Long length) {
        public Long end() {
            return start + length - 1;
        }
    }
}

package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.GardenAlmanach.Conversion;
import ch.ebynaqon.aoc.aoc23.GardenAlmanach.RangeMapping;
import ch.ebynaqon.aoc.aoc23.GardenAlmanach.RangeMappingResult;
import ch.ebynaqon.aoc.aoc23.GardenAlmanach.ValueRange;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GardenAlmanachTest {

    public static final String EXAMPLE = """
            seeds: 79 14 55 13
                            
            seed-to-soil map:
            50 98 2
            52 50 48
                            
            soil-to-fertilizer map:
            0 15 37
            37 52 2
            39 0 15
                            
            fertilizer-to-water map:
            49 53 8
            0 11 42
            42 0 7
            57 7 4
                            
            water-to-light map:
            88 18 7
            18 25 70
                            
            light-to-temperature map:
            45 77 23
            81 45 19
            68 64 13
                            
            temperature-to-humidity map:
            0 69 1
            1 0 69
                            
            humidity-to-location map:
            60 56 37
            56 93 4
            """.trim();

    @Test
    void parseInput() {
        var input = EXAMPLE;

        var actual = GardenAlmanach.of(input);

        assertThat(actual).isEqualTo(new GardenAlmanach(
                List.of(79L, 14L, 55L, 13L),
                List.of(
                        new Conversion(List.of(
                                new RangeMapping(50L, 98L, 2L),
                                new RangeMapping(52L, 50L, 48L)
                        )),
                        new Conversion(List.of(
                                new RangeMapping(0L, 15L, 37L),
                                new RangeMapping(37L, 52L, 2L),
                                new RangeMapping(39L, 0L, 15L)
                        )),
                        new Conversion(List.of(
                                new RangeMapping(49L, 53L, 8L),
                                new RangeMapping(0L, 11L, 42L),
                                new RangeMapping(42L, 0L, 7L),
                                new RangeMapping(57L, 7L, 4L)
                        )),
                        new Conversion(List.of(
                                new RangeMapping(88L,18L,7L),
                                new RangeMapping(18L,25L,70L)
                        )),
                        new Conversion(List.of(
                                new RangeMapping(45L,77L,23L),
                                new RangeMapping(81L,45L,19L),
                                new RangeMapping(68L,64L,13L)
                        )),
                        new Conversion(List.of(
                                new RangeMapping(0L,69L,1L),
                                new RangeMapping(1L,0L,69L)
                        )),
                        new Conversion(List.of(
                                new RangeMapping(60L, 56L, 37L),
                                new RangeMapping(56L, 93L, 4L)
                        ))
                )
        ));
    }

    @Test
    void getSeedLocations() {
        var input = EXAMPLE;

        var almanach = GardenAlmanach.of(input);
        var actual = almanach.getSeedLocations();
        var smallest = almanach.getClosestSeedLocation();

        assertThat(actual).isEqualTo(List.of(82L, 43L, 86L, 35L));
        assertThat(smallest).isEqualTo(35L);
    }

    @Test
    void getClosestSeedLocationForPuzzleInputPart1() throws IOException, URISyntaxException {
        var input = TestHelper.readInput("/day5-gardening-almanach.txt").trim();

        var almanach = GardenAlmanach.of(input);
        var actual = almanach.getClosestSeedLocation();

        assertThat(actual).isEqualTo(388071289L);
    }

    @Test
    void sanityCheck() throws IOException, URISyntaxException {
        var input = TestHelper.readInput("/day5-gardening-almanach.txt").trim();

        var almanach = GardenAlmanach.of(input);
        almanach.sanityCheck();
    }

    @Test
    void getSeedRanges() {
        var input = EXAMPLE;

        var almanach = GardenAlmanach.of(input);
        var actual = almanach.seedRanges();

        assertThat(actual).isEqualTo(List.of(
                new ValueRange(79L, 14L),
                new ValueRange(55L, 13L)
        ));
    }

    @Test
    void testConversion() {
        RangeMapping mapping = new RangeMapping(10L, 20L, 5L);
        var actual = mapping.mapRange(new ValueRange(15L, 20L));

        assertThat(actual).isEqualTo(new RangeMappingResult(
                new ValueRange(10L, 5L),
                List.of(
                        new ValueRange(15L, 5L),
                        new ValueRange(25L, 10L)
                )
        ));
    }

    @Test
    void getClosestSeedLocationFromRangesForExample() {
        var input = EXAMPLE;

        var actual = GardenAlmanach.of(input)
                .getClosestSeedLocationFromRanges();

        assertThat(actual).isEqualTo(46L);
    }

    @Test
    void getClosestSeedLocationFromRangesForPuzzleInput() throws IOException, URISyntaxException {
        var input = TestHelper.readInput("/day5-gardening-almanach.txt").trim();

        var actual = GardenAlmanach.of(input)
                .getClosestSeedLocationFromRanges();

        assertThat(actual).isEqualTo(84206669L);
    }



}

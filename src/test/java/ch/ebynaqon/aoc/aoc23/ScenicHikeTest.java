package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.helper.TestHelper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ScenicHikeTest {
    @Test
    void findLongestPathForSimpleExampleWithoutWaypoints() {
        var input = """
                #.######
                #.######
                #.######
                #.######
                #.######
                #.######
                #......#
                ######.#
                """.trim();

        var actual = new ScenicHike(input, true).findLongestPath();

        assertThat(actual).isEqualTo(12);
    }

    @Test
    void findLongestPathForSimpleExampleWithTwoEquallyLongPaths() {
        var input = """
                #.######
                #.######
                #.######
                #......#
                #.####.#
                #.####.#
                #......#
                ######.#
                """.trim();

        var actual = new ScenicHike(input, true).findLongestPath();

        assertThat(actual).isEqualTo(12);
    }

    @Test
    void findLongestPathForExample() {
        var input = """
                #.#####################
                #.......#########...###
                #######.#########.#.###
                ###.....#.>.>.###.#.###
                ###v#####.#v#.###.#.###
                ###.>...#.#.#.....#...#
                ###v###.#.#.#########.#
                ###...#.#.#.......#...#
                #####.#.#.#######.#.###
                #.....#.#.#.......#...#
                #.#####.#.#.#########v#
                #.#...#...#...###...>.#
                #.#.#v#######v###.###v#
                #...#.>.#...>.>.#.###.#
                #####v#.#.###v#.#.###.#
                #.....#...#...#.#.#...#
                #.#########.###.#.#.###
                #...###...#...#...#.###
                ###.###.#.###v#####v###
                #...#...#.#.>.>.#.>.###
                #.###.###.#.###.#.#v###
                #.....###...###...#...#
                #####################.#
                """.trim();

        var actual = new ScenicHike(input, true).findLongestPath();

        assertThat(actual).isEqualTo(94);
    }

    @Test
    void findLongestPathForPart1() {
        var input = TestHelper.readInput("/day23-hike-map.txt").trim();

        var actual = new ScenicHike(input, true).findLongestPath();

        assertThat(actual).isEqualTo(2166);
    }

    @Test
    void findLongestPathIgnoringSlopesForExample() {
        var input = """
                #.#####################
                #.......#########...###
                #######.#########.#.###
                ###.....#.>.>.###.#.###
                ###v#####.#v#.###.#.###
                ###.>...#.#.#.....#...#
                ###v###.#.#.#########.#
                ###...#.#.#.......#...#
                #####.#.#.#######.#.###
                #.....#.#.#.......#...#
                #.#####.#.#.#########v#
                #.#...#...#...###...>.#
                #.#.#v#######v###.###v#
                #...#.>.#...>.>.#.###.#
                #####v#.#.###v#.#.###.#
                #.....#...#...#.#.#...#
                #.#########.###.#.#.###
                #...###...#...#...#.###
                ###.###.#.###v#####v###
                #...#...#.#.>.>.#.>.###
                #.###.###.#.###.#.#v###
                #.....###...###...#...#
                #####################.#
                """.trim();

        var actual = new ScenicHike(input, false).findLongestPath();

        assertThat(actual).isEqualTo(154);
    }

    @Test
    void findLongestPathIgnoringSlopesForPart2() {
        var input = TestHelper.readInput("/day23-hike-map.txt").trim();

        var actual = new ScenicHike(input, false).findLongestPath();

        assertThat(actual).isEqualTo(6378);
    }
}

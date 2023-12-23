package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.helper.TestHelper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ScenicHikeTest {
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

        var actual = new ScenicHike(input).findLongestPath();

        assertThat(actual).isEqualTo(94);
    }

    @Test
    void findLongestPathForPart1() {
        var input = TestHelper.readInput("/day23-hike-map.txt").trim();

        var actual = new ScenicHike(input).findLongestPath();

        assertThat(actual).isEqualTo(2166);
    }
}

package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.helper.TestHelper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SnowverloadTest {

    public static final String EXAMPLE = """
            jqt: rhn xhk nvd
            rsh: frs pzl lsr
            xhk: hfx
            cmg: qnr nvd lhk bvb
            rhn: xhk bvb hfx
            bvb: xhk hfx
            pzl: lsr hfx nvd
            qnr: nvd
            ntq: jqt hfx bvb xhk
            nvd: lhk
            lsr: lhk
            rzs: qnr cmg lsr rsh
            frs: qnr lhk lsr
            """;

    @Test
    void infoAboutExample() {
        var input = EXAMPLE.trim();

        var nodeCount = new Snowverload(input).numberOfNodes();
        var linkCount = new Snowverload(input).numberOfLinks();

        assertThat(nodeCount).isEqualTo(15);
        assertThat(linkCount).isEqualTo(33);
    }

    @Test
    void readInput() {
        var input = TestHelper.readInput("/day25-snowverload.txt").trim();

        var nodeCount = new Snowverload(input).numberOfNodes();
        var linkCount = new Snowverload(input).numberOfLinks();

        assertThat(nodeCount).isEqualTo(1539);
        assertThat(linkCount).isEqualTo(3450);
    }

    @Test
    void groupSizeForExample() {
        var input = EXAMPLE.trim();

        var actual = new Snowverload(input).disconnectedGroupSize();

        assertThat(actual).isEqualTo(54);
    }

    @Test
    @Disabled("Too slow to run in CI")
    void groupSizeForPart1() {
        var input = TestHelper.readInput("/day25-snowverload.txt").trim();

        var actual = new Snowverload(input).disconnectedGroupSize();

        assertThat(actual).isEqualTo(591890);
    }
}

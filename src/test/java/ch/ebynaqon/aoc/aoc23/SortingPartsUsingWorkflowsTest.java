package ch.ebynaqon.aoc.aoc23;

import ch.ebynaqon.aoc.aoc23.SortingPartsUsingWorkflows.Part;
import ch.ebynaqon.aoc.aoc23.SortingPartsUsingWorkflows.Rule;
import ch.ebynaqon.aoc.aoc23.SortingPartsUsingWorkflows.Workflow;
import ch.ebynaqon.aoc.aoc23.helper.TestHelper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SortingPartsUsingWorkflowsTest {
    @Test
    void parse() {
        var input = """
                px{a<2006:qkq,m>2090:A,rfg}
                pv{a>1716:R,A}
                                
                {x=787,m=2655,a=1222,s=2876}
                {x=1679,m=44,a=2067,s=496}
                """.trim();

        var actual = SortingPartsUsingWorkflows.parse(input);

        assertThat(actual).isEqualTo(new SortingPartsUsingWorkflows(
                List.of(
                        new Workflow("px", List.of(
                                new Rule.LessThan("a", 2006, "qkq"),
                                new Rule.MoreThan("m", 2090, "A"),
                                new Rule.Default("rfg")
                        )),
                        new Workflow("pv", List.of(
                                new Rule.MoreThan("a", 1716, "R"),
                                new Rule.Default("A")
                        ))
                ),
                List.of(
                        new Part(787, 2655, 1222, 2876),
                        new Part(1679, 44, 2067, 496)
                )
        ));
    }

    @Test
    void getScoreOfAcceptedPartsForExample() {
        var input = """
                px{a<2006:qkq,m>2090:A,rfg}
                pv{a>1716:R,A}
                lnx{m>1548:A,A}
                rfg{s<537:gd,x>2440:R,A}
                qs{s>3448:A,lnx}
                qkq{x<1416:A,crn}
                crn{x>2662:A,R}
                in{s<1351:px,qqz}
                qqz{s>2770:qs,m<1801:hdj,R}
                gd{a>3333:R,R}
                hdj{m>838:A,pv}
                                
                {x=787,m=2655,a=1222,s=2876}
                {x=1679,m=44,a=2067,s=496}
                {x=2036,m=264,a=79,s=2244}
                {x=2461,m=1339,a=466,s=291}
                {x=2127,m=1623,a=2188,s=1013}
                """.trim();

        var actual = SortingPartsUsingWorkflows.parse(input).getScoreOfAcceptedParts();

        assertThat(actual).isEqualTo(19114);
    }

    @Test
    void getNumberOfAcceptedPartValuesForExample() {
        var input = """
                px{a<2006:qkq,m>2090:A,rfg}
                pv{a>1716:R,A}
                lnx{m>1548:A,A}
                rfg{s<537:gd,x>2440:R,A}
                qs{s>3448:A,lnx}
                qkq{x<1416:A,crn}
                crn{x>2662:A,R}
                in{s<1351:px,qqz}
                qqz{s>2770:qs,m<1801:hdj,R}
                gd{a>3333:R,R}
                hdj{m>838:A,pv}
                                
                {x=787,m=2655,a=1222,s=2876}
                {x=1679,m=44,a=2067,s=496}
                {x=2036,m=264,a=79,s=2244}
                {x=2461,m=1339,a=466,s=291}
                {x=2127,m=1623,a=2188,s=1013}
                """.trim();

        var actual = SortingPartsUsingWorkflows.parse(input).getNumberOfAcceptedPartValues();

        assertThat(actual).isEqualTo(167409079868000L);
    }

    @Test
    void getScoreOfAcceptedPartsForPart1() {
        var input = TestHelper.readInput("/day19-workflows-and-parts.txt").trim();

        var actual = SortingPartsUsingWorkflows.parse(input).getScoreOfAcceptedParts();

        assertThat(actual).isEqualTo(409898);
    }

    @Test
    void getNumberOfAcceptedPartValuesForPart2() {
        var input = TestHelper.readInput("/day19-workflows-and-parts.txt").trim();

        var actual = SortingPartsUsingWorkflows.parse(input).getNumberOfAcceptedPartValues();

        assertThat(actual).isEqualTo(113057405770956L);
    }
}

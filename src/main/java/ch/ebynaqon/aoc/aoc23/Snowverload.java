package ch.ebynaqon.aoc.aoc23;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class Snowverload {

    private final List<Link> links;
    private final HashMap<String, Node> nodes;

    public Snowverload(String input) {
        links = Arrays.stream(input.split("\n"))
                .flatMap(line -> {
                    String[] fromTo = line.split(":");
                    var from = fromTo[0].trim();
                    return Arrays.stream(fromTo[1].trim().split("\\s+"))
                            .map(to -> new Link(from, to));
                })
                .toList();
        nodes = new HashMap<>();
        for (var link : links) {
            getNode(link.from).linked().add(getNode(link.to));
            getNode(link.to).linked().add(getNode(link.from));
        }
    }

    private Node getNode(String name) {
        if (!nodes.containsKey(name)) {
            nodes.put(name, new Node(name, new ArrayList<>()));
        }
        return nodes.get(name);
    }

    public int numberOfNodes() {
        return nodes.size();
    }

    public int numberOfLinks() {
        return links.size();
    }

    public int disconnectedGroupSize() {
        var nodesByNumberOfConnections = nodes.values().stream()
                .sorted(Comparator.comparing(node -> node.linked().size()))
                .toList();
        var nextNode = nodesByNumberOfConnections.getFirst();
        var nodeSet = new ArrayList<Node>();
        nodeSet.add(nextNode);
        int nodeSetConnections = connections(nodeSet);
        while (nodeSetConnections != 3) {
            var connected = connectedNodes(nodeSet);
            Node bestNextNode = null;
            Integer bestNextValue = null;
            for (var connectedNode : connected) {
                var nextSet = new ArrayList<>(nodeSet);
                nextSet.add(connectedNode);
                int next = connections(nextSet);
                if (bestNextValue == null || next < bestNextValue) {
                    bestNextValue = next;
                    bestNextNode = connectedNode;
                }
            }
            if (bestNextNode == null) {
                throw new IllegalStateException("Could not find any nodes to connect!");
            }
            nodeSet.add(bestNextNode);
            nodeSetConnections = bestNextValue;
        }
        return nodeSet.size() * (nodes.size() - nodeSet.size());
    }

    private List<Node> connectedNodes(List<Node> nodes) {
        return nodes.stream()
                .flatMap(node -> node.linked().stream()
                        .filter(Predicate.not(nodes::contains))
                )
                .toList();
    }

    private int connections(List<Node> nodes) {
        return (int) nodes.stream()
                .mapToLong(node -> node.linked().stream()
                        .filter(Predicate.not(nodes::contains))
                        .count()
                )
                .sum();
    }

    public record Link(String from, String to) {
    }

    public static final class Node {
        private final String name;
        private final List<Node> linked;

        public Node(String name, List<Node> linked) {
            this.name = name;
            this.linked = linked;
        }

        public String name() {
            return name;
        }

        public List<Node> linked() {
            return linked;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Node) obj;
            return Objects.equals(this.name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public String toString() {
            return "Node[name=" + name + "]";
        }
    }
}

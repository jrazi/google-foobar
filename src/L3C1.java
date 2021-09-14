import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * This is not a good solution. I misunderstood the problem and came up with this, which is needlessly complex.
 * The solution was accepted, but it's pretty much a solution for a different problem.
 */
public class L3C1 {

    public static class DivisionGraph {
        private final GraphNode root;
        public final Set<GraphNode> nodes;
        public final Map<GraphNode, Set<GraphNode>> edges;
        public final Map<GraphNode, Set<GraphNode>> inputEdges;
        private final Set<GraphNode> startTerminalNodes;
        private final Set<GraphNode> endTerminalNodes;
        private final Set<GraphNode> junctions;

        public DivisionGraph(int[] nums) {
            this();
            for (int i = nums.length - 1; i >= 0; i--) {
                int n = nums[i];
                addNewNode(n, i);
            }
            for (GraphNode node : nodes) {
                Set<GraphNode> input = inputEdges.get(node);

                if (input.size() > 1) {
                    junctions.add(node);
                }
            }
            Set<GraphNode> rootAdj = new HashSet<>();
            edges.put(root, rootAdj);

            startTerminalNodes.forEach(v -> rootAdj.add(v));
        }

        private DivisionGraph() {
            nodes = new HashSet<>();
            edges = new HashMap<>();
            inputEdges = new HashMap<>();
            startTerminalNodes = new HashSet<>();
            endTerminalNodes = new HashSet<>();
            junctions = new HashSet<>();
            root = new GraphNode(-1, -1);
        }

        private void addNewNode(int value, int index) {
            GraphNode node = putNode(index, value);

            startTerminalNodes.add(node);

            for (GraphNode w : nodes.stream().collect(Collectors.toSet())) {
                if ((w.value % node.value == 0) &&
                        (node.index < w.index)
                ) {
                    if (startTerminalNodes.contains(w)) {
                        edges.get(node).add(w);
                        inputEdges.get(w).add(node);
                        startTerminalNodes.remove(w);
                    }
                    else {
                        boolean shouldBeParent = !inputEdges.get(w)
                                .stream()
                                .anyMatch(adj -> {
                                    GraphNode lAdj = (GraphNode) adj;
                                    return lAdj.value % node.value == 0;
                                });
                        if (shouldBeParent) {
                            edges.get(node).add(w);
                            inputEdges.get(w).add(node);
                        }
                    }
                }
            }

            if (edges.get(node).isEmpty()) {
                endTerminalNodes.add(node);
            }
        }

        public int dfsLuckyTriples() {
            Map<GraphNode, Object[]> junctionCalculations = new HashMap<>();

            Object[] dfsStats = dfsLuckyTriples(root, junctionCalculations, false);
            int totalTriples = ((int[]) dfsStats[0])[0];

            return totalTriples;
        }


        public Object[] dfsLuckyTriples(GraphNode node, Map<GraphNode, Object[]> junctionCalculations, boolean tripleGeneratingNode) {
            boolean isMultiRoadJunction = junctions.contains(node);

            int factor = tripleGeneratingNode ? 1 : 0;
            int[] tripleLuckyStats = {0, 0, factor};

            Set<GraphNode> wEdges = edges.get(node);

            Map<GraphNode, Integer> junctionCount = new HashMap<>();

            Map<GraphNode, int[]> newJunctionStats = new HashMap<>();


            int[] junctionCalculationOffset = new int[]{0, 0, 0};

            for (GraphNode w : wEdges) {
                Object[] junctionCalc = junctionCalculations.get(w);
                boolean junctionCalculated = junctionCalc != null;
                boolean endTerminalNode = endTerminalNodes.contains(w);

                Object[] dfsResult = junctionCalculated ? junctionCalc : dfsLuckyTriples(w, junctionCalculations, true);
                int[] childStats = (int[]) dfsResult[0];
                Map<GraphNode, int[]> junctionStats = (Map<GraphNode, int[]>) dfsResult[1];

                junctionStats.forEach((j, jStats) -> {
                    newJunctionStats.put(j, jStats);
                    int count = Optional.ofNullable(junctionCount.get(j)).orElse(0);

                    if (isMultiRoadJunction && count == 0) {
                        junctionCalculationOffset[0] -= jStats[0];
                        junctionCalculationOffset[1] -= jStats[1];
                        junctionCalculationOffset[2] -= (jStats[2]);
                    }

                    junctionCount.put(j, count + 1);
                });

                tripleLuckyStats[0] += endTerminalNode ? 0 : childStats[0] + childStats[1]*factor;
                tripleLuckyStats[1] += childStats[1] + childStats[2]*factor;
                tripleLuckyStats[2] += childStats[2];
            }

            junctionCount.entrySet()
                    .stream()
                    .filter(j -> j.getValue() > 1)
                    .forEach(j -> {
                        int[] jDfsResult = ((Map<GraphNode, int[]>) junctionCalculations.get(j.getKey())[1]).get(j.getKey());
                        int count = j.getValue() - 1;
                        tripleLuckyStats[0] -= count * (jDfsResult[0] + jDfsResult[1]*factor);
                        tripleLuckyStats[1] -= count * (jDfsResult[1] + jDfsResult[2]);
                        tripleLuckyStats[2] -= count * (jDfsResult[2]);
                    });

            Object[] dfsResult = new Object[2];
            dfsResult[0] = tripleLuckyStats;
            dfsResult[1] = newJunctionStats;

            if (isMultiRoadJunction) {
                int[] junctionTlReport = new int[]{
                        tripleLuckyStats[0] + junctionCalculationOffset[0],
                        tripleLuckyStats[1] + junctionCalculationOffset[1],
                        tripleLuckyStats[2] + junctionCalculationOffset[2],
                };

                newJunctionStats.put(node, junctionTlReport);
                junctionCalculations.put(node, dfsResult);
            }

            return dfsResult;
        }


        private GraphNode putNode(int index, int value) {
            GraphNode node = new GraphNode(index, value);

            nodes.add(node);
            if (!edges.containsKey(node))
                edges.put(node, new HashSet<>());

            if (!inputEdges.containsKey(node))
                inputEdges.put(node, new HashSet<>());

            return node;
        }
    }

    public static class GraphNode {
        public int index;
        public int value;

        public GraphNode(int index, int value) {
            this.index = index;
            this.value = value;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode("GraphNode_".concat(String.valueOf(value).concat("__").concat(String.valueOf(index))));
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof GraphNode &&
                    ((GraphNode) obj).value == value &&
                    ((GraphNode) obj).index == index;
        }
    }



    public static int bruteForce(int[] l) {
        if (l.length < 3)
            return 0;

        Collection<String> triples = new ArrayList<>();
        for (int i = 0; i < l.length; i++) {
            int a = l[i];
            for (int j = i+1; j < l.length; j++) {
                int b = l[j];
                for (int k = j+1; k < l.length; k++) {
                    int c = l[k];

                    if (a <= b && a <= c && b <= c) {
                        if ((b % a == 0) && (c % b == 0) && (c % a == 0)) {
                            triples.add(String.format("%d_%d_%d", a, b, c));
                        }
                    }
                }
            }
        }
        return triples.size();
    }

    public static int solution(int[] l) {
        DivisionGraph graph = new DivisionGraph(l);

        int count = graph.dfsLuckyTriples();


        return count;
    }

}

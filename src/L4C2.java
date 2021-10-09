import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class L4C2 {

    private static class ResidualGraph {
        public final int[][] residualCapacities;
        public final Map<Integer, Set<Integer>> edges;
        public final int source;
        public final int sink;

        public ResidualGraph(int[][] adjMatrix, int source, int sink) {
            this.residualCapacities = new int[adjMatrix.length][adjMatrix.length];
            this.edges = new HashMap<>();
            this.source = source;
            this.sink = sink;
            for (int i = 0; i < adjMatrix.length; i++) {
                Set<Integer> adj = new HashSet<>();
                this.edges.put(i, adj);
                for (int j = 0; j < adjMatrix[i].length; j++) {
                    this.residualCapacities[i][j] = adjMatrix[i][j];

                    if (adjMatrix[i][j] != 0)
                        adj.add(j);
                }
            }
        }

        public void sendFlowViaEdge(int u, int v, int flowValue) {
            residualCapacities[u][v] -= flowValue;
            residualCapacities[v][u] += flowValue;
            modifyEdgeListIfSaturationStatusChanged(u, v);
        }

        private void modifyEdgeListIfSaturationStatusChanged(int u, int v) {
            boolean hasEdge = edges.get(u).contains(v);
            boolean hasReverseEdge = edges.get(v).contains(u);

            boolean directEdgeExistsNow = residualCapacities[u][v] != 0;
            boolean reverseEdgeExistsNow = residualCapacities[v][u] != 0;

            if (hasEdge && !directEdgeExistsNow)
                edges.get(u).remove(v);
            if (!hasEdge && directEdgeExistsNow)
                edges.get(u).add(v);

            if (hasReverseEdge && !reverseEdgeExistsNow)
                edges.get(v).remove(u);
            if (!hasReverseEdge && reverseEdgeExistsNow)
                edges.get(v).add(u);
        }

        public LevelGraph getLevelGraph() {
            return new LevelGraph(this);
        }
    }

    private static class LevelGraph {
        public final ResidualGraph maxFlowGraph;
        public final Map<Integer, Integer> levels;

        public LevelGraph(ResidualGraph maxFlowGraph) {
            this.maxFlowGraph = maxFlowGraph;
            this.levels = new HashMap<>();
            resolveLevels();
        }

        private void resolveLevels() {
            Queue<int[]> queue = new ArrayDeque<>();

            queue.add(new int[]{maxFlowGraph.source, 0});
            levels.put(maxFlowGraph.source, 0);

            while (!queue.isEmpty()) {
                int[] queueItem = queue.poll();
                int current = queueItem[0];
                int level = queueItem[1];

                Set<Integer> adj = maxFlowGraph.edges.get(current);
                for (Integer v : adj) {
                    if (levels.containsKey(v))
                        continue;

                    queue.add(new int[]{v, level + 1});
                    levels.put(v, level + 1);
                }
            }
        }

        public int findBlockingFlow() {

            boolean pathExists = true;

            int totalFlow = 0;
            while(pathExists) {
                Stack<Integer> path = new Stack<>();

                path.push(maxFlowGraph.source);
                pathExists = dfsSourceToSinkPath(maxFlowGraph.source, path);

                if (!pathExists)
                    continue;

                int[] pathArray = path.stream().mapToInt(i -> i).toArray();

                int[][] pathEdges = IntStream.range(0, path.size()-1)
                        .boxed()
                        .map(i -> new int[]{pathArray[i], pathArray[i+1]})
                        .toArray((size) -> new int[size][2]);

                int minCapacity = Arrays.stream(pathEdges)
                        .min(Comparator.comparingInt(e -> maxFlowGraph.residualCapacities[e[0]][e[1]]))
                        .map(minEdge -> maxFlowGraph.residualCapacities[minEdge[0]][minEdge[1]])
                        .get();

                for (int[] edge : pathEdges) {
                    maxFlowGraph.sendFlowViaEdge(edge[0], edge[1], minCapacity);
                }

                totalFlow += minCapacity;
            }

            return totalFlow;
        }

        private boolean dfsSourceToSinkPath(int node, Stack<Integer> path) {
            if (node == maxFlowGraph.sink)
                return true;

            Set<Integer> adj = getLevelGraphAdj(node)
                    .stream()
                    .filter(v -> maxFlowGraph.residualCapacities[node][v] > 0)
                    .collect(Collectors.toSet());

            for (Integer v : adj) {
                path.push(v);

                boolean pathComplete = dfsSourceToSinkPath(v, path);
                if (pathComplete)
                    return true;

                path.pop();
            }

            return false;
        }

        private Set<Integer> getLevelGraphAdj(Integer u) {
            return maxFlowGraph.edges.get(u)
                    .stream()
                    .filter(v -> levels.get(u).equals(levels.get(v) - 1))
                    .collect(Collectors.toSet());
        }

        public boolean hasPathFromSourceToSink() {
            return levels.containsKey(maxFlowGraph.sink);
        }
    }

    private static ResidualGraph buildMaxFlowGraph(int[][] adjMatrix, int[] sources, int[] sinks) {
        if (sources.length == 1 && sinks.length == 1) {
            return new ResidualGraph(adjMatrix, sources[0], sinks[0]);
        }

        int n = adjMatrix.length;
        int[][] modifiedAdj = new int[n + 2][n + 2];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                modifiedAdj[i][j] = adjMatrix[i][j];
            }
        }
        final int MAX_WEIGHT = 2000000;

        for (int source : sources) {
            modifiedAdj[n][source] = MAX_WEIGHT;
        }
        for (int sink : sinks) {
            modifiedAdj[sink][n+1] = MAX_WEIGHT;
        }

        return new ResidualGraph(modifiedAdj, n, n+1);
    }

    public static int solution(int[] entrances, int[] exits, int[][] path) {
        ResidualGraph graph = buildMaxFlowGraph(path, entrances, exits);

        LevelGraph levelGraph = graph.getLevelGraph();
        int totalFlow = 0;
        while(levelGraph.hasPathFromSourceToSink()) {
            totalFlow += levelGraph.findBlockingFlow();

            levelGraph = graph.getLevelGraph();
        }

        return totalFlow;
    }


}
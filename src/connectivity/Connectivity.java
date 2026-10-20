package connectivity;
import graph.Graph;
import java.util.*;
public class Connectivity {
    public static long operations;
    public static List<Integer> topological(Graph g) {
        operations = 0;
        int[] indegree = new int[g.n];
        for (Graph.Edge e : g.edges) { indegree[e.to()]++; operations++; }
        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < g.n; i++) { operations++; if (indegree[i] == 0) queue.add(i); }
        List<Integer> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            int v = queue.remove();
            result.add(v);
            operations++;
            for (Graph.Edge e : g.adj.get(v)) {
                operations++;
                if (--indegree[e.to()] == 0) queue.add(e.to());
            }
        }
        return result.size() == g.n ? result : List.of();
    }

    public static List<List<Integer>> scc(Graph g) {
        operations = 0;
        boolean[] seen = new boolean[g.n];
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < g.n; i++) if (!seen[i]) dfs1(g, i, seen, order);
        Graph reversed = new Graph(g.n, true);
        for (Graph.Edge e : g.edges) { reversed.add(e.to(), e.from(), e.weight()); operations++; }
        Arrays.fill(seen, false);
        Collections.reverse(order);
        List<List<Integer>> result = new ArrayList<>();
        for (int v : order) if (!seen[v]) {
            List<Integer> group = new ArrayList<>();
            dfs2(reversed, v, seen, group);
            result.add(group);
        }
        return result;
    }

    private static void dfs1(Graph g, int v, boolean[] seen, List<Integer> order) {
        seen[v] = true;
        operations++;
        for (Graph.Edge e : g.adj.get(v)) {
            operations++;
            if (!seen[e.to()]) dfs1(g, e.to(), seen, order);
        }
        order.add(v);
    }

    private static void dfs2(Graph g, int v, boolean[] seen, List<Integer> group) {
        seen[v] = true;
        group.add(v);
        operations++;
        for (Graph.Edge e : g.adj.get(v)) {
            operations++;
            if (!seen[e.to()]) dfs2(g, e.to(), seen, group);
        }
    }
}

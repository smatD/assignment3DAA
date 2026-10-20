package mst;
import graph.Graph;
import dsu.DSU;
import java.util.*;
public class MSTAlgorithms {
    public record MST(long weight, int edges, boolean connected, long operations) {}
    public static MST kruskal(Graph g) {
        List<Graph.Edge> sorted = new ArrayList<>(g.edges);
        long[] comparisons = {0};
        sorted.sort((a, b) -> { comparisons[0]++; return Integer.compare(a.weight(), b.weight()); });
        DSU dsu = new DSU(g.n);
        long weight = 0, examined = 0;
        int count = 0;
        for (Graph.Edge e : sorted) {
            examined++;
            if (dsu.union(e.from(), e.to())) {
                weight += e.weight();
                count++;
                if (count == g.n - 1) break;
            }
        }
        return new MST(weight, count, count == g.n - 1, comparisons[0] + examined + dsu.operations);
    }

    public static MST prim(Graph g) {
        boolean[] visited = new boolean[g.n];
        long[] comparisons = {0};
        PriorityQueue<Graph.Edge> pq = new PriorityQueue<>((a, b) -> {
            comparisons[0]++;
            return Integer.compare(a.weight(), b.weight());
        });
        long weight = 0, examined = 0;
        int count = 0, components = 0;
        for (int start = 0; start < g.n; start++) {
            if (visited[start]) continue;
            components++;
            visited[start] = true;
            pq.addAll(g.adj.get(start));
            examined += g.adj.get(start).size();
            while (!pq.isEmpty()) {
                Graph.Edge e = pq.poll();
                examined++;
                if (visited[e.to()]) continue;
                visited[e.to()] = true;
                weight += e.weight();
                count++;
                for (Graph.Edge next : g.adj.get(e.to())) {
                    pq.add(next);
                    examined++;
                }
            }
        }
        return new MST(weight, count, components == 1, examined + comparisons[0]);
    }
}
